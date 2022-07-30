import { Router } from '@angular/router';
import { TopicDenunciationService } from './../../../../helpers/services/topic-denunciation.service';
import { MessageService } from 'primeng/api';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { TopicProposalService } from './../../../../helpers/services/topic-proposal.service';
import { mergeMap } from 'rxjs/operators';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicProposal } from './../../../../helpers/models/topic-proposal';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-topic-proposal',
  templateUrl: './topic-proposal.component.html',
  styleUrls: ['./topic-proposal.component.scss']
})

export class TopicProposalComponent implements OnInit {
  topicProposal = {} as TopicProposal
  specificObjective: string = ''
  reference: string = ''
  topicProposalPreview: boolean = false
  existProposal: boolean = false
  existDenunciation: boolean = true
  paymentStatus: boolean = true
  edit: boolean = false
  loading: boolean = true
  loadingButton: boolean = false
  constructor(private tokenSrv: TokenService, private topicStundentSrv: TopicStudentService,
    private topicPropposalSrv: TopicProposalService, private confirmationDialogSrv: ConfirmationDialogService,
    private messageSrv: MessageService, private topicDenunciationService: TopicDenunciationService, private router: Router) {
  }

  ngOnInit(): void {
    this.getTopicDenunciation()
    this.getTopicStudent()
    this.getPropposal()
  }

  getTopicDenunciation() {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap(userData => {
        return this.topicDenunciationService.getTopicDenunciationByTopicStudentCi(userData.id)
      })
    ).subscribe(() => {
      this.loading = false
      this.existDenunciation = true
    }, () => {
      this.loading = false
      this.existDenunciation = false
    })
  }

  getTopicStudent() {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap(userData => {
        return this.topicStundentSrv.getStudentTopicByStudentId(userData.id)
      }), mergeMap(topicData => {
        this.topicProposal = Object.assign({ topicStudent: topicData, objectivesSpecific: [], bibliographicReferences: [] })
        return this.topicStundentSrv.verifiedPaymentStatus(topicData.topic.id)
      })
    ).subscribe(paymentCheck => {
      this.paymentStatus = paymentCheck
    })
  }

  getPropposal() {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap(userData => {
        return this.topicStundentSrv.getStudentTopicByStudentId(userData.id)
      }), mergeMap(topicData => {
        this.topicProposal = Object.assign({ topicStudent: topicData, objectivesSpecific: [], bibliographicReferences: [] })
        return this.topicPropposalSrv.getTopicProposalByTopicId(topicData.topic.id)
      })
    ).subscribe(topicPropposal => {
      if (topicPropposal[0]) {
        console.log(topicPropposal[0]);
        this.topicProposal = topicPropposal[0]
        this.existProposal = true
      }

    })
  }

  addSpecificObjectives(text: string) {
    if (this.specificObjective !== '') {
      this.topicProposal.objectivesSpecific.push(text)
      this.specificObjective = ''
    }


  }
  removeSpecificObjectives(index: number) {
    this.topicProposal.objectivesSpecific.splice(index, 1)
  }
  addReferences(text: string) {
    if (!this.topicProposal.bibliographicReferences) {
      this.topicProposal.bibliographicReferences = []
    }
    if (this.reference !== '') {
      this.topicProposal.bibliographicReferences.push(text)
      this.reference = ''
    }
  }

  removeReferences(index: number) {
    this.topicProposal.bibliographicReferences.splice(index, 1)
  }

  sendTopicProposal(topicProposal: TopicProposal) {
    this.confirmationDialogSrv.confirmationDialog('Esta seguro de enviar la propuesta de tema', 'Envío Cancelado').then(() => {
      this.loadingButton = true
      this.topicPropposalSrv.createTopicProposal(topicProposal).subscribe(() => {
        this.messageSrv.add({ severity: 'info', summary: 'Confirmado', detail: 'Propuesta enviada correctamente' })
        this.loadingButton = false
        setTimeout(() => {
          this.messageSrv.clear();
          this.router.navigateByUrl('/student/selected')
          this.loadingButton = false
        }, 2000)
      })
    })
  }

  showTopicProposalPreviewModal() {
    this.topicProposalPreview = true
  }

  closeTopicProposalPreview() {
    this.topicProposalPreview = false
  }

  editProposal() {
    this.edit = !this.edit
  }

  saveChanges(topicProposal: TopicProposal) {
    this.confirmationDialogSrv.confirmationDialog('Desea guardar los cambios', 'No se guardaron los cambios').then(() => {
      this.loadingButton = true
      this.topicPropposalSrv.editTopicProposal(topicProposal.id, topicProposal).subscribe(() => {
        this.edit = false
        this.loadingButton = false
        this.router.navigateByUrl('/student/selected')
      })
    })

  }

  cancel() {
    this.router.navigateByUrl('/student/selected')
  }
}

import { UserService } from './../../../../helpers/services/user.service';
import { User } from 'src/app/helpers/models/user';
import { ToastService } from './../../../../helpers/services/toast.service';
import { TopicApprovalService } from './../../../../helpers/services/topic-approval.service';
import { TopicApproval } from './../../../../helpers/models/topic-approval';
import { StudentComponent } from './../student.component';
import { TopicProposal } from './../../../../helpers/models/topic-proposal';
import { TopicProposalService } from './../../../../helpers/services/topic-proposal.service';
import { TopicDenunciation } from 'src/app/helpers/models/topic-denunciation';
import { TopicDenunciationService } from './../../../../helpers/services/topic-denunciation.service';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { mergeMap } from 'rxjs/operators';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { Router } from '@angular/router';
import { Component, OnInit, Injector } from '@angular/core';
@Component({
  selector: 'app-topic-selected',
  templateUrl: './topic-selected.component.html',
  styleUrls: ['./topic-selected.component.scss']
})
export class TopicSelectedComponent implements OnInit {

  payStatus: string = ''
  topicStudent: TopicStudent = null
  topicDenunciation: TopicDenunciation = null
  topicProposal: TopicProposal = null
  topicApproval: TopicApproval = null
  topicDenunciationDate: string = ''
  denunciationPreview: boolean = false
  proposalPreview: boolean = false
  topicApprovalPreview: boolean = false
  director: User
  loading: boolean = true
  private tokenService: TokenService
  private router: Router
  private topicStudentService: TopicStudentService
  private studentStepper: StudentComponent
  private topicDenunciationSrv: TopicDenunciationService
  private topicProposalSrv: TopicProposalService
  private topicApprovalSrv: TopicApprovalService
  private toastSrv: ToastService
  private userSrv: UserService
  constructor(private injector: Injector) {
    this.router = this.injector.get<Router>(Router)
    this.topicStudentService = this.injector.get<TopicStudentService>(TopicStudentService)
    this.tokenService = this.injector.get<TokenService>(TokenService)
    this.studentStepper = this.injector.get<StudentComponent>(StudentComponent)
    this.topicDenunciationSrv = this.injector.get<TopicDenunciationService>(TopicDenunciationService)
    this.topicProposalSrv = this.injector.get<TopicProposalService>(TopicProposalService)
    this.topicApprovalSrv = this.injector.get<TopicApprovalService>(TopicApprovalService)
    this.toastSrv = this.injector.get<ToastService>(ToastService)
    this.userSrv = this.injector.get<UserService>(UserService)
    this.getStudentTopic()
    this.getApprovalNotificationByStudent()
    this.getCarrerDirector()
  }

  ngOnInit(): void { }
  getStudentTopic() {
    this.loading = true
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        return this.topicStudentService.getStudentTopicByStudentId(userData.id);
      })
    ).subscribe((data) => {
      this.topicStudent = data
      this.payStatus = data.paymentDenunciation
      if (this.payStatus === 'No pagado') {
        this.studentStepper.changeStepperState([false, false, true, true])
      } else {
        this.studentStepper.changeStepperState([false, false, false, false])
      }
      this.getStudentProposal()
      this.getStudentDenunciation()
      this.loading = false
    }, () => {
      this.loading = false
    })
  }

  getStudentDenunciation() {
    this.topicDenunciationSrv.getTopicDenunciationByTopicStudentId(this.topicStudent.id).subscribe(denunciation => {
      this.formatDate(denunciation.date)
      this.topicDenunciation = denunciation
    })
  }

  formatDate(denunciationDate: Date) {
    let date = new Date(denunciationDate);
    let monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    let year = date.getFullYear()
    let month = monthNames[date.getMonth()]
    let day = date.getDate()
    this.topicDenunciationDate = day + ' de ' + month + ' del ' + year
  }
  getStudentProposal() {
    this.topicProposalSrv.getTopicProposalByTopicId(this.topicStudent.topic.id).subscribe(data => {
      this.topicProposal = data[0]
    }, err => {
      this.topicProposal = null
      console.log(err.error.mensaje)
    })
  }

  nextStep() {
    if (this.topicStudent.paymentDenunciation === 'Pagado') this.router.navigateByUrl('student/denunciation')
  }

  showDocumentPreview(id: string) {
    switch (id) {
      case this.topicDenunciation.id:
        this.denunciationPreview = true
        break
      case this.topicProposal.id:
        this.proposalPreview = true
        break
      default:
        this.topicApprovalPreview = true
        break
    }
  }

  getApprovalNotificationByStudent() {
    this.topicApprovalSrv.getApprovalNotificationByStudent().subscribe(approval => {
      this.topicApproval = approval
    }, () => {
      this.topicApproval = null
    })
  }

  closeTopicDenunciationPreviewModal() {
    this.denunciationPreview = false
  }

  closeTopicProposalPreviewModal() {
    this.proposalPreview = false
  }

  closeTopicApprovalPreviewModal() {
    this.topicApprovalPreview = false
  }


  saveAsPdf(topicStudentId: string) {
    this.topicDenunciationSrv.generateTopicDenunciationPdf(topicStudentId).subscribe((data) => {
      let studentName = this.topicDenunciation.topicStudent.student.name + '-' + this.topicDenunciation.topicStudent.student.lastName
      let downloadURL = window.URL.createObjectURL(data)
      let link = document.createElement('a')
      link.href = downloadURL
      link.title = 'Denuncia'
      link.download = studentName + "-DenunciaTema.pdf"
      link.click();
    })
  }

  pdfTopicProposal(topicId: string) {
    this.topicProposalSrv.generateTopicProposalPdf(topicId).subscribe((data) => {
      let studentName = this.topicProposal.topicStudent.student.name + '-' + this.topicProposal.topicStudent.student.lastName
      let downloadURL = window.URL.createObjectURL(data)
      let link = document.createElement('a')
      link.href = downloadURL;
      link.title = 'Propuesta'
      link.download = studentName + "-PropuestaTema.pdf";
      link.click()
    })
  }

  getNotificationPdf(id: string, name: string) {
    this.topicApprovalSrv.getNotificationPdf(id).subscribe((data) => {
      this.toastSrv.showInfo('Descargando...', 'Notificación')
      let studentName = name
      let downloadURL = window.URL.createObjectURL(data)
      let link = document.createElement('a')
      link.href = downloadURL
      link.title = 'Notificación'
      link.download = studentName + "-NotificaciónApr.pdf"
      link.click()
    })
  }
  getCarrerDirector() {
    this.tokenService.getUserInfo().pipe(
      mergeMap((user) => {
        return this.userSrv.getUserByRoleAndCareerId('ROLE_CAREER_DIRECTOR', user.career.id)
      })
    ).subscribe((director => {
      this.director = director[0]
    }))
  }
}

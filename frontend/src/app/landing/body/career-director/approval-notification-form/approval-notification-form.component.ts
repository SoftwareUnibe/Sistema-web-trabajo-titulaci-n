import { mergeMap } from 'rxjs/operators';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { Router, ActivatedRoute } from '@angular/router';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { TopicApprovalService } from './../../../../helpers/services/topic-approval.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { TopicProposal } from './../../../../helpers/models/topic-proposal';
import { User } from './../../../../helpers/models/user';
import { TopicProposalService } from './../../../../helpers/services/topic-proposal.service';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicApproval } from './../../../../helpers/models/topic-approval';
import { TopicEvaluation, TopicStudent } from './../../../../helpers/models/topic-student';
import { Component, OnInit, Injector } from '@angular/core';

@Component({
  selector: 'app-approval-notification-form',
  templateUrl: './approval-notification-form.component.html',
  styleUrls: ['./approval-notification-form.component.scss']
})
export class ApprovalNotificationFormComponent implements OnInit {
  evaluations = Object.keys(TopicEvaluation).map(key => ({ evaluation: TopicEvaluation[key], value: key }))
  evaluationSelected: string = ''
  monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
  monthNumber = new Date().getMonth();
  month = this.monthNames[this.monthNumber]
  day = new Date().getDate();
  year = new Date().getFullYear();
  actualDate = this.day + ' de ' + this.month + ' del ' + this.year
  topicProposal: TopicProposal = null
  topicStudent: TopicStudent[] = []
  userData: User
  studentCi: string = ''
  trato: string = ''
  documentNumber: number = 0
  meetingNumber: string = ''
  meetingDate: Date
  observation:string=''
  observations: string[] = []
  documentNumberExists: boolean = false
  loading: boolean = true
  loadingButton: boolean = false
  private tokenService: TokenService
  private topicPropsalService: TopicProposalService
  private toastService: ToastService
  private topicApprovalService: TopicApprovalService
  private topicStudentService: TopicStudentService
  private router: Router;
  private confirmationSrv: ConfirmationDialogService
  private activeRoute: ActivatedRoute
  constructor(private injector: Injector) {
    this.router = this.injector.get<Router>(Router)
    this.topicStudentService = this.injector.get<TopicStudentService>(TopicStudentService)
    this.tokenService = this.injector.get<TokenService>(TokenService)
    this.topicPropsalService = this.injector.get<TopicProposalService>(TopicProposalService)
    this.confirmationSrv  = this.injector.get<ConfirmationDialogService>(ConfirmationDialogService)
    this.topicApprovalService = this.injector.get<TopicApprovalService>(TopicApprovalService)
    this.toastService = this.injector.get<ToastService>(ToastService)
    this.activeRoute=this.injector.get<ActivatedRoute>(ActivatedRoute)
    this.activeRoute.params.subscribe(params => {
      this.evaluationSelected = params['evaluation']
    });
  }

  ngOnInit(): void {
    this.getUserData()
    this.getProposalByTopicId();
    this.getStudentsTopicByTopicId();
  }
  getUserData() {
    this.tokenService.getUserInfo().subscribe(user => {
      this.userData = user
    })
  }


  getProposalByTopicId() {
    let topicId = this.activeRoute.snapshot.params.id

    this.topicPropsalService.getTopicProposalById(topicId).subscribe(topicProposal => {
      this.topicProposal = topicProposal
    }, err => {
      this.toastService.showWarn(err.error.message, 'No encontrado')
      this.topicProposal = null
      this.trato = ''
      this.documentNumber = 0
      this.meetingNumber = ''
      this.meetingDate = null
    })
  }

  getStudentsTopicByTopicId() {
    let topicId = this.activeRoute.snapshot.params.id
    this.topicPropsalService.getTopicProposalById(topicId).pipe(
      mergeMap(data => {
        return this.topicStudentService.getStudentsTopicByTopicId(data.topicStudent.topic.id)
      })
    ).subscribe(topicStudents => {
      this.loading = false
      this.topicStudent = topicStudents
    })
  }
  
  addObservation(text: string) {
    if (this.observation !== '') {
      this.observations.push(text)
      this.observation = ''
    }

  }
  removeObservation(index: number) {
    this.observations.splice(index, 1)
  }

  sendNotification(twoStudents: boolean, topicStudentOne: TopicStudent, topicStudentTwo?: TopicStudent) {
    topicStudentOne.topicEvaluation = TopicEvaluation[this.evaluationSelected]
    let studentOneName = '<b>' + topicStudentOne.student.name + ' ' + topicStudentOne.student.lastName + '</b>'
    if (twoStudents) {
      topicStudentTwo.topicEvaluation = TopicEvaluation[this.evaluationSelected]
      let notification: TopicApproval = {
        documentNumber: this.documentNumber,
        meetingNumber: this.meetingNumber,
        meetingDate: this.meetingDate,
        observations: this.observations,
        topicStudent: topicStudentOne,
        trato: this.trato,
        date: new Date()
      }
      let notification2: TopicApproval = {
        documentNumber: this.documentNumber + 1,
        meetingNumber: this.meetingNumber,
        meetingDate: this.meetingDate,
        observations: this.observations,
        topicStudent: topicStudentTwo,
        trato: this.trato,
        date: new Date()
      }
      let topicApprovalList: TopicApproval[] = [notification, notification2]
      let studentTwoName = '<b>' + topicStudentTwo.student.name + ' ' + topicStudentTwo.student.lastName + '</b>'
      this.confirmationSrv.confirmationDialog('¿Esta seguro de crear la propuesta de tema para los estudiantes ' +
        studentOneName + ' y ' + studentTwoName + '?', 'Notificación de aprobación cancelada').then(() => {
          this.loadingButton=true
          this.topicApprovalService.createApprovalNotificationPairStudents(topicApprovalList).subscribe(() => {
            this.toastService.showSuccess('Notificación creada con éxito', 'Creada')
            this.loadingButton=false
            this.router.navigateByUrl('/topicApproval')
          }, err => {
            this.documentNumberExists = true;
            this.loadingButton=false
            this.toastService.showWarn(err.error.message, 'Error')
          })
        })
    } else {
      let notification: TopicApproval = {
        documentNumber: this.documentNumber,
        meetingNumber: this.meetingNumber,
        meetingDate: this.meetingDate,
        observations: this.observations,
        topicStudent: topicStudentOne,
        trato: this.trato,
        date: new Date()
      }
      this.confirmationSrv.confirmationDialog('¿Esta seguro de crear la propuesta de tema para el estudianate ' +
        studentOneName + '?', 'Notificación de aprobación cancelada').then(() => {
          this.loadingButton=true
          this.topicApprovalService.generateApproval(notification).subscribe(() => {
            this.toastService.showSuccess('Notificación creada con éxito', 'Creada')
            this.router.navigateByUrl('/topicApproval')
            this.loadingButton=false
          }, err => {
            this.documentNumberExists = true;
            this.loadingButton=false
            this.toastService.showWarn(err.error.message, 'Error')
          })
        })
    }
  }

  validateDni(event: any) {
    let charCode = event.which
    if (charCode > 31 && (charCode < 48 || charCode > 57)) return false
    return true
  }
  cancel() {
    this.topicProposal = null
    this.trato = ''
    this.documentNumber = 0
    this.meetingNumber = ''
    this.meetingDate = null
    this.router.navigateByUrl('/home')
  }
}

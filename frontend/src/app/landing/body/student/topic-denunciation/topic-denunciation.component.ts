import { User } from 'src/app/helpers/models/user';
import { UserService } from './../../../../helpers/services/user.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { TopicDenunciationService } from './../../../../helpers/services/topic-denunciation.service';
import { mergeMap } from 'rxjs/operators';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { SemesterLevel, InvestigationLine, ProjectType, InvestigationModality } from './../../../../helpers/models/topic-denunciation';
import { Component, OnInit, Injector } from '@angular/core';
import { TopicDenunciation } from 'src/app/helpers/models/topic-denunciation';

@Component({
  selector: 'app-topic-denunciation',
  templateUrl: './topic-denunciation.component.html',
  styleUrls: ['./topic-denunciation.component.scss']
})
export class TopicDenunciationComponent implements OnInit {

  topicDenunciation: TopicDenunciation = {}
  semesterLevel = Object.keys(SemesterLevel).map(key => ({ semester: SemesterLevel[key], value: key }))
  investigationLine = Object.keys(InvestigationLine).map(key => ({ name: InvestigationLine[key], value: key }))
  proyectType = Object.keys(ProjectType).map(key => ({ name: ProjectType[key], value: key }))
  investigationModality = Object.keys(InvestigationModality).map(key => ({ name: InvestigationModality[key], value: key }))
  monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
  monthNumber = new Date().getMonth();
  month = this.monthNames[this.monthNumber]
  day = new Date().getDate()
  year = new Date().getFullYear()
  actualDate = this.day + ' de ' + this.month + ' del ' + this.year
  existDenunciation: boolean = false
  denunciationPreview: boolean = false;
  existTopicStudent: boolean = true
  edited: boolean = false
  topicDenunciationDate: string = ''
  director: User
  private topicStudentSrv: TopicStudentService
  private tokenService: TokenService
  private topicDenunciationSrv: TopicDenunciationService
  private confirmationDialogSrv: ConfirmationDialogService
  private messageSrv: MessageService
  private router: Router
  private toastSrv: ToastService
  private userSrv: UserService
  loading: boolean = true
  loadingButton: boolean = false

  constructor(private injector: Injector) {
    this.router = this.injector.get<Router>(Router);
    this.topicStudentSrv = this.injector.get<TopicStudentService>(TopicStudentService);
    this.tokenService = this.injector.get<TokenService>(TokenService);
    this.topicDenunciationSrv = this.injector.get<TopicDenunciationService>(TopicDenunciationService);
    this.toastSrv = this.injector.get<ToastService>(ToastService);
    this.userSrv = this.injector.get<UserService>(UserService);
    this.messageSrv = this.injector.get<MessageService>(MessageService);
    this.confirmationDialogSrv = this.injector.get<ConfirmationDialogService>(ConfirmationDialogService);
  }

  ngOnInit(): void {
    this.getTopicStudent()
    this.getTopicDenunciation()
    this.getCarrerDirector()
  }
  getTopicDenunciation() {
    this.loading = true
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        return this.topicDenunciationSrv.getTopicDenunciationByTopicStudentCi(userData.id)
      })
    ).subscribe((data) => {
      this.topicDenunciation = data
      this.existDenunciation = true
      console.log(data);
      
      this.formatDate(data.date)
      this.loading = false
    }, () => {
      this.loading = false
    })
  }

  formatDate(denunciationDate: Date) {
    let date = new Date(denunciationDate)
    let monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    let year = date.getFullYear()
    let month = monthNames[date.getMonth()]
    let day = date.getDate()
    this.topicDenunciationDate = day + ' de ' + month + ' del ' + year
  }

  getTopicStudent() {
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        return this.topicStudentSrv.getStudentTopicByStudentId(userData.id)
      })
    ).subscribe((data) => {
      this.topicDenunciation = Object.assign({ topicStudent: data })
    }, () => {
      this.existTopicStudent = false;
    })
  }


  showTopicDenunciationPreview() {
    this.denunciationPreview = true
  }

  closeTopicDenunciationPreviewModal() {
    this.denunciationPreview = false
  }

  sendTopicDenunciation(topicDenunciation: TopicDenunciation) {
    topicDenunciation.date = new Date()
    this.confirmationDialogSrv.confirmationDialog('¿Esta seguro de enviar la denuncia de tema?', 'Envío Cancelado').then(() => {
      this.loadingButton=true
      this.topicDenunciationSrv.createTopicDenunciation(topicDenunciation).subscribe(data => {
        this.messageSrv.add({ severity: 'info', summary: 'Confirmado', detail: String(data) })
      })
    }).then(() => {
      this.loadingButton=false
      setTimeout(() => {
        this.messageSrv.clear();
        this.router.navigateByUrl('/student/proposal');
        this.messageSrv.add({ severity: 'info', detail: 'Continua con el paso 4' })
      }, 1000)
    })
  }
  editDenunciation() {
    this.edited = !this.edited
  }
  updateTopicDenunciation(topicDenunciation: TopicDenunciation) {
    this.loadingButton=true
    this.topicDenunciationSrv.updateTopicDenunciation(topicDenunciation, topicDenunciation.id).subscribe(() => {
      this.toastSrv.showSuccess("Denuncia actualizada con éxito", "Guardado")
      this.loadingButton=false
      this.edited = false
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

import { UserService } from './../../../../helpers/services/user.service';
import { StudentComponent } from './../student.component';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { User } from './../../../../helpers/models/user';
import { TopicTable } from './../../../../helpers/models/topic-table';
import { mergeMap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { TopicService } from './../../../../helpers/services/topic.service';
import { TokenService } from './../../../../helpers/services/token.service';
import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { Topic } from 'src/app/helpers/models/topic';

@Component({
  selector: 'app-topic-selection',
  templateUrl: './topic-selection.component.html',
  styleUrls: ['./topic-selection.component.scss']
})
export class TopicSelectionComponent implements OnInit {
  career: string
  topics: TopicTable[] = []
  userData: User
  @ViewChild('dt') dt: Table | undefined
  selectedTopic: any
  topicsLength: number
  haveTopic: boolean = false
  showModalStudentTopic: boolean = false
  checkboxSelection: string
  studentFound: boolean = false
  userFound: User = null
  searchCI: string = ""
  incompleteSearchCi: boolean = false
  loading: boolean = false
  private tokenService: TokenService
  private topicService: TopicService
  private router: Router
  private topicStudentService: TopicStudentService
  private toastService: ToastService
  private confirmationService: ConfirmationDialogService
  private studentStepper: StudentComponent
  private userService: UserService
  constructor(private injector: Injector) {
    this.toastService = this.injector.get<ToastService>(ToastService)
    this.topicService = this.injector.get<TopicService>(TopicService)
    this.router = this.injector.get<Router>(Router)
    this.topicStudentService = this.injector.get<TopicStudentService>(TopicStudentService)
    this.tokenService = this.injector.get<TokenService>(TokenService)
    this.confirmationService = this.injector.get<ConfirmationDialogService>(ConfirmationDialogService)
    this.studentStepper = this.injector.get<StudentComponent>(StudentComponent)
    this.userService = this.injector.get<UserService>(UserService)
  }

  ngOnInit(): void {
    this.getUserTopicById()
    this.getTopics()
  }

  getUserTopicById() {
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        return this.topicStudentService.getStudentTopicByStudentId(userData.id)
      })
    ).subscribe((data) => {
      this.haveTopic = true
      if (data.paymentDenunciation === 'No pagado') {
        this.studentStepper.changeStepperState([false, false, true, true])
      } else {
        this.studentStepper.changeStepperState([false, false, false, false])
      }
      this.selectedTopic = {
        name: data.topic.name,
        articulation: data.topic.articulation,
        id: data.id,
        career: data.topic.career.name,
        paymentDenunciation: data.paymentDenunciation
      }
      this.loading = false
    }, () => {
      this.studentStepper.changeStepperState([true, true, true, true])
    })
  }
  applyFilterGlobal($event: any, stringVal: any) {
    this.dt.filterGlobal(($event.target as HTMLInputElement).value, stringVal)
  }

  validateDni(event: any) {
    let charCode = event.which
    if (charCode > 31 && (charCode < 48 || charCode > 57)) return false
    return true
  }

  getTopics() {
    this.loading = true
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        this.userData = userData
        this.career = userData.career.name
        return this.topicService.getTopicByCareer(userData.career.id)
      })
    ).subscribe(topicList => {
      this.topics = topicList
      this.loading = false
    })
  }

  onRowUnselect() {
    this.selectedTopic = null
  }

  onRowSelect() {
    this.showModalStudentTopic = true
  }

  getStudentByCi(currentStudenCi: string, studentToSearchCi: string, careerId: string) {
    if ((currentStudenCi !== studentToSearchCi) && studentToSearchCi.length === 10) {
      this.incompleteSearchCi = false
      this.userService.getUserByCiAndCareerId(studentToSearchCi, careerId).subscribe((userFound) => {
        this.userFound = userFound
        this.studentFound = true
      }, err => {
        this.studentFound = false
        this.toastService.showWarn(err.error.message, 'No encontrado')
      })
    } else {
      this.incompleteSearchCi = true
    }
  }

  confirmTopicSelection(user: User, topic: Topic, secondUser?: User) {
    if (this.checkboxSelection === 'pair') {
      let secondStudentName = '<b>' + secondUser.name + ' ' + secondUser.lastName + '</b>'
      this.confirmationService.confirmationDialog('¿Está seguro de elaborar este trabajo con el estudiante '
        + secondStudentName + '?', 'Cancelado').then(() => {
          this.loading = true
          topic.career = user.career
          let topicToSave: Topic = topic
          let topicStudent: TopicStudent = Object.assign({ topic: topicToSave, student: user, assignedDate: new Date() })
          let secondTopicStudent: TopicStudent = Object.assign({ topic: topicToSave, student: secondUser, assignedDate: new Date() })
          let topicStudentToSave: TopicStudent[] = [topicStudent, secondTopicStudent]
          this.topicStudentService.generateStudentPairTopic(topicStudentToSave).subscribe(() => {
            this.toastService.showSuccess('Tema asignado correctamente a los dos estudiantes', 'Tema elegido')
            this.loading = false
            this.studentStepper.changeStepperState([false, false, true, true])
            this.router.navigateByUrl('/student/selected')
          }, err => {
            this.loading = false
            this.toastService.showWarn(err.error.message, 'Error')
          })
        })
    } else {
      this.confirmationService.confirmationDialog('¿Está seguro de elaborar este trabajo individualmente?', 'Cancelado')
        .then(() => {
          this.loading = true
          topic.career = user.career
          let topicToSave: Topic = topic
          topicToSave.twoStudents = false
          let topicStudent = Object.assign({ topic: topicToSave, student: user, assignedDate: new Date() })
          this.topicStudentService.generateStudentTopic(topicStudent).subscribe(() => {
            this.toastService.showSuccess('Tema asignado correctamente al estudiante', 'Tema elegido')
            this.loading = false
            this.studentStepper.changeStepperState([false, false, true, true])
            this.router.navigateByUrl('/student/selected')
          }, err => {
            this.loading = false
            this.toastService.showWarn(err.error.message, 'Error')
          })
        })
    }
  }

  deleteStudentTopic(id: string) {
    this.confirmationService.confirmationDialog('¿Está seguro que quiere cancelar y ecoger otro tema?',
      'Operación cancelada').then(() => {
        this.topicStudentService.deleteTopicStudentById(id).subscribe(() => {
          this.toastService.showInfo('Ahora puede elegir otro tema', 'Tema elminado')
          this.selectedTopic = null
          this.haveTopic = false
          this.studentStepper.changeStepperState([true, true, true, true])
          this.getTopics()
        }, err => {
          this.toastService.showWarn(err.error.message, "No se puede eliminar")
        })
      })
  }

  navigateToSecondSteep() {
    this.router.navigateByUrl('/student/selected')
  }
}

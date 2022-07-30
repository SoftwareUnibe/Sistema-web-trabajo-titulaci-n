import { TutoringConstancy } from './../../../../helpers/models/tutoring-constancy';
import { User } from 'src/app/helpers/models/user';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { mergeMap } from 'rxjs/operators';
import { ToastService } from './../../../../helpers/services/toast.service';
import { TutoringHour } from '../../../../helpers/models/tutorig-hour';
import { ActivatedRoute } from '@angular/router';
import { TutorigConstancyService } from './../../../../helpers/services/tutorig-constancy.service';
import { TokenService } from './../../../../helpers/services/token.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-constancy-tutoring',
  templateUrl: './constancy-tutoring.component.html',
  styleUrls: ['./constancy-tutoring.component.scss']
})
export class ConstancyTutoringComponent implements OnInit {
  tutorigConstancy: TutoringHour[] = null
  handleConstancyDisplay: boolean = false
  nextNumber: number = 0
  edit: boolean = false
  newContancy: TutoringHour = {} as TutoringHour
  topicStudents: TopicStudent[]
  showPreview: boolean = false
  teacher: User
  hasTutoringConstancy: boolean = false
  tutoringDocument: TutoringConstancy = {} as TutoringConstancy
  loading: boolean = false

  constructor(private tutoringConstancySrv: TutorigConstancyService, private tokenSrv: TokenService,
    private activatedRoute: ActivatedRoute, private toastSrv: ToastService, private topicStudentSrv: TopicStudentService) {
    const id = this.activatedRoute.snapshot.params.id
    this.getTutoringConstancy(id)
    this.getTopicStudents(id)
    this.getTeacherInfo()
    this.existTutoringConstancy(id)
  }

  ngOnInit(): void { }

  existTutoringConstancy(id: string) {
    this.tutoringConstancySrv.existConstancyByTopic(id).subscribe(exist => {
      this.hasTutoringConstancy = exist
    })
  }

  getTeacherInfo() {
    this.tokenSrv.getUserInfo().subscribe(userInfo => {
      this.teacher = userInfo;
      this.tutoringDocument.tutor = userInfo
    })
  }

  openTutoringConstancyPreviewModal(hours: TutoringHour[]) {
    if (hours.length > 0) {
      this.showPreview = true
    } else {
      this.toastSrv.showWarn('Aun no ha registrado ninguna hora de tutoria', 'Aviso')
    }
  }

  closeTutoringConstancyPreviewModal() {
    this.showPreview = false
  }

  getTopicStudents(id: string) {
    this.topicStudentSrv.getStudentsTopicByTopicId(id).subscribe(topicStudents => {
      this.topicStudents = topicStudents
      this.tutoringDocument.topic = this.topicStudents[0].topic
    })
  }

  getTutoringConstancy(id: string) {
    this.tutoringConstancySrv.getTutoringConstancyByTopicId(id).subscribe((data) => {
      this.tutorigConstancy = data
      if (data.length === 0) {
        this.nextNumber = 1
      } else {
        this.nextNumber = data[data.length - 1].number + 1
      }
    })
  }

  showBasicDialog() {
    this.handleConstancyDisplay = true;
  }

  saveTutoringConstancy(constancy: TutoringHour) {
    this.loading = true
    constancy.number = this.nextNumber
    constancy.date = new Date()
    constancy.period = this.getAcademicPeriod()
    const id = this.activatedRoute.snapshot.params.id;
    constancy.topic = { id: id }
    this.tutoringConstancySrv.createTutoringConstancy(constancy).subscribe(() => {
      this.loading = false
      this.resetContancy()
      this.handleConstancyDisplay = false
      this.toastSrv.showSuccess('Constancia de tutoria guardada', 'Guardada')
      this.getTutoringConstancy(id)
    }, err => {
      this.loading = false
      this.toastSrv.showError(err.error.message)
    })
  }

  resetContancy() {
    this.newContancy = {} as TutoringHour
    this.edit = false
  }

  openDialogToEdit(constancy: TutoringHour) {
    this.handleConstancyDisplay = true
    this.newContancy = constancy
    this.edit = true
  }

  editTutoringConstancy(constancy: TutoringHour) {
    this.loading = true
    this.tutoringConstancySrv.updateTutoringConstancy(constancy, constancy.id).subscribe(() => {
      this.resetContancy()
      this.handleConstancyDisplay = false
      this.edit = false
      this.loading = false
      this.toastSrv.showSuccess('Constancia de tutoria actualizada', 'Actualizada')
    }, err => {
      this.toastSrv.showError(err.error.message)
      this.loading = false
    })
  }

  formatDate(dateToTransform: Date): string {
    let date = new Date(dateToTransform)
    let monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    let year = date.getFullYear()
    let month = monthNames[date.getMonth()]
    let day = date.getDate()
    return day + ' de ' + month + ' del ' + year
  }

  getAcademicPeriod(): string {
    let date = new Date()
    let monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    let year = date.getFullYear();
    let month = monthNames[date.getMonth()];
    if (month === "Abril" || month === "Mayo" || month === "Junio" || month === "Julio" || month === "Agosto") {
      return "ABRIL " + year + '-' + "AGOSTO " + year
    } else {
      return "SEPTIEMBRE " + year + '-' + "FEBRERO " + year + 1
    }
  }

  getTotalHours(array: TutoringHour[]) {
    let total: number = 0
    array.forEach(element => {
      total = total + element.hours
    })
    return total
  }

  generateConstancyInPdf(topicId: string, tutoringHours: TutoringHour[], TutoringDocument: TutoringConstancy) {
    TutoringDocument.generationDate = new Date
    if (this.getTotalHours(tutoringHours) < 16) {
      this.toastSrv.showWarn('Debe tener 16 horas de tutorías registradas para generar la constancia', 'Aviso')
    } else {
      this.toastSrv.showInfo('La descarga empezará en unos segundos','Aviso')
      this.tutoringConstancySrv.generateConstancy(TutoringDocument).pipe(
        mergeMap(() => {
          this.hasTutoringConstancy = true
          return this.tutoringConstancySrv.getConstancyInPdf(topicId)
        })
      ).subscribe(file => {
        let downloadURL = window.URL.createObjectURL(file)
        let link = document.createElement('a')
        link.href = downloadURL
        link.download = "Constancia.pdf"
        link.click()

      })
    }
  }
  downloadConstancy(topicId: string) {
    this.toastSrv.showInfo('La descarga empezará en unos segundos','Aviso')
    this.tutoringConstancySrv.getConstancyInPdf(topicId).subscribe(file => {
      let downloadURL = window.URL.createObjectURL(file)
      let link = document.createElement('a')
      link.href = downloadURL
      link.download = "Constancia.pdf"
      link.click()
    })
  }
}

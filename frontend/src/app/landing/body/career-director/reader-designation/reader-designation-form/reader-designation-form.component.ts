import { Topic } from 'src/app/helpers/models/topic';
import { ReaderService } from './../../../../../helpers/services/reader.service';
import { TopicStudent } from './../../../../../helpers/models/topic-student';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from './../../../../../helpers/services/token.service';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { ToastService } from './../../../../../helpers/services/toast.service';
import { User } from './../../../../../helpers/models/user';
import { UserService } from './../../../../../helpers/services/user.service';
import { ConfirmationDialogService } from './../../../../../helpers/services/confirmation-dialog.service';
import { Reader } from './../../../../../helpers/models/reader';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reader-designation-form',
  templateUrl: './reader-designation-form.component.html',
  styleUrls: ['./reader-designation-form.component.scss']
})
export class ReaderDesignationFormComponent implements OnInit {

  users: User[] = []
  topicStudent: TopicStudent[] = []
  actualDate = ''
  filteredResults: any[] = []
  temporalSelectedReader: User = null
  teacherSelectedAsReader: User = null;
  readerExists: boolean = false;
  maxDate: Date
  maxDateText = ''
  edit: boolean = false
  loading: boolean = true
  loadingButton: boolean = false

  constructor(private confirmationDialogSrv: ConfirmationDialogService, private userSrv: UserService,
    private topicStudentSrv: TopicStudentService, private readerSrv: ReaderService, private activatedRoute: ActivatedRoute,
    private toastSrv: ToastService, private router: Router) {
    const topicId = this.activatedRoute.snapshot.params.topicId
    this.userSrv.getPossibleReaders().subscribe(usersInfo => {
      this.users = usersInfo
    })
    this.topicStudentSrv.getStudentsTopicByTopicId(topicId).subscribe(topicStudentData => {
      this.topicStudent = topicStudentData
      this.loading = false
    })
  }

  ngOnInit(): void {
    this.getActualDate();
  }

  search(event: any) {
    let filteredTeachers: Reader[] = []
    let query = event.query.toLowerCase()
    filteredTeachers = this.users.filter(data => {
      let completeName = data.name + ' ' + data.secondName + ' ' + data.lastName + ' ' + data.secondLastName
      return completeName.toLowerCase().indexOf(query) > -1
    })
    this.filteredResults = filteredTeachers
  }

  setFieldValue() {
    this.teacherSelectedAsReader = this.temporalSelectedReader
    this.temporalSelectedReader = null
    this.readerExists = true
  }

  cleanField() {
    return null;
  }

  removeReader() {
    this.teacherSelectedAsReader = null;
    this.readerExists = false
  }

  selectedDate(event: any) {
    let dateSelected = new Date(event).toLocaleDateString('Ec', { weekday: 'long', day: '2-digit', month: 'long', year: 'numeric' })
    this.maxDateText = dateSelected
    this.edit = false
  }

  editDate() {
    this.edit = true
  }
  cancelEditDate() {
    this.edit = false
  }
  getActualDate() {
    let date = new Date();
    let day = date.toLocaleDateString('Ec', { day: '2-digit', month: 'long', year: 'numeric' })
    this.actualDate = day
  }

  createReaders() {
    let reader = {
      date: new Date(),
      maxDate: this.maxDate,
      reader: this.teacherSelectedAsReader,
      topic: this.topicStudent[0].topic,
      state: "Asignado"
    }
    this.confirmationDialogSrv.confirmationDialog('<b>¿Está seguro (a) de asignar al trabajo de titulación el lector seleccionado?</b> <br> <small>- Se enviará un notificación al correo del estudiante (es) y del docente asignado como lector</small>',
      'Lectores no asignados').then(() => {
        this.loadingButton = true
        this.readerSrv.createReader(reader).subscribe(response => {
          let message = Object.values(response)
          this.toastSrv.showSuccess(message[0], 'Éxito')
          this.router.navigateByUrl('readerDesignationList')
          this.loadingButton = false
        }, err => {
          this.toastSrv.showWarn(err.error.message, 'Advertencia')
          this.loadingButton = false
        })
      })
  }
}

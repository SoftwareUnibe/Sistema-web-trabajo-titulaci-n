import { EmailService } from './../../../../helpers/services/email.service';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { AuthService } from 'src/app/helpers/services/auth.service';
import { TutorDesignationService } from '../../../../helpers/services/tutor-designation.service';
import { TopicStudent } from '../../../../helpers/models/topic-student';
import { ToastService } from '../../../../helpers/services/toast.service';
import { map, mergeMap } from 'rxjs/operators';
import { User } from 'src/app/helpers/models/user';
import { Table } from 'primeng/table';
import { TokenService } from '../../../../helpers/services/token.service';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { faCoffee } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-anti-plagiarism-letter',
  templateUrl: './anti-plagiarism-letter.component.html',
  styleUrls: ['./anti-plagiarism-letter.component.scss']
})
export class AntiPlagiarismLetterComponent implements OnInit {

  @ViewChild('dt') dt: Table | undefined;
  user: User = null
  topicStudent: TopicStudent[] = []
  tableListInfo: any[] = null
  visibleDialog: boolean = false
  emptyLink: boolean = false
  emptyLinkMessage: string = ''
  sendingEmail: boolean = false;
  faCoffee = faCoffee
  constructor(private tutorDesignationSrv: TutorDesignationService, private tokenSrv: TokenService,
    private toastSrv: ToastService, private topicStudentSrv: TopicStudentService, private authSrv: AuthService,
    private confirmSrv: ConfirmationDialogService, private emailSrv: EmailService) {
    this.tokenSrv.getUserInfo().subscribe(userData => {
      this.user = userData
    })
  }

  ngOnInit(): void {
    this.getTopicStudents();

  }

  applyFilterTopics($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  getTopicStudents() {
    this.authSrv.getUserDetails().pipe(
      mergeMap((userDetails) => {
        return this.tutorDesignationSrv.getDesignationsTTByTeacherCi(userDetails.ci)
      })
    ).subscribe(data => {
      this.tableListInfo = data
    })
  }

  showDialog(topicId: string) {
    this.visibleDialog = true
    this.emptyLink = false
    this.topicStudentSrv.getStudentsTopicByTopicId(topicId).subscribe(data => {
      this.topicStudent = data
    })
  }

  customUpload(event: any, documentSelected: any, topicId: string) {
    this.confirmSrv.confirmationDialog('¿Está seguro (a) de enviar el documento seleccionado?', 'Se cancelo el envío').then(() => {
      this.sendingEmail = true
      let file = event.files[0]
      let documentData = new FormData();
      documentData.append('file', file)
      this.emailSrv.sendAntiPlagiarismLetterToStudent(documentData, topicId).subscribe(response => {
        this.sendingEmail = false
        let message = Object.values(response)
        this.toastSrv.showSuccess(message[0], 'Envío exitoso')
        this.getTopicStudents()
        documentSelected.clear();
        this.visibleDialog = false
      }, err => {
        console.log(err.error.message);
        this.sendingEmail = false
      })
    })
  }


  bytesToSize(bytes: any) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }


  deleteFile(documentSelected: any) {
    documentSelected.clear();
  }

}

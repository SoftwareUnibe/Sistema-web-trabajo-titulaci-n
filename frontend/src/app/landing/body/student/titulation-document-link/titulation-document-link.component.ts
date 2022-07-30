import { ReaderService } from './../../../../helpers/services/reader.service';
import { EmailService } from './../../../../helpers/services/email.service';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { User } from 'src/app/helpers/models/user';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { mergeMap } from 'rxjs/operators';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { TokenService } from './../../../../helpers/services/token.service';
import { Component, OnInit } from '@angular/core';
import { Reader } from 'src/app/helpers/models/reader';

@Component({
  selector: 'app-titulation-document-link',
  templateUrl: './titulation-document-link.component.html',
  styleUrls: ['./titulation-document-link.component.scss']
})
export class TitulationDocumentLinkComponent implements OnInit {

  topicStudent: TopicStudent = {}
  student: User = {}
  emptyLink: boolean = false;
  readerInfo: Reader = {}
  sendingEmail: boolean = false;
  loading: boolean = true
  constructor(private tokenSrv: TokenService, private topicStudentSrv: TopicStudentService, private confirmSrv: ConfirmationDialogService,
    private toastSrv: ToastService, private emailSrv: EmailService, private readerSrv: ReaderService) {
  }

  ngOnInit(): void {
    this.getTopicStudentAndStudentInfo()
    this.getReaderInfo()
  }

  getTopicStudentAndStudentInfo() {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap(student => {
        this.student = student
        return this.topicStudentSrv.getStudentTopicByStudentId(student.id)
      })
    ).subscribe(data => {
      this.topicStudent = data
    })
  }

  getReaderInfo() {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap(student => {
        return this.readerSrv.getReadersByStudentIdAndTopicId(student.id)
      })
    ).subscribe(data => {
      this.loading = false
      this.readerInfo = data
    }, () => {
      this.loading = false
    })
  }

  selectAllText(event: any) {
    event.target.select()
  }

  customUpload(event: any, fileSelected: any, studentId: string, readerEmail:string) {
    this.confirmSrv.confirmationDialog('¿Está seguro (a) de enviar el documento seleccionado?', 'Se cancelo el envío').then(() => {
      this.sendingEmail = true
      let file = event.files[0]
      let documentData = new FormData();
      documentData.append('file', file)
      this.emailSrv.sendDocumentToReader(documentData, studentId, readerEmail).subscribe(response => {
        this.sendingEmail = false
        let message = Object.values(response)
        this.toastSrv.showSuccess(message[0], 'Envío exitoso')
        fileSelected.clear();
      }, err => {
        console.log(err.error.message);
        this.sendingEmail = false
      })
    })
  }

  deleteFile(fileSelected: any) {
    fileSelected.clear();
  }


  bytesToSize(bytes: any) {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
}

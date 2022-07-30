import { ToastService } from './../../../../helpers/services/toast.service';
import { ReaderAccordanceService } from './../../../../helpers/services/reader-accordance.service';
import { Reader } from './../../../../helpers/models/reader';
import { mergeMap, catchError } from 'rxjs/operators';
import { TopicStudentService } from './../../../../helpers/services/topic-student.service';
import { TokenService } from './../../../../helpers/services/token.service';
import { ReaderService } from './../../../../helpers/services/reader.service';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { EvaluationService } from 'src/app/helpers/services/evaluation.service';

@Component({
  selector: 'app-student-readers-list',
  templateUrl: './student-readers-list.component.html',
  styleUrls: ['./student-readers-list.component.scss']
})
export class StudentReadersListComponent implements OnInit {

  errorObject = null
  reader$: Observable<Reader | null | undefined>
  readerTable = [1]
  loading: boolean = true
  hasObservations$: Observable<boolean>
  constructor(private readerSrv: ReaderService, private tokenSrv: TokenService, private topicStudentSrv: TopicStudentService,
    private readerAccordanceSrv: ReaderAccordanceService, private evaluationSrv: EvaluationService, private toastSrv: ToastService) {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap((student) => {
        return this.topicStudentSrv.getStudentTopicByStudentId(student.id)
      })
    ).subscribe(data => {
      let studentId = data.student.id
      this.hasObservations$ = this.readerAccordanceSrv.existsByTopicId(data.topic.id);
      this.loading = false
      this.reader$ = this.readerSrv.getReadersByStudentIdAndTopicId(studentId)
    })
  }

  ngOnInit(): void { }

  saveAsPdf(id: string) {
    this.initDownloadInfo()
    this.readerSrv.generateReaderLetterPdf(id).subscribe((data) => {
      let downloadURL = window.URL.createObjectURL(data);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.title = 'Carta_Lector'
      link.download = "Carta al Lector.pdf";
      link.click();
    })
  }
  saveEvaluationPdf(topidId: string) {
    this.initDownloadInfo()
    this.evaluationSrv.getEvaluationPdf(topidId).subscribe((data) => {
      let downloadURL = window.URL.createObjectURL(data)
      let link = document.createElement('a')
      link.href = downloadURL
      link.download = "EvaluaciónDelTT.pdf"
      link.click()
    })
  }
  saveObservations(topicId: string) {
    this.initDownloadInfo()
    this.readerAccordanceSrv.saveObservationsPdf(topicId).subscribe((data) => {
      let downloadURL = window.URL.createObjectURL(data);
      let link = document.createElement('a')
      link.href = downloadURL
      link.title = 'Observaciones'
      link.download = "Observaciones.pdf"
      link.click()
    }, () => {
      this.toastSrv.showWarn("No tiene observaciones registradas", "Aviso")
    })
  }
  saveAccordance(topicId: string) {
    this.initDownloadInfo()
    this.readerAccordanceSrv.saveAccordancePdf(topicId).subscribe((data) => {
      let downloadURL = window.URL.createObjectURL(data);
      let link = document.createElement('a')
      link.href = downloadURL
      link.title = 'Conformidad'
      link.download = "Conformidad.pdf"
      link.click()
    })
  }
  downloadReaderProcessResult(topicId: string) {
    this.initDownloadInfo()
    this.readerAccordanceSrv.generateReaderProcessResultLetterPDF(topicId).subscribe((data) => {
      let downloadURL = window.URL.createObjectURL(data)
      let link = document.createElement('a')
      link.href = downloadURL;
      link.title = 'Resultado proceso lectoría'
      link.download = "Resultado-proceso-lectoría.pdf"
      link.click()
    })
  }
  initDownloadInfo() {
    this.toastSrv.showInfo("Su documento se esta descargando", "Aviso")
  }
}

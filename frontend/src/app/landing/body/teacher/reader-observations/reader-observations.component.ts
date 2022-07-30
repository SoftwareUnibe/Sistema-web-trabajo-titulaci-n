import { ConfirmationService } from 'primeng/api';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { ReaderAccordanceService } from './../../../../helpers/services/reader-accordance.service';
import { ReaderObservations } from './../../../../helpers/models/reader-observations';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { TopicStudentService } from './../../../../helpers/services/topic-student.service';
import { Reader } from './../../../../helpers/models/reader';
import { ReaderService } from './../../../../helpers/services/reader.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastService } from './../../../../helpers/services/toast.service';
import { Component, HostListener, OnInit, ViewChild, ElementRef } from '@angular/core';
import { mergeMap } from 'rxjs/operators';
import { Topic } from 'src/app/helpers/models/topic';
import { ComponentCanDeactivate } from 'src/app/helpers/guards/can-deactivate.guard';

@Component({
  selector: 'app-reader-observations',
  templateUrl: './reader-observations.component.html',
  styleUrls: ['./reader-observations.component.scss']
})
export class ReaderObservationsComponent implements OnInit, ComponentCanDeactivate {

  observationsModal: boolean;
  observationMainList: string[] = []
  observationDetailList: string[] = []
  observationMain: string = ""
  observationDetail: string = ""
  edit: boolean = false;
  editIndex: number = 0;
  reader: Reader;
  topicStudents: TopicStudent[] = null
  hasChanges: boolean = false;
  w = false
  loading: boolean = false
  constructor(private toastSrv: ToastService, private activatedRoute: ActivatedRoute, private readerSrv: ReaderService,
    private topicStudentSrv: TopicStudentService, private readerAccordanceSrv: ReaderAccordanceService,
    private router: Router) {
    const id = this.activatedRoute.snapshot.params.id
    this.getReaderInfo(id)
  }

  ngOnInit(): void {
  }

  canDeactivate(): boolean {
    if (this.hasChanges) {
      return confirm('¿Estás seguro de salir? Las observaciones creadas no se guardarán.');
    }
    return true;
  }


  getReaderInfo(id: string) {
    this.readerSrv.getReaderById(id).pipe(
      mergeMap((reader) => {
        this.reader = reader
        return this.topicStudentSrv.getStudentsTopicByTopicId(reader.topic.id)
      })
    ).subscribe(studentList => {
      this.topicStudents = studentList
    })

  }
  addObservation() {
    this.observationMain = ''
    this.observationDetail = ''
    this.observationsModal = true;
  }

  registerObs(main: string, desc: string) {
    if (!main || !desc) {
      this.toastSrv.showWarn('No puede dejar ningun registro en blanco', 'Error')
    } else {
      if (this.edit) {
        this.observationMainList[this.editIndex] = main
        this.observationDetailList[this.editIndex] = desc
        this.observationsModal = false
        this.edit = false
      }
      else {
        this.observationMainList.push(main)
        this.observationDetailList.push(desc)
        this.observationsModal = false
        this.hasChanges = true
      }
    }

  }
  editObs(index: number) {
    this.editIndex = index
    this.observationsModal = true;
    this.observationMain = this.observationMainList[index]
    this.observationDetail = this.observationDetailList[index]
    this.edit = true
  }
  saveObservations(topic: Topic) {
    let readerObs: ReaderObservations = {
      mainObservation: this.observationMainList,
      descObservation: this.observationDetailList,
      topic: topic,
      date: new Date()
    }
    if (this.observationMainList.length == 0 || this.observationDetailList.length == 0) {
      this.toastSrv.showWarn("Debe agrear al menos una observación", 'Error')
    } else {
      this.hasChanges = false
      this.loading = true
      this.readerAccordanceSrv.saveObservations(readerObs).subscribe(() => {
        this.toastSrv.showSuccess('Se han guardado las observaciones', 'Operación exitosa')
        this.loading = false
        this.router.navigateByUrl('/teacher/lectures')
      }, err => {
        this.hasChanges = true
        this.loading = false
        this.toastSrv.showWarn(err.error.message, 'Revise las observaciones')
      })
    }
  }
}

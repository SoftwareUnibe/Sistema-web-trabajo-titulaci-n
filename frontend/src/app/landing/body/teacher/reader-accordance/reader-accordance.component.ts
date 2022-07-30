import { Topic } from 'src/app/helpers/models/topic';
import { ReaderObservations } from './../../../../helpers/models/reader-observations';
import { ToastService } from './../../../../helpers/services/toast.service';
import { ReaderAccordanceService } from './../../../../helpers/services/reader-accordance.service';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { ReaderAccordance } from './../../../../helpers/models/reader-accordance';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ReaderService } from './../../../../helpers/services/reader.service';
import { Component, OnInit } from '@angular/core';
import { Reader } from 'src/app/helpers/models/reader';
import { catchError, mergeMap } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-reader-accordance',
  templateUrl: './reader-accordance.component.html',
  styleUrls: ['./reader-accordance.component.scss']
})
export class ReaderAccordanceComponent implements OnInit {
  selectedValue: boolean;
  maxDate: Date
  maxDateText = ''
  topicStudents: TopicStudent[] = []
  observation: string = ''
  documentDate = new Date().toLocaleDateString('Ec', { weekday: 'long', day: '2-digit', month: 'long', year: 'numeric' })
  curretDate = new Date
  reader: Reader;
  readerAccordance: ReaderAccordance = {
    date: new Date(),
    maxDate: undefined,
    accordance: false,
    topic: undefined,
    id: '',
    observations: []
  }
  loading: boolean = false

  constructor(private readerSrv: ReaderService, private activatedRoute: ActivatedRoute, private topicStudentSrv: TopicStudentService, private confirmSrv: ConfirmationDialogService,
    private readerAccordanceService: ReaderAccordanceService, private router: Router, private toastSrv: ToastService) {
    const id = this.activatedRoute.snapshot.params.id;
    this.getReaderInfo(id)
  }

  ngOnInit(): void {
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
  selectedDate(event: any) {
    let dateSelected = new Date(event).toLocaleDateString('Ec', { weekday: 'long', day: '2-digit', month: 'long', year: 'numeric' })
    this.maxDateText = dateSelected
  }

  addObservation(text: string) {
    if (this.observation !== '') {
      this.readerAccordance.observations.push(text)
      this.observation = ''
    }
  }
  removeObservation(index: number) {
    this.readerAccordance.observations.splice(index, 1)
  }

  saveAccordance(topic: Topic) {
    let readerAccordance: ReaderAccordance = {
      date: new Date(),
      accordance: this.selectedValue,
      maxDate: this.maxDate,
      topic: topic,
      observations: this.readerAccordance.observations
    }
    if (!readerAccordance.accordance && this.readerAccordance.observations.length == 0) {
      this.toastSrv.showWarn('Debe añadir al menos una observación', 'No conformidad')
    } else {
      this.loading = true
      this.readerAccordanceService.saveAccordance(readerAccordance).subscribe(() => {
        this.toastSrv.showSuccess('Se ha creado el documento de conformidad o no conformidad', 'Operación exitosa')
        this.router.navigateByUrl('/teacher/lectures')
        this.loading = false
      }, err => {
        this.toastSrv.showWarn(err.error.message, 'Error')
        this.loading = false
      })
    }

  }

}

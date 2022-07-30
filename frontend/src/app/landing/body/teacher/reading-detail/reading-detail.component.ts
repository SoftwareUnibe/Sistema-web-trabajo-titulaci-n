import { ToastService } from './../../../../helpers/services/toast.service';
import { TutorDesignation } from 'src/app/helpers/models/tutor-designation';
import { TutorDesignationService } from './../../../../helpers/services/tutor-designation.service';
import { Topic } from 'src/app/helpers/models/topic';
import { EvaluationService } from './../../../../helpers/services/evaluation.service';
import { Evaluation, ProductEvaluation, TitulationWorkEvaluation, TitulationWorkEvaluationWithoutProduct } from './../../../../helpers/models/evaluation';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { Reader } from 'src/app/helpers/models/reader';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { map, mergeMap } from 'rxjs/operators';
import { ReaderService } from './../../../../helpers/services/reader.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-reading-detail',
  templateUrl: './reading-detail.component.html',
  styleUrls: ['./reading-detail.component.scss']
})
export class ReadingDetailComponent implements OnInit {

  reader: Reader
  evaluation: Evaluation = {} as Evaluation
  topicStudents: TopicStudent[] = null
  titulationWorkNotes: number[] = []
  productNotes: number[] = []
  date: Date = new Date()
  comentary: string = null
  handleComentaryDisplay: boolean = false
  titulationWorkEvaluation = null
  titulationProductEvaluation = ProductEvaluation
  id: string = ''
  $tutor: Observable<TutorDesignation>
  loading: boolean = false

  constructor(private activatedRoute: ActivatedRoute, private readerSrv: ReaderService, private topicStudentSrv: TopicStudentService,
    private evaluationSrv: EvaluationService, private router: Router, private tutorDesignationSrv: TutorDesignationService,
    private toastSrv: ToastService) {
    this.id = this.activatedRoute.snapshot.params.id;
    const id = this.activatedRoute.snapshot.params.id;
    this.getLectureDetails(id)
  }

  ngOnInit(): void { }

  getLectureDetails(id: string) {
    this.readerSrv.getReaderById(id).pipe(
      mergeMap((reader) => {
        this.reader = reader
        this.titulationWorkEvaluation = reader.topic.career.hasProduct ? TitulationWorkEvaluation : TitulationWorkEvaluationWithoutProduct
        return this.topicStudentSrv.getStudentsTopicByTopicId(reader.topic.id)
      })
    ).subscribe(studentList => {
      this.topicStudents = studentList
      this.$tutor = this.tutorDesignationSrv.getDesignationByTopicStudentId(studentList[0].id)
    })

  }
  calculateTotalWithoutProduct(titulationWorkNotes: number[]) {
    let sumNotes: number = 0
    if (titulationWorkNotes.length < 7) {
      return 0
    } else {
      titulationWorkNotes.forEach((note) => {
        sumNotes = note + sumNotes
      })
      return sumNotes / 10
    }

  }
  calculateTotal(titulationWorkNotes: number[], productNotes: number[]) {
    let sumNotes: number = 0
    if (titulationWorkNotes.length < 7 || productNotes.length < 4) {
      return 0
    } else {
      titulationWorkNotes.forEach((note, index) => {
        sumNotes = index < 5 ? (sumNotes + (note * 0.10)) : sumNotes + ((note * 2) * 0.05)
      })
      productNotes.forEach(note => {
        sumNotes = sumNotes + (note * 0.10)
      })
      return sumNotes
    }

  }

  saveEvaluation(evaluation: Evaluation, topic: Topic) {
    this.loading=true
    evaluation.topic = topic
    evaluation.productEvaluation = topic.career.hasProduct ? this.productNotes : null
    evaluation.workEvaluation = this.titulationWorkNotes
    evaluation.finalNote = topic.career.hasProduct ? this.calculateTotal(this.titulationWorkNotes, this.productNotes) : this.calculateTotalWithoutProduct(this.titulationWorkNotes)
    this.evaluationSrv.createEvaluation(evaluation).subscribe(() => {
      this.loading=false
      this.router.navigateByUrl('/teacher/lecture/observations/' + this.id)
      this.toastSrv.showSuccess('Evaluación del trabajo de titulación y el producto creado', 'Operación exitosa')
    }, err => {
      this.loading=false
      this.toastSrv.showWarn(err.error.message, 'Advertencia')
    }
    )
  }
}

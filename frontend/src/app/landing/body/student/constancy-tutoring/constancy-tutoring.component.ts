import { TutorigConstancyService } from './../../../../helpers/services/tutorig-constancy.service';
import { Table } from 'primeng/table';
import { TutorDesignation } from './../../../../helpers/models/tutor-designation';
import { TutorDesignationService } from './../../../../helpers/services/tutor-designation.service';
import { mergeMap } from 'rxjs/operators';
import { TopicApproval } from './../../../../helpers/models/topic-approval';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicApprovalService } from './../../../../helpers/services/topic-approval.service';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-constancy-tutoring',
  templateUrl: './constancy-tutoring.component.html',
  styleUrls: ['./constancy-tutoring.component.scss']
})
export class ConstancyTutoringComponent implements OnInit {
  @ViewChild('dt') dt: Table | undefined;
  TopicApproval: TopicApproval
  tutorDesignation: TutorDesignation
  hastutorDesignation: boolean = false
  hasTopicApproval: boolean = false
  tutorigConstancyList$: any = null
  loading: boolean = true
  constructor(private topicApprovalSrv: TopicApprovalService,
    private tutorDesignationSrv: TutorDesignationService, private tutorigConstancyService: TutorigConstancyService) { }

  ngOnInit(): void {
    this.getTopicApproval()
    this.getDesignatioTT()
  }
  applyFilterGlobal($event: any, stringVal: any) {
    this.dt.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
  getDesignatioTT() {
    this.topicApprovalSrv.getApprovalNotificationByStudent().pipe(
      mergeMap((topicApproval) => {
        return this.tutorDesignationSrv.getDesignationByTopicStudentId(topicApproval.topicStudent.id)
      })
    ).subscribe((tutorDesignation) => {
      this.tutorDesignation = tutorDesignation
      this.hastutorDesignation = true
      this.loading = false
      this.tutorigConstancyList$ = this.tutorigConstancyService.getTutoringConstancyByTopicId(tutorDesignation.topicStudent.topic.id)
    }, () => {
      this.hastutorDesignation = false
      this.loading = false
    })
  }
  getTopicApproval() {
    this.topicApprovalSrv.getApprovalNotificationByStudent().subscribe(approvalNotification => {
      this.TopicApproval = approvalNotification
      this.hasTopicApproval = true
    }, () => {
      this.hasTopicApproval = false
    })
  }
  formatDate(dateToTransform: Date): string {
    let date = new Date(dateToTransform);
    let monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    let year = date.getFullYear();
    let month = monthNames[date.getMonth()];
    let day = date.getDate();
    return day + ' de ' + month + ' del ' + year
  }
}

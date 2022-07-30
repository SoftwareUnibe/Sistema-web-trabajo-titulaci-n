import { ConfirmationDialogService } from './../../../helpers/services/confirmation-dialog.service';
import { Table } from 'primeng/table';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-financial',
  templateUrl: './financial.component.html',
  styleUrls: ['./financial.component.scss']
})
export class FinancialComponent implements OnInit {

  topicList: any[] = [];
  @ViewChild('dt') dt: Table | undefined;
  loading: boolean = false
  constructor(private topicStudentService: TopicStudentService, private confirmService: ConfirmationDialogService) {
    this.getTopics()
  }

  ngOnInit(): void {
  }
  applyFilterGlobal($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
  getTopics() {
    this.loading=true
    this.topicStudentService.getTopicsStudentPayments().subscribe(topics => {
      this.topicList = topics
      this.loading=false
    })
  }
  registerPayment(studentName: string, ci: string, payment: string) {
    this.confirmService.confirmationDialog('Se va a registrar el pago del estudiante: ' + '<b>' + studentName + '</b>'
      , 'Pago no registrado').then(() => {
        this.loading=true
        this.topicStudentService.updatePaymentStatus(ci, payment).subscribe(() => {
          this.loading=false
          this.getTopics()
        }, () => {
          this.loading=false
        })
      })
  }
}

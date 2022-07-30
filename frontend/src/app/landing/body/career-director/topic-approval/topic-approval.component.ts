import { User } from 'src/app/helpers/models/user';
import { UserService } from './../../../../helpers/services/user.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { Table } from 'primeng/table';
import { TopicApproval } from './../../../../helpers/models/topic-approval';
import { mergeMap } from 'rxjs/operators';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicApprovalService } from './../../../../helpers/services/topic-approval.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-topic-approval',
  templateUrl: './topic-approval.component.html',
  styleUrls: ['./topic-approval.component.scss']
})
export class TopicApprovalComponent implements OnInit {

  @ViewChild('dt') dt: Table | undefined;
  approvalTopics: any[] = null
  career = ''
  notificationDetail: TopicApproval = null
  topicApprovalPreview: boolean = false;
  topicApprovalDate: string = ''
  director:User
  constructor(private topciApprovalService: TopicApprovalService, private tokenService: TokenService,
    private toastService: ToastService, private userSrv:UserService) {
  }

  ngOnInit(): void {
    this.getApprovalTopics()
    this.getCarrerDirector()
  }
  applyFilterTopics($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  closeTopicApprovalPreviewModal() {
    this.topicApprovalPreview = false;
  }

  getApprovalTopics() {
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        this.career = userData.career.name
        return this.topciApprovalService.getApprovalsByCareer(userData.career.id)
      })
    ).subscribe(approvalTopics => {
      this.approvalTopics = approvalTopics
      console.log(approvalTopics);

    })
  }
  getNotificationDetail(id: string) {
    this.topciApprovalService.getApprovalById(id).subscribe(notificationData => {
      this.formatDate(notificationData.date);
      this.notificationDetail = notificationData
      this.topicApprovalPreview = true
    })
  }
  getNotificationPdf(id: string, name: string) {
    this.topciApprovalService.getNotificationPdf(id).subscribe((data) => {
      this.toastService.showInfo('Descargando...', 'Notificación')
      let studentName = name;
      let blob = new Blob([data], { type: 'application/pdf' });
      var downloadURL = window.URL.createObjectURL(data);
      var link = document.createElement('a');
      link.href = downloadURL;
      link.title = 'Notificación'
      link.download = studentName + "-NotificaciónApr.pdf";
      link.click();
    }, err => {
      console.log(err);
    })
  }

  formatDate(topicApprovalDate: Date) {
    let date = new Date(topicApprovalDate);
    let monthNames = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"]
    let year = date.getFullYear();
    let month = monthNames[date.getMonth()];
    let day = date.getDate();
    this.topicApprovalDate = day + ' de ' + month + ' del ' + year
  }
  getCarrerDirector(){
    this.tokenService.getUserInfo().pipe(
      mergeMap((user)=>{
        return this.userSrv.getUserByRoleAndCareerId('ROLE_CAREER_DIRECTOR', user.career.id);
      })
    ).subscribe((director=>{
      this.director=director[0]
    }))
  }
}

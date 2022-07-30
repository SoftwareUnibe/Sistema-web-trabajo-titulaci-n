import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TopicApproval } from './../models/topic-approval';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TopicApprovalService {

  topicApprovalURL = 'http://localhost:8090/approval_notification'
  constructor(private httpClient: HttpClient) { }

  getApprovalsByCareer(careerID: string): Observable<any[]> {
    return this.httpClient.get<any[]>(this.topicApprovalURL + '/career/' + careerID,{withCredentials: true})
  }
  getApprovalById(id: string): Observable<TopicApproval> {
    return this.httpClient.get<TopicApproval>(this.topicApprovalURL + '/' + id,{withCredentials: true});
  }

  generateApproval(approval: TopicApproval): Observable<string> {
    return this.httpClient.post<string>(this.topicApprovalURL, approval,{withCredentials: true});
  }

  createApprovalNotificationPairStudents(approval: TopicApproval[]): Observable<string> {
    return this.httpClient.post<string>(this.topicApprovalURL + '/pair/', approval,{withCredentials: true});

  }

  getNotificationPdf(topicStudentID: string) {
    return this.httpClient.get(this.topicApprovalURL + '/pdf/' + topicStudentID, { responseType: 'blob',withCredentials: true })
  }

  getApprovalNotificationByStudent(): Observable<TopicApproval> {
    return this.httpClient.get<TopicApproval>(this.topicApprovalURL + '/student',{withCredentials: true} )
  }
}

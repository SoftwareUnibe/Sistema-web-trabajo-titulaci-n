import { TopicApproval } from './../models/topic-approval';
import { TopicStudent } from './../models/topic-student';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TopicStudentService {

  private topicStudentURL = 'http://localhost:8090/student_topic'
  constructor(private httpCliente: HttpClient) { }

  getStudentTopicByStudentId(studentID: string): Observable<TopicStudent> {
    return this.httpCliente.get<TopicStudent>(this.topicStudentURL + '/student/' + studentID,{withCredentials: true});
  }
  getStudentTopicByStatusAndCareer(status: string, careerID: string): Observable<TopicStudent[]> {
    return this.httpCliente.get<TopicStudent[]>(this.topicStudentURL + '/career/' + careerID + '/topic_status/' + status,{withCredentials: true});
  }
  generateStudentTopic(topicStudent: TopicStudent): Observable<string> {
    return this.httpCliente.post<string>(this.topicStudentURL, topicStudent,{withCredentials: true});
  }
  generateStudentPairTopic(topicStudent: TopicStudent[]): Observable<string> {
    return this.httpCliente.post<string>(this.topicStudentURL + '/pair', topicStudent,{withCredentials: true});
  }
  deleteTopicStudentById(studentID: string): Observable<string> {
    return this.httpCliente.delete<string>(this.topicStudentURL + '/' + studentID,{withCredentials: true});
  }
  getTopicsStudentPayments(): Observable<any[]> {
    return this.httpCliente.get<any[]>(this.topicStudentURL + '/payment',{withCredentials: true});
  }

  getStudentsTopicByTopicId(topicId: string): Observable<TopicStudent[]> {
    return this.httpCliente.get<TopicStudent[]>(this.topicStudentURL + '/topic/' + topicId,{withCredentials: true});
  }

  getTopicStudentsByUserCi(userCareerId: string): Observable<TopicStudent[]> {
    return this.httpCliente.get<TopicStudent[]>(this.topicStudentURL + '/assignLector/' + userCareerId,{withCredentials: true});

  }
  updatePaymentStatus(studentCi: string, payment: string): Observable<string> {
    return this.httpCliente.patch<string>(this.topicStudentURL + '/student/' + studentCi, payment,{withCredentials: true})
  }

  evaluationProposal(id: string, topicStudent: TopicStudent): Observable<any> {
    return this.httpCliente.patch(this.topicStudentURL + '/' + id, topicStudent,{withCredentials: true})
  }
  verifiedPaymentStatus(topicId: string): Observable<boolean> {
    return this.httpCliente.get<boolean>(this.topicStudentURL + '/verifiedPayment/' + topicId,{withCredentials: true});
  }
}

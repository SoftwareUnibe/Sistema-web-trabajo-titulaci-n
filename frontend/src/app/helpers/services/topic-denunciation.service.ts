import { TopicDenunciation } from 'src/app/helpers/models/topic-denunciation';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TopicDenunciationService {
  topicDenunciationURL = 'http://localhost:8090/topic_denunciation'
  constructor(private httpCliente: HttpClient) { }

  getTopicDenunciationByTopicStudentId(topicStudentID: string): Observable<TopicDenunciation> {
    return this.httpCliente.get<TopicDenunciation>(this.topicDenunciationURL + '/' + topicStudentID,{withCredentials: true});
  }

  getTopicDenunciationByStudentId(): Observable<TopicDenunciation> {
    return this.httpCliente.get<TopicDenunciation>(this.topicDenunciationURL,{withCredentials: true});
  }

  createTopicDenunciation(topicDenunciation: TopicDenunciation): Observable<string> {
    return this.httpCliente.post<string>(this.topicDenunciationURL, topicDenunciation,{withCredentials: true});
  }

  generateTopicDenunciationPdf(topicStudentID: string) {
    return this.httpCliente.get(this.topicDenunciationURL + '/pdf/' + topicStudentID, { responseType: 'blob',withCredentials: true });
  }
  
  getTopicDenunciationByTopicStudentCi(studentId: string): Observable<TopicDenunciation> {
    return this.httpCliente.get<TopicDenunciation>(this.topicDenunciationURL + '/student/' + studentId,{withCredentials: true});
  }
  updateTopicDenunciation(topicDenunciation: TopicDenunciation, id: string): Observable<string>{
    console.log(topicDenunciation);
    return this.httpCliente.put<string>(this.topicDenunciationURL+'/update/'+id, topicDenunciation,{withCredentials: true});
  }
}

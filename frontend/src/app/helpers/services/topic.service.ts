import { TopicTable } from './../models/topic-table';
import { Topic } from './../models/topic';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private topicURL = 'http://localhost:8090/topic'

  constructor(private httpClient: HttpClient) { }
  getTopicByCareer(careerID: string): Observable<TopicTable[]> {
    return this.httpClient.get<TopicTable[]>(this.topicURL + '/career/' + careerID,{withCredentials: true});
  }
  getTopicByID(id: string): Observable<Topic> {
    return this.httpClient.get<Topic>(this.topicURL + '/' + id,{withCredentials: true});
  }
  createTopic(topic: Topic): Observable<String> {
    return this.httpClient.post<String>(this.topicURL, topic,{withCredentials: true});
  }
  updateTopic(id: string, topic: Topic): Observable<String> {
    return this.httpClient.put<String>(this.topicURL + '/' + id, topic,{withCredentials: true});
  }
  getTopicsSize(id: string): Observable<number> {
    return this.httpClient.get<number>(this.topicURL + '/size/' + id,{withCredentials: true})
  }
  updateTopicStatus(id:string):Observable<string>{
    return this.httpClient.patch<string>(this.topicURL +'/'+id,'Ejecutado',{withCredentials: true})
  }
}

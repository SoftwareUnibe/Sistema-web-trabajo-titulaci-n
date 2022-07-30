import { TutoringConstancy } from './../models/tutoring-constancy';
import { TutoringHour } from '../models/tutorig-hour';
import { TutorDesignation } from './../models/tutor-designation';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TutorigConstancyService {
  private tutoringURL="http://localhost:8090/tutoring"

  constructor(private httpClient:HttpClient) { }

  getTutoringConstancyByTopicId(topicId:string):Observable<TutoringHour[]>{
    return this.httpClient.get<TutoringHour[]>(this.tutoringURL+'/'+topicId,{withCredentials: true});
  }
  createTutoringConstancy(tutoringConstancy:TutoringHour):Observable<any>{
    return this.httpClient.post<any>(this.tutoringURL, tutoringConstancy,{withCredentials: true});
  }
  updateTutoringConstancy(tutoringConstancy:TutoringHour, tutoringConstancyId:string):Observable<string>{
    return this.httpClient.put<string>(this.tutoringURL+'/'+tutoringConstancyId, tutoringConstancy,{withCredentials: true});
  }
  getConstancyInPdf(topicId:string){
    return this.httpClient.get(this.tutoringURL+'/pdf/'+topicId,{responseType:'blob', withCredentials: true});
  }
  existConstancyByTopic(topicId:string):Observable<boolean>{
    return this.httpClient.get<boolean>(this.tutoringURL+'/tutoringConstancy/'+topicId,{withCredentials: true});
  }
  generateConstancy(constancy:TutoringConstancy){
    return this.httpClient.post(this.tutoringURL+'/createConstancy', constancy,{withCredentials: true});
  }
}

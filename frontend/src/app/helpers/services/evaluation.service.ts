import { Observable } from 'rxjs';
import { Evaluation } from './../models/evaluation';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EvaluationService {
  evaluationURL = 'http://localhost:8090/evaluation'
  constructor(private httpClient: HttpClient) { }

  createEvaluation(evaluation: Evaluation): Observable<any> {
    return this.httpClient.post<any>(this.evaluationURL, evaluation, { withCredentials: true });
  }
  getEvaluationPdf(topicId: string) {
    return this.httpClient.get(this.evaluationURL + '/pdf/' + topicId, { responseType: 'blob', withCredentials: true })
  }
}

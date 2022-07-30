import { Observable } from 'rxjs';
import { ReaderObservations } from './../models/reader-observations';
import { ReaderAccordance } from './../models/reader-accordance';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReaderAccordanceService {
  accordanceURL = 'http://localhost:8090/readerAccordance'
  observationsURL = 'http://localhost:8090/readerObservations'
  constructor(private httpClient: HttpClient) { }
  saveAccordance(readerAccordance: ReaderAccordance) {
    return this.httpClient.post(this.accordanceURL + '/create', readerAccordance, { withCredentials: true });
  }
  saveObservations(readerObservations: ReaderObservations) {
    return this.httpClient.post(this.observationsURL + '/create', readerObservations, { withCredentials: true });
  }
  saveAccordancePdf(topicId: string) {
    return this.httpClient.get(this.accordanceURL + '/pdf/' + topicId, { responseType: 'blob', withCredentials: true })
  }
  saveObservationsPdf(topicId: string) {
    return this.httpClient.get(this.observationsURL + '/pdf/' + topicId, { responseType: 'blob', withCredentials: true })
  }

  generateReaderProcessResultLetterPDF(topicId: string) {
    return this.httpClient.get(this.accordanceURL + '/readerProcessResultPDF/' + topicId, { responseType: 'blob', withCredentials: true })
  }

  existsByTopicId(topicId: string): Observable<boolean> {
    return this.httpClient.get<boolean>(this.observationsURL + '/verify/' + topicId, { withCredentials: true })

  }
}

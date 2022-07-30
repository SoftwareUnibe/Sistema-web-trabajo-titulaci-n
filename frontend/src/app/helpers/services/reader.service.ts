import { Reader } from './../models/reader';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReaderService {
  readerURL = 'http://localhost:8090/reader'
  constructor(private httpClient: HttpClient) { }

  createReader(reader: Reader): Observable<string> {
    return this.httpClient.post<string>(this.readerURL + '/createReader', reader, { withCredentials: true });
  }

  getReadersByStudentIdAndTopicId(studentId: string): Observable<Reader> {
    return this.httpClient.get<Reader>(this.readerURL + '/studentReaders/' + studentId, { withCredentials: true })
  }

  getListByReaderCi(readerCi: string): Observable<Reader[]> {
    return this.httpClient.get<Reader[]>(this.readerURL + '/listByReader/' + readerCi, { withCredentials: true })
  }
  getReaderById(readerID: string): Observable<Reader> {
    return this.httpClient.get<Reader>(this.readerURL + '/reader/' + readerID, { withCredentials: true });
  }

  generateReaderLetterPdf(readerId: string) {
    return this.httpClient.get(this.readerURL + '/pdf/' + readerId, { responseType: 'blob', withCredentials: true });
  }
  checkStatusToCreateCalendar(careerId: string) {
    return this.httpClient.get(this.readerURL + '/checkStatus/' + careerId, { withCredentials: true })
  }
  getListByCareer(careerId:string):Observable<Reader[]>{
    return this.httpClient.get<Reader[]>(this.readerURL+'/all/'+careerId,{withCredentials:true})
  }
  resetReaderProcess(careerId:string){
    return this.httpClient.delete(this.readerURL+'/resetProcess/'+careerId,{withCredentials:true})
  }
}

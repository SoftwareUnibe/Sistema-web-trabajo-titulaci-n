import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private emailURL = 'http://localhost:8090/email'
  constructor(private httpClient: HttpClient) { }

  sendDocumentToReader(document: any, studentId: string, readerEmail: string): Observable<FormData> {
    return this.httpClient.post<FormData>(this.emailURL + '/sendToReader/' + studentId + '/' + readerEmail, document)
  }

  sendAntiPlagiarismLetterToStudent(document: any, topicId: string): Observable<FormData> {
    return this.httpClient.post<FormData>(this.emailURL + '/sendToStudent/' + topicId, document)
  }

  sendResetPasswordEmail(emailDto: any) {
    return this.httpClient.post(this.emailURL + '/sendResetPasswordEmail', emailDto)
  }

  resetPassword(dto: any): Observable<string> {
    return this.httpClient.post<string>(this.emailURL + '/change-password', dto)
  }

  checkIfUserHasTokenPassword(token: string): Observable<boolean> {
    return this.httpClient.get<boolean>(this.emailURL + '/checkTokenPassword/' + token)
  }
}

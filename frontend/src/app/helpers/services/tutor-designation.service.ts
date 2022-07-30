import { TutorDesignationTableDto } from './../models/tutor-designation-table-dto';
import { Observable } from 'rxjs';
import { TutorDesignation } from './../models/tutor-designation';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TutorDesignationService {

  private tutorDesignationURL = 'http://localhost:8090/designationTT'
  constructor(private httpClient: HttpClient) { }

  createDesignationTT(tutorDesignation: TutorDesignation): Observable<string> {
    return this.httpClient.post<string>(this.tutorDesignationURL, tutorDesignation,{withCredentials: true});
  }

  createDesignationTTPairStudents(tutorDesignation: any): Observable<string> {
    return this.httpClient.post<string>(this.tutorDesignationURL + '/pair/', tutorDesignation,{withCredentials: true});
  }

  getAll(): Observable<TutorDesignation[]> {
    return this.httpClient.get<TutorDesignation[]>(this.tutorDesignationURL,{withCredentials: true});

  }

  getAllTutorDesignationDto(userCi: string): Observable<TutorDesignationTableDto[]> {
    return this.httpClient.get<TutorDesignationTableDto[]>(this.tutorDesignationURL + '/designationTTDto/' + userCi,{withCredentials: true});
  }

  getDesignationByTopicStudentId(topicStudentId: string): Observable<TutorDesignation> {
    return this.httpClient.get<TutorDesignation>(this.tutorDesignationURL + '/topic/' + topicStudentId,{withCredentials: true});
  }

  getDesignationByTeacher(teacherCi: string): Observable<TutorDesignationTableDto[]> {
    return this.httpClient.get<TutorDesignationTableDto[]>(this.tutorDesignationURL + '/teacher/' + teacherCi,{withCredentials: true});
  }

  getDesignationsTTByTeacherCi(userCi: string): Observable<TutorDesignation[]> {
    return this.httpClient.get<TutorDesignation[]>(this.tutorDesignationURL + '/antiPlagiarismLetterSent/' + userCi,{withCredentials: true});
  }

  downloadStudentDesignationLetterAsPdf(topicStudentId: string) {
    return this.httpClient.get(this.tutorDesignationURL + '/pdf/' + topicStudentId, { responseType: 'blob', withCredentials: true });
  }


  downloadTutorDesignationLetterAsPdf(topicStudentId: string, careerDirectorCi: string) {
    return this.httpClient.get(this.tutorDesignationURL + '/pdf/tutor/' + topicStudentId + '/careerDirector/' + careerDirectorCi, { responseType: 'blob', withCredentials: true });
  }
}

import { CalendarDetail } from './../models/calendar-detail';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CalendarDetailService {

  calendarDetailURL = 'http://localhost:8090/calendarDetail'
  constructor(private httpClient: HttpClient) { }

  createCalendar(calendarDetail: any): Observable<string> {
    return this.httpClient.post<string>(this.calendarDetailURL, calendarDetail, { withCredentials: true })
  }

  generateFinalCalendar(calendarDetail: any): Observable<string> {
    return this.httpClient.put<string>(this.calendarDetailURL + '/finalCalendar', calendarDetail, { withCredentials: true })
  }

  getCalenderDetailInfo(careerId: string): Observable<any> {
    return this.httpClient.get<any>(this.calendarDetailURL + '/' + careerId, { withCredentials: true })
  }

  getByFinalDegreeCalendarId(finalDegreeCalendarId: string): Observable<CalendarDetail[]> {
    return this.httpClient.get<CalendarDetail[]>(this.calendarDetailURL + '/calendarDetails/' + finalDegreeCalendarId, { withCredentials: true })
  }

  updateByCalendarId(calendarId: string, calendarToUpdate: CalendarDetail): Observable<string> {
    return this.httpClient.put<string>(this.calendarDetailURL + '/calendar/' + calendarId, calendarToUpdate, { withCredentials: true })
  }

  checkIfCalendarDetailExists(finalDegreeCalendarId: string): Observable<boolean> {
    return this.httpClient.get<boolean>(this.calendarDetailURL + '/existsCalendarDetail/' + finalDegreeCalendarId, { withCredentials: true })
  }

  getCalendarStatus(studentsIds: string[]): Observable<boolean> {
    return this.httpClient.get<boolean>(this.calendarDetailURL + '/status/' + studentsIds, { withCredentials: true })
  }
}

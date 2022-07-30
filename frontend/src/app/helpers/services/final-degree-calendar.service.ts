import { Observable } from 'rxjs';
import { FinalDegreeCalendar } from './../models/final-degree-calendar';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FinalCalendarDegreeService {

  private finalCalendarDegreeURL = 'http://localhost:8090/finalDegreeCalendar'
  constructor(private httpClient: HttpClient) { }

  createFinalDegreeCalendar(finalDegreeCalendar: FinalDegreeCalendar): Observable<string> {
    return this.httpClient.post<string>(this.finalCalendarDegreeURL, finalDegreeCalendar, { withCredentials: true });
  }

  getAllFinalDegreesCalendar(): Observable<FinalDegreeCalendar[]> {
    return this.httpClient.get<FinalDegreeCalendar[]>(this.finalCalendarDegreeURL, { withCredentials: true })
  }
  getFinalDegreeCalendarById(finalDegreeCalendarId: string): Observable<FinalDegreeCalendar> {
    return this.httpClient.get<FinalDegreeCalendar>(this.finalCalendarDegreeURL + '/' + finalDegreeCalendarId, { withCredentials: true })
  }
  getFinalDegreeCalendarByCareerId(careerId: string): Observable<FinalDegreeCalendar[]> {
    return this.httpClient.get<FinalDegreeCalendar[]>(this.finalCalendarDegreeURL + '/list/' + careerId, { withCredentials: true })
  }
}


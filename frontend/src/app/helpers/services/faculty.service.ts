import { HttpClient } from '@angular/common/http';
import { Faculty } from './../models/faculty';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FacultyService {
  private facultyURL = 'http://localhost:8090/faculty'
  constructor(private httpClient:HttpClient) { }
  getAll(): Observable<Faculty[]> {
    return this.httpClient.get<Faculty[]>(this.facultyURL,{withCredentials: true})
  }
  create(faculty:Faculty){
    return this.httpClient.post(this.facultyURL,faculty,{withCredentials: true})
  }
  edit(faculty:Faculty){
    return this.httpClient.put(this.facultyURL,faculty,{withCredentials: true})
  }
}

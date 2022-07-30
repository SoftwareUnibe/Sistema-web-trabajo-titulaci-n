import { Career } from './../models/career';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CareerService {
  private careerURL = 'http://localhost:8090/career'
  constructor(private httpClient: HttpClient) { }

  getAllCarrers(): Observable<Career[]> {
    return this.httpClient.get<Career[]>(this.careerURL)
  }
  saveCareer(career:Career){
    return this.httpClient.post(this.careerURL,career,{withCredentials: true})
  }
  editCareer(career:Career){
    return this.httpClient.put(this.careerURL,career,{withCredentials: true})
  }
}

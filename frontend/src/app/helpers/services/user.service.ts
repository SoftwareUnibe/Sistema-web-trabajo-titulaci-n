import { Observable } from 'rxjs';
import { User } from 'src/app/helpers/models/user';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  userURL = "http://localhost:8090/user"
  constructor(private httpClient: HttpClient) { }
  getUserByCiAndCareerId(ci: string, careerId: string): Observable<User> {
    return this.httpClient.get<User>(this.userURL + '/' + ci + '/' + careerId, { withCredentials: true });
  }
  getUserByRoleAndCareerId(role: string, careerId: string): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userURL + '/role/' + role + '/career/' + careerId, { withCredentials: true });
  }

  getByRoleName(userRole: string): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userURL + '/role/' + userRole, { withCredentials: true })
  }

  getByRolesNames(roles: string[]): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userURL + '/roleNames/' + roles, { withCredentials: true })
  }

  getPossibleReaders(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userURL + '/readers', { withCredentials: true });
  }

}

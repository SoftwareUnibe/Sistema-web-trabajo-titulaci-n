import { JwtDTO } from './../models/jwt-dto';
import { LoginUser } from './../models/login-user';
import { User } from './../models/user';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user$: Observable<User | null | undefined>
  private authURL = 'http://localhost:8090/auth/'
  constructor(private httpClient: HttpClient) { }

  register(user: User): Observable<any> {
    return this.httpClient.post<any>(this.authURL + 'register', user)
  }
  login(loginUser: LoginUser) {
    return this.httpClient.post(this.authURL + 'login', loginUser, { withCredentials: true })
  }
  refresh(): Observable<JwtDTO> {
    return this.httpClient.post<JwtDTO>(this.authURL + 'refresh', { withCredentials: true });
  }
  logOut() {
    return this.httpClient.get(this.authURL + 'logout', { withCredentials: true })
  }
  getUserDetails(): Observable<User> {
    return this.httpClient.get<User>(this.authURL + 'details', { withCredentials: true });
  }
  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.authURL + 'admin/allUsers', { withCredentials: true });
  }
  verificateUser(userCi: string): Observable<string> {
    return this.httpClient.patch<string>(this.authURL + 'admin/verifiedUser/' + userCi, true, { withCredentials: true });
  }
  updateUserInfo(user: User): Observable<string> {
    return this.httpClient.put<string>(this.authURL + 'update', user, { withCredentials: true });
  }

  
}

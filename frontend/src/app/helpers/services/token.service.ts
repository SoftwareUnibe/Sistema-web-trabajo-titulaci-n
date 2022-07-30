import { User } from './../models/user';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/helpers/services/auth.service';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
const IS_LOGGED = 'isLogged';
@Injectable({
  providedIn: 'root'
})
export class TokenService {

  roles: Array<string> = ['ROLE_ADMIN', 'ROLE_USER', 'ROLE_STUDENT', 'ROLE_SECRETARY', 'ROLE_CAREER_DIRECTOR', 'ROLE_LIABLE_TT',
    'ROLE_READER', 'ROLE_DEAN', 'ROLE_ACADEMIC_DIRECTOR', 'ROLE_TEACHER', 'ROLE_FINANCIAL'];

  constructor(
    private router: Router, private authService: AuthService
  ) {
  }

  public setSessionState(state: string): void {
    window.localStorage.removeItem(IS_LOGGED);
    window.localStorage.setItem(IS_LOGGED, state);
  }

  public getState() {
    let state = localStorage.getItem(IS_LOGGED);
    return state
  }

  public isLogged(): boolean {
    if (this.getState()) {
      return true;
    }
    return false;
  }

  public logOut(): void {
    this.authService.logOut().subscribe(()=>{
      this.router.navigate(['/auth']);
      window.localStorage.clear();
    })
    
  }
  getUserInfo() {
    return this.authService.getUserDetails()
  }
}

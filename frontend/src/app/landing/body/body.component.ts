import { Router } from '@angular/router';
import { TokenService } from './../../helpers/services/token.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.scss']
})
export class BodyComponent implements OnInit {
  userRole:string=''
  constructor(private tokenService: TokenService, private router:Router) {
  }

  ngOnInit(): void {
    this.tokenService.getUserInfo().subscribe(userInfo=>{

      let userRole =userInfo.roles[0]?.rolName
      switch(userRole){
        case 'ROLE_STUDENT':{
          this.router.navigateByUrl('/student/topics')
          break;
        }
        case 'ROLE_ADMIN':{
          this.router.navigateByUrl('/admin')
          break;
        }
        case 'ROLE_DEAN':{
          this.router.navigateByUrl('/admin')
          break;
        }
        case 'ROLE_TEACHER':{
          this.router.navigateByUrl('/teacher/designations')
          break;
        }
        case 'ROLE_LIABLE_TT':{
          this.router.navigateByUrl('/calendarsList')
        }
      }
      this.userRole=userRole
    })
  
  }
}

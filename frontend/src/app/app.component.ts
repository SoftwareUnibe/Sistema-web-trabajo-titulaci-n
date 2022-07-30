import { User } from './helpers/models/user';
import { TokenService } from './helpers/services/token.service';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { Component } from '@angular/core';
import { filter, map, mergeMap } from 'rxjs/operators';
import {MegaMenuItem, MenuItem} from 'primeng/api';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  visible: boolean = true;
  title = 'Titulation-Process-Web-System';
 
  loading:boolean=true
 

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private tokenService: TokenService){
   
  }
  ngOnInit(): void {

   this.getRouterInfo()
  }
  
  
  getRouterInfo(){
    this.router.events.pipe(
      filter(events => events instanceof NavigationEnd),
      map(() => this.activatedRoute),
      map(route => {
        while (route.firstChild) {
          route = route.firstChild;
        }
        return route;
      }))
      .pipe(
        filter(route => route.outlet === 'primary' || route.outlet === 'sub'),
        mergeMap(route => route.data)
      ).subscribe((x) => {
        x.header === false ? this.visible = false : this.visible = true
        this.loading=false
      })
  }
  handleMenu() {
    const menu = document.getElementsByClassName('header__links');
    const close = document.getElementsByClassName('p-button-raised');
    menu[0].classList.toggle('show');
    close[0].classList.remove('show');
}


}

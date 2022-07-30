import { filter, map, mergeMap } from 'rxjs/operators';
import { TokenService } from './../../../helpers/services/token.service';
import { TopicStudentService } from './../../../helpers/services/topic-student.service';
import { MenuItem } from 'primeng/api';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Steps } from 'primeng/steps';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.scss']
})
export class StudentComponent implements OnInit {

  stepperState = new BehaviorSubject<boolean[]>([false, false, false, false]);
  currentStepperState: Observable<boolean[]> = this.stepperState.asObservable();
  items: MenuItem[];
  readOnly: boolean = false
  loading:boolean=true
  showStepper:boolean=true
  @ViewChild('stepper', { static: true }) stepper: Steps;
  
  constructor(private topicStudentService: TopicStudentService, private tokenService: TokenService,
    private router:Router, private activatedRoute:ActivatedRoute) {
      activatedRoute.url.subscribe(()=>{
        activatedRoute.snapshot.firstChild.data.stepper === false ? this.showStepper = true : this.showStepper = false
        this.loading=false
      })
     }

  ngOnInit(): void {
    this.generateItems()
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
        x.stepper === false ? this.showStepper = true : this.showStepper = false
        this.loading=false
      })
      
  }
  changeStepperState(state: boolean[]) {
    this.stepperState.next(state)
  }

  generateItems() {
    this.currentStepperState.subscribe(statesArray => {
      this.items = [{
        label: 'Banco de temas',
        routerLink: 'topics',
        disabled: statesArray[0]
      },
      {
        label: 'Tema seleccionado',
        routerLink: 'selected',
        disabled: statesArray[1]
      },
      {
        label: 'Denuncia de tema y modalidad',
        routerLink: 'denunciation',
        disabled: statesArray[2]
      },
      {
        label: 'Propuesta de tema',
        routerLink: 'proposal',
        disabled: statesArray[3]
      }
      ];
    })

  }

}

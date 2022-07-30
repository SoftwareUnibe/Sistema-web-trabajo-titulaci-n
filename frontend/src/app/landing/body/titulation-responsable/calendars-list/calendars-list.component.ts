import { ReaderService } from './../../../../helpers/services/reader.service';
import { Table } from 'primeng/table';
import { ToastService } from './../../../../helpers/services/toast.service';
import { CalendarDetailService } from './../../../../helpers/services/calendar-detail.service';
import { mergeMap, switchMap } from 'rxjs/operators';
import { FinalDegreeCalendar } from './../../../../helpers/models/final-degree-calendar';
import { FinalCalendarDegreeService } from './../../../../helpers/services/final-degree-calendar.service';
import { User } from './../../../../helpers/models/user';
import { TokenService } from './../../../../helpers/services/token.service';
import { Career } from './../../../../helpers/models/career';
import { CareerService } from './../../../../helpers/services/career.service';
import { Router } from '@angular/router';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-calendars-list',
  templateUrl: './calendars-list.component.html',
  styleUrls: ['./calendars-list.component.scss']
})
export class CalendarsListComponent implements OnInit {

  @ViewChild('table') table: Table | undefined;
  existCalendarDetail: boolean = false;
  careers: Career[] = []
  displayModal = false;
  user: User = {}
  userRole = ''
  periods = [
    { name: '1ERA JORNADA', value: '1ERA JORNADA' },
    { name: '2DA JORNADA', value: '2DA JORNADA' },
    { name: '3RA JORNADA', value: '3RA JORNADA' }
  ]
  showError: boolean = false;
  selectedPeriod: string = ''
  career: Career = null
  calendarStatus: string = ''
  finalCalendarsList: FinalDegreeCalendar[] = null
  loading: boolean = false
  constructor(private router: Router, private careerSrv: CareerService, private tokenSrv: TokenService,
    private finalDegreeCalendarSrv: FinalCalendarDegreeService, private calendarDetailSrv: CalendarDetailService,
    private toastSrv: ToastService, private readerSrv: ReaderService) {
    this.tokenSrv.getUserInfo().pipe(
      mergeMap(userData => {
        this.userRole = userData.roles[0].rolName
        this.user = userData
        this.career = userData.career
        if (this.userRole === 'ROLE_CAREER_DIRECTOR') {
          return this.finalDegreeCalendarSrv.getFinalDegreeCalendarByCareerId(userData.career.id)
        }
        return this.finalDegreeCalendarSrv.getAllFinalDegreesCalendar()
      })
    ).subscribe(data => {
      this.finalCalendarsList = data
    })
  }



  ngOnInit(): void {
    this.getAllCareers();

  }

  getAllCareers() {
    this.careerSrv.getAllCarrers().subscribe(careerInfo => {
      this.careers = careerInfo
    })
  }

  openModal() {
    this.readerSrv.checkStatusToCreateCalendar(this.user.career.id).subscribe(() => {
      this.displayModal = true
    }, err => {
      let message = err.error.message
      this.toastSrv.showInfo(message, 'Atención')
    })
  }

  addNewCalendar() {
    if (!this.selectedPeriod) {
      this.showError = true
      return
    }
    let finalDegreeCalendar: FinalDegreeCalendar = {
      career: this.career,
      period: this.selectedPeriod
    }
    this.loading = true
    this.finalDegreeCalendarSrv.createFinalDegreeCalendar(finalDegreeCalendar).subscribe(data => {
      let calendarId = Object.values(data)
      this.loading = false
      this.router.navigateByUrl('/finalDegreeCalendar/' + calendarId[0])
    })
  }

  navigateToCalendarForm(finalDegreeCalendarId: string) {
    if (this.user.roles[0].rolName === 'ROLE_LIABLE_TT') {
      this.calendarDetailSrv.checkIfCalendarDetailExists(finalDegreeCalendarId).subscribe(response => {
        response
          ? this.router.navigateByUrl('/finalDegreeCalendar/' + finalDegreeCalendarId)
          : this.toastSrv.showWarn('El director (a) de carrera aún no ha generado el cronograma con la información necesaria', 'Advertencia')
      })
      return
    }
    this.router.navigateByUrl('/finalDegreeCalendar/' + finalDegreeCalendarId)
  }

  applyFilterTopics($event: any, stringVal: any) {
    this.table!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
}

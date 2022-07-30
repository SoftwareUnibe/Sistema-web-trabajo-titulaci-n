import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { FinalDegreeCalendar } from './../../../../helpers/models/final-degree-calendar';
import { mergeMap, map } from 'rxjs/operators';
import { FinalCalendarDegreeService } from './../../../../helpers/services/final-degree-calendar.service';
import { UserService } from './../../../../helpers/services/user.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { User } from 'src/app/helpers/models/user';
import { Career } from './../../../../helpers/models/career';
import { CalendarDetail } from './../../../../helpers/models/calendar-detail';
import { TokenService } from './../../../../helpers/services/token.service';
import { CalendarDetailService } from './../../../../helpers/services/calendar-detail.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit, Injector } from '@angular/core';
import { of } from 'rxjs';

@Component({
  selector: 'app-final-degree-calendar',
  templateUrl: './final-degree-calendar.component.html',
  styleUrls: ['./final-degree-calendar.component.scss']
})
export class FinalDegreeCalendarComponent implements OnInit {

  calendarDetailInfo: CalendarDetail[] = null
  finalCalendarDegree: FinalDegreeCalendar = {} as FinalDegreeCalendar
  filteredResults: any[] = []
  temporalUserSelected: User = null
  userAsTribunalPresident: User = null
  users: User[] = []
  secretary: string = ''
  careerDirector: User = {};
  edit = {}
  clonedCalendar: { [s: string]: CalendarDetail; } = {};
  calendarId: string = ''
  calendarExist: boolean = false
  actualYear = new Date().getFullYear()
  period: string = ''
  userLoggedRole: string = ''
  calendarStatus: boolean = false
  loading: boolean = false
  private activatedRouter: ActivatedRoute
  private calendarDetailSrv: CalendarDetailService
  private tokenSrv: TokenService
  private toastSrv: ToastService
  private userSrv: UserService
  private finalCalendarDegreeSrv: FinalCalendarDegreeService
  private router: Router
  private confirmDialogSrv: ConfirmationDialogService
  constructor(private injector: Injector) {
    this.activatedRouter = this.injector.get<ActivatedRoute>(ActivatedRoute)
    this.calendarDetailSrv = this.injector.get<CalendarDetailService>(CalendarDetailService)
    this.tokenSrv = this.injector.get<TokenService>(TokenService)
    this.toastSrv = this.injector.get<ToastService>(ToastService)
    this.userSrv = this.injector.get<UserService>(UserService)
    this.finalCalendarDegreeSrv = this.injector.get<FinalCalendarDegreeService>(FinalCalendarDegreeService)
    this.router = this.injector.get<Router>(Router)
    this.confirmDialogSrv = this.injector.get<ConfirmationDialogService>(ConfirmationDialogService)
    this.calendarId = this.activatedRouter.snapshot.params.careerId
  }

  ngOnInit(): void {
    this.checkCalendarExists();
    this.getCalendarDetailInfo();
    this.getUserInfo();
    this.getUsersByRole();
    this.getCalendarPeriod();
  }

  checkCalendarExists() {

  }

  getUsersByRole() {
    let rolesNames: string[] = ["ROLE_DEAN", "ROLE_CAREER_DIRECTOR"]
    this.userSrv.getByRolesNames(rolesNames).subscribe(usersInfo => {
      this.users = usersInfo
    }, () => {
      this.users = null
    })
  }


  search(event: any) {
    let filteredUsers: User[] = []
    let query = event.query.toLowerCase()
    filteredUsers = this.users.filter(data => {
      let completeName = data.name + ' ' + data.secondName + ' ' + data.lastName + ' ' + data.secondLastName
      return completeName.toLowerCase().indexOf(query) > -1
    })
    this.filteredResults = filteredUsers
  }

  setFieldValue(event: any, calendar: CalendarDetail) {
    calendar.tribunalBoss = event
  }

  showSelectedUser(tribunalBoss: User) {
    return tribunalBoss.degree + ' ' + tribunalBoss.name + ' ' + tribunalBoss.lastName
  }

  selectAllText(event: any) {
    event.target.select()
  }

  getCalendarPeriod() {
    this.finalCalendarDegreeSrv.getFinalDegreeCalendarById(this.calendarId).subscribe(calendar => {
      this.period = calendar.period
    })
  }

  getCalendarDetailInfo() {
    this.calendarDetailSrv.checkIfCalendarDetailExists(this.calendarId).pipe(
      mergeMap(exists => {
        this.calendarExist = exists;
        return of(exists);
      })
    ).subscribe(response => {
      if (response) {
        this.getFinalDegreeCalendarById();
      } else {
        this.getFinalDegreeCalendar()
      }
    })


  }

  getFinalDegreeCalendarById() {
    this.calendarDetailSrv.getByFinalDegreeCalendarId(this.calendarId).pipe(
      map(calendarData => calendarData.map(calendar => {
        let hourToDate = new Date(calendar.hour)
        calendar.hour = hourToDate
        this.secretary = calendar.secretary
        return calendar
      })), mergeMap(calendarDetail => {
        let studentsEmail = calendarDetail.map(data => data.student.email)
        this.getCalendarStatus(studentsEmail)
        this.calendarDetailInfo = calendarDetail
        let role = 'ROLE_CAREER_DIRECTOR'
        let careerId = calendarDetail[0].career.id
        return this.userSrv.getUserByRoleAndCareerId(role, careerId)
      })).subscribe(data => {
        this.careerDirector = data[0]
      })
  }

  getFinalDegreeCalendar() {
    this.finalCalendarDegreeSrv.getFinalDegreeCalendarById(this.calendarId).pipe(
      mergeMap(finalCalendarInfo => {
        this.finalCalendarDegree = finalCalendarInfo
        return this.calendarDetailSrv.getCalenderDetailInfo(finalCalendarInfo.career.id)
      })
    ).subscribe(calendarInfo => {
      this.calendarDetailInfo = calendarInfo
    })
  }

  getCalendarStatus(emails: string[]) {
    this.calendarDetailSrv.getCalendarStatus(emails).subscribe(status => {
      this.calendarStatus = status
    })
  }


  getUserInfo() {
    this.tokenSrv.getUserInfo().subscribe(data => {
      this.userLoggedRole = data.roles[0].rolName
      this.careerDirector = data
    })
  }

  getDayName(calendarDetail: CalendarDetail) {
    let date = calendarDetail.date;
    let dayName = new Date(date).toLocaleDateString('Ec', { weekday: 'long' });
    return dayName
  }


  onRowEditInit(calendar: CalendarDetail, index: number) {
    this.edit[index] = true
    this.clonedCalendar[index] = { ...calendar }
  }

  onRowEditSave(calendar: CalendarDetail, index: number) {
    calendar.secretary = this.secretary;
    this.calendarDetailInfo[index] = calendar;
    if (this.calendarExist) {
      let id = this.calendarDetailInfo[index].id
      let calendarDetail = this.calendarDetailInfo[index]
      this.calendarDetailSrv.updateByCalendarId(id, calendarDetail).subscribe(response => {
        let message = Object.values(response)
        this.toastSrv.showSuccess(message[0], 'Éxito')
      })
    }
  }

  onRowEditCancel(index: number) {
    this.calendarDetailInfo[index] = this.clonedCalendar[index]
    this.edit[index] = false
    delete this.clonedCalendar[index];
  }

  groupCalendarInfo(calendarDetailInfo: CalendarDetail[]) {
    for (let i = 0; i < calendarDetailInfo.length; i++) {
      calendarDetailInfo[i].career = this.careerDirector.career
      calendarDetailInfo[i].secretary = this.secretary
      calendarDetailInfo[i].finalDegreeCalendar = this.finalCalendarDegree
    }
    let createCalendar = {
      calendarDetailList: calendarDetailInfo
    }
    this.saveCalendar(createCalendar)
  }

  saveCalendar(calendarDetail: any) {
    this.loading = true
    this.calendarDetailSrv.createCalendar(calendarDetail).subscribe(response => {
      let message = Object.values(response)
      this.toastSrv.showSuccess(message[0], 'Éxito')
      this.router.navigateByUrl('/calendarsList')
      this.loading = false
    }, err => {
      let messageError = err.error.message
      this.toastSrv.showWarn(messageError, 'Advertencia')
      this.loading = false
    })
  }

  saveFinalCalendar(calendarDetail: any) {
    this.confirmDialogSrv.confirmationDialog(this.confirmDialogMessage(), 'No se ha generado el calendario')
      .then(() => {
        this.loading = true
        let studentEmails: string[] = calendarDetail.map((data: any) => data.student.email)
        this.calendarDetailSrv.generateFinalCalendar(studentEmails).subscribe(response => {
          let message = Object.values(response)
          this.toastSrv.showSuccess(message[0], 'Éxito')
          this.loading = false
          this.router.navigateByUrl('/calendarsList')
        })
      })
  }

  confirmDialogMessage(): string {
    let question = '<b>¿Está seguro (a) de generar el calendario de defensa final grado?</b>'
    let advice = '<br> <br> <b>Tomar en consideración: </b>'
    let advices = '<br> - Una vez creado no se podrá modificar ningún campo <br> - Se enviará una notificación al correo de cada estudiante'
    return question + advice + advices
  }
}

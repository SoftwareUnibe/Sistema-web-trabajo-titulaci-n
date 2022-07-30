import { EmailService } from './../helpers/services/email.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { TokenService } from './../helpers/services/token.service';
import { LoginUser } from './../helpers/models/login-user';
import { ToastService } from './../helpers/services/toast.service';
import { AuthService } from './../helpers/services/auth.service';
import { CareerService } from './../helpers/services/career.service';
import { Career } from './../helpers/models/career';
import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from '../helpers/models/user';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  @ViewChild('registerForm') registerForm: NgForm
  @ViewChild('passwordReset') passwordReset: NgForm
  roles = [
    { name: 'Estudiante', value: 'ROLE_STUDENT' },
    { name: 'Director de carrera', value: 'ROLE_CAREER_DIRECTOR' },
    { name: 'Responsable unidad de titulación', value: 'ROLE_LIABLE_TT' },
    { name: 'Decano', value: 'ROLE_DEAN' },
    { name: 'Director académico', value: 'ROLE_ACADEMIC_DIRECTOR' },
    { name: 'Docente', value: 'ROLE_TEACHER' },
    { name: 'Dirección financiera', value: 'ROLE_FINANCIAL' },
  ];
  degrees = ['Mgst.', 'Ph.D.', 'Dr.', 'Msc.']
  newUser = {} as User
  loginUser: LoginUser = {
    userName: '', password: ''
  }
  careers: Career[] = [];
  selectedCareer = {} as Career
  selectedRole = ''
  resetEmail: string = ''
  emailVerifyPattern = '[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}'
  activeTabViewIndex = 0
  passwordMeter = {
    weakLabel: 'Nivel de seguridad de contraseña débil',
    mediumLabel: 'Nivel de seguridad de contraseña medio ',
    strongLabel: 'Nivel de seguridad de contraseña alto'
  }
  emptyEmail: boolean = false
  openResetPasswordModal: boolean = false
  emailSentToStudent: boolean = false
  sendingEmail: boolean = false
  loading: boolean = false
  displayTermsDialog: boolean = false

  constructor(private careerService: CareerService, private authService: AuthService, private toastService: ToastService,
    private tokenS: TokenService, private router: Router, private emailSrv: EmailService) {
    this.newUser.roles = []
    this.careerService.getAllCarrers().subscribe(careers => {
      this.careers = careers
    })
  }

  ngOnInit(): void { }

  showTermsDialog() {
    this.displayTermsDialog = true;
  }

  register() {
    this.loading = true
    if (this.registerForm.invalid) {
      this.toastService.showError('Faltan campos por llenar, revise e intente de muevo')
      this.loading = false
      return
    }
    if (!(this.selectedRole == 'ROLE_CAREER_DIRECTOR' || this.selectedRole == 'ROLE_STUDENT'
      || this.selectedRole == 'ROLE_DEAN' || this.selectedRole == 'ROLE_ACADEMIC_DIRECTOR')) {
      this.newUser.career = null
    }
    this.newUser.roles[0] = this.selectedRole
    this.newUser.email = this.newUser.email.trim()
    this.authService.register(this.newUser).subscribe(() => {
      this.toastService.showSuccess('Inicie sesión para continuar', 'Usuario registrado')
      this.refreshForm()
      this.loading = false
    }, err => {
      this.toastService.showError(err.error.message)
      this.loading = false
    })
  }

  login(loginData: LoginUser) {
    this.loading = true
    this.authService.login(loginData).subscribe(() => {
      this.tokenS.setSessionState("true")
      this.loading = false
      this.toastService.showSuccess('Sesión iniciada', 'Bienvenido')
      this.router.navigateByUrl('/home')
    }, err => {
      this.toastService.showError(err.error.message)
      this.loading = false
    })
  }

  validateDni(event: any) {
    let charCode = event.which
    if (charCode > 31 && (charCode < 48 || charCode > 57)) return false;
    return true;
  }

  refreshForm() {
    this.registerForm.reset();
    this.selectedRole = ''
    this.activeTabViewIndex = 0
  }

  showResetPasswordModal() {
    this.openResetPasswordModal = true
  }

  closeResetPasswordModal() {
    this.openResetPasswordModal = false
  }

  closeReceivedEmailModal() {
    this.openResetPasswordModal = false
    this.emailSentToStudent = false
    this.sendingEmail = false
    this.resetEmail = ''
  }

  sendResetPasswordEmail() {
    if (this.passwordReset.invalid) {
      this.emptyEmail = true
      return
    }
    this.sendingEmail = true
    let email = {
      mailTo: this.resetEmail.trim()
    }
    this.emailSrv.sendResetPasswordEmail(email).subscribe(() => {
      this.emailSentToStudent = true
    }, () => {
      this.emailSentToStudent = false
      this.resetEmail = ''
    })
  }
}

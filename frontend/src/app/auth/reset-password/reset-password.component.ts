import { UserService } from './../../helpers/services/user.service';
import { ToastService } from './../../helpers/services/toast.service';
import { EmailService } from './../../helpers/services/email.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {


  dto = {
    tokenPassword: '',
    password: '',
    confirmPassword: ''
  }

  passwordRestored: boolean = false;
  restoringPassword: boolean = false
  passwordMeter = {
    weakLabel: 'Nivel de seguridad de contraseña débil',
    mediumLabel: 'Nivel de seguridad de contraseña medio ',
    strongLabel: 'Nivel de seguridad de contraseña alto'
  }
  constructor(private activatedRoute: ActivatedRoute, private emailSrv: EmailService, private toastSrv: ToastService,
    private router: Router) {


  }

  ngOnInit(): void {
    let token = this.activatedRoute.snapshot.params.token
    this.emailSrv.checkIfUserHasTokenPassword(token).subscribe(exist => {
      exist ? this.dto.tokenPassword = token : this.router.navigateByUrl('/auth')
    })
  }


  resetPassword() {
    this.restoringPassword = true
    this.emailSrv.resetPassword(this.dto).subscribe(response => {
      this.passwordRestored = true
      let message = Object.values(response)
      this.toastSrv.showSuccess(message[0], 'Operación exitosa')
    }, err => {
      this.restoringPassword = false
      this.toastSrv.showError(err.error.message)
    })
  }

  navigateToHomeLogin() {
    this.router.navigateByUrl('auth')
    this.passwordRestored = false
  }
}

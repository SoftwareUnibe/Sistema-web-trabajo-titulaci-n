import { ConfirmationDialogService } from './../helpers/services/confirmation-dialog.service';
import { ToastService } from './../helpers/services/toast.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/helpers/services/auth.service';
import { User, Role } from './../helpers/models/user';
import { TokenService } from './../helpers/services/token.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  user = {} as User;
  userRole: string = ''
  constructor(private tokenSrv: TokenService, private authSrv: AuthService, private router: Router,
    private toastSrv: ToastService, private confirmDialogSrv: ConfirmationDialogService) { }

  ngOnInit(): void {
    this.tokenSrv.getUserInfo().subscribe(userData => {
      this.user = userData
      this.userRole = this.getUserRoleName(userData.roles[0].rolName)
    })
  }

  getUserRoleName(roleName: string) {
    switch (roleName) {
      case "ROLE_CAREER_DIRECTOR":
        roleName = 'Director de carrera'
        break;
      case "ROLE_STUDENT":
        roleName = 'Estudiante'
        break;
      case "ROLE_FINANCIAL":
        roleName = 'Financiero'
        break;
      case "ROLE_TEACHER":
        roleName = 'Docente'
        break;
      case "ROLE_ADMIN":
        roleName = 'Administrador'
        break;
      case "ROLE_LIABLE_TT":
        roleName = 'Responsable unidad de titulación'
        break;
      default:
        break;
    }
    return roleName
  }

  updateUser(user: User) {
    let updateUser: any = {
      name: user.name,
      lastName: user.lastName,
      secondName: user.secondLastName,
      secondLastName: user.secondLastName,
      ci: user.ci,
      email: user.email
    }
    this.confirmDialogSrv.confirmationDialog('¿Está seguro de actualizar los datos?', 'Cancelado').then(() => {
      this.authSrv.updateUserInfo(updateUser).subscribe(() => {
        this.toastSrv.showSuccess('Datos actualizados correctamente', 'Guardado')
      }, err => {
        this.toastSrv.showWarn(err.error.message, 'Error')
      })
    })

  }

}

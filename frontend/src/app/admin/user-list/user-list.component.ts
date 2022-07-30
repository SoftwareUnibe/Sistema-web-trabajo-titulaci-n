import { UserService } from './../../helpers/services/user.service';
import { mergeMap } from 'rxjs/operators';
import { ToastService } from './../../helpers/services/toast.service';
import { ConfirmationDialogService } from './../../helpers/services/confirmation-dialog.service';
import { Table } from 'primeng/table';
import { AuthService } from 'src/app/helpers/services/auth.service';
import { User } from 'src/app/helpers/models/user';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  userList: User[] = []
  @ViewChild('dt') dt: Table | undefined
  loading: boolean = false
  constructor(private authService: AuthService, private confirmationDialogService: ConfirmationDialogService,
    private toastService: ToastService, private userService: UserService) { }

  ngOnInit(): void {
    this.getAllUsers();
  }
  applyFilterGlobal($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
  getAllUsers() {
    this.authService.getAllUsers().subscribe(users => {
      this.userList = users;
    })
  }
  verificateUser(user: User) {
    this.confirmationDialogService.confirmationDialog('¿Esta seguro que desea verificar al usuario ' + '<b>' + user.name + ' ' + user.lastName + '</b>' + ' con el <b> rol ' + this.roleToString(user.roles[0]) + '</b>' + '?', 'Usuario no verificado').then(() => {
      this.loading=true
      this.authService.verificateUser(user.ci).pipe(
        mergeMap(() => {
          this.toastService.showSuccess('Usuario verificado correctamente', 'Verificado')
          return this.authService.getAllUsers()
        })
      ).subscribe(data => {
        this.userList = data
        this.loading=false
      })
    })

  }
  roleToString(role: any) {
    switch (role.rolName) {
      case 'ROLE_ADMIN': {
        return 'Administrador'
      }
      case 'ROLE_STUDENT': {
        return 'Estudiante'
      }
      case 'ROLE_SECRETARY': {
        return 'Secretaria/o'
      }
      case 'ROLE_CAREER_DIRECTOR': {
        return 'Director de Carrera'
      }
      case 'ROLE_LIABLE_TT': {
        return 'Responsable del trabajo de titulación'
      }
      case 'ROLE_READER': {
        return 'Lector'
      }
      case 'ROLE_DEAN': {
        return 'Decano'
      }
      case 'ROLE_ACADEMIC_DIRECTOR': {
        return 'Director Académico'
      }
      case 'ROLE_TEACHER': {
        return 'Profesor'
      }
      case 'ROLE_FINANCIAL': {
        return 'Financiero'
      }
      default:
        return role.rolName;
    }

  }
}

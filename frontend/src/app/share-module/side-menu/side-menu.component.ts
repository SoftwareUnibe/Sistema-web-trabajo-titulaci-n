import { Router } from '@angular/router';
import { TokenService } from './../../helpers/services/token.service';
import { Component, OnInit, Output, EventEmitter, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { User } from 'src/app/helpers/models/user';
import { BehaviorSubject } from 'rxjs';
@Component({
  selector: 'side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss']
})
export class SideMenuComponent implements OnInit {
  items: MenuItem[];
  userInfo: User
  userData: BehaviorSubject<User> = new BehaviorSubject<User>(null);
  constructor(private tokenService: TokenService, private router: Router) {
    this.getUserInfo()
  }

  ngOnInit(): void {
    this.generateMenu()
  }
  getUserInfo() {
    this.tokenService.getUserInfo().subscribe(userInfo => {
      this.userInfo = userInfo
      this.userData.next(userInfo);
    })
  }

  generateMenu() {
    this.userData.subscribe(userDetails => {
      if (userDetails !== null) {
        let rol = userDetails.roles[0].rolName
        switch (rol) {
          case 'ROLE_CAREER_DIRECTOR': {
            this.items = [
              {
                label: 'Perfil',
                icon: 'fa-solid fa-user-gear',
                routerLink: ['/profile'],
              },
              {
                label: 'Banco de temas',
                icon: 'fa-solid fa-clipboard-list',

                items: [{
                  label: 'Nuevo tema',
                  icon: 'pi pi-pw pi-pencil',
                  command: () => {
                    this.router.navigateByUrl('/topicBank/new')
                  }
                },
                {
                  label: 'Lista de temas',
                  icon: 'pi pi-pw pi-list',
                  command: () => {
                    this.router.navigateByUrl('/home')
                  }
                }]
              },
              {
                label: 'Notificaciones de aprobación',
                icon: 'fa-solid fa-file-circle-question',
                routerLink: ['/topicApproval'],

              },
              {
                label: 'Designar tutores',
                icon: 'fa-solid fa-chalkboard-user',
                routerLink: ['/tutorDesignation'],

              },
              {
                label: 'Cartas designación tutores',
                icon: 'fa-solid fa-id-badge',
                command: () => {
                  this.router.navigateByUrl('/tutorDesignationList')
                }
              },
              {
                label: 'Designación de lectores',
                icon: 'fa-solid fa-book',
                command: () => {
                  this.router.navigateByUrl('/readerDesignationList')
                }
              },
              {
                label: 'Seguimiento Lectorias',
                icon: 'fa-solid fa-swatchbook',
                command: () => {
                  this.router.navigateByUrl('/readerList')
                }
              },
              {
                label: 'Tutorias',
                icon: 'fa-solid fa-chalkboard-user',
                command: () => {
                  this.router.navigateByUrl('/teacher/designations')
                }
              },
              {
                label: 'Lectorías',
                icon: 'fa-solid fa-book-open-reader',
                command: () => {
                  this.router.navigateByUrl('/teacher/lectures')
                }
              },
              {
                label: 'Carta antiplagio',
                icon: 'fa-solid fa-envelope-open-text',
                command: () => {
                  this.router.navigateByUrl('/antiPlagiarismLetter')
                }
              },
              {
                label: 'Cronograma final de grado',
                icon: 'fa-solid fa-calendar-days',
                command: () => {
                  this.router.navigateByUrl('/calendarsList')
                }
              },
              {
                label: 'Salir',
                icon: 'fa-solid fa-right-from-bracket',
                command: () => {
                  this.tokenService.logOut()
                },
              }

            ]
            break
          }
          case 'ROLE_STUDENT': {
            this.items = [
              {
                label: 'Perfil',
                icon: 'fa-solid fa-user-gear',
                command: () => {
                  this.router.navigateByUrl('/profile')
                }
              },
              {
                label: 'Temas',
                icon: 'fa-solid fa-list',
                command: () => {
                  this.router.navigateByUrl('/student/topics/')
                }
              },
              {
                label: 'Tutorias',
                icon: 'fa-solid fa-person-chalkboard',
                command: () => {
                  this.router.navigateByUrl('/student/constancy')
                }
              },
              {
                label: 'Lectoría',
                icon: 'fa-solid fa-book-open-reader',
                command: () => {
                  this.router.navigateByUrl('/student/readersList')
                }
              },
              {
                label: 'Documento trabajo de titulación',
                icon: 'fa-solid fa-file-circle-plus',
                command: () => {
                  this.router.navigateByUrl('/student/thesisDocument')
                }
              },
              {
                label: 'Salir',
                icon: 'fa-solid fa-arrow-right-from-bracket',
                command: () => {
                  this.tokenService.logOut()
                },
              }
            ]
            break
          }
          case 'ROLE_TEACHER': {
            this.items = [
              {
                label: 'Perfil',
                icon: 'fa-solid fa-user-gear',
                command: () => {
                  this.router.navigateByUrl('/profile')
                }
              },
              {
                label: 'Tutorias',
                icon: 'fa-solid fa-clipboard-check',
                command: () => {
                  this.router.navigateByUrl('/teacher/designations')
                }
              },
              {
                label: 'Carta antiplagio',
                icon: 'fa-solid fa-envelope-open-text',
                command: () => {
                  this.router.navigateByUrl('/antiPlagiarismLetter')
                }
              },
              {
                label: 'Lectorías',
                icon: 'fa-solid fa-book-open-reader',
                command: () => {
                  this.router.navigateByUrl('/teacher/lectures')
                }
              },
              {
                label: 'Salir',
                icon: 'fa-solid fa-arrow-right-from-bracket',
                command: () => {
                  this.tokenService.logOut()
                },
              }
            ]
            break
          }
          case 'ROLE_FINANCIAL': {
            this.items = [
              {
                label: 'Perfil',
                icon: 'fa-solid fa-user-gear',
                command: () => {
                  this.router.navigateByUrl('/profile')
                }
              },
              {
                label: 'Lista estudiantes',
                icon: 'fa-solid fa-list',
                command: () => {
                  this.router.navigateByUrl('/home')
                }
              },
              {
                label: 'Salir',
                icon: 'fa-solid fa-arrow-right-from-bracket',
                command: () => {
                  this.tokenService.logOut()
                },
              }
            ]
            break
          }
          case "ROLE_LIABLE_TT": {
            this.items = [
              {
                label: 'Perfil',
                icon: 'fa-solid fa-user',
                command: () => {
                  this.router.navigateByUrl('/profile')
                }
              },
              {
                label: 'Cronograma final de grado',
                icon: 'fa-solid fa-calendar-days',
                command: () => {
                  this.router.navigateByUrl('/calendarsList')
                }
              },
              {
                label: 'Salir',
                icon: 'fa-solid fa-arrow-right-from-bracket',
                command: () => {
                  this.tokenService.logOut()
                },
              }
            ]
            break;
          }
          case "ROLE_ADMIN": {
            this.items = [
              {
                label: 'Usuarios',
                icon: 'fa-solid fa-user-check',
                command: () => {
                  this.router.navigateByUrl('/admin/users')
                }
              },
              {
                label: 'Facultades',
                icon: 'fa-solid fa-school-flag',
                command: () => {
                  this.router.navigateByUrl('/admin/faculties')
                }
              },
              {
                label: 'Carreras',
                icon: 'fa-solid fa-graduation-cap',
                command: () => {
                  this.router.navigateByUrl('/admin/careers')
                }
              },
              {
                label: 'Salir',
                icon: 'fa-solid fa-arrow-right-from-bracket',
                command: () => {
                  this.tokenService.logOut()
                },
              }
            ]
            break;
          }
          default: {
            this.items = [
              {
                label: 'Perfil',
                icon: 'pi pi-user',
                command: () => {
                  this.router.navigateByUrl('/profile')
                }
              },
              {
                label: 'Salir',
                icon: 'pi pi-sign-out',
                command: () => {
                  this.tokenService.logOut()
                },
              }
            ]
          }
        }
      }
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
        return 'Responsable unidad de titulación'
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
        return 'Docente'
      }
      case 'ROLE_FINANCIAL': {
        return 'Financiero'
      }
      default:
        return role.rolName;
    }
  }

}

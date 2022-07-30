import { Router } from '@angular/router';
import { TokenService } from './../../helpers/services/token.service';
import { User } from './../../helpers/models/user';
import { MenuItem } from 'primeng/api';
import { Component, OnInit } from '@angular/core';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

@Component({
  selector: 'header-app',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  user: User
  userMenuItems: MenuItem[];
  resposiveMenuItems: MenuItem[];
  constructor(private tokenService: TokenService, private router: Router) { }

  ngOnInit(): void {
    this.getUserInfo()
    this.subMenuGenerator()
  }

  getUserInfo() {
    this.tokenService.getUserInfo().subscribe(userData => {
      this.user = userData
      this.mobileMenu(userData.roles[0].rolName)
    })
  }
  subMenuGenerator() {
    this.userMenuItems = [
      {
        label: 'Perfil',
        icon: 'pi pi-fw pi-user-edit',
        routerLink: '/profile'
      },
      {
        label: 'Cerrar sesión',
        icon: 'pi pi-fw pi-sign-out',
        command: () => {
          this.tokenService.logOut()
        }
      },
    ]
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
  mobileMenu(rol: string) {
    switch (rol) {
      case 'ROLE_CAREER_DIRECTOR': {
        this.resposiveMenuItems = [
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
        this.resposiveMenuItems = [
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
        this.resposiveMenuItems = [
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
        this.resposiveMenuItems = [
          {
            label: 'Perfil',
            icon: 'pi pi-user',
            command: () => {
              this.router.navigateByUrl('/profile')
            }
          },
          {
            label: 'Lista estudiantes',
            icon: 'pi pi-list',
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
        this.resposiveMenuItems = [
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
        this.resposiveMenuItems = [
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
        this.resposiveMenuItems = [
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
}

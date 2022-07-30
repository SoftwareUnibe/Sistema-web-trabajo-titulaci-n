import { map } from 'rxjs/operators';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { ToastService } from './../../../../helpers/services/toast.service';
import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { TutorDesignation } from './../../../../helpers/models/tutor-designation';
import { TutorDesignationService } from './../../../../helpers/services/tutor-designation.service';
import { CareerService } from './../../../../helpers/services/career.service';
import { UserService } from './../../../../helpers/services/user.service';
import { User } from './../../../../helpers/models/user';
import { TokenService } from './../../../../helpers/services/token.service';
import { TopicProposalService } from './../../../../helpers/services/topic-proposal.service';
import { TopicProposal } from './../../../../helpers/models/topic-proposal';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit, ViewChild, Injector } from '@angular/core';
import { Topic } from 'src/app/helpers/models/topic';
import { NgForm } from '@angular/forms';
import { SelectItemGroup } from 'primeng/api';

@Component({
  selector: 'app-tutor-designation',
  templateUrl: './tutor-designation.component.html',
  styleUrls: ['./tutor-designation.component.scss']
})
export class TutorDesignationComponent implements OnInit {

  @ViewChild('studentLetterForm') studentLetterForm: NgForm
  @ViewChild('tutorLetterForm') tutorLetterForm: NgForm

  users: User[] = []
  filteredResults: any[] = []
  existsTutor: boolean = false;
  selectedRole: string = '';
  selectedCareer: string = '';
  selectedDate: Date
  temporalSelectedTutor: User = null
  selectedTutor: User = null
  topicProposal: TopicProposal
  careerDirector: User = null
  tutorDesignation: TutorDesignation = {}
  topicStudent = []
  actualDate = new Date()
  createDocumentsButton: boolean = false;
  chipStudentDocument: string = 'Incompleto'
  chipTutorDocument: string = ''
  size = window.innerWidth > 840 ? false : true
  loading: boolean = true
  loadingButton: boolean = false
  private router: Router
  private activatedRoute: ActivatedRoute
  private topicProposalSrv: TopicProposalService
  private tokenSrv: TokenService
  private tutorDesignationSrv: TutorDesignationService
  private confirmMessageSrv: ConfirmationDialogService
  private toastSrv: ToastService
  private userSrv: UserService

  constructor(private injector: Injector) {
    this.router = this.injector.get<Router>(Router);
    this.activatedRoute = this.injector.get<ActivatedRoute>(ActivatedRoute);
    this.topicProposalSrv = this.injector.get<TopicProposalService>(TopicProposalService);
    this.tokenSrv = this.injector.get<TokenService>(TokenService);
    this.tutorDesignationSrv = this.injector.get<TutorDesignationService>(TutorDesignationService);
    this.toastSrv = this.injector.get<ToastService>(ToastService);
    this.userSrv = this.injector.get<UserService>(UserService);
    this.confirmMessageSrv = this.injector.get<ConfirmationDialogService>(ConfirmationDialogService);
    let id = this.activatedRoute.snapshot.params.topicProposalId
    this.getTopicProposal(id)
  }

  ngOnInit(): void {
    this.getCareerDirectorInfo()
    this.getAllRoles()
  }

  getTopicProposal(id: string) {
    this.topicProposalSrv.getOneTopicProposalById(id).subscribe((data) => {
      this.topicStudent = Object.values(data.topicStudent)
      this.topicProposal = data
      this.loading = false
    })
  }

  getAllRoles() {
    let rolesNames: string[] = ["ROLE_CAREER_DIRECTOR", "ROLE_TEACHER"]
    this.userSrv.getByRolesNames(rolesNames).subscribe(data => {
      this.users = data
    }, err => {
      console.log(err);
      this.users = null
    })
  }

  getCareerDirectorInfo() {
    this.tokenSrv.getUserInfo().subscribe(data => {
      this.careerDirector = data
    })
  }

  search(event: any) {
    let filteredTeachers: User[] = []
    let query = event.query.toLowerCase()
    filteredTeachers = this.users.filter(data => {
      let completeName = data.name + ' ' + data.secondName + ' ' + data.lastName + ' ' + data.secondLastName
      return completeName.toLowerCase().indexOf(query) > -1
    })
    this.filteredResults = filteredTeachers
  }

  setFieldValue() {
    this.selectedTutor = this.temporalSelectedTutor
    console.log(this.selectedTutor);
    
    this.existsTutor = true
  }

  userSelectedOnDropdown(user: User) {
    return user.name + ' ' + user.secondName + ' ' + user.lastName + ' ' + user.secondLastName;
  }

  clearTemporalSelectedTutor() {
    this.temporalSelectedTutor = null
  }

  createStudentsLetter(twoStudents: boolean, topicStudentOne: TopicStudent, topicStudentTwo?: TopicStudent) {
    if (twoStudents) {
      let tutorDesignationOne: TutorDesignation = {
        date: this.selectedDate,
        teacher: this.selectedTutor,
        topicStudent: topicStudentOne
      }
      let tutorDesignationTwo: TutorDesignation = {
        date: this.selectedDate,
        teacher: this.selectedTutor,
        topicStudent: topicStudentTwo
      }

      let tutorDesignations: TutorDesignation[] = [tutorDesignationOne, tutorDesignationTwo]
      let tutorDesignationToCreate = {
        designationTTList: tutorDesignations
      }
      this.confirmMessageSrv.confirmationDialog('¿Está seguro(a) de generar los documentos con el tutor asignado? <br> <small>- Se enviará una notificación al correo del estudiante y docente asignado como tutor</small>',
        'Documentos no generados').then(() => {
          this.loadingButton = true
          this.tutorDesignationSrv.createDesignationTTPairStudents(tutorDesignationToCreate).subscribe(response => {
            let toastText = Object.values(response)
            this.toastSrv.showSuccess(toastText[0], 'Operación exitosa')
            this.router.navigateByUrl('tutorDesignationList')
            this.loadingButton = false
          }, err => {
            console.log(err);
            this.toastSrv.showWarn(err.error.message, 'Advertencia')
            this.loadingButton = false
          })
        })
    } else {
      let tutorDesignation: TutorDesignation = {
        date: this.selectedDate,
        teacher: this.selectedTutor,
        topicStudent: topicStudentOne
      }
      this.confirmMessageSrv.confirmationDialog('¿Está seguro(a) de generar los documentos con el tutor asignado? <br> <small>- Se enviará una notificación al correo del estudiante y docente asignado como tutor</small>',
        'Documentos no generados').then(() => {
          this.loadingButton = true
          this.tutorDesignationSrv.createDesignationTT(tutorDesignation).subscribe(response => {
            let toastText = Object.values(response)
            this.toastSrv.showSuccess(toastText[0], 'Operación exitosa')
            this.router.navigateByUrl('/tutorDesignationList')
            this.loadingButton = false
          }, err => {
            this.toastSrv.showWarn(err.error.message, 'Advertencia')
            this.loadingButton = false
          })
        })
    }
  }


  checkFormsDocuments() {
    if (this.selectedDate && this.selectedTutor) {
      this.chipTutorDocument = 'Completo'
      this.chipStudentDocument = 'Completo'
    } else {
      this.chipTutorDocument = 'Incompleto'
      this.chipStudentDocument = 'Incompleto'
    }

  }

  changeCareerName(careerName: string) {
    switch (careerName) {
      case "Ingeniería en Software":
        return "Software"
      case "Nutrición":
        return "Nutrición"
      default:
        return true;
    }
  }
}

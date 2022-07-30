import { AuthService } from 'src/app/helpers/services/auth.service';
import { TutorDesignation } from 'src/app/helpers/models/tutor-designation';
import { TokenService } from './../../../../../helpers/services/token.service';
import { ToastService } from './../../../../../helpers/services/toast.service';
import { UserService } from './../../../../../helpers/services/user.service';
import { User } from './../../../../../helpers/models/user';
import { mergeMap, switchMap } from 'rxjs/operators';
import { MenuItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { TutorDesignationTableDto } from './../../../../../helpers/models/tutor-designation-table-dto';
import { TutorDesignationService } from './../../../../../helpers/services/tutor-designation.service'; import { Component, OnInit, ViewChild } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-tutor-designation-list',
  templateUrl: './tutor-designation-list.component.html',
  styleUrls: ['./tutor-designation-list.component.scss']
})
export class TutorDesignationListComponent implements OnInit {

  @ViewChild('dt') dt: Table | undefined;
  @ViewChild('previewDocuments') previewDocuments: any
  @ViewChild('downloadDocument') downloadDocument: any
  tutorDesignations: TutorDesignationTableDto[] = null
  visualizeDocuments: MenuItem[]
  downLoadDocuments: MenuItem[];
  tutorDesignation: TutorDesignationTableDto = null
  tutorDocumentPreview: boolean = false;
  studentDocumentPreview: boolean = false;
  haveSecondStudent: boolean = false;
  wantToDownloadPdf: boolean = false;
  careerDirector: User[]
  actualCareerDirector: User
  constructor(private tutorDesignationSrv: TutorDesignationService, private userSrv: UserService,
    private toastSrv: ToastService, private tokenSrv: TokenService) {
    this.tokenSrv.getUserInfo().subscribe(user => {
      user.password = null;
      this.actualCareerDirector = user
      this.getDesignationsAndCarrerDirectorInfo(user.ci)
    })
  }

  ngOnInit(): void {
  }

  getDesignationsAndCarrerDirectorInfo(ci: string) {
    this.tutorDesignationSrv.getAllTutorDesignationDto(ci).pipe(
      mergeMap(designationData => {
        let hash = {}
        designationData = designationData.filter(data => hash[data.topicStudent[0].topic.id] ? false : hash[data.topicStudent[0].topic.id] = true)
        this.tutorDesignations = designationData
        return designationData.map((data) => {
          return this.userSrv.getUserByRoleAndCareerId('ROLE_CAREER_DIRECTOR', data.topicStudent[0].topic.career.id)
        })
      }), switchMap(careerDirector => {
        return careerDirector
      })
    ).subscribe(careerDirectorInfo => {
      this.careerDirector = careerDirectorInfo
    })
  }
  generateDocumentsOptions(event: any, tutorDesignation: TutorDesignationTableDto) {
    this.previewDocuments.toggle(event)
    let isTwoStudents: boolean = tutorDesignation.topicStudent[0].topic.twoStudents

    let studentOne = tutorDesignation.topicStudent[0].student.name + ' ' + tutorDesignation.topicStudent[0].student.lastName
    let studentTwo = ''
    if (isTwoStudents) studentTwo = tutorDesignation.topicStudent[1].student.name + ' ' + tutorDesignation.topicStudent[1].student.lastName
    let teacher = tutorDesignation.teacher.name + ' ' + tutorDesignation.teacher.lastName
    this.visualizeDocuments = [
      {
        label: 'Carta al estudiante ' + studentOne,
        command: () => {
          this.haveSecondStudent = false;
          this.tutorDesignationSrv.getDesignationByTopicStudentId(tutorDesignation.topicStudent[0].id).subscribe(data => {
            this.tutorDesignation = data
            this.openStudentDocumentModal()
          })
        }
      },
      {
        label: 'Carta al estudiante ' + studentTwo,
        visible: isTwoStudents ? true : false,
        command: () => {
          this.haveSecondStudent = true;
          this.tutorDesignationSrv.getDesignationByTopicStudentId(tutorDesignation.topicStudent[1].id).subscribe(data => {
            this.tutorDesignation = data
            this.openStudentDocumentModal()
          })
        }
      },
      {
        label: 'Carta al tutor ' + teacher,
        command: () => {
          console.log(tutorDesignation);
          this.tutorDesignation = tutorDesignation
          this.openTeacherDocumentModal()
        }
      }
    ]
  }

  generateDocumentsDownloadOptions(event: any, tutorDesignation: TutorDesignationTableDto) {
    this.downloadDocument.toggle(event)
    let isTwoStudents: boolean = tutorDesignation.topicStudent[0].topic.twoStudents
    let studentOne = tutorDesignation.topicStudent[0].student.name + ' ' + tutorDesignation.topicStudent[0].student.lastName
    let studentTwo = ''
    if (isTwoStudents) studentTwo = tutorDesignation.topicStudent[1].student.name + ' ' + tutorDesignation.topicStudent[1].student.lastName
    let teacher = tutorDesignation.teacher.name + ' ' + tutorDesignation.teacher.lastName
    this.downLoadDocuments = [
      {
        label: 'Carta al estudiante ' + studentOne,
        command: () => {
          this.downloadStudentLetterPdf(tutorDesignation.topicStudent[0].id)
        }
      },
      {
        label: 'Carta al estudiante ' + studentTwo,
        visible: isTwoStudents ? true : false,
        command: () => {
          this.downloadStudentLetterPdf(tutorDesignation.topicStudent[1].id)
        }
      },
      {
        label: 'Carta al tutor ' + teacher,
        command: () => {
          this.downloadTutorLetterPdf(tutorDesignation.topicStudent[0].topic.id, tutorDesignation.teacher.ci)
        }
      }
    ]
  }

  applyFilterTopics($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  openTeacherDocumentModal() {
    this.tutorDocumentPreview = true;
  }

  openStudentDocumentModal() {
    this.studentDocumentPreview = true
  }

  closeTutorDocumentPreviewModal() {
    this.tutorDocumentPreview = false;
  }

  closeStudentDocumentPreviewModal() {
    this.studentDocumentPreview = false;
  }

  downloadStudentLetterPdf(topicStudentId: string) {
    this.toastSrv.showInfo('', 'La descarga comenzará en unos segundos')
    this.tutorDesignationSrv.downloadStudentDesignationLetterAsPdf(topicStudentId).subscribe((data) => {
      this.toastSrv.clear()
      let downloadURL = window.URL.createObjectURL(data);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.title = 'Carta_Asignacion_Estudiante'
      link.download = "Carta_Asignacion_Estudiante.pdf";
      link.click();
    }, () => {
      this.toastSrv.showWarn('', 'Error, intente nuevamente')
    })
  }

  downloadTutorLetterPdf(designationId: string, ci: string) {
    this.toastSrv.showInfo('', 'La descarga comenzará en unos segundos')
    this.tutorDesignationSrv.downloadTutorDesignationLetterAsPdf(designationId, ci).subscribe((data) => {
      this.toastSrv.clear()
      let downloadURL = window.URL.createObjectURL(data);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.title = 'Carta_Asignacion_Tutor'
      link.download = "Carta_Asignacion_Tutor.pdf";
      link.click();
    }, () => {
      this.toastSrv.showWarn('', 'Error, intente nuevamente')
    })
  }
}

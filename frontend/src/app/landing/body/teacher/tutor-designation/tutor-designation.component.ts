import { mergeMap } from 'rxjs/operators';
import { AuthService } from 'src/app/helpers/services/auth.service';
import { TutorDesignationTableDto } from 'src/app/helpers/models/tutor-designation-table-dto';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { TopicStudent } from './../../../../helpers/models/topic-student';
import { Observable } from 'rxjs';
import { Table } from 'primeng/table';
import { TokenService } from './../../../../helpers/services/token.service';
import { TutorDesignationService } from './../../../../helpers/services/tutor-designation.service';
import { TutorDesignation } from './../../../../helpers/models/tutor-designation';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-tutor-designation',
  templateUrl: './tutor-designation.component.html',
  styleUrls: ['./tutor-designation.component.scss']
})
export class TutorDesignationComponent implements OnInit {
  @ViewChild('dt') dt: Table | undefined;
  tutorDesignations: any[] = null

  constructor(private tutorDesignationSrv: TutorDesignationService, private tokenSrv: TokenService,
    private topicStudentSrv: TopicStudentService, private authSrv: AuthService) { }

  ngOnInit(): void {
    this.getDesignations()
  }
  applyFilterGlobal($event: any, stringVal: any) {
    console.log($event.target.value);
    this.dt.filterGlobal($event.target.value, stringVal);
  }
  getDesignations() {
    this.authSrv.getUserDetails().pipe(
      mergeMap((userInfo) => {
        return this.tutorDesignationSrv.getDesignationByTeacher(userInfo.ci)
      })
    ).subscribe((designations => {
      this.tutorDesignations = designations
    }))
  }
  formatDate(dateToTransform: Date): string {
    let date = new Date(dateToTransform).toLocaleDateString('Ec', { day: '2-digit', month: 'long', year: 'numeric' })
    return date
  }

  getStudents(topicId: string) {
    this.topicStudentSrv.getStudentsTopicByTopicId(topicId).subscribe(data => {
      console.log(data);
    })
  }
}

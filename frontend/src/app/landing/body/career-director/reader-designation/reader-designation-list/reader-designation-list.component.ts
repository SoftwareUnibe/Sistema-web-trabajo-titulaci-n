import { AuthService } from 'src/app/helpers/services/auth.service';
import { TutorigConstancyService } from './../../../../../helpers/services/tutorig-constancy.service';
import { User } from 'src/app/helpers/models/user';
import { Table } from 'primeng/table';
import { Router } from '@angular/router';
import { TopicStudent } from './../../../../../helpers/models/topic-student';
import { Topic } from 'src/app/helpers/models/topic';
import { map, mergeMap, take } from 'rxjs/operators';
import { TokenService } from './../../../../../helpers/services/token.service';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-reader-designation-list',
  templateUrl: './reader-designation-list.component.html',
  styleUrls: ['./reader-designation-list.component.scss']
})
export class ReaderDesignationListComponent implements OnInit {

  @ViewChild('dt') dt: Table | undefined;
  topicStudent: TopicStudent[] = []
  tableListInfo:any[] = null
  user: User = null
  visibleDialog: boolean = false
  antiPlagiarismLetterSent: string = ''
  thesisSentExist: string = ''
  documentTutoringHoursExists: string = ''
  loading:boolean=true
  constructor(private topicStudentSrv: TopicStudentService, private tokenSrv: TokenService, private router: Router, private tutoringSrv: TutorigConstancyService,
    private authSrv:AuthService) {
    this.tokenSrv.getUserInfo().subscribe(userData => {
      this.user = userData
    })

  }

  ngOnInit(): void {
    this.getTopicStudents();
  }

  applyFilterTopics($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  designReader(topicId: string) {
    this.antiPlagiarismLetterSent = 'pi-check'
    this.thesisSentExist = 'pi-check'
    this.documentTutoringHoursExists = 'pi-check'
    this.topicStudentSrv.getStudentsTopicByTopicId(topicId).pipe(
      mergeMap(data => {
        this.topicStudent = data
        console.log(data);
        
        return this.tutoringSrv.existConstancyByTopic(data[0].topic.id)
      })
    ).subscribe(hourConstancyDoc => {
      let topicStudent = this.topicStudent[0]
      if (!topicStudent.antiPlagiarismLetterSent) {
        this.visibleDialog = true
        this.antiPlagiarismLetterSent = 'pi-times'
      }
      if (!hourConstancyDoc) {
        this.visibleDialog = true;
        this.documentTutoringHoursExists = 'pi-times'
      }
      if (topicStudent.antiPlagiarismLetterSent && hourConstancyDoc)
        this.router.navigateByUrl('readerDesignationForm/' + topicId)
    })
  }

  getTopicStudents() {
    
    this.authSrv.getUserDetails().pipe(
      mergeMap((userDetails)=>{
        return this.topicStudentSrv.getTopicStudentsByUserCi(userDetails.ci)
      })
    ).subscribe(data => {
      this.tableListInfo = data
      this.loading=false
    })

  }
}

import { Router } from '@angular/router';
import { TopicProposalService } from './../../../../../helpers/services/topic-proposal.service';
import { mergeMap } from 'rxjs/operators';
import { TokenService } from './../../../../../helpers/services/token.service';
import { Table } from 'primeng/table';
import { TopicProposal } from './../../../../../helpers/models/topic-proposal';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-manage-execution-topics',
  templateUrl: './manage-execution-topics.component.html',
  styleUrls: ['./manage-execution-topics.component.scss']
})
export class ManageExecutionTopicsComponent implements OnInit {

  topicProposalOnExecution: TopicProposal[] = null
  @ViewChild('dt2') dt2: Table | undefined;
  constructor(private tokenService: TokenService, private topicProposalSrv: TopicProposalService,
    private router: Router) { }

  ngOnInit(): void {
    this.createTutorDesignationDocumentStudentAndTeacher()
  }

  createTutorDesignationDocumentStudentAndTeacher() {
    this.tokenService.getUserInfo().pipe(
      mergeMap(userData => {
        return this.topicProposalSrv.getTopicProposalByCareerIdAndTopicStatus(userData.career.id, 'En ejecución')
      })
    ).subscribe(proposalData => {
      this.topicProposalOnExecution = proposalData
    }, () => {
      this.topicProposalOnExecution = null
    })
  }


  applyFilterProposalTopics($event: any, stringVal: any) {
     this.dt2!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
   }

   test(topicProposal: string) {
    this.router.navigateByUrl('tutorDesignation/' + topicProposal)
  }
}

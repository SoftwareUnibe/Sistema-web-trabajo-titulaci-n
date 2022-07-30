import { MenuItem } from 'primeng/api';
import { TopicProposal } from './../../../helpers/models/topic-proposal';
import { TopicProposalService } from './../../../helpers/services/topic-proposal.service';
import { TopicStudentService } from 'src/app/helpers/services/topic-student.service';
import { TopicTable } from './../../../helpers/models/topic-table';
import { TopicService } from './../../../helpers/services/topic.service';
import { TokenService } from './../../../helpers/services/token.service';
import { Router } from '@angular/router';
import { mergeMap } from 'rxjs/operators';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Table } from 'primeng/table'
@Component({
  selector: 'app-career-director',
  templateUrl: './career-director.component.html',
  styleUrls: ['./career-director.component.scss']
})
export class CareerDirectorComponent implements OnInit {
  topics: TopicTable[]
  topicProposal: TopicProposal[] = []
  topicProposalOnExecution: TopicProposal[] = []
  career: string
  topicsLength: number
  careerId: string = ''
  selectedStudentTopics: any[] = []
  executedStudentTopics: any[] = []
  evaluationOptions: MenuItem[]
  topicProposalId: string = '';
  evaluationSelected: string = '';
  x: TopicProposal
  @ViewChild('dt') dt: Table | undefined;
  @ViewChild('dt2') dt2: Table | undefined;
  @ViewChild('dt3') dt3: Table | undefined;
  @ViewChild('dt4') dt4: Table | undefined;
  @ViewChild('menu') menu: any;
  loading: boolean = true
  size = window.innerWidth > 840 ? false : true
  constructor(private tokenService: TokenService, private topicService: TopicService, private router: Router,
    private topicStudentService: TopicStudentService,
    private topicProposalSrv: TopicProposalService) {
    this.evaluationOptions = [
      {
        label: 'Aprobado',
        command: () => {
          this.sendToCreateTopicApprovalNotification('Aprobado');

        }
      },
      {
        label: 'Aprobado con observaciones',
        command: () => {
          this.sendToCreateTopicApprovalNotification('Aprobado con observaciones');

        }
      },
      {
        label: 'Reprobado',
        command: () => {
          this.sendToCreateTopicApprovalNotification('Reprobado');

        }
      }
    ]

  }

  ngOnInit(): void {
    this.getTopics()
    this.getTopicsByStatus()
    this.getTopicProposalByCareerId();
  }

  applyFilterTopics($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  applyFilterProposalTopics($event: any, stringVal: any) {
    this.dt2!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  applyFilterSelectedTopics($event: any, stringVal: any) {
    this.dt3!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }



  getTopicsByStatus() {
    this.loading = true
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        return this.topicStudentService.getStudentTopicByStatusAndCareer('En ejecución', userData.career.id)
      })
    ).subscribe(studentTopics => {
      this.loading = false
      let filteredArray = studentTopics.filter((value, index, self) =>
        index === self.findIndex((data) => data.topic.id === value.topic.id))
      this.selectedStudentTopics = filteredArray
    }, () => {
      this.loading = false
    })

  }

  getTopics() {
    this.loading = true
    this.tokenService.getUserInfo().pipe(
      mergeMap((userData) => {
        this.career = userData.career.name
        return this.topicService.getTopicByCareer(userData.career.id)
      })
    ).subscribe(topicList => {
      this.topics = topicList
      this.loading = false
    }, () => {
      this.loading = false
    })
  }

  getTopicProposalByCareerId() {
    this.loading = true
    this.tokenService.getUserInfo().pipe(
      mergeMap(userData => {
        return this.topicProposalSrv.getTopicProposalByCareerIdAndTopicStatus(userData.career.id, 'Seleccionado')
      })
    ).subscribe(proposalData => {
      this.topicProposal = proposalData
      this.loading = false
    }, () => {
      this.topicProposal = null
      this.loading = false
    })
  }

  getTopicProposalIdOnPressButton(event: any, topicProposalId: string) {
    this.menu.toggle(event);
    this.topicProposalId = topicProposalId;
  }

  sendToCreateTopicApprovalNotification(evaluationSelected: string) {
    this.router.navigate(['generateApprovalNotification/' + this.topicProposalId, { evaluation: evaluationSelected }])
  }

  logout() {
    this.tokenService.logOut()
  }

  getDetails() {
    this.tokenService.getUserInfo().subscribe(data => {
      console.log(data.roles[0]);
    })

  }

  edit(id: string) {
    this.router.navigateByUrl('/topicBank/' + id)
  }

}

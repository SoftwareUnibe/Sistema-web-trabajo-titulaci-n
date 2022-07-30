import { Observable } from 'rxjs';
import { TopicProposal } from './../models/topic-proposal';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TopicProposalService {
  private topicProposalURL = 'http://localhost:8090/proposal'
  constructor(private httpCliente: HttpClient) { }


  getTopicProposalByTopicId(topicId: string): Observable<TopicProposal[]> {
    return this.httpCliente.get<TopicProposal[]>(this.topicProposalURL + '/topic/' + topicId,{withCredentials: true})
  }

  getTopicProposalById(topicProposalId: string): Observable<TopicProposal> {
    return this.httpCliente.get<TopicProposal>(this.topicProposalURL + '/career/' + topicProposalId,{withCredentials: true})
  }

  createTopicProposal(topicProposal: TopicProposal): Observable<String> {
    return this.httpCliente.post<String>(this.topicProposalURL, topicProposal,{withCredentials: true})
  }

  getTopicProposalByStudentCi(studentCi: string): Observable<TopicProposal> {
    return this.httpCliente.get(this.topicProposalURL + '/student/' + studentCi,{withCredentials: true});
  }

  getTopicProposalByCareerIdAndTopicStatus(careerId: string, topicStatus: string): Observable<TopicProposal[]> {
    return this.httpCliente.get<TopicProposal[]>(this.topicProposalURL + '/career/' + careerId + '/topic_status/' + topicStatus,{withCredentials: true})
  }

  generateTopicProposalPdf(topicId: string) {
    return this.httpCliente.get(this.topicProposalURL + '/pdf/' + topicId, { responseType: 'blob',withCredentials: true})
  }
  
  editTopicProposal(topicId: string, topicProposal: TopicProposal) {
    return this.httpCliente.put(this.topicProposalURL + '/' + topicId, topicProposal,{withCredentials: true});
  }

  getOneTopicProposalById(id: string): Observable<TopicProposal> {
    return this.httpCliente.get<TopicProposal>(this.topicProposalURL + '/topic_proposal/' + id,{withCredentials: true})
  }
}

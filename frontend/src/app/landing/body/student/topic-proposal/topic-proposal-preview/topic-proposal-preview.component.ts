import { TopicProposal } from './../../../../../helpers/models/topic-proposal';
import { TopicStudent } from './../../../../../helpers/models/topic-student';
import { Component, Input, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-topic-proposal-preview',
  templateUrl: './topic-proposal-preview.component.html',
  styleUrls: ['./topic-proposal-preview.component.scss']
})
export class TopicProposalPreviewComponent implements OnInit {


  @Input('topicProposal') topicProposal: TopicProposal
  @Input('showTopicProposalPreview') showTopicProposalPreview: boolean = false
  @Output() closeTopicProposalPreview = new EventEmitter<boolean>();
  constructor() { }

  ngOnInit(): void {
  }
  closeProposalPreview() {
    this.closeTopicProposalPreview.emit(true);
  }
}

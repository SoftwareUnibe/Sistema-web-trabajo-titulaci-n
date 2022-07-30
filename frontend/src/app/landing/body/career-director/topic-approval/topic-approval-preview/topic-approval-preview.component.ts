import { User } from 'src/app/helpers/models/user';
import { TopicApproval } from './../../../../../helpers/models/topic-approval';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-topic-approval-preview',
  templateUrl: './topic-approval-preview.component.html',
  styleUrls: ['./topic-approval-preview.component.scss']
})
export class TopicApprovalPreviewComponent implements OnInit {

  @Input('topicApproval') notificationDetail : TopicApproval
  @Input('topicApprovalPreview') topicApprovalPreview: boolean = false
  @Input('topicApprovalDate') topicApprovalDate: string;
  @Input('careerDirector') careerDirector: User;
  @Output() showTopicApprovalPreview = new EventEmitter<boolean>();
  constructor() { }

  ngOnInit(): void {
  }

  closeDenunciationModal() {
    this.showTopicApprovalPreview.emit(true);
  }
}

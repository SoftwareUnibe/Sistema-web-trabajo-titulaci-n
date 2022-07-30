import { User } from 'src/app/helpers/models/user';
import { TopicStudent } from './../../../../../helpers/models/topic-student';
import { TopicDenunciation } from 'src/app/helpers/models/topic-denunciation';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-topic-denunciation-preview',
  templateUrl: './topic-denunciation-preview.component.html',
  styleUrls: ['./topic-denunciation-preview.component.scss']
})
export class TopicDenunciationPreviewComponent implements OnInit {

  @Input('topicDenunciation') topicDenunciation: TopicDenunciation
  @Input('actualDate') actualDate: any
  @Input('topicDenunciationDate') topicDenunciationDate: string;
  @Input('denunciationPreview') denunciationPreview: boolean = false
  @Input('director') director: User
  @Output() showDenunciationPreview = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  closeDenunciationModal() {
    this.showDenunciationPreview.emit()
  }

}

import { User } from 'src/app/helpers/models/user';
import { TopicStudent } from './../../../../../helpers/models/topic-student';
import { TutorDesignation } from './../../../../../helpers/models/tutor-designation';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { TutorDesignationTableDto } from 'src/app/helpers/models/tutor-designation-table-dto';

@Component({
  selector: 'app-tutor-document-preview',
  templateUrl: './tutor-document-preview.component.html',
  styleUrls: ['./tutor-document-preview.component.scss']
})
export class TutorDocumentPreviewComponent implements OnInit {

  @Input('tutorDesignation') tutorDesignation: TutorDesignationTableDto
  @Input('actualCareerDirector') actualCareerDirector: User
  @Input('tutorDocumentPreview') tutorDocumentPreview: boolean = false
  @Output() closeTutorDocumentPreview = new EventEmitter<boolean>();
  constructor() { }

  ngOnInit(): void {
  }

  closeTutorDocumentModal() {
    this.closeTutorDocumentPreview.emit(true);
  }


  changeCareerName(careerName: string) {
    switch (careerName) {
      case "Ingeniería en Software":
        return careerName = "Software"
      default:
        return true;
    }
  }
}

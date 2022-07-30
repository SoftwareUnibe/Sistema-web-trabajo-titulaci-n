import { TutorDesignation } from 'src/app/helpers/models/tutor-designation';
import { User } from 'src/app/helpers/models/user';
import { TutorDesignationTableDto } from './../../../../../helpers/models/tutor-designation-table-dto';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-student-document-preview',
  templateUrl: './student-document-preview.component.html',
  styleUrls: ['./student-document-preview.component.scss']
})
export class StudentDocumentPreviewComponent implements OnInit {

  @Input('tutorDesignation') tutorDesignationStudent: TutorDesignation
  @Input('studentDocumentPreview') studentDocumentPreview: boolean = false
  @Input('haveSecondStudent') haveSecondStudent: boolean = false;
  @Input('createdBy') createdByCareerDirector: User[]
  @Output() closeStudentDocumentPreview = new EventEmitter<boolean>();
  constructor() { }

  ngOnInit(): void {
  }

  closeStudentDocumentModal() {
    this.closeStudentDocumentPreview.emit(true);
  }

}

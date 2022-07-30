import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeacherRoutingModule } from './teacher-routing.module';
import { TeacherComponent } from './teacher.component';
import { ConstancyTutoringComponent } from './constancy-tutoring/constancy-tutoring.component';
import { TutorDesignationComponent } from './tutor-designation/tutor-designation.component';
import { ShareModule } from 'src/app/share-module/share.module';
import { ReadingsListComponent } from './readings-list/readings-list.component';
import { ReadingDetailComponent } from './reading-detail/reading-detail.component';
import { ReaderAccordanceComponent } from './reader-accordance/reader-accordance.component';
import { ReaderObservationsComponent } from './reader-observations/reader-observations.component';


@NgModule({
  declarations: [
    TeacherComponent,
    ConstancyTutoringComponent,
    TutorDesignationComponent,
    ReadingsListComponent,
    ReadingDetailComponent,
    ReaderAccordanceComponent,
    ReaderObservationsComponent
  ],
  imports: [
    CommonModule,
    TeacherRoutingModule,
    ShareModule
  ]
})
export class TeacherModule { }

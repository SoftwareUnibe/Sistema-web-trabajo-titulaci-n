import { CanDeactivateGuard } from './../../../helpers/guards/can-deactivate.guard';
import { ReaderAccordanceComponent } from './reader-accordance/reader-accordance.component';
import { ReadingDetailComponent } from './reading-detail/reading-detail.component';
import { ReadingsListComponent } from './readings-list/readings-list.component';
import { ConstancyTutoringComponent } from './constancy-tutoring/constancy-tutoring.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeacherComponent } from './teacher.component';
import { TutorDesignationComponent } from './tutor-designation/tutor-designation.component';
import { ReaderObservationsComponent } from './reader-observations/reader-observations.component';

const routes: Routes = [{ path: '', component: TeacherComponent,
children:[
  {
    path: '',
    redirectTo: 'designations',
    pathMatch: 'full',
  },
  {
    path:'designations',
    component: TutorDesignationComponent
  },
  {
    path: 'tutoring/:id',
    component: ConstancyTutoringComponent
  },
  {
    path:'lectures',
    component: ReadingsListComponent
  },
  {
    path:'lecture/:id',
    component: ReadingDetailComponent
  },{
    path:'accordance/:id',
    component: ReaderAccordanceComponent
  },{
    path:'lecture/observations/:id',
    canDeactivate: [CanDeactivateGuard],
    component: ReaderObservationsComponent
  }
] }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TeacherRoutingModule { }

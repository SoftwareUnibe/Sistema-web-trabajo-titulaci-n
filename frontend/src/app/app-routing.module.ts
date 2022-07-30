import { CalendarsListComponent } from './landing/body/titulation-responsable/calendars-list/calendars-list.component';
import { FinalDegreeCalendarComponent } from './landing/body/titulation-responsable/final-degree-calendar/final-degree-calendar.component';
import { AntiPlagiarismLetterComponent } from './landing/body/career-director/anti-plagiarism-letter/anti-plagiarism-letter.component';
import { ReaderDesignationFormComponent } from './landing/body/career-director/reader-designation/reader-designation-form/reader-designation-form.component';
import { ReaderDesignationListComponent } from './landing/body/career-director/reader-designation/reader-designation-list/reader-designation-list.component';
import { ManageExecutionTopicsComponent } from './landing/body/career-director/tutor-designation/manage-execution-topics/manage-execution-topics.component';
import { TutorDesignationListComponent } from './landing/body/career-director/tutor-designation/tutor-designation-list/tutor-designation-list.component';
import { TutorDesignationComponent } from './landing/body/career-director/tutor-designation/tutor-designation.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ApprovalNotificationFormComponent } from './landing/body/career-director/approval-notification-form/approval-notification-form.component';
import { TopicApprovalComponent } from './landing/body/career-director/topic-approval/topic-approval.component';
import { TopicBankFormComponent } from './landing/body/career-director/topic-bank-form/topic-bank-form.component';
import { VerifyAuthGuard } from './helpers/guards/verify-auth.guard';
import { BodyComponent } from './landing/body/body.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes, CanActivate } from '@angular/router';
import { LoginGuard } from './helpers/guards/login.guard';
import { ReaderListComponent } from './landing/body/career-director/reader-list/reader-list.component';

const routes: Routes = [

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },

  {
    path: 'auth',
    canActivate: [LoginGuard],
    data: { header: false },
    loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'home',
    canActivate: [VerifyAuthGuard],
    component: BodyComponent
  },
  {
    path: 'profile',
    canActivate: [VerifyAuthGuard],
    component: UserProfileComponent
  },
  {
    path: 'topicBank/:id',
    canActivate: [VerifyAuthGuard],
    component: TopicBankFormComponent
  },
  {
    path: 'topicApproval',
    canActivate: [VerifyAuthGuard],
    component: TopicApprovalComponent
  },
  {
    path: 'generateApprovalNotification/:id',
    canActivate: [VerifyAuthGuard],
    component: ApprovalNotificationFormComponent
  },
  {
    path: 'tutorDesignation',
    canActivate: [VerifyAuthGuard],
    component: ManageExecutionTopicsComponent
  },
  {
    path: 'tutorDesignation/:topicProposalId',
    canActivate: [VerifyAuthGuard],
    component: TutorDesignationComponent
  },
  {
    path: 'tutorDesignationList',
    canActivate: [VerifyAuthGuard],
    component: TutorDesignationListComponent
  },
  {
    path: 'readerDesignationList',
    canActivate: [VerifyAuthGuard],
    component: ReaderDesignationListComponent
  },
  {
    path: 'readerDesignationForm/:topicId',
    canActivate: [VerifyAuthGuard],
    component: ReaderDesignationFormComponent
  },
  {
    path: 'antiPlagiarismLetter',
    canActivate: [VerifyAuthGuard],
    component: AntiPlagiarismLetterComponent
  },
  {
    path: 'calendarsList',
    canActivate: [VerifyAuthGuard],
    component: CalendarsListComponent
  },
  {
    path: 'finalDegreeCalendar/:careerId',
    canActivate: [VerifyAuthGuard],
    component: FinalDegreeCalendarComponent
  },
  {
    path:'readerList',
    canActivate: [VerifyAuthGuard],
    component: ReaderListComponent
  },
  {
    path: 'admin',
    canActivate: [VerifyAuthGuard],
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'student',
    canActivate: [VerifyAuthGuard],
    loadChildren: () => import('./landing/body/student/student.module').then(m => m.StudentModule)
  },
  {
    path: 'teacher',
    canActivate: [VerifyAuthGuard],
    loadChildren: () => import('./landing/body/teacher/teacher.module').then(m => m.TeacherModule)
  },
  {
    path: '**',
    redirectTo: 'home'
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

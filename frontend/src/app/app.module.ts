import { StudentDocumentPreviewComponent } from './landing/body/career-director/tutor-designation/student-document-preview/student-document-preview.component';
import { TutorDocumentPreviewComponent } from './landing/body/career-director/tutor-designation/tutor-document-preview/tutor-document-preview.component';
import { StudentModule } from './landing/body/student/student.module';
import { intercerptorProvider } from './helpers/interceptor/interceptor.service';
import { ShareModule } from './share-module/share.module';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BodyComponent } from './landing/body/body.component';
import { HttpClientModule } from '@angular/common/http';
import { MessageService, ConfirmationService } from 'primeng/api';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';
import { TopicBankFormComponent } from './landing/body/career-director/topic-bank-form/topic-bank-form.component';
import { CareerDirectorComponent } from './landing/body/career-director/career-director.component';
import { FinancialComponent } from './landing/body/financial/financial.component';
import { TopicApprovalComponent } from './landing/body/career-director/topic-approval/topic-approval.component';
import { ApprovalNotificationFormComponent } from './landing/body/career-director/approval-notification-form/approval-notification-form.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { TutorDesignationComponent } from './landing/body/career-director/tutor-designation/tutor-designation.component';
import { TutorDesignationListComponent } from './landing/body/career-director/tutor-designation/tutor-designation-list/tutor-designation-list.component';
import { ManageExecutionTopicsComponent } from './landing/body/career-director/tutor-designation/manage-execution-topics/manage-execution-topics.component';
import { ReaderDesignationListComponent } from './landing/body/career-director/reader-designation/reader-designation-list/reader-designation-list.component';
import { ReaderDesignationFormComponent } from './landing/body/career-director/reader-designation/reader-designation-form/reader-designation-form.component';
import { AntiPlagiarismLetterComponent } from './landing/body/career-director/anti-plagiarism-letter/anti-plagiarism-letter.component';
import { FinalDegreeCalendarComponent } from './landing/body/titulation-responsable/final-degree-calendar/final-degree-calendar.component';
import { CalendarsListComponent } from './landing/body/titulation-responsable/calendars-list/calendars-list.component';
import { ReaderListComponent } from './landing/body/career-director/reader-list/reader-list.component';

@NgModule({
  declarations: [
    AppComponent,
    BodyComponent,
    TopicBankFormComponent,
    CareerDirectorComponent,
    FinancialComponent,
    TopicApprovalComponent,
    ApprovalNotificationFormComponent,
    UserProfileComponent,
    TutorDesignationComponent,
    TutorDesignationListComponent,
    TutorDocumentPreviewComponent,
    StudentDocumentPreviewComponent,
    ManageExecutionTopicsComponent,
    ReaderDesignationListComponent,
    ReaderDesignationFormComponent,
    AntiPlagiarismLetterComponent,
    FinalDegreeCalendarComponent,
    CalendarsListComponent,
    ReaderListComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ShareModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MessagesModule,
    MessageModule,
    ToastModule,
    StudentModule

  ],
  providers: [
   intercerptorProvider,
    MessageService,
    ConfirmationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

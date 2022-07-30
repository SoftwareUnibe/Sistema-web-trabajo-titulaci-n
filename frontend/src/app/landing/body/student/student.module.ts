import { StudentReadersListComponent } from './student-readers-list/student-readers-list.component';
import { intercerptorProvider } from './../../../helpers/interceptor/interceptor.service';
import { ShareModule } from './../../../share-module/share.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentRoutingModule } from './student-routing.module';
import { StudentComponent } from './student.component';
import { TopicSelectionComponent } from './topic-selection/topic-selection.component';
import { TopicSelectedComponent } from './topic-selected/topic-selected.component';
import { TopicDenunciationComponent } from './topic-denunciation/topic-denunciation.component';
import { TopicProposalComponent } from './topic-proposal/topic-proposal.component';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { TopicDenunciationPreviewComponent } from './topic-denunciation/topic-denunciation-preview/topic-denunciation-preview.component';
import { TopicProposalPreviewComponent } from './topic-proposal/topic-proposal-preview/topic-proposal-preview.component';
import { ConstancyTutoringComponent } from './constancy-tutoring/constancy-tutoring.component';
import { TitulationDocumentLinkComponent } from './titulation-document-link/titulation-document-link.component';

@NgModule({
  declarations: [
    StudentComponent,
    TopicSelectionComponent,
    TopicSelectedComponent,
    TopicDenunciationComponent,
    TopicProposalComponent,
    TopicDenunciationPreviewComponent,
    TopicProposalPreviewComponent,
    ConstancyTutoringComponent,
    TitulationDocumentLinkComponent,
    StudentReadersListComponent

  ],
  imports: [
    CommonModule,
    StudentRoutingModule,
    ShareModule,
    MessagesModule,
    MessageModule
  ], exports: [
    StudentComponent
  ]
})
export class StudentModule { }

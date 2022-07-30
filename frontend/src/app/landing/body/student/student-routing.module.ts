import { StudentReadersListComponent } from './student-readers-list/student-readers-list.component';
import { TitulationDocumentLinkComponent } from './titulation-document-link/titulation-document-link.component';
import { ConstancyTutoringComponent } from './constancy-tutoring/constancy-tutoring.component';
import { TopicSelectionComponent } from './topic-selection/topic-selection.component';
import { TopicProposalComponent } from './topic-proposal/topic-proposal.component';
import { TopicDenunciationComponent } from './topic-denunciation/topic-denunciation.component';
import { TopicSelectedComponent } from './topic-selected/topic-selected.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StudentComponent } from './student.component';

const routes: Routes = [

  {
    path: '',
    component: StudentComponent,
    children: [
      {
        path: '',
        redirectTo: 'topics',
        pathMatch: 'full',
      },
      {
        path: 'topics',
        data: { stepper: false },
        component: TopicSelectionComponent
      },
      {
        path: 'selected',
        data: { stepper: false },
        component: TopicSelectedComponent
      },
      {
        path: 'denunciation',
        data: { stepper: false },
        component: TopicDenunciationComponent
      },
      {
        path: 'proposal',
        data: { stepper: false },
        component: TopicProposalComponent
      },
      {
        path: 'constancy',
        component: ConstancyTutoringComponent
      },
      {
        path: 'thesisDocument',
        component: TitulationDocumentLinkComponent
      },
      {
        path: 'readersList',
        component: StudentReadersListComponent
      },
      {
        path: '**',
        redirectTo: 'topics'
      }

    ]
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StudentRoutingModule { }

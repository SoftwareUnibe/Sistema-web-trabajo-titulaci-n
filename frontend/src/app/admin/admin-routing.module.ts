import { FacultyListComponent } from './faculty-list/faculty-list.component';
import { CareerListComponent } from './career-list/career-list.component';
import { UserListComponent } from './user-list/user-list.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';

const routes: Routes = [
  { path: '', component: AdminComponent,
  children:[
    {
      path: '',
      redirectTo: 'users',
      pathMatch: 'full',
    },
    {
      path: 'users',
      component: UserListComponent
    },
    {
      path:'careers',
      component: CareerListComponent
    },
    {
      path:'faculties',
      component:FacultyListComponent
    }
  ] },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }

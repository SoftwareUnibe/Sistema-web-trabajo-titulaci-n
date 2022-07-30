import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UserListComponent } from './user-list/user-list.component';
import { ShareModule } from '../share-module/share.module';
import { CareerListComponent } from './career-list/career-list.component';
import { FacultyListComponent } from './faculty-list/faculty-list.component';


@NgModule({
  declarations: [
    AdminComponent,
    UserListComponent,
    CareerListComponent,
    FacultyListComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    ShareModule
  ]
})
export class AdminModule { }

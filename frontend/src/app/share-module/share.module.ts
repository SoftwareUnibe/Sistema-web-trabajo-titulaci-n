import { DocumentsHeaderComponent } from './documents-header/documents-header.component';
import { TopicApprovalPreviewComponent } from './../landing/body/career-director/topic-approval/topic-approval-preview/topic-approval-preview.component';
import { CalendarModule } from 'primeng/calendar';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { PanelMenuModule } from 'primeng/panelmenu';
import { SideMenuComponent } from './side-menu/side-menu.component';
import { TableModule } from 'primeng/table';
import { TabViewModule } from 'primeng/tabview';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { CardModule } from 'primeng/card';
import { StepsModule } from 'primeng/steps';
import { DividerModule } from 'primeng/divider';
import { MessageModule } from 'primeng/message';
import { ImageModule } from 'primeng/image';
import { DialogModule } from 'primeng/dialog';
import { ChipModule } from 'primeng/chip';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MenuModule } from 'primeng/menu';
import { TooltipModule } from 'primeng/tooltip';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { HeaderComponent } from './header/header.component';
import { RadioButtonModule } from 'primeng/radiobutton';
import { ConstancyTutoringPreviewComponent } from './constancy-tutoring-preview/constancy-tutoring-preview.component';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { SplitButtonModule } from 'primeng/splitbutton';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FileUploadModule } from 'primeng/fileupload';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import {InputSwitchModule} from 'primeng/inputswitch';
import {CheckboxModule} from 'primeng/checkbox';
const Modules = [
  ButtonModule,
  FormsModule,
  InputTextModule,
  DropdownModule,
  PanelMenuModule,
  TableModule,
  TabViewModule,
  InputTextareaModule,
  CardModule,
  StepsModule,
  DividerModule,
  MessageModule,
  ImageModule,
  DialogModule,
  ChipModule,
  ScrollPanelModule,
  ConfirmDialogModule,
  CalendarModule,
  MenuModule,
  TooltipModule,
  TieredMenuModule,
  RadioButtonModule,
  AutoCompleteModule,
  SplitButtonModule,
  FontAwesomeModule,
  FileUploadModule,
  ProgressSpinnerModule,
  InputSwitchModule,
  CheckboxModule
]
@NgModule({
  declarations: [
    SideMenuComponent,
    HeaderComponent,
    TopicApprovalPreviewComponent,
    ConstancyTutoringPreviewComponent,
    DocumentsHeaderComponent
  ],
  imports: [
    CommonModule,
    Modules
  ],
  exports: [
    Modules,
    SideMenuComponent,
    HeaderComponent,
    TopicApprovalPreviewComponent,
    ConstancyTutoringPreviewComponent,
    DocumentsHeaderComponent

  ]
})
export class ShareModule { }

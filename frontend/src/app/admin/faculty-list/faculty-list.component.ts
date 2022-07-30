import { ToastService } from './../../helpers/services/toast.service';
import { Table } from 'primeng/table';
import { FacultyService } from './../../helpers/services/faculty.service';
import { Faculty } from './../../helpers/models/faculty';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-faculty-list',
  templateUrl: './faculty-list.component.html',
  styleUrls: ['./faculty-list.component.scss']
})
export class FacultyListComponent implements OnInit {
  @ViewChild('dt') dt: Table | undefined;
  dialog: boolean = false
  submit: boolean = false
  edit: boolean = false
  faculties: Faculty[]
  newFaculty: Faculty = {} as Faculty;
  loading: boolean = false
  constructor(private facultiesSrv: FacultyService, private toastSrv: ToastService) { }

  ngOnInit(): void {
    this.getFaculties()
  }

  applyFilterGlobal($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }

  getFaculties() {
    this.facultiesSrv.getAll().subscribe(list => {
      this.faculties = list
    })
  }

  saveFaculty() {
    this.loading = true
    this.facultiesSrv.create(this.newFaculty).subscribe(() => {
      this.toastSrv.showSuccess('Facultad creada', 'Guardado')
      this.getFaculties()
      this.loading = false
      this.hideDialog()
    }, err => {
      this.toastSrv.showError(err.error.message)
      this.submit = true
      this.loading = false
    })
  }

  editFaculty() {
    this.loading = true
    this.facultiesSrv.edit(this.newFaculty).subscribe(() => {
      this.toastSrv.showSuccess('Facultad editada', 'Guardado')
      this.hideDialog()
      this.edit = false
      this.loading = false
    }, err => {
      this.toastSrv.showError(err.error.message)
      this.submit = true
      this.loading = false
    })
  }

  hideDialog() {
    this.getFaculties()
    this.dialog = false
    this.edit = false
  }

  openDialog() {
    this.dialog = true
    this.newFaculty = {} as Faculty
  }

  openEditDialog(faculty: Faculty) {
    this.edit = true
    this.dialog = true
    this.newFaculty = faculty
  }

}

import { ToastService } from './../../helpers/services/toast.service';
import { Faculty } from './../../helpers/models/faculty';
import { FacultyService } from './../../helpers/services/faculty.service';
import { Table } from 'primeng/table';
import { CareerService } from './../../helpers/services/career.service';
import { Career } from './../../helpers/models/career';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-career-list',
  templateUrl: './career-list.component.html',
  styleUrls: ['./career-list.component.scss']
})
export class CareerListComponent implements OnInit {
  careers: Career[]
  dialog: boolean = false
  newCareer: Career = {} as Career
  faculties: Faculty[]
  submit: boolean = false
  edit: boolean = false
  checked: boolean = false
  attributesToEdit: string = ''
  @ViewChild('dt') dt: Table | undefined
  loading: boolean = false
  constructor(private careerSrv: CareerService, private facultySrv: FacultyService, private toastSrv: ToastService) { }

  ngOnInit(): void {
    this.getAll()
    this.getFaculties()
  }
  applyFilterGlobal($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal)
  }
  getAll() {
    this.careerSrv.getAllCarrers().subscribe(list => {
      this.careers = list
    })
  }
  getFaculties() {
    this.facultySrv.getAll().subscribe(list => {
      this.faculties = list
    })
  }
  hideDialog() {
    this.dialog = false
    this.edit = false
    this.getAll()
  }
  openDialog() {
    this.dialog = true
    this.edit = false
    this.attributesToEdit = null
    this.newCareer = {} as Career
  }
  openEditDialog(career: Career) {
    this.edit = true
    this.dialog = true
    this.attributesToEdit = '*'
    this.newCareer = career
  }
  editCareer() {
    this.loading = true
    this.careerSrv.saveCareer(this.newCareer).subscribe(() => {
      this.toastSrv.showSuccess('Carrera guardada', 'Guardado')
      this.loading = false
      this.dialog = false
      this.edit = false
      this.getAll()
    }, err => {
      this.submit = true;
      this.loading = false
      this.toastSrv.showError(err.error.message)
    })
  }

}

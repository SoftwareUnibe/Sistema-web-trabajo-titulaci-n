import { ConfirmationDialogService } from './../../../../helpers/services/confirmation-dialog.service';
import { ToastService } from './../../../../helpers/services/toast.service';
import { Table } from 'primeng/table';
import { Reader } from './../../../../helpers/models/reader';
import { mergeMap } from 'rxjs/operators';
import { User } from './../../../../helpers/models/user';
import { AuthService } from './../../../../helpers/services/auth.service';
import { ReaderService } from './../../../../helpers/services/reader.service';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-reader-list',
  templateUrl: './reader-list.component.html',
  styleUrls: ['./reader-list.component.scss']
})
export class ReaderListComponent implements OnInit {
  @ViewChild('dt') dt: Table | undefined;
  careerDirector: User
  readers: Reader[] = null
  loading: boolean = false
  constructor(private readerSrv: ReaderService, private authService: AuthService,
    private toastSrv: ToastService, private confirmSrv: ConfirmationDialogService) { }

  ngOnInit(): void {
    this.getReaderListByCareer()
  }

  applyFilterTopics($event: any, stringVal: any) {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal)
  }
  getReaderListByCareer() {
    this.authService.getUserDetails().pipe(
      mergeMap((user) => {
        this.careerDirector = user
        return this.readerSrv.getListByCareer(user.career.id)
      })
    ).subscribe(list => {
      this.readers = list
    })
  }
  resetProcess(topicId: string) {
    this.confirmSrv.confirmationDialog("¿Está seguro?, Se borraran todos los documentos de la lectoría del estudiante"
      , "cancelado").then(() => {
        this.loading = true
        this.readerSrv.resetReaderProcess(topicId).subscribe(() => {
          this.getReaderListByCareer()
          this.toastSrv.showSuccess("Exito", "Lectoria reestablecida")
          this.loading = false
        }), () => {
          this.toastSrv.showError("No se pudo restablecer")
          this.loading = false
        }
      })

  }
}

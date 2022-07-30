import { Injectable } from '@angular/core';
import { ConfirmationService, ConfirmEventType, MessageService } from 'primeng/api';
@Injectable({
  providedIn: 'root'
})
export class ConfirmationDialogService {

  constructor(private confirmationService: ConfirmationService, private messageSrv: MessageService) { }

  confirmationDialog(message: string, errorMessage: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.confirmationService.confirm({
        message: message,
        acceptLabel:'Aceptar',
        rejectLabel: 'Cancelar',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          resolve(true)
        },
        reject: (type: ConfirmEventType) => {
          switch (type) {
            case ConfirmEventType.CANCEL:
              this.messageSrv.add({ severity: 'warn', summary: 'Cancelado', detail: errorMessage })
              break;
            case ConfirmEventType.REJECT:
              this.messageSrv.add({ severity: 'warn', summary: 'Cancelado', detail: errorMessage })
              break;
          }
        }
      })
    })
  }

  deleteConfirmDialog(message: string, confirmMessage: string, errorMessage: string): Promise<any> {
    return new Promise((resolve, reject) => {
      this.confirmationService.confirm({
        message: message,
        header: 'Borrar',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          resolve(true)
        },
        reject: (type: ConfirmEventType) => {
          switch (type) {
            case ConfirmEventType.CANCEL:
              this.messageSrv.add({ severity: 'warn', summary: 'Cancelado', detail: errorMessage })
              break;
            case ConfirmEventType.REJECT:
              this.messageSrv.add({ severity: 'warn', summary: 'Cancelado', detail: errorMessage })
              break;
          }
        }
      })
    })

  }
}

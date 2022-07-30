import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor(private messageService: MessageService) { }

  showSuccess(text: string, tittle: string) {
    this.messageService.add({ severity: 'success', summary: tittle, detail: text })
  }
  showInfo(text: string, tittle: string) {
    this.messageService.add({ severity: 'info', summary: tittle, detail: text });
  }

  showWarn(text: string, tittle: string) {
    this.messageService.add({ severity: 'warn', summary: tittle, detail: text });
  }

  showError(text: string) {
    this.messageService.add({ severity: 'error', summary: 'Error', detail: text });
  }

  clear(){
    this.messageService.clear();
  }
}

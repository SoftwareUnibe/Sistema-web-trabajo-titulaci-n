import { ToastService } from './../../../../helpers/services/toast.service';
import { mergeMap } from 'rxjs/operators';
import { AuthService } from 'src/app/helpers/services/auth.service';
import { ReaderAccordanceService } from './../../../../helpers/services/reader-accordance.service';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Table } from 'primeng/table';
import { ReaderService } from './../../../../helpers/services/reader.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Reader } from 'src/app/helpers/models/reader';
import { EvaluationService } from 'src/app/helpers/services/evaluation.service';

@Component({
  selector: 'app-readings-list',
  templateUrl: './readings-list.component.html',
  styleUrls: ['./readings-list.component.scss']
})
export class ReadingsListComponent implements OnInit {

  readerList: Reader[] = null
  @ViewChild('dt') dt: Table | undefined;
  constructor(private readerSrv: ReaderService, private toastSrv: ToastService, private router:Router, private evaluationSrv:EvaluationService,
    private readerAccordanceSrv:ReaderAccordanceService, private authSrv:AuthService ) { }

  ngOnInit(): void {
    this.getReadingList()
  }
  applyFilterGlobal($event: any, stringVal: any) {
    console.log($event.target.value);
    this.dt.filterGlobal($event.target.value, stringVal);
  }
  getReadingList() {
    this.authSrv.getUserDetails().pipe(
      mergeMap((userInfo)=>{
        return  this.readerSrv.getListByReaderCi(userInfo.ci)
      })
    ).subscribe(list => {
      this.readerList = list
    })

  }
  formatDate(dateToTransform: Date): string {
    let date = new Date(dateToTransform);
    let monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"]
    let year = date.getFullYear();
    let month = monthNames[date.getMonth()];
    let day = date.getDate();
    return day + ' de ' + month + ' del ' + year
  }
  saveAsPdf(id:string) {
    this.downloadNotify()
    this.readerSrv.generateReaderLetterPdf(id).subscribe((data) => {
      let downloadURL = window.URL.createObjectURL(data);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.title = 'Carta_Lector'
      link.download = "Carta al Lector.pdf";
      link.click();
    })
  }
  downloadEvaluation(topicId: string){
    this.downloadNotify()
    this.evaluationSrv.getEvaluationPdf(topicId).subscribe(file=>{
      let downloadURL = window.URL.createObjectURL(file);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.download = "Evaluación.pdf";
      link.click();
    })
  }
  downloadAccordance(topicId: string){
    this.downloadNotify()
    this.readerAccordanceSrv.saveAccordancePdf(topicId).subscribe(file=>{
      let downloadURL = window.URL.createObjectURL(file);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.download = "Carta de conformidad o no conformidad.pdf";
      link.click();
    })
  }
  downloadObservations(topicId:string){
    this.downloadNotify()
    this.readerAccordanceSrv.saveObservationsPdf(topicId).subscribe(file=>{
      let downloadURL = window.URL.createObjectURL(file);
      let link = document.createElement('a');
      link.href = downloadURL;
      link.download = "Observaciones.pdf";
      link.click();
    }, () => {
      this.toastSrv.showWarn('No existen observaciones registradas','')
    })
  }
  downloadNotify(){
    this.toastSrv.showInfo('El documento se descargará en unos segundos','Descarga iniciada')
  }
  generatedMenu(state: string, topicId:string, readerId:string): MenuItem[] {
    let items: MenuItem[]
    switch (state) {
      case 'Asignado': {
        items = [
          {
            label: 'Archivos',
            icon: 'pi pi-fw pi-file',
            items: [
              {
                label: 'Designación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.saveAsPdf(readerId)
                }
              },
            ]
          },{
            label:'Acciones',
            icon:'pi pi-fw pi-pencil',
            items:[
              {
                label:'Evaluar',
                icon:'pi pi-fw pi-align-left',
                command: ()=>{
                  this.router.navigateByUrl('/teacher/lecture/'+readerId)
                }
              }
            ]
          }
        ]
        return items
      }
      case 'Evaluado': {
        items = [
          {
            label: 'Archivos',
            icon: 'pi pi-fw pi-file',
            items: [
              {
                label: 'Designación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.saveAsPdf(readerId)
                }
              },
              {
                label: 'Evaluación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.downloadEvaluation(topicId)
                }
              },
            ]
            
          },
          {
            label:'Acciones',
            icon:'pi pi-fw pi-pencil',
            items:[
              {
                label:'Añadir Observaciones',
                icon:'pi pi-fw pi-align-left',
                command: ()=>{
                  this.router.navigateByUrl('/teacher/lecture/observations/'+readerId)
                }
              },
              {
                label:'Conformidad o no conformidad',
                icon:'pi pi-fw pi-align-left',
                command: ()=>{
                  this.router.navigateByUrl('/teacher/accordance/'+readerId)
                }
              }
            ]
          }
        ]
        return items
      }
      case 'Evaluado con observaciones': {
        items = [
          {
            label: 'Archivos',
            icon: 'pi pi-fw pi-file',
            items: [
              {
                label: 'Designación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.saveAsPdf(readerId)
                }
              },
              {
                label: 'Evaluación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.downloadEvaluation(topicId)
                }
              },
              {
                label: 'Observaciones',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.downloadObservations(topicId)
                }
              }
            ]
            
          },
          {
            label:'Acciones',
            icon:'pi pi-fw pi-pencil',
            items:[
              {
                label:'Conformidad o no conformidad',
                icon:'pi pi-fw pi-align-left',
                command: ()=>{
                  this.router.navigateByUrl('/teacher/accordance/'+readerId)
                }
              }
            ]
          }
        ]
        return items
      }
      case 'En conformidad': {
        items = [
          {
            label: 'Archivos',
            icon: 'pi pi-fw pi-file',
            items: [
              {
                label: 'Designación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.saveAsPdf(readerId)
                }
              },
              {
                label: 'Evaluación',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.downloadEvaluation(topicId)
                }
              },
              {
                label: 'Carta de conformidad',
                icon: 'pi pi-fw pi-file-pdf',
                command: ()=>{
                  this.downloadAccordance(topicId)
                }
              },
            ]
            
          }
        ]
        return items
      }
      case 'Creando calendario': {
        return this.generateFinalItems(items, readerId, topicId)
      }
      case 'En defensa': {
        return this.generateFinalItems(items, readerId, topicId)
      }
      case 'En Defensa': {
        return this.generateFinalItems(items, readerId, topicId)
      }
      case 'En no conformidad': {
        return this.generateFinalItems(items, readerId, topicId)
      }
      default:
        items = [
          {
            label: 'Archivos',
            icon: 'pi pi-fw pi-file',
            items: [
              {
                label: 'Designación',
                icon: 'pi pi-fw pi-plus',
              },
              {
                label: 'Evaluación',
                icon: 'pi pi-fw pi-trash'
              },
            ]
            
          },
          {
            label:'Acciones',
            icon:'pi pi-fw pi-pencil',
            items:[
              {
                label:'Conformidad o no conformidad',
                icon:'pi pi-fw pi-align-left',
                command: ()=>{
                  this.router.navigateByUrl('/teacher/accordance/'+readerId)
                }
              }
            ]
          }
        ]
        return items
    }
  }
  private generateFinalItems(items: MenuItem[], readerId: string, topicId: string) {
    items = [
      {
        label: 'Archivos',
        icon: 'pi pi-fw pi-file',
        items: [
          {
            label: 'Designación',
            icon: 'pi pi-fw pi-file-pdf',
            command: () => {
              this.saveAsPdf(readerId);
            }
          },
          {
            label: 'Evaluación',
            icon: 'pi pi-fw pi-file-pdf',
            command: () => {
              this.downloadEvaluation(topicId);
            }
          },
          {
            label: 'Carta de conformidad',
            icon: 'pi pi-fw pi-file-pdf',
            command: () => {
              this.downloadAccordance(topicId);
            }
          },
        ]
      }
    ];
    return items;
  }
}

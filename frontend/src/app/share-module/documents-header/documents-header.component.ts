import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-documents-header',
  templateUrl: './documents-header.component.html',
  styleUrls: ['./documents-header.component.scss']
})
export class DocumentsHeaderComponent implements OnInit {

  @Input('documentTitle') documentTitle: string = '';
  @Input('subtitle') subtitle: string = '';
  @Input('formNumber') formNumber: string = '';

  actualDate = new Date().getFullYear();
  constructor() { }

  ngOnInit(): void {
  }

}

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentDocumentPreviewComponent } from './student-document-preview.component';

describe('StudentDocumentPreviewComponent', () => {
  let component: StudentDocumentPreviewComponent;
  let fixture: ComponentFixture<StudentDocumentPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StudentDocumentPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentDocumentPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

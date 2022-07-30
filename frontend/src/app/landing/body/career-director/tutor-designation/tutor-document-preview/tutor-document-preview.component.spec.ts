import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TutorDocumentPreviewComponent } from './tutor-document-preview.component';

describe('TutorDocumentPreviewComponent', () => {
  let component: TutorDocumentPreviewComponent;
  let fixture: ComponentFixture<TutorDocumentPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TutorDocumentPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TutorDocumentPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

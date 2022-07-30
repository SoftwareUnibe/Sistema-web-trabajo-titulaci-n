import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitulationDocumentLinkComponent } from './titulation-document-link.component';

describe('TitulationDocumentLinkComponent', () => {
  let component: TitulationDocumentLinkComponent;
  let fixture: ComponentFixture<TitulationDocumentLinkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TitulationDocumentLinkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TitulationDocumentLinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

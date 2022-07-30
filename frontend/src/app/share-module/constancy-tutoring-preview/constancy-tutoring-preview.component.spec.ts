import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstancyTutoringPreviewComponent } from './constancy-tutoring-preview.component';

describe('ConstancyTutoringPreviewComponent', () => {
  let component: ConstancyTutoringPreviewComponent;
  let fixture: ComponentFixture<ConstancyTutoringPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConstancyTutoringPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConstancyTutoringPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

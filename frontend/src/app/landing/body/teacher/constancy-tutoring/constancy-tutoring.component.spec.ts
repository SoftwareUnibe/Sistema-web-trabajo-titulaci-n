import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstancyTutoringComponent } from './constancy-tutoring.component';

describe('ConstancyTutoringComponent', () => {
  let component: ConstancyTutoringComponent;
  let fixture: ComponentFixture<ConstancyTutoringComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConstancyTutoringComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConstancyTutoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalDegreeCalendarComponent } from './final-degree-calendar.component';

describe('FinalDegreeCalendarComponent', () => {
  let component: FinalDegreeCalendarComponent;
  let fixture: ComponentFixture<FinalDegreeCalendarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinalDegreeCalendarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinalDegreeCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

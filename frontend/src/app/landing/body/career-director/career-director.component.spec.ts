import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CareerDirectorComponent } from './career-director.component';

describe('CareerDirectorComponent', () => {
  let component: CareerDirectorComponent;
  let fixture: ComponentFixture<CareerDirectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CareerDirectorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CareerDirectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

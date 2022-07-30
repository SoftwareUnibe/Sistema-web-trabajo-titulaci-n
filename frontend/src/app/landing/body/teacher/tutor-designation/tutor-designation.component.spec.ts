import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TutorDesignationComponent } from './tutor-designation.component';

describe('TutorDesignationComponent', () => {
  let component: TutorDesignationComponent;
  let fixture: ComponentFixture<TutorDesignationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TutorDesignationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TutorDesignationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

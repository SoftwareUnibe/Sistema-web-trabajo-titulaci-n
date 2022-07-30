import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TutorDesignationListComponent } from './tutor-designation-list.component';

describe('TutorDesignationListComponent', () => {
  let component: TutorDesignationListComponent;
  let fixture: ComponentFixture<TutorDesignationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TutorDesignationListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TutorDesignationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

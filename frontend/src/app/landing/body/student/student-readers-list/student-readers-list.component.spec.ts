import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentReadersListComponent } from './student-readers-list.component';

describe('StudentReadersListComponent', () => {
  let component: StudentReadersListComponent;
  let fixture: ComponentFixture<StudentReadersListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StudentReadersListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentReadersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

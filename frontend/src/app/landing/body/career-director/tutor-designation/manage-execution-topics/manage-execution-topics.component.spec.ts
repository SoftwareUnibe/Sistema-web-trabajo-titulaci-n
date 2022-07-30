import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageExecutionTopicsComponent } from './manage-execution-topics.component';

describe('ManageExecutionTopicsComponent', () => {
  let component: ManageExecutionTopicsComponent;
  let fixture: ComponentFixture<ManageExecutionTopicsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageExecutionTopicsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageExecutionTopicsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

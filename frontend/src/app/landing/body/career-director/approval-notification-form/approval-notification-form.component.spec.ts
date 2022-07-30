import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalNotificationFormComponent } from './approval-notification-form.component';

describe('ApprovalNotificationFormComponent', () => {
  let component: ApprovalNotificationFormComponent;
  let fixture: ComponentFixture<ApprovalNotificationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalNotificationFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalNotificationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

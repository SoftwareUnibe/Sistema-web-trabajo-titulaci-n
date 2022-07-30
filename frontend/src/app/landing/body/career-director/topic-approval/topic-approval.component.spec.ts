import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicApprovalComponent } from './topic-approval.component';

describe('TopicApprovalComponent', () => {
  let component: TopicApprovalComponent;
  let fixture: ComponentFixture<TopicApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicApprovalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicApprovalPreviewComponent } from './topic-approval-preview.component';

describe('TopicApprovalPreviewComponent', () => {
  let component: TopicApprovalPreviewComponent;
  let fixture: ComponentFixture<TopicApprovalPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicApprovalPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicApprovalPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

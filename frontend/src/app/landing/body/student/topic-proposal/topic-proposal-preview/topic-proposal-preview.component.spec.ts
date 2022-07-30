import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicProposalPreviewComponent } from './topic-proposal-preview.component';

describe('TopicProposalPreviewComponent', () => {
  let component: TopicProposalPreviewComponent;
  let fixture: ComponentFixture<TopicProposalPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicProposalPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicProposalPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

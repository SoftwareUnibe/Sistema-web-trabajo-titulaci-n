import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicDenunciationPreviewComponent } from './topic-denunciation-preview.component';

describe('TopicDenunciationPreviewComponent', () => {
  let component: TopicDenunciationPreviewComponent;
  let fixture: ComponentFixture<TopicDenunciationPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicDenunciationPreviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicDenunciationPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

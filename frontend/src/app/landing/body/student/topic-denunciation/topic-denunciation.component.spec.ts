import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicDenunciationComponent } from './topic-denunciation.component';

describe('TopicDenunciationComponent', () => {
  let component: TopicDenunciationComponent;
  let fixture: ComponentFixture<TopicDenunciationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicDenunciationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicDenunciationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

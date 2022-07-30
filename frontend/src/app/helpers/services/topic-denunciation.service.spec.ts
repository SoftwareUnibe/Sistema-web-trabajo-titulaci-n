import { TestBed } from '@angular/core/testing';

import { TopicDenunciationService } from './topic-denunciation.service';

describe('TopicDenunciationService', () => {
  let service: TopicDenunciationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicDenunciationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

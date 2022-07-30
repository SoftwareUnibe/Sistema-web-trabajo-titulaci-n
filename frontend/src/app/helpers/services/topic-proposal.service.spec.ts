import { TestBed } from '@angular/core/testing';

import { TopicProposalService } from './topic-proposal.service';

describe('TopicProposalService', () => {
  let service: TopicProposalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicProposalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

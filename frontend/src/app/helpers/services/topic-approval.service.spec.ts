import { TestBed } from '@angular/core/testing';

import { TopicApprovalService } from './topic-approval.service';

describe('TopicApprovalService', () => {
  let service: TopicApprovalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicApprovalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

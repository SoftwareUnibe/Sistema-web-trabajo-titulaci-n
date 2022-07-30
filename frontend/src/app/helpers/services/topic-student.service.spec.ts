import { TestBed } from '@angular/core/testing';

import { TopicStudentService } from './topic-student.service';

describe('TopicStudentService', () => {
  let service: TopicStudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopicStudentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

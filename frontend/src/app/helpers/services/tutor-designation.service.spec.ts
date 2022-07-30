import { TestBed } from '@angular/core/testing';

import { TutorDesignationService } from './tutor-designation.service';

describe('TutorDesignationService', () => {
  let service: TutorDesignationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TutorDesignationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

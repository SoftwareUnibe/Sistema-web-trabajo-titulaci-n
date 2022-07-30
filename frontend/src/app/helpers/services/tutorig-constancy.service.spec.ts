import { TestBed } from '@angular/core/testing';

import { TutorigConstancyService } from './tutorig-constancy.service';

describe('TutorigConstancyService', () => {
  let service: TutorigConstancyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TutorigConstancyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

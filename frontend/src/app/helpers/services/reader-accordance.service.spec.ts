import { TestBed } from '@angular/core/testing';

import { ReaderAccordanceService } from './reader-accordance.service';

describe('ReaderAccordanceService', () => {
  let service: ReaderAccordanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReaderAccordanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

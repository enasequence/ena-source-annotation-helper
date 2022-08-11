import { TestBed } from '@angular/core/testing';

import { SahCommonsService } from './sah-commons.service';

describe('SahCommonsService', () => {
  let service: SahCommonsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SahCommonsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

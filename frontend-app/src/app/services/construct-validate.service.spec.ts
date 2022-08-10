import { TestBed } from '@angular/core/testing';

import { ConstructValidateService } from './construct-validate.service';

describe('ConstructValidateService', () => {
  let service: ConstructValidateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConstructValidateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

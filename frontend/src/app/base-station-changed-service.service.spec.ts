import { TestBed } from '@angular/core/testing';

import { BaseStationChangedService } from './base-station-changed-service.service';

describe('BaseStationChangedServiceService', () => {
  let service: BaseStationChangedService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaseStationChangedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

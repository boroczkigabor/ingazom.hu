import { TestBed } from '@angular/core/testing';

import { SliderValueChangeService } from './slider-value-change.service';

describe('SliderValueChangeService', () => {
  let service: SliderValueChangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SliderValueChangeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

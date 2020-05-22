import { Component, OnInit } from '@angular/core';
import { SliderValueChangeService } from '../slider-value-change.service';

@Component({
  selector: 'app-slider',
  templateUrl: './slider.component.html',
  styleUrls: ['./slider.component.scss']
})
export class SliderComponent implements OnInit {

  constructor(
    private sliderValueChangeService: SliderValueChangeService
  ) { }

  ngOnInit(): void {
  }

  sliderValueChanged(event: Event) {
    this.sliderValueChangeService.valueChanged(+(event.target as HTMLInputElement).value);
  }
}

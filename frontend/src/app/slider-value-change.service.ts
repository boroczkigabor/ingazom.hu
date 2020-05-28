import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SliderValueChangeService {

  private sliderValueChanged = new Subject<number>();

  constructor() { }

  whenChanged() {
    return this.sliderValueChanged.asObservable();
  }

  valueChanged(value: number) {
    this.sliderValueChanged.next(value);
  }
}

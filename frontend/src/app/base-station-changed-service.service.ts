import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BaseStationChangedService {
  private baseStationSelectionSource = new Subject<string>();

  baseStationChanged$ = this.baseStationSelectionSource.asObservable();

  constructor() { }

  baseStationChanged(station: string) {
    this.baseStationSelectionSource.next(station);
  }
}

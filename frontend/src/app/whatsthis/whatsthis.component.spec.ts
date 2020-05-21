import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WhatsthisComponent } from './whatsthis.component';

describe('WhatsthisComponent', () => {
  let component: WhatsthisComponent;
  let fixture: ComponentFixture<WhatsthisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WhatsthisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WhatsthisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

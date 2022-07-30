import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderObservationsComponent } from './reader-observations.component';

describe('ReaderObservationsComponent', () => {
  let component: ReaderObservationsComponent;
  let fixture: ComponentFixture<ReaderObservationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderObservationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaderObservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

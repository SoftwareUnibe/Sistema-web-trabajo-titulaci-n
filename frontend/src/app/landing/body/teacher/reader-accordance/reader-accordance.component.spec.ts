import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderAccordanceComponent } from './reader-accordance.component';

describe('ReaderAccordanceComponent', () => {
  let component: ReaderAccordanceComponent;
  let fixture: ComponentFixture<ReaderAccordanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderAccordanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaderAccordanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

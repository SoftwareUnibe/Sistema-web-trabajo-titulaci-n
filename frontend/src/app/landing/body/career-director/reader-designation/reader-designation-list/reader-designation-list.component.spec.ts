import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderDesignationListComponent } from './reader-designation-list.component';

describe('ReaderDesignationListComponent', () => {
  let component: ReaderDesignationListComponent;
  let fixture: ComponentFixture<ReaderDesignationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderDesignationListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaderDesignationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

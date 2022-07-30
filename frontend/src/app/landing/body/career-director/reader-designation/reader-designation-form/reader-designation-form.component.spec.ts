import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderDesignationFormComponent } from './reader-designation-form.component';

describe('ReaderDesignationFormComponent', () => {
  let component: ReaderDesignationFormComponent;
  let fixture: ComponentFixture<ReaderDesignationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaderDesignationFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaderDesignationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

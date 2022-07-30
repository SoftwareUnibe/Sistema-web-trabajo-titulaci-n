import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AntiPlagiarismLetterComponent } from './anti-plagiarism-letter.component';

describe('AntiPlagiarismLetterComponent', () => {
  let component: AntiPlagiarismLetterComponent;
  let fixture: ComponentFixture<AntiPlagiarismLetterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AntiPlagiarismLetterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AntiPlagiarismLetterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

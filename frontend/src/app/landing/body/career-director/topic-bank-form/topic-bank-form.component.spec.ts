import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicBankFormComponent } from './topic-bank-form.component';

describe('TopicBankFormComponent', () => {
  let component: TopicBankFormComponent;
  let fixture: ComponentFixture<TopicBankFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicBankFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicBankFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

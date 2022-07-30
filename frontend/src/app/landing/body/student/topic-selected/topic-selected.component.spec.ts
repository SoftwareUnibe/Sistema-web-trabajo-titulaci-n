import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicSelectedComponent } from './topic-selected.component';

describe('TopicSelectedComponent', () => {
  let component: TopicSelectedComponent;
  let fixture: ComponentFixture<TopicSelectedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicSelectedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicSelectedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

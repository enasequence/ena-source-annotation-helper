import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstructstoreComponent } from './constructstore.component';

describe('ConstructstoreComponent', () => {
  let component: ConstructstoreComponent;
  let fixture: ComponentFixture<ConstructstoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConstructstoreComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConstructstoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidatestoreComponent } from './validatestore.component';

describe('ValidatestoreComponent', () => {
  let component: ValidatestoreComponent;
  let fixture: ComponentFixture<ValidatestoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidatestoreComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ValidatestoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

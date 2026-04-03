import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CatalogComponent } from './catalog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { RecommendationService } from '../services/recommendation.service';
import { of } from 'rxjs';

describe('CatalogComponent', () => {
  let component: CatalogComponent;
  let fixture: ComponentFixture<CatalogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogComponent, HttpClientTestingModule, RouterTestingModule],
      providers: [
        {
          provide: RecommendationService,
          useValue: { getReceivers: () => of([]) }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CatalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
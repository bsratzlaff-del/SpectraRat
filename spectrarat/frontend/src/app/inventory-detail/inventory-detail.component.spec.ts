import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InventoryDetailComponent } from './inventory-detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { RecommendationService } from '../services/recommendation.service';

describe('InventoryDetailComponent', () => {
  let component: InventoryDetailComponent;
  let fixture: ComponentFixture<InventoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        InventoryDetailComponent, 
        HttpClientTestingModule, 
        RouterTestingModule
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => '1' } } // Fakes the ID in the URL
          }
        },
        {
          provide: RecommendationService,
          useValue: {
            getRecommendations: () => of([]) // Fakes a successful API call
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(InventoryDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
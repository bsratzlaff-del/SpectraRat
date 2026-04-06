import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecommendationService } from '../services/recommendation.service';
import { FccService } from '../services/fcc.service'; // <--- ADD THIS
import { RecommendationResult } from '../models/recommendation-result.model';
import { Receiver } from '../models/receiver.model';
import { FrequencyBand } from '../models/frequency-band'; // <--- ADD THIS
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-recommendation',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './recommendation.component.html',
  styleUrls: ['./recommendation.component.css']
})
export class RecommendationComponent implements OnInit {
  // Services
  private recommendationService = inject(RecommendationService);
  private fccService = inject(FccService); // <--- ADD THIS

  // Form State
  zipCode: string = '';
  selectedReceiverId: number | null = null;
  selectedBandId: number | null = null; // <--- ADD THIS for the new dropdown

  // Data State
  receivers: Receiver[] = [];
  results: RecommendationResult[] = [];
  protected frequencyBands = signal<FrequencyBand[]>([]); // <--- ADD THIS

  // UI State
  loading: boolean = false;
  hasSearched: boolean = false;

  ngOnInit(): void {
    // 1. Load the original receivers list
    this.recommendationService.getReceivers().subscribe(data => {
      this.receivers = data;
      if (this.receivers.length > 0) this.selectedReceiverId = this.receivers[0].id;
    });

    // 2. Load the 15 Frequency Bands from Azure/PostgreSQL
    this.loadFrequencyBands();
  }

  loadFrequencyBands() {
    this.fccService.getFrequencyBands().subscribe({
      next: (data) => {
        this.frequencyBands.set(data);
        console.log('Successfully loaded bands into Recommendation Component:', data.length);
      },
      error: (err) => console.error('FccService failed in RecommendationComponent', err)
    });
  }

  analyzeSpectrum() {
    // Basic validation: ensures zip and receiver are picked
    if (!this.zipCode || !this.selectedReceiverId) return;

    this.loading = true;
    this.recommendationService.getRecommendations(this.zipCode, this.selectedReceiverId)
      .subscribe({
        next: (data) => {
          this.results = data;
          this.loading = false;
          this.hasSearched = true;
        },
        error: (err) => {
          console.error('Analysis failed', err);
          this.loading = false;
        }
      });
  }
}
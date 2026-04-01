import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // <--- ADD THIS
import { FormsModule } from '@angular/forms';   // <--- ADD THIS
import { RecommendationService } from '../services/recommendation.service';
import { RecommendationResult } from '../models/recommendation-result.model';
import { Receiver } from '../models/receiver.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-recommendation',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink], // MUST HAVE THESE TWO
  templateUrl: './recommendation.component.html',
  styleUrls: ['./recommendation.component.css']
})

export class RecommendationComponent implements OnInit {
  // Form State
  zipCode: string = '';
  selectedReceiverId: number | null = null;
  
  // Data State
  receivers: Receiver[] = [];
  results: RecommendationResult[] = [];
  
  // UI State
  loading: boolean = false;
  hasSearched: boolean = false;

  constructor(private recommendationService: RecommendationService) {}

  ngOnInit(): void {
    // Load the dropdown list on startup
    this.recommendationService.getReceivers().subscribe(data => {
      this.receivers = data;
      if (this.receivers.length > 0) this.selectedReceiverId = this.receivers[0].id;
    });
  }

  analyzeSpectrum() {
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
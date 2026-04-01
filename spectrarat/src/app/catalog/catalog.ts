import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RecommendationService } from '../services/recommendation.service';

@Component({
  selector: 'app-catalog',
  standalone: true,
  imports: [CommonModule, RouterLink], // RouterLink is vital for the 'Configure' button
  templateUrl: './catalog.html',
  styleUrls: ['./catalog.css']
})
export class CatalogComponent implements OnInit {
  receivers: any[] = [];
  loading: boolean = true;

  private recService = inject(RecommendationService);

  ngOnInit(): void {
    this.recService.getReceivers().subscribe({
      next: (data) => {
        this.receivers = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load catalog', err);
        this.loading = false;
      }
    });
  }
}
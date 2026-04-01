import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { RecommendationService } from '../services/recommendation.service';

@Component({
  selector: 'app-inventory-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inventory-detail.component.html',
  styleUrls: ['./inventory-detail.component.css']
})
export class InventoryDetailComponent implements OnInit {
  // --- 1. PROPERTY DECLARATIONS ---
  selectedReceiver: any = {};
  selectedCapsule: string = 'SM58';
  quantity: number = 1;
  zipCode: string = '';
  isAutoFilled: boolean = false;
  selectedBand: string = '';
  analysisResults: any[] = [];
  currentUser: any = null; // Assuming this will be used for user-specific data
  recommendedBand: any = null;

  // --- 2. SERVICE INJECTIONS ---
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);
  private recommendationService = inject(RecommendationService);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.loadHardwareDetails(id);
  }

  // --- 3. UPDATED SELECTION LOGIC ---
  analyzeBands() {
    if (!this.zipCode) return;
    this.recommendationService.getRecommendations(this.zipCode, this.selectedReceiver.id)
      .subscribe(results => {
        const highestMatch = Math.max(...results.map((r: any) => r.matchPercentage));
        
        this.analysisResults = results.map((res: any) => ({
          ...res,
          isRecommended: res.matchPercentage === highestMatch 
        }));

        // This isolates the winner so the UI can highlight the best option
        this.recommendedBand = this.analysisResults.find(r => r.isRecommended);

        if (this.recommendedBand) {
          this.selectedBand = this.recommendedBand.modelName;
        }
      });
  }

  // This runs when the user clicks "Use This Band" on a recommendation card
  applyRecommendation() {
    if (this.recommendedBand) {
      this.selectedBand = this.recommendedBand.modelName;
    }
  }

  // --- 4. FINALIZING THE PURCHASE ---
  addToInventory() {
    if (!this.selectedReceiver.id || !this.selectedBand) {
      alert('Please select a receiver and analyze the spectrum to choose a band.');
      return;
    }

    const inventoryItem = {
      receiverId: this.selectedReceiver.id,
      receiverName: `${this.selectedReceiver.manufacturer} ${this.selectedReceiver.modelName}`,
      band: this.selectedBand,
      capsule: this.selectedCapsule,
      quantity: this.quantity,
      zipCode: this.zipCode,
      purchaseDate: new Date().toISOString()
    };

    console.log('Adding to inventory:', inventoryItem);

    // In a real application, you would send this to a backend service.
    // For example:
    // this.http.post('/api/inventory', inventoryItem).subscribe({
    //   next: () => this.router.navigate(['/dashboard']),
    //   error: (err) => console.error('Failed to add to inventory', err)
    // });

    alert('Item has been added to your virtual inventory! (Check console for details)');
    this.router.navigate(['/dashboard']);
  }

  loadHardwareDetails(id: string | null) {
    if (!id) return;
    this.http.get<any>(`/api/receivers/${id}`).subscribe(receiver => {
      this.selectedReceiver = receiver;
    });
  }
}
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
  selectedBand: string = '';
  analysisResults: any[] = [];
  currentUser: any = null; 
  recommendedBand: any = null;
  loadingAnalysis: boolean = false;

  // --- 2. SERVICE INJECTIONS ---
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);
  private recommendationService = inject(RecommendationService);

  ngOnInit() {
    // 1. Get the Hardware ID from the URL
    const id = this.route.snapshot.paramMap.get('id');
    this.loadHardwareDetails(id);

    // 2. AUTO-FILL LOGIC: Check if user is logged in and pull their ZIP
    const sessionData = localStorage.getItem('currentUser');
    if (sessionData) {
      this.currentUser = JSON.parse(sessionData);
      this.zipCode = this.currentUser.zipCode || '';
      
      // If we auto-filled the zip, run the analysis automatically for them!
      if (this.zipCode) {
        this.analyzeBands();
      }
    }
  }

  // --- 3. THE "SMART" SELECTION LOGIC ---
  analyzeBands() {
    if (!this.zipCode || !this.selectedReceiver.id) return;
    
    this.loadingAnalysis = true;
    this.recommendationService.getRecommendations(this.zipCode, this.selectedReceiver.id)
      .subscribe({
        next: (results) => {
          const highestMatch = Math.max(...results.map((r: any) => r.matchPercentage));
          
          this.analysisResults = results.map((res: any) => ({
            ...res,
            isRecommended: res.matchPercentage === highestMatch 
          }));

          this.recommendedBand = this.analysisResults.find(r => r.isRecommended);

          // Default the selection to the recommended band
          if (this.recommendedBand) {
            this.selectedBand = this.recommendedBand.modelName;
          }
          this.loadingAnalysis = false;
        },
        error: (err) => {
          console.error('Spectrum analysis failed', err);
          this.loadingAnalysis = false;
        }
      });
  }

  applyRecommendation() {
    if (this.recommendedBand) {
      this.selectedBand = this.recommendedBand.modelName;
    }
  }

  // --- 4. THE PURCHASE & DATABASE WIRING ---
  addToInventory() {
    if (!this.selectedBand) {
      alert('Please select a frequency band before purchasing.');
      return;
    }

    // Creating the payload for your Spring Boot 'PurchaseRecord' entity
    const purchaseData = {
      businessId: this.currentUser?.id,
      receiverModel: `${this.selectedReceiver.manufacturer} ${this.selectedReceiver.modelName}`,
      capsuleType: this.selectedCapsule,
      quantity: this.quantity,
      assignedBand: this.selectedBand,
      totalPrice: this.calculateTotal(),
      purchaseDate: new Date().toISOString().split('T')[0] // Formats as YYYY-MM-DD
    };

    // Replace with your actual Spring Boot API URL
    this.http.post('http://localhost:8082/api/purchases', purchaseData).subscribe({
      next: () => {
        alert('System successfully added to your inventory!');
        this.router.navigate(['/dashboard']); 
      },
      error: (err) => {
        console.error('Failed to save purchase', err);
        alert('Checkout failed. Please ensure the backend server is running.');
      }
    });
  }

  loadHardwareDetails(id: string | null) {
    if (!id) return;
    // Calling your specific receiver endpoint
    this.http.get<any>(`http://localhost:8082/api/receivers/${id}`).subscribe(data => {
      this.selectedReceiver = data;
    });
  }

  calculateTotal(): number {
    const basePrice = 1299.00;
    const premiums: { [key: string]: number } = {
      'SM58': 0,
      'Beta58A': 150.00,
      'KSM9': 550.00
    };
    return (basePrice + (premiums[this.selectedCapsule] || 0)) * this.quantity;
  }
}
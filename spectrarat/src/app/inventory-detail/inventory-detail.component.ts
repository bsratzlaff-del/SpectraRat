import { Component, inject, OnInit } from '@angular/core';
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
  selectedReceiver: any = {};
  zipCode: string = '';
  quantity: number = 1;
  isAutoFilled: boolean = false;
  selectedBand: string = '';
  analysisResults: any[] = [];
  capsules = [
    { name: 'Shure SM58 (Industry Standard)', value: 'SM58', price: 0 },
    { name: 'Shure Beta 58A (Supercardioid)', value: 'Beta58A', price: 150 },
    { name: 'Shure KSM9 (Premium Condenser)', value: 'KSM9', price: 550 }
  ];
  selectedCapsule: string = 'SM58'; // Default to the first capsule
  currentUser: any = null;
  recommendedBand: any = null;

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);
  private recommendationService = inject(RecommendationService);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadHardwareDetails(id);
    }
    // Load the current user from local storage to associate the purchase
    this.currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  }

  loadHardwareDetails(id: string) {
    this.http.get<any>(`http://localhost:8082/api/receivers/${id}`).subscribe(receiver => {
      this.selectedReceiver = receiver;
    });
  }

  calculateTotal(): number {
  console.log("=== STARTING MATH CHECK ===");
  
  // 1. Check what the receiver actually looks like
  console.log("1. Receiver Data:", this.selectedReceiver);

  // 2. Safely get the base price (Fallback to 1299 if DB is empty)
  const basePrice = Number(this.selectedReceiver?.price) || 1299.00;
  console.log("2. Base Price determined as:", basePrice);

  // 3. Check the capsule
  const premiums: { [key: string]: number } = {
    'SM58': 0,
    'Beta58A': 150.00,
    'KSM9': 550.00
  };
  const capsulePremium = premiums[this.selectedCapsule] || 0;
  console.log("3. Capsule Premium for " + this.selectedCapsule + ":", capsulePremium);

  // 4. THE USUAL SUSPECT: Check the quantity
  console.log("4. Raw Quantity from HTML:", this.quantity);
  
  // Force quantity to be a number, and if it's broken or 0, default to 1
  const safeQuantity = Number(this.quantity) || 1; 

  // 5. The Final Equation
  const finalCost = (basePrice + capsulePremium) * safeQuantity;
  console.log("5. FINAL MATH RESULT:", finalCost);
  console.log("===========================");

  return finalCost;
}

  addToCart() {
    const calculatedCost = this.calculateTotal();
  
    console.log("BASE RECEIVER:", this.selectedReceiver.modelName);
    console.log("SAVING COST AS:", calculatedCost);

    if (!this.selectedBand) {
      alert('Please select a frequency band before adding to cart.');
      return;
    }

    const itemToStore = {
    businessId: this.currentUser?.id,
    receiverModel: `${this.selectedReceiver.manufacturer} ${this.selectedReceiver.modelName}`,
    capsuleType: this.selectedCapsule,
    quantity: this.quantity,
    assignedBand: this.selectedBand,
    // CRITICAL: Ensure this calls the function and assigns the number!
    cost: this.calculateTotal(), 
    purchaseDate: new Date().toISOString().split('T')[0]
  };

    const currentCart = JSON.parse(localStorage.getItem('spectraCart') || '[]');
    currentCart.push(itemToStore);
    localStorage.setItem('spectraCart', JSON.stringify(currentCart));
  
    this.router.navigate(['/cart']);
}

  // --- Analysis methods required for band selection ---
  analyzeBands() {
    if (!this.zipCode || !this.selectedReceiver.id) return;

    this.recommendationService.getRecommendations(this.zipCode, this.selectedReceiver.id)
      .subscribe((results: any[]) => {
        const highestMatch = Math.max(...results.map(r => r.matchPercentage));
        this.analysisResults = results.map(res => ({
          ...res,
          isRecommended: res.matchPercentage === highestMatch
        }));
        this.recommendedBand = this.analysisResults.find(r => r.isRecommended);

        if (this.recommendedBand) {
          this.selectedBand = this.recommendedBand.modelName;
          this.isAutoFilled = true;
        }
      });
  }

  applyRecommendation() {
    if (this.recommendedBand) {
      this.selectedBand = this.recommendedBand.modelName;
      this.isAutoFilled = true;
    }
  }
}
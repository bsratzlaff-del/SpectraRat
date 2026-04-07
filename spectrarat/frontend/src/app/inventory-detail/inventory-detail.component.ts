import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RecommendationService } from '../services/recommendation.service';
import { ReceiverService } from '../services/receiver.service';
import { FccService } from '../services/fcc.service';
import { FrequencyBand } from '../models/frequency-band';

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
  recommendations: any[] = [];
  loading: boolean = false;
  
  analysisResults: any[] = [];
  capsules = [
    { name: 'Shure SM58 (Industry Standard)', value: 'SM58', price: 0 },
    { name: 'Shure Beta 58A (Supercardioid)', value: 'Beta58A', price: 150 },
    { name: 'Shure KSM9 (Premium Condenser)', value: 'KSM9', price: 550 }
  ];
  selectedCapsule: string = 'SM58';
  currentUser: any = null;
  recommendedBand: any = null;
  frequencyBands = signal<FrequencyBand[]>([]);

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private recommendationService = inject(RecommendationService);
  private receiverService = inject(ReceiverService);
  private fccService = inject(FccService);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadHardwareDetails(id);
    }
    this.currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  }

  loadHardwareDetails(id: string) {
    this.receiverService.getReceiverById(id).subscribe(receiver => {
      this.selectedReceiver = receiver;
    });
  }

  calculateTotal(): number {
    const basePrice = Number(this.selectedReceiver?.cost) || 0;
    const selectedCapsuleObject = this.capsules.find(c => c.value === this.selectedCapsule);
    const capsulePremium = selectedCapsuleObject ? selectedCapsuleObject.price : 0;
    const safeQuantity = Number(this.quantity) || 1;
    return (basePrice + capsulePremium) * safeQuantity;
  }

  addToCart() {
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
      cost: this.calculateTotal(),
      purchaseDate: new Date().toISOString().split('T')[0]
    };

    const currentCart = JSON.parse(localStorage.getItem('spectraCart') || '[]');
    currentCart.push(itemToStore);
    localStorage.setItem('spectraCart', JSON.stringify(currentCart));
    this.router.navigate(['/cart']);
  }

    analyzeBands() {
      if (!this.zipCode || !this.selectedReceiver?.id) return;

      this.loading = true; // Optional: add a loading spinner state if you have it
      this.recommendationService.getRecommendations(this.zipCode, this.selectedReceiver.id)
        .subscribe({
          next: (results: any[]) => {
            // We take the top 3 results from the backend
            this.recommendations = results.slice(0, 3);
            this.loading = false;
          },
          error: (err) => {
            console.error('Analysis failed', err);
            this.loading = false;
          }
        });
    }

    applyRecommendation(bandName: string) {
      if (!bandName || !this.selectedReceiver?.availableFrequencyBands) return;

      console.log("Attempting to match:", bandName);

      // 1. Find the band in your hardware list that is mentioned in the recommendation string
      const matchedBand = this.selectedReceiver.availableFrequencyBands.find((band: any) => 
        bandName.includes(band.bandName) || band.bandName === bandName
      );

      if (matchedBand) {
        // 2. Assign the EXACT value that the dropdown expects
        this.selectedBand = matchedBand.bandName;
        this.isAutoFilled = true;
        console.log("Match found! Setting dropdown to:", matchedBand.bandName);
      } else {
        console.warn("No matching band found in the hardware list for:", bandName);
        // Fallback: try setting it anyway in case it's a direct match
        this.selectedBand = bandName;
      }

      // 3. Smooth scroll to the checkout area
      document.getElementById('frequencyBand')?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
  }

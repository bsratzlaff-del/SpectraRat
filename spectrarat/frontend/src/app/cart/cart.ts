import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './cart.html',
  styleUrls: ['./cart.css']
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  totalCartValue: number = 0;

  private http = inject(HttpClient);
  private router = inject(Router);

  ngOnInit() {
    this.loadCart();
  }

  loadCart() {
    const data = localStorage.getItem('spectraCart');
    this.cartItems = data ? JSON.parse(data) : [];
    
    this.totalCartValue = 0;
    this.cartItems.forEach(item => {
    this.totalCartValue += Number(item.cost) || 0;
  });
  }

  calculateTotal(): void {
        this.totalCartValue = this.cartItems.reduce((sum, item) => sum + (Number(item.cost) || 0), 0);
  }

  removeItem(index: number) {
    this.cartItems.splice(index, 1);
    localStorage.setItem('spectraCart', JSON.stringify(this.cartItems));
    this.calculateTotal();
  }

  checkout() {
    if (this.cartItems.length === 0) {
      alert("Your cart is empty.");
      return;
    }

    // This assumes a backend endpoint exists at /api/purchases/batch
    this.http.post(`http://193.122.198.189/api/purchases/batch`, this.cartItems).subscribe({
      next: () => {
        alert('Checkout successful!');
        localStorage.removeItem('spectraCart');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Checkout failed', err);
        alert('Checkout failed. Please try again.');
      }
    });
  }
  finalizeCheckout() {
  // 3. Clear the "Basket" because the items are now in the "Database"
  localStorage.removeItem('spectraCart');
  
  // 4. Update the local variable so the UI doesn't show ghost items
  this.cartItems = []; 
  
  alert('Purchase Successful! Redirecting to Business Profile...');
  
  // 5. Head to the dashboard to see the new History
  this.router.navigate(['/dashboard']);
}
}

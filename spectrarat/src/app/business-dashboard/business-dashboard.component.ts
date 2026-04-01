import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-business-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './business-dashboard.component.html',
  styleUrls: ['./business-dashboard.component.css']
})
export class BusinessDashboardComponent implements OnInit {
  view: string = 'profile';
  account: any = { businessName: '', businessEmail: '', streetAddress: '', zipCode: '' };
  
  // RENAME THIS from 'history' to 'purchaseHistory' to avoid browser conflicts
  purchaseHistory: any[] = []; 

  private http = inject(HttpClient);

  ngOnInit() {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    if (user.id) {
      // Fetch the REAL history from your Java API
      this.http.get<any[]>(`http://localhost:8082/api/purchases/business/${user.id}`)
        .subscribe(data => {
          this.purchaseHistory = data; // This fills your table!
        });
    }
  }

  updateProfile() {
    console.log('Profile updated', this.account);
  }

  generateReport() {
    const reportWindow = window.open('', '_blank');
    reportWindow?.document.write(`
      <html>
        <body>
          <h1>Business Report: ${this.account.businessName}</h1>
          <ul>
            ${this.purchaseHistory.map(h => `<li>${h.purchaseDate}: ${h.receiverModel}</li>`).join('')}
          </ul>
        </body>
      </html>
    `);
    reportWindow?.print();
  }
}
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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

  ngOnInit() {
    // Load your data here
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
            ${this.purchaseHistory.map(h => `<li>${h.purchaseDate}: ${h.modelName}</li>`).join('')}
          </ul>
        </body>
      </html>
    `);
    reportWindow?.print();
  }
}
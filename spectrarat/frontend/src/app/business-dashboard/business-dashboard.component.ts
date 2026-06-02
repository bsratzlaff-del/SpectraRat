import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-business-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule], 
  templateUrl: './business-dashboard.component.html',
  styleUrls: ['./business-dashboard.component.css']
})
export class BusinessDashboardComponent implements OnInit {
  editForm!: FormGroup;
  view: string = 'profile';
  purchaseHistory: any[] = []; 
  currentUser: any = null;

  private fb = inject(FormBuilder);
  private http = inject(HttpClient);

  ngOnInit() {
    // 1. Initialize the form structure
    this.editForm = this.fb.group({
      businessName: [''],
      streetAddress: [''],
      city: [''],
      state: [''],
      zipCode: ['']
    });

    const userJson = localStorage.getItem('currentUser');
    if (userJson) {
      this.currentUser = JSON.parse(userJson);
      
      // 2. Wrap the patchValue in a timeout to ensure the UI is ready
      setTimeout(() => {
        this.editForm.patchValue({
          businessName: this.currentUser.businessName,
          streetAddress: this.currentUser.streetAddress,
          city: this.currentUser.city,
          state: this.currentUser.state,
          zipCode: this.currentUser.zipCode
        });
        console.log("Form Patched with:", this.editForm.value);
      }, 50); // This timer waits 50ms to ensure the form is loaded

      // 3. Fetch History
      this.fetchPurchaseHistory(this.currentUser.username);
    }
  }

  fetchPurchaseHistory(username: string) {
   this.http.get<any[]>(`${environment.apiUrl}/purchases/user/${username}`).subscribe({
      next: (data) => {
        this.purchaseHistory = data;
        console.log("Raw Purchase Data:", data);
        console.table(data); 
      },
      error: (err) => console.error("History fetch failed:", err)
    });
  }

  updateProfile() {
    if (this.editForm.valid) {
      console.log('Update Data to send to Java:', this.editForm.value);
      // Future TODO: Add this.http.put to save changes to the DB
    }
  }

  generateReport() {
    window.print();
  }
}
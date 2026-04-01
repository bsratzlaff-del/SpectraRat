import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html'
})
export class LoginComponent implements OnInit {
  // Toggles between Login form and Registration form
  isRegisterMode = false;
  
  // Consolidated form data for both login and registration
  formData = {
    username: '',
    password: '',
    businessName: '',
    address: '',
    phone: '',
    zipCode: ''
  };

  errorMessage: string = '';

  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  ngOnInit() {
    // Listen for the ?mode=register query param from the Navbar "Sign Up" button
    this.route.queryParams.subscribe(params => {
      if (params['mode'] === 'register') {
        this.isRegisterMode = true;
      } else {
        this.isRegisterMode = false;
      }
    });
  }

  toggleMode() {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = ''; // Clear errors when switching
  }

  onSubmit() {
    const endpoint = this.isRegisterMode ? 'register' : 'login';
    const payload = this.formData;

    this.http.post(`http://localhost:8082/api/auth/${endpoint}`, payload).subscribe({
      next: (user: any) => {
        // Save user session
        localStorage.setItem('currentUser', JSON.stringify(user));
        
        // Redirect to dashboard
        window.location.href = '/dashboard'; 
      },
      error: (err) => {
        if (this.isRegisterMode) {
          this.errorMessage = 'Registration failed. Username might be taken.';
        } else {
          this.errorMessage = 'Invalid username or password. Please try again.';
        }
        console.error(`${endpoint} failed`, err);
      }
    });
  }
}
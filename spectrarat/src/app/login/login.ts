import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {
  credentials = {
    email: '',
    password: ''
  };
  errorMessage: string = '';

  private http = inject(HttpClient);
  private router = inject(Router);

  onSubmit() {
    this.http.post('http://localhost:8082/api/auth/login', this.credentials).subscribe({
      next: (user: any) => {
        // Save the verified user into the browser's memory
        localStorage.setItem('currentUser', JSON.stringify(user));
        
        // Force a page redirect so the Navbar catches the update
        window.location.href = '/dashboard'; 
      },
      error: (err) => {
        this.errorMessage = 'Invalid email or password. Please try again.';
        console.error('Login failed', err);
      }
    });
  }
}
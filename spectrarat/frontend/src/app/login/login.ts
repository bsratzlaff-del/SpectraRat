import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html'
})
export class LoginComponent implements OnInit {
  isRegisterMode = false;
  registerForm!: FormGroup;
  errorMessage: string = '';

  private http = inject(HttpClient);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);

  ngOnInit() {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      businessName: [''],
      streetAddress: [''],
      city: [''],
      state: [''],
      zipCode: [''],
      email: ['']
    });

    this.route.queryParams.subscribe(params => {
      this.isRegisterMode = params['mode'] === 'register';
      this.updateValidators(this.isRegisterMode);
    });
  }

  updateValidators(isRegister: boolean) {
    const emailControl = this.registerForm.get('email');
    const registrationControls = ['businessName', 'streetAddress', 'city', 'state', 'zipCode'];

    if (isRegister) {
      emailControl?.setValidators([Validators.required, Validators.email]);
      registrationControls.forEach(controlName => {
        this.registerForm.get(controlName)?.setValidators(Validators.required);
      });
    } else {
      emailControl?.clearValidators();
      registrationControls.forEach(controlName => {
        this.registerForm.get(controlName)?.clearValidators();
      });
    }
    emailControl?.updateValueAndValidity();
    registrationControls.forEach(controlName => {
      this.registerForm.get(controlName)?.updateValueAndValidity();
    });
  }

  toggleMode() {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = ''; // Clear errors when switching
    this.registerForm.reset();
    this.updateValidators(this.isRegisterMode);
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      this.errorMessage = 'Please fill out all required fields correctly.';
      return;
    }

    const endpoint = this.isRegisterMode ? 'register' : 'login';
    let payload: any;

    // Build the EXACT payload Spring Boot wants depending on the mode
    if (this.isRegisterMode) {
      payload = {
        username: this.registerForm.value.username,
        password: this.registerForm.value.password,
        email: this.registerForm.value.email || "pending@spectrarat.com",
        businessName: this.registerForm.value.businessName,
        streetAddress: this.registerForm.value.streetAddress,
        city: this.registerForm.value.city,
        state: this.registerForm.value.state,
        zipCode: this.registerForm.value.zipCode
      };
    } else {
      // Login only needs these two
      payload = {
        username: this.registerForm.value.username,
        password: this.registerForm.value.password
      };
    }

    this.http.post(`${environment.apiUrl}/auth/${endpoint}`, payload).subscribe({
      next: (user: any) => {
        // Save user session
        localStorage.setItem('currentUser', JSON.stringify(user));
        window.location.href = '/dashboard'; 
      },
      error: (err) => {
        if (this.isRegisterMode) {
          this.errorMessage = 'Registration failed. Username might be taken or fields are missing.';
        } else {
          this.errorMessage = 'Invalid username or password. Please try again.';
        }
        console.error(`${endpoint} failed`, err);
      }
    });
  }
}
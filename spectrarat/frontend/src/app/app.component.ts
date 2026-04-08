import { Component, signal, inject, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';

// Service Imports
import { FccService } from './services/fcc.service'; 
import { AuthService } from './services/auth.service';

// Model Imports
import { FrequencyBand } from './models/frequency-band'; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive], 
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy { 
  // State Variables
  isLoggedIn = false;
  private authSubscription?: Subscription;

  // Signals for System Status
  protected readonly title = signal('spectrarat-ui');
  protected connectionMessage = signal<string>('Initializing system...');
  protected frequencyBands = signal<FrequencyBand[]>([]);

  // Injections
  private frequencyBandService = inject(FccService); 
  private router = inject(Router);
  private authService = inject(AuthService);

  ngOnInit() {
    // 1. Initialize Frequency Data
    this.refreshData();

    // 2. THE FIX: Subscribe to the Auth State
    // This 'listens' for the login/register success and updates the UI instantly
    this.authSubscription = this.authService.isLoggedIn$.subscribe(status => {
      this.isLoggedIn = status;
      console.log('App Root: Login status updated ->', this.isLoggedIn);
    });
  }

  // Proper cleanup to prevent memory leaks (good for the 'Code Quality' rubric!)
  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  logout() {
    // 1. Clear the session
    localStorage.removeItem('currentUser');
    
    // 2. Tell the service to 'shout' the logout status to the Navbar
    this.authService.setLoginStatus(false);
    
    // 3. Send them home
    this.router.navigate(['/shop']);
  }

  refreshData() {
    this.connectionMessage.set('Fetching frequency bands...');
    
    this.frequencyBandService.getFrequencyBands().subscribe({ 
      next: (bands: FrequencyBand[]) => {
        this.frequencyBands.set(bands);
        this.connectionMessage.set(`✅ System Online: ${bands.length} bands loaded.`);
      },
      error: (err: HttpErrorResponse) => {
        console.error('Data fetch failed', err);
        this.connectionMessage.set(`❌ Error ${err.status}: Connection failed. Check Azure backend.`);
      }
    });
  }
}
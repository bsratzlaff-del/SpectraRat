import { Component, signal, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // <-- CRITICAL: Required for *ngIf
import { FccService } from './services/fcc.service'; 
import { FrequencyBand } from './models/frequency-band'; 
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  // CRITICAL: CommonModule must be in this array!
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive], 
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit { 
  protected readonly title = signal('spectrarat-ui');
  
  private fccService = inject(FccService);
  private router = inject(Router); 
  
  protected connectionMessage = signal<string>('Initializing system...');
  protected frequencyBands = signal<FrequencyBand[]>([]);
  
  // --- Authentication Tracker ---
  isLoggedIn: boolean = false;

  ngOnInit() {
    this.refreshData();
    this.checkLoginStatus(); // Check VIP pass on load
  }

  // Check if user data exists in local storage
  checkLoginStatus() {
    const sessionData = localStorage.getItem('currentUser');
    this.isLoggedIn = !!sessionData; 
  }

  // --- CRITICAL: The Missing Logout Function ---
  logout() {
    localStorage.removeItem('currentUser');
    this.isLoggedIn = false;
    this.router.navigate(['/shop']);
  }

  refreshData() {
    this.connectionMessage.set('Fetching frequency bands...');
    
    this.fccService.getFccSpectrumBands().subscribe({
      next: (bands: FrequencyBand[]) => {
        this.frequencyBands.set(bands);
        this.connectionMessage.set(`✅ System Online: ${bands.length} bands loaded.`);
      },
      error: (err) => {
        console.error('Data fetch failed', err);
        this.connectionMessage.set(`❌ Connection Error. Is Spring Boot on port 8082?`);
      }
    });
  }
}
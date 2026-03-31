import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FccService } from './services/fcc.service'; // Import your service
import { FrequencyBand } from './models/frequency-band'; // Import the interface for FrequencyBand

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit { // 2. Implement OnInit
  protected readonly title = signal('spectrarat-ui');
  private fccService = inject(FccService);
  
  protected connectionMessage = signal<string>('Initializing system...');
  protected frequencyBands = signal<FrequencyBand[]>([]);

  testConnection() {
  this.refreshData(); // This just points the button to the new logic
}

  // 3. This runs automatically when the page loads
  ngOnInit() {
    this.refreshData();
  }

  // I moved the logic here so you can call it anytime (like after an update)
  refreshData() {
    this.connectionMessage.set('Fetching frequency bands...');
    
    this.fccService.getFccSpectrumBands().subscribe({
      next: (bands: FrequencyBand[]) => {
        this.frequencyBands.set(bands);
        this.connectionMessage.set(`✅ System Online: ${bands.length} bands loaded.`);
      },
      error: (err) => {
        console.error('Data fetch failed', err);
        this.connectionMessage.set(`❌ Connection Error. Is Spring Boot on port 8081?`);
      }
    });
  }
}
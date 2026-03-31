import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FrequencyBand } from '../models/frequency-band'; // Import the interface

@Injectable({
  providedIn: 'root'
})
export class FccService {
  // Use the modern Angular 18 'inject' method
  private http = inject(HttpClient); 
  
  // Your backend address
  private baseUrl = 'http://localhost:8081/api/fcc';

  /**
   * Hits the /validate endpoint on your Spring Boot Controller.
   * We expect a plain string back, so we tell Angular not to look for JSON.
   */
  validateApiConnection(): Observable<string> {
    return this.http.get(`${this.baseUrl}/validate`, { responseType: 'text' });
  }

  /**
   * Hits the /bands endpoint.
   * Note: You'll eventually want to create a FrequencyBand interface for this!
   */
  getFccSpectrumBands(): Observable<FrequencyBand[]> {
  return this.http.get<FrequencyBand[]>(`${this.baseUrl}/bands`);
}
}

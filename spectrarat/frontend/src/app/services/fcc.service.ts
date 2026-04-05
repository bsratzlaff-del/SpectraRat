import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FrequencyBand } from '../models/frequency-band'; // Import the interface
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class FccService {
  
  private http = inject(HttpClient); 
  
  private baseUrl = `${environment.apiUrl}/frequency-bands`;

  /**
   * Hits the /validate endpoint on your Spring Boot Controller.
   * We expect a plain string back, so we tell Angular not to look for JSON.
   */
  validateApiConnection(): Observable<string> {
    return this.http.get(`${this.baseUrl}/validate`, { responseType: 'text' });
  }

  getFrequencyBands(): Observable<FrequencyBand[]> {
  return this.http.get<FrequencyBand[]>(this.baseUrl);
}
}

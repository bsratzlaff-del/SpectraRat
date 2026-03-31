import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FccService {
  // Angular 18 way to inject dependencies
  private http = inject(HttpClient); 
  
  // Your Spring Boot backend URL
  private baseUrl = 'http://localhost:8081/api/fcc';

  /**
   * Pings the backend to validate the API connection.
   * Note: We use responseType: 'text' because your backend returns a raw String, not a JSON object.
   */
  validateApiConnection(): Observable<string> {
    return this.http.get(`${this.baseUrl}/validate`, { responseType: 'text' });
  }
}
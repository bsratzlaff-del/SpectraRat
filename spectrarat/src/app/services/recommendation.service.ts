import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecommendationResult } from '../models/recommendation-result.model';
import { Receiver } from '../models/receiver.model';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {
  private baseUrl = 'http://localhost:8082/api';

  constructor(private http: HttpClient) { }

  // Fetches the list of receivers for the dropdown
  getReceivers(): Observable<Receiver[]> {
    return this.http.get<Receiver[]>(`${this.baseUrl}/receivers`);
  }

  // Runs the Spectral Analysis algorithm
  getRecommendations(zipCode: string, receiverId: number): Observable<RecommendationResult[]> {
    const params = new HttpParams()
      .set('zipCode', zipCode)
      .set('receiverId', receiverId.toString());

    return this.http.get<RecommendationResult[]>(`${this.baseUrl}/recommendations/top-candidates`, { params });
  }
}
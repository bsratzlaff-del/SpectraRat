import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

import { RecommendationResult } from '../models/recommendation-result.model';
import { Receiver } from '../models/receiver.model';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {
  
  private baseUrl = environment.apiUrl; 

  constructor(private http: HttpClient) { }

  getReceivers(): Observable<Receiver[]> {
    return this.http.get<Receiver[]>(`${this.baseUrl}/receivers`);
  }

  getRecommendations(zipCode: string, receiverId: number): Observable<RecommendationResult[]> {
    const params = new HttpParams()
      .set('zipCode', zipCode)
      .set('receiverId', receiverId.toString());

    return this.http.get<RecommendationResult[]>(`${this.baseUrl}/recommendations/top-candidates`, { params });
  }
}

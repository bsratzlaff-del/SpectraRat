import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecommendationResult } from '../models/recommendation-result.model';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {
  private apiUrl = 'http://localhost:8082/api/recommendations'; // Match your Spring port!

  constructor(private http: HttpClient) {}

  getTopCandidates(zipCode: string, receiverId: number): Observable<RecommendationResult[]> {
    const params = new HttpParams()
      .set('zipCode', zipCode)
      .set('receiverId', receiverId.toString());

    return this.http.get<RecommendationResult[]>(`${this.apiUrl}/top-candidates`, { params });
  }
}
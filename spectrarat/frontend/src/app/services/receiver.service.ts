import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Receiver } from '../models/receiver.model';

@Injectable({
  providedIn: 'root'
})
export class ReceiverService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/receivers`;

  getReceivers(): Observable<Receiver[]> {
    return this.http.get<Receiver[]>(this.apiUrl);
  }

  getReceiverById(id: string): Observable<Receiver> {
    return this.http.get<Receiver>(`${this.apiUrl}/${id}`);
  }
}
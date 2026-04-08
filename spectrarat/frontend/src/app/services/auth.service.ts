import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // 1. This 'Subject' is the internal broadcast station.
  // We initialize it by checking if a user is already in the 'filing cabinet' (localStorage).
  private loggedInStatus = new BehaviorSubject<boolean>(this.hasToken());

  // 2. This is the 'Public Radio Station' (The Observable).
  // The AppComponent 'listens' to this.
  isLoggedIn$ = this.loggedInStatus.asObservable();

  constructor() {}

  // 3. This is the 'Volume Knob' (The Method).
  // This updates the status and tells everyone listening the new value.
  setLoginStatus(status: boolean) {
    this.loggedInStatus.next(status);
  }

  // Helper to check if the user is currently logged in on page load
  private hasToken(): boolean {
    return !!localStorage.getItem('currentUser');
  }
}
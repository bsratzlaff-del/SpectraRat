import { Routes } from '@angular/router';
import { RecommendationComponent } from './recommendation/recommendation.component';
import { BusinessDashboardComponent } from './business-dashboard/business-dashboard.component';
// Assuming you named your registration component 'RegisterComponent'
// import { RegisterComponent } from './register/register.component'; 

export const routes: Routes = [
  // 1. The Main Analysis Tool
  { path: 'analyze', component: RecommendationComponent },

  // 2. The Business Dashboard (Profile + History)
  { path: 'dashboard', component: BusinessDashboardComponent },

  // 3. The Registration Page
  // { path: 'register', component: RegisterComponent },

  // 4. The Default "Landing Page" 
  // If the user goes to just 'localhost:4200', send them to the analyzer
  { path: '', redirectTo: '/analyze', pathMatch: 'full' },

  // 5. The "Catch-all" (Optional)
  // If they type a random URL, send them back to the analyzer
  { path: '**', redirectTo: '/analyze' }
];
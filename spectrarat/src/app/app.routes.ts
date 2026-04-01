import { Routes } from '@angular/router';
import { RecommendationComponent } from './recommendation/recommendation.component';
import { BusinessDashboardComponent } from './business-dashboard/business-dashboard.component';
import { InventoryDetailComponent } from './inventory-detail/inventory-detail.component';
// Assuming you named your registration component 'RegisterComponent'
// import { RegisterComponent } from './register/register.component'; 

export const routes: Routes = [
  // Route for viewing the details of a specific inventory item
  { path: 'inventory/:id', component: InventoryDetailComponent },

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
import { Routes } from '@angular/router';
import { RecommendationComponent } from './recommendation/recommendation.component';
import { BusinessDashboardComponent } from './business-dashboard/business-dashboard.component';
import { InventoryDetailComponent } from './inventory-detail/inventory-detail.component';
import { CatalogComponent } from './catalog/catalog';
// Assuming you named your registration component 'RegisterComponent'
// import { RegisterComponent } from './register/register.component'; 

export const routes: Routes = [
  { path: '', redirectTo: '/shop', pathMatch: 'full' },
  { path: 'shop', component: CatalogComponent }, // This is the new page
  { path: 'inventory/:id', component: InventoryDetailComponent }, // The config page
  { path: 'dashboard', component: BusinessDashboardComponent }
];
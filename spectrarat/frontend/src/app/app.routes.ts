import { Routes } from '@angular/router';
import { RecommendationComponent } from './recommendation/recommendation.component';
import { BusinessDashboardComponent } from './business-dashboard/business-dashboard.component';
import { InventoryDetailComponent } from './inventory-detail/inventory-detail.component';
import { CatalogComponent } from './catalog/catalog';
import { CartComponent } from './cart/cart';
import { LoginComponent } from './login/login';

export const routes: Routes = [
  { path: '', redirectTo: '/shop', pathMatch: 'full' },
  { path: 'shop', component: CatalogComponent }, // This is the new page
  { path: 'cart', component: CartComponent }, // The new Shopping Cart page
  { path: 'inventory/:id', component: InventoryDetailComponent }, // The config page
  { path: 'dashboard', component: BusinessDashboardComponent },
  { path: 'analyze', component: RecommendationComponent }, // The original tool
  { path: 'login', component: LoginComponent }, // The login page
  { path: 'dashboard', component: BusinessDashboardComponent }
];
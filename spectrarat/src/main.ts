import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './main/app/app.config';
import { AppComponent } from './main/app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

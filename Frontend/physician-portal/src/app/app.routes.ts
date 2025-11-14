import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { SpecimenPickupWrapperComponent } from './specimen-pickup-wrapper/specimen-pickup-wrapper.component';
import { NewSpecimenPickupComponent } from './new-specimen-pickup/new-specimen-pickup.component';
import { ConfirmationComponent } from './confirmation/confirmation.component';

import { PatientsComponent } from './patients/patients.component';
import { RequestsComponent } from './requests/requests.component';
import { OrdersComponent } from './orders/orders.component';
import { SuppliesComponent } from './supplies/supplies.component';
import { BillingTrailersComponent } from './billing-trailers/billing-trailers.component';
import { TestDirectoryComponent } from './test-directory/test-directory.component';
import { HelpCenterComponent } from './help-center/help-center.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'specimen-pickup', component: SpecimenPickupWrapperComponent },
  { path: 'new-specimen-pickup', component: NewSpecimenPickupComponent },
  { path: 'confirmation', component: ConfirmationComponent },
  { path: 'patients', component: PatientsComponent },
  { path: 'requests', component: RequestsComponent },
  { path: 'orders', component: OrdersComponent },
  { path: 'supplies', component: SuppliesComponent },
  { path: 'billing-trailers', component: BillingTrailersComponent },
  { path: 'test-directory', component: TestDirectoryComponent },
  { path: 'help-center', component: HelpCenterComponent },
];

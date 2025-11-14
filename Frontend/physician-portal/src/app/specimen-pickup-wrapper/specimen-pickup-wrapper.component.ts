import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { SpecimenPickupComponent } from '../specimen-pickup/specimen-pickup.component';

@Component({
  selector: 'app-specimen-pickup-wrapper',
  standalone: true,
  imports: [HeaderComponent, SidebarComponent, SpecimenPickupComponent],
  templateUrl: './specimen-pickup-wrapper.component.html',
  styleUrls: ['./specimen-pickup-wrapper.component.css']
})
export class SpecimenPickupWrapperComponent {}

import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.css']
})
export class ConfirmationComponent {
  @Input() confirmationNumber: any = '';
  @Input() scheduledDate: string = '';
  @Input() pickupAddress: string = '';
  @Input() temperatureInfo: string = '';
  @Input() pickupType: string = '';
  @Input() eta: string = '';

  constructor(private router: Router) {
    const nav = this.router.getCurrentNavigation();
    if (nav && nav.extras && nav.extras.state) {
      this.confirmationNumber = nav.extras.state['confirmationNumber'] ?? '';
      // Format scheduledDate as YYYY-MM-DD
      const rawDate = nav.extras.state['scheduledDate'] ?? '';
      this.scheduledDate = rawDate ? rawDate.split('T')[0] : '';
      this.pickupAddress = nav.extras.state['pickupAddress'] ?? '';
      this.temperatureInfo = nav.extras.state['temperatureInfo'] ?? '';
      this.pickupType = nav.extras.state['pickupType'] ?? '';
      this.eta = nav.extras.state['eta'] ?? '';
    }
  }
}

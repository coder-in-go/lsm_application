import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-billing-trailers',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="coming-soon-container">
      <h2>Still working on it</h2>
      <p>Will be coming soon.</p>
    </div>
  `,
  styleUrls: ['./billing-trailers.component.css'],
})
export class BillingTrailersComponent {}

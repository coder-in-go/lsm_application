import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-supplies',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="coming-soon-container">
      <h2>Still working on it</h2>
      <p>Will be coming soon.</p>
    </div>
  `,
  styleUrls: ['./supplies.component.css'],
})
export class SuppliesComponent {}

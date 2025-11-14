import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-help-center',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="coming-soon-container">
      <h2>Still working on it</h2>
      <p>Will be coming soon.</p>
    </div>
  `,
  styleUrls: ['./help-center.component.css'],
})
export class HelpCenterComponent {}

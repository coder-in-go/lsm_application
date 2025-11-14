

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  loginFailed: boolean = false;

  constructor(private router: Router) {}

  onSubmit() {
    // Accept only username 'madhukar' and password 'madhukar'
    if (this.username === 'madhukar' && this.password === 'madhukar') {
      this.loginFailed = false;
      this.router.navigate(['/home']);
    } else {
      this.loginFailed = true;
    }
  }
}

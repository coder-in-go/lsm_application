import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client/dist/sockjs';
import { Router } from '@angular/router';
interface NotificationDetail {
  id: string;
  pickupAddress: string;
  eta: string;
}

interface Notification {
  message: string;
  details: NotificationDetail[];
  timestamp: number;
}

@Component({
  selector: 'app-specimen-pickup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './specimen-pickup.component.html',
  styleUrls: ['./specimen-pickup.component.css'],
})
export class SpecimenPickupComponent {
  stompClient: Client | null = null;
  notifications: Notification[] = [];

  constructor(private router: Router) {}

  startNewRequest() {
    this.router.navigate(['/new-specimen-pickup']);
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    const wsUrl = 'http://localhost:8080/ws';
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(wsUrl),
      reconnectDelay: 5000,
    });
    this.stompClient.onConnect = () => {
      this.stompClient?.subscribe(
        '/topic/vehicle-assigned',
        (message: Message) => {
          try {
            const result = JSON.parse(message.body);
            if (result.assignedSpecimenRequestList && Array.isArray(result.assignedSpecimenRequestList)) {
              result.assignedSpecimenRequestList.forEach((spec: any) => {
                const notification = {
                  message: 'Vehicle assigned successfully!',
                  details: [{
                    id: spec.id,
                    pickupAddress: spec.pickup_address || spec.pickupAddress,
                    eta: spec.estimated_time_of_arrival || spec.eta || '',
                  }],
                  timestamp: Date.now() + Math.random(), // ensure uniqueness
                };
                this.notifications.unshift(notification);
                setTimeout(() => {
                  this.notifications = this.notifications.filter(n => n.timestamp !== notification.timestamp);
                }, 20000);
              });
            }
          } catch (e) {
            // Optionally handle error notification
          }
        }
      );
    };
    this.stompClient.activate();
  }
}

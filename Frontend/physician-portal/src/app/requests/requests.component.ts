import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { DateTimeFormatPipe } from "../shared/date-time-format.pipe";

@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [CommonModule, NavbarComponent, DateTimeFormatPipe],
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.css'],
})
export class RequestsComponent {
  requests: any[] = [];
  title: string = 'Requests Raised';

  constructor() {
    this.fetchRequests();
  }

  fetchRequests() {
    fetch(
      'http://localhost:8080/api/v1/specimen-pickup-request/specimen-pickup-requests'
    )
      .then((res) => res.json())
      .then((data) => {
        if (data && Array.isArray(data.data)) {
          this.requests = data.data.map((item: any) => ({
            id: item.id ?? '',
            scheduledDate: item.scheduled_date ?? '',
            pickupAddress: item.pickup_address ?? '',
            temperatureInfo: item.temperature_info ?? '',
            status: item.status ?? '',
            pickupType: item.pickup_type,
            closureTime: item.closure_time ?? '',
            pickupRequestTimeBefore: item.pickup_request_time_before ?? '',
            pickupRequestTimeAfter: item.pickup_request_time_after ?? '',
            estimatedTimeOfArrival: item.estimated_time_of_arrival ?? '',
          }));
        } else {
          this.requests = [];
        }
      })
      .catch(() => {
        this.requests = [];
      });
  }
}

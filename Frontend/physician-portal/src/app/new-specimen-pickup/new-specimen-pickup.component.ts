import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-new-specimen-pickup',
  standalone: true,
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './new-specimen-pickup.component.html',
  styleUrls: ['./new-specimen-pickup.component.css'],
})
export class NewSpecimenPickupComponent implements OnInit {
  addresses: Array<{ id: string; address: string; landmark: string }> = [];
  // For radio button selection
  pickupLocation: string = '';
  temperature: string = '';
  selectedDate: Date = new Date();
  calendarOpen = false;
  minDate: string = new Date().toISOString().substring(0, 10);
  inOfficeChecked: boolean = false;
  officeHours: string[] = [];
  addressSelected: boolean = false;
  pickupTimeFrames: string[] = [
    '8:00 AM - 10:00 AM',
    '10:00 AM - 12:00 PM',
    '12:00 PM - 2:00 PM',
    '2:00 PM - 4:00 PM',
    '4:00 PM - 6:00 PM',
  ];
  pickupTimeType: string = '';
  selectedPickupTime: string = '';
  selectedPickupTimeAfter: string = '';
  beforeIntervals: string[] = [
    '8:00 AM',
    '8:30 AM',
    '9:00 AM',
    '9:30 AM',
    '10:00 AM',
    '10:30 AM',
    '11:00 AM',
    '11:30 AM',
    '12:00 PM',
  ];
  afterIntervals: string[] = [
    '12:30 PM',
    '1:00 PM',
    '1:30 PM',
    '2:00 PM',
    '2:30 PM',
    '3:00 PM',
    '3:30 PM',
    '4:00 PM',
    '4:30 PM',
    '5:00 PM',
    '5:30 PM',
    '6:00 PM',
  ];
  pickupTimeIntervals: string[] = [
    '2:00 pm',
    '2:30 pm',
    '3:00 pm',
    '3:30 pm',
    '4:00 pm',
    '4:30 pm',
    '5:00 pm',
    '5:30 pm',
  ];
  pickupTimeAfterIntervals: string[] = [
    '12:30 pm',
    '1:00 pm',
    '1:30 pm',
    '2:00 pm',
    '2:30 pm',
    '3:00 pm',
    '3:30 pm',
    '4:00 pm',
  ];
  pickupTimeframeDropdownOpen: boolean = false;
  selectedClosureTime: string = '';
  showClosureTimeError: boolean = false;
  specimenDetailsChecked: boolean = false;
  pickupLocationChecked: boolean = false;
  title: string = 'New Specimen Pickup';

  constructor(private router: Router) {
    this.fetchAddresses();
  }

  ngOnInit(): void {
    this.officeHours = this.generateOfficeHours();
  }

  fetchAddresses() {
    fetch('http://localhost:8080/api/v1/workstation/workstations')
      .then((res) => res.json())
      .then((data) => {
        if (data && Array.isArray(data.data)) {
          this.addresses = data.data.map((item: any) => ({
            id: item.id,
            address: item.address,
            landmark: item.landmark,
          }));
        }
      })
      .catch(() => {
        this.addresses = [];
      });
  }

  // Programmatically open the closure select when the caret icon is clicked
  openClosureDropdown(selectEl: HTMLSelectElement, event?: Event) {
    if (event) event.stopPropagation();
    try {
      // focus then click to open native select dropdown in most browsers
      selectEl.focus();
      setTimeout(() => {
        try {
          (selectEl as any).click();
        } catch {}
      }, 0);
    } catch (e) {
      // ignore failures gracefully
    }
  }

  // Custom panel control
  showClosurePanel: boolean = false;

  toggleClosurePanel(event?: Event) {
    if (event) event.stopPropagation();
    this.showClosurePanel = !this.showClosurePanel;
  }

  selectClosureTime(time: string) {
    this.selectedClosureTime = time;
    this.showClosurePanel = false;
    this.showClosureTimeError = false;
  }

  // Helper to check if a closure time is before current time
  isClosureTimeDisabled(time: string): boolean {
    // If selected date is not today, allow all closure times
    const today = new Date();
    const selected = new Date(this.selectedDate);
    if (
      selected.getFullYear() !== today.getFullYear() ||
      selected.getMonth() !== today.getMonth() ||
      selected.getDate() !== today.getDate()
    ) {
      return false;
    }
    // Otherwise, use current time logic
    const now = new Date();
    let roundedHour = now.getHours();
    let roundedMinute = now.getMinutes() < 30 ? 30 : 0;
    if (now.getMinutes() >= 30) roundedHour++;
    let [hour, minute] = [0, 0];
    let t = time.trim().toLowerCase();
    let ampm = t.includes('am') ? 'AM' : t.includes('pm') ? 'PM' : '';
    t = t.replace(/am|pm/, '').trim();
    let parts = t.split(':');
    if (parts.length === 2) {
      hour = parseInt(parts[0], 10);
      minute = parseInt(parts[1], 10);
      if (ampm === 'PM' && hour < 12) hour += 12;
      if (ampm === 'AM' && hour === 12) hour = 0;
    } else {
      return false;
    }
    if (
      hour < roundedHour ||
      (hour === roundedHour && minute <= roundedMinute)
    ) {
      return true;
    }
    return false;
  }

  @HostListener('document:click')
  closeClosurePanelOnOutsideClick() {
    // Close the closure panel when clicking anywhere outside. toggleClosurePanel
    // stops propagation on clicks that should keep it open.
    this.showClosurePanel = false;
  }
  async onSubmit() {
    // Collect selected values and build DTO
    let pickupAddress = '';
    const addressDropdown = document.querySelector(
      '.address-dropdown'
    ) as HTMLSelectElement;
    if (addressDropdown && addressDropdown.selectedIndex > 0) {
      const selectedId = addressDropdown.value;
      const selectedObj = this.addresses.find((a) => a.id === selectedId);
      pickupAddress = selectedObj ? selectedObj.address : '';
    }

    // Build DTO matching backend
    // Helper to safely build local ISO string from date and time
    function buildLocalIso(date: Date, time: string): string | null {
      if (!date || !time || typeof time !== 'string' || !time.trim())
        return null;
      let [hour, minute] = [0, 0];
      let t = time.trim().toLowerCase();
      let ampm = t.includes('am') ? 'AM' : t.includes('pm') ? 'PM' : '';
      t = t.replace(/am|pm/, '').trim();
      let parts = t.split(':');
      if (parts.length === 2) {
        hour = parseInt(parts[0], 10);
        minute = parseInt(parts[1], 10);
        if (ampm === 'PM' && hour < 12) hour += 12;
        if (ampm === 'AM' && hour === 12) hour = 0;
      } else {
        return null;
      }
      let d = new Date(date);
      d.setHours(hour, minute, 0, 0);
      // Build local ISO string (YYYY-MM-DDTHH:mm:ss)
      const pad = (n: number) => n.toString().padStart(2, '0');
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(
        d.getDate()
      )}T${pad(hour)}:${pad(minute)}:00`;
    }

    const payload = {
      scheduled_date: this.selectedDate
        ? this.selectedDate.toISOString()
        : null,
      pickup_address: pickupAddress,
      temperature_info: this.temperature
        ? this.temperature.toUpperCase()
        : null,
      pickup_type:
        this.pickupLocation === 'In Office'
          ? 'IN_OFFICE'
          : this.pickupLocation === 'Lockbox'
          ? 'LOCKBOX'
          : null,
      closure_time:
        this.inOfficeChecked && this.selectedClosureTime
          ? buildLocalIso(this.selectedDate, this.selectedClosureTime)
          : null,
      pickup_request_time_before:
        this.pickupTimeType === 'before' && this.selectedPickupTime
          ? buildLocalIso(this.selectedDate, this.selectedPickupTime)
          : null,
      pickup_request_time_after:
        this.pickupTimeType === 'after' && this.selectedPickupTimeAfter
          ? buildLocalIso(this.selectedDate, this.selectedPickupTimeAfter)
          : null,
      status: 'CREATED',
    };

    try {
      console.log('Submitting payload:', payload);
      const response = await fetch(
        'http://localhost:8080/api/v1/specimen-pickup-request/specimen-pickup-request',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(payload),
        }
      );
      if (!response.ok) throw new Error('API error');
      const result = await response.json();
      console.log('Server response:', result);
      // Route to confirmation page with backend id and ETA
      if (result && result.data) {
        this.router.navigate(['/confirmation'], {
          state: {
            confirmationNumber: result.data.id,
            scheduledDate: result.data.scheduled_date,
            pickupAddress: result.data.pickup_address,
            temperatureInfo: result.data.temperature_info,
            pickupType: result.data.pickup_type,
            eta: result.data.estimated_time_of_arrival,
          },
        });
      } else {
        alert('Failed to submit pickup request.');
      }
    } catch (err) {
      alert('Failed to submit pickup request.');
    }
  }

  generateOfficeHours(): string[] {
    const times: string[] = [];
    for (let hour = 0; hour < 24; hour++) {
      const suffix = hour < 12 ? 'AM' : 'PM';
      const displayHour = hour % 12 === 0 ? 12 : hour % 12;
      times.push(`${displayHour}:00 ${suffix}`);
      times.push(`${displayHour}:30 ${suffix}`);
    }
    return times;
  }
  onAddressChange(event: any) {
    this.addressSelected = !!event.target.value;
  }

  openCalendar() {
    this.calendarOpen = true;
  }

  onDateChange(event: any) {
    this.selectedDate = new Date(event.target.value);
    this.calendarOpen = false;
  }

  clearPickupTimeType() {
    this.pickupTimeType = '';
    this.selectedPickupTime = '';
    this.selectedPickupTimeAfter = '';
  }

  togglePickupTimeframeDropdown() {
    this.pickupTimeframeDropdownOpen = !this.pickupTimeframeDropdownOpen;
  }

  // Example validation method to be called on submit or blur
  validateClosureTime() {
    this.showClosureTimeError =
      this.inOfficeChecked && !this.selectedClosureTime;
  }

  onSpecimenDetailsChange(event: any) {
    this.temperature = event.target.value;
    this.specimenDetailsChecked = !!this.temperature;
  }

  onPickupLocationChange(event: any) {
    this.pickupLocation = event.target.value;
    this.pickupLocationChecked = !!this.pickupLocation;
    this.inOfficeChecked = this.pickupLocation === 'In Office';
    // If In Office was unchecked, clear any previously selected closure time and error
    if (!this.inOfficeChecked) {
      this.selectedClosureTime = '';
      this.showClosureTimeError = false;
    }
  }

  isFormValid(): boolean {
    // Mandatory: addressSelected, specimenDetailsChecked, pickupLocationChecked
    if (!this.addressSelected) return false;
    if (!this.specimenDetailsChecked) return false;
    if (!this.pickupLocationChecked) return false;
    // If user selected In Office, ensure closure time is selected
    if (this.inOfficeChecked) {
      const hasClosure = !!(
        this.selectedClosureTime && this.selectedClosureTime.trim()
      );
      this.showClosureTimeError = !hasClosure;
      return hasClosure;
    }

    // All other validations passed
    this.showClosureTimeError = false;
    return true;
  }
}

export const pickuptimeframeAfterOptions = [
  '12:30 PM',
  '1:00 PM',
  '1:30 PM',
  '2:00 PM',
  '2:30 PM',
  '3:00 PM',
  '3:30 PM',
  '4:00 PM',
];

export const pickuptimeframeBeforeOptions = [
  '2:00 PM',
  '2:30 PM',
  '3:00 PM',
  '3:30 PM',
  '4:00 PM',
  '4:30 PM',
  '5:00 PM',
  '5:30 PM',
];

export const closureTimeOptions: string[] = [];
for (let hour = 0; hour < 24; hour++) {
  for (let min = 0; min < 60; min += 30) {
    const h = hour % 12 === 0 ? 12 : hour % 12;
    const ampm = hour < 12 ? 'AM' : 'PM';
    const m = min === 0 ? '00' : '30';
    closureTimeOptions.push(`${h}:${m} ${ampm}`);
  }
}

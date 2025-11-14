import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { ChartComponent } from '../chart/chart.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  imports: [SidebarComponent, HeaderComponent, RouterOutlet, ChartComponent],
})
export class HomeComponent implements OnInit {
  requestStats: { date: string; count: number }[] = [];
  statusSummary: { [key: string]: number } = {};
  barChartData: { labels: string[]; data: number[] } | null = null;
  pieChartData: { labels: string[]; data: number[] } | null = null;

  ngOnInit(): void {
    this.fetchPhysicianRequestStats();
    this.fetchPhysicianStatusSummary();
  }

  fetchPhysicianRequestStats() {
    fetch(`http://localhost:8080/api/v1/specimen-pickup-request/request-stats`)
      .then((res) => res.json())
      .then((data) => {
        if (data && Array.isArray(data.dailyStats)) {
          this.requestStats = data.dailyStats;
          this.barChartData = {
            labels: this.requestStats.map((r) => r.date),
            data: this.requestStats.map((r) => r.count),
          };
        }
      });
  }

  fetchPhysicianStatusSummary() {
    fetch(
      `http://localhost:8080/api/v1/specimen-pickup-request/request-status-summary`
    )
      .then((res) => res.json())
      .then((data) => {
        if (data) {
          this.statusSummary = data;
          this.pieChartData = {
            labels: Object.keys(this.statusSummary),
            data: Object.values(this.statusSummary),
          };
        }
      });
  }

  // Chart rendering is now handled by ChartComponent
}

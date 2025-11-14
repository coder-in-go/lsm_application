import {
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  AfterViewInit,
  ViewChild,
  ElementRef,
} from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrl: './chart.component.css',
})
export class ChartComponent implements OnChanges, AfterViewInit {
  @Input() barChartData: { labels: string[]; data: number[] } | null = null;
  @Input() pieChartData: { labels: string[]; data: number[] } | null = null;

  @ViewChild('barCanvas') barCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('pieCanvas') pieCanvas!: ElementRef<HTMLCanvasElement>;

  barChart: Chart | null = null;
  pieChart: Chart | null = null;

  ngAfterViewInit() {
    this.renderCharts();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['barChartData'] || changes['pieChartData']) {
      this.renderCharts();
    }
  }

  renderCharts() {
    // Bar Chart
    if (this.barCanvas && this.barChartData) {
      if (this.barChart) this.barChart.destroy();
      // 7 theme-adapted colors for days of the week
      const barThemeColors = [
        '#5c009aff', // Updated theme color
        '#28a745', // Green
        '#ffc107', // Yellow
        '#6c757d', // Gray
        '#150118ff', // Dark blue
        '#b3c6e0', // Light blue/gray accent
        '#004a80', // Accent blue
      ];
      // Helper to lighten/darken a hex color
      function adjustColor(hex: string, amt: number) {
        let usePound = false;
        if (hex[0] === '#') {
          hex = hex.slice(1);
          usePound = true;
        }
        let num = parseInt(hex, 16);
        let r = (num >> 16) + amt;
        let g = ((num >> 8) & 0x00ff) + amt;
        let b = (num & 0x0000ff) + amt;
        r = Math.max(Math.min(255, r), 0);
        g = Math.max(Math.min(255, g), 0);
        b = Math.max(Math.min(255, b), 0);
        return (
          (usePound ? '#' : '') +
          r.toString(16).padStart(2, '0') +
          g.toString(16).padStart(2, '0') +
          b.toString(16).padStart(2, '0')
        );
      }
      this.barChart = new Chart(this.barCanvas.nativeElement, {
        type: 'bar',
        data: {
          labels: this.barChartData.labels,
          datasets: [
            {
              label: 'Requests Raised',
              data: this.barChartData.data,
              backgroundColor: this.barChartData.labels.map(
                (_, i) => barThemeColors[i % barThemeColors.length]
              ),
              borderColor: this.barChartData.labels.map(
                (_, i) => barThemeColors[i % barThemeColors.length]
              ),
              borderWidth: 2,
              hoverBackgroundColor: this.barChartData.labels.map((_, i) => {
                const base = barThemeColors[i % barThemeColors.length];
                // Make green lighter, blue lighter, yellow lighter, etc.
                if (base === '#28a745') return adjustColor(base, 40); // lighter green
                if (base === '#5c009aff') return '#b085f7'; // light violet (not pink)
                if (base === '#01266c' || base === '#004a80')
                  return adjustColor(base, -40); // darker blue
                if (base === '#ffc107') return adjustColor(base, 30); // lighter yellow
                if (base === '#6c757d') return adjustColor(base, 30); // lighter gray
                if (base === '#b3c6e0') return adjustColor(base, -30); // darker accent
                return adjustColor(base, -20); // fallback
              }),
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: false },
            title: { display: true, text: 'Requests Raised Per Day' },
          },
          scales: {
            x: { title: { display: true, text: 'Date' } },
            y: { title: { display: true, text: 'Count' }, beginAtZero: true },
          },
        },
      });
    }
    // Pie Chart
    if (
      this.pieCanvas &&
      this.pieChartData &&
      this.pieChartData.labels &&
      this.pieChartData.data
    ) {
      if (this.pieChart) this.pieChart.destroy();
      const themeColors = [
        '#ad02c7ff', // Theme blue
        '#0284acff', // Green
        '#ffc107', // Yellow
        '#6c757d', // Gray
        '#01266c', // Dark blue
        '#b3c6e0', // Light blue/gray accent
      ];
      this.pieChart = new Chart(this.pieCanvas.nativeElement, {
        type: 'pie',
        data: {
          labels: this.pieChartData.labels,
          datasets: [
            {
              data: this.pieChartData.data,
              backgroundColor: this.pieChartData.labels.map(
                (_, i) => themeColors[i % themeColors.length]
              ),
              borderColor: '#fff',
              borderWidth: 2,
              hoverOffset: 8,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'bottom' },
            title: { display: true, text: 'Request Status Distribution' },
            tooltip: {
              callbacks: {
                label: function (context) {
                  const label = context.label || '';
                  const value = context.parsed;
                  const data = context.dataset.data;
                  const total = Array.isArray(data)
                    ? data.reduce((a, b) => a + b, 0)
                    : 0;
                  const percentage = total
                    ? ((value / total) * 100).toFixed(1)
                    : 0;
                  return `${label}: ${value} (${percentage}%)`;
                },
              },
            },
          },
        },
      });
    }
  }
}

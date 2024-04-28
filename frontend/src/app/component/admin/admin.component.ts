import {Component, OnInit} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BookService} from "../../services/book.service";
import {Book} from "../../models/book.model";
import {LoginService} from "../../services/session.service";
import {Review} from "../../models/review.model";
import {ReviewService} from "../../services/review.service";
import {AlgorithmsService} from "../../services/algorithms.service";
import {UserService} from "../../services/user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {User} from "../../models/user.model";
import {Observable} from "rxjs";
import {Chart, registerables} from "chart.js";
import {Router} from "@angular/router";

Chart.register(...registerables);


@Component({
  selector: "app-admin",
  templateUrl: "./admin.component.html",
  styleUrls: ["./admin.component.css", "../../../animations.css"],
})
export class AdminComponent implements OnInit {

  public mostReadAuthorsChart: any;

  loadingMostReadAuthorsChart = true;

  constructor(private http: HttpClient, public bookService: BookService, public loginService: LoginService, public reviewService: ReviewService, public algorithmService: AlgorithmsService, public profileService: UserService, public router: Router) {

  }

  ngOnInit(): void {

    this.loginService.checkLogged().subscribe({
      next: r => {
        if (r) {
          this.loginService.getLoggedUser().subscribe({
            next: user => {
              if (user.roles.includes("ADMIN")) {
                this.loadAuthorsChart();
                this.loadBestReadersChart();
              } else {
                this.router.navigate(['/error'], {
                  queryParams: {
                    title: "Error de acceso",
                    description: "No tienes permisos para acceder a esta página."
                  }
                });
              }
            }
          });
        } else {
          this.router.navigate(['/error'], {
            queryParams: {
              title: "Error de acceso",
              description: "No tienes permisos para acceder a esta página."
            }
          });
        }
      },
      error: r => {
        this.router.navigate(['/error'], {
          queryParams: {
            title: "Se ha producido un error",
            description: r.error
          }
        });
      }
    });
  }

  // Load chart
  loadAuthorsChart() {

    // Get the chart data

    let authorNames: string[] = []; // list for the genre names
    let authorCounts: number[] = []; // list for the genre counts
    // Build the chart for the most read genres (using getMostReadGenresGeneralCount())
    this.algorithmService.getMostReadAuthorsGeneralCount().subscribe({
      next: authors => {
        authors.forEach(g => {
          authorNames.push(g[0]);
          authorCounts.push(g[1]);

        });

        // Create the chart

        new Chart("mostReadAuthorsDoughnutChart", {
          type: "doughnut",
          data: {
            labels: authorNames,
            datasets: [
              {
                label: "Número de libros leídos",
                data: authorCounts,
                backgroundColor: [
                  "#FF6384",
                  "#36A2EB",
                  "#FFCE56",
                  "#4BC0C0",
                  "#9966FF",
                  "#FF9F40",
                  "#28d991",
                  "#3469da",
                  "#ff4d4d",
                  "#ff66b3",
                ],
                borderWidth: 1,
              },
            ],
          },
          options: {
            aspectRatio: 2,
            plugins: {
              legend: {
                position: "right",
              },
            },
          },
        });

        this.loadingMostReadAuthorsChart = false;
      },
      error: r => {
        console.error("Error getting most read genres: " + JSON.stringify(r));
      }
    });
  }

  loadBestReadersChart() {
    // Get the chart data

    let userNames: string[] = []; // list for the genre names
    let userBookCount: number[] = []; // list for the genre counts
    // Build the chart for the most read genres (using getMostReadGenresGeneralCount())
    this.algorithmService.getBestReaders().subscribe({
      next: readers => {
        readers.forEach(g => {
          userNames.push(g[0]);
          userBookCount.push(g[1]);

        });

        // Create the chart

        new Chart("mostReadUsersChart", {
          type: "bar",
          data: {
            labels: userNames,
            datasets: [
              {
                label: "Número de libros leídos",
                data: userBookCount,
                backgroundColor: "#519E8A",
                borderWidth: 1,
              },
            ],
          },
          options: {
            aspectRatio: 2,
            scales: {
              y: {
                beginAtZero: true,
                ticks: {
                  callback: function (value, index, values) {
                    return Math.round(value as number);
                  },
                },
                grid: {
                  display: true,
                },
              },
              x: {
                grid: {
                  display: false,
                },
              },
            },
          },
        });

        this.loadingMostReadAuthorsChart = false;
      },
      error: r => {
        console.error("Error getting most read genres: " + JSON.stringify(r));
      }
    });
  }
}

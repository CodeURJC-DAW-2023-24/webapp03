import {Component, OnInit} from "@angular/core";
import {BookService} from "../../services/book.service";
import {Chart, registerables} from "chart.js";
import {Router} from "@angular/router";
import {Book} from "../../models/book.model";
import {LoginService} from "../../services/session.service";


Chart.register(...registerables);

@Component({
  selector: "app-createbook",
  templateUrl: "./createbook.component.html",
  styleUrls: ["./createbook.component.css", "../../../animations.css"]
})

export class CreateBookComponent implements OnInit {
  title = "Bookmarks"
  //isAdmin = false;
  repeatTitle = false;
  repeatAuthor = false;
  repeatISBN = false;
  repeatGenre = false;
  repeatImage = false;
  differentImage = false;
  bookImage = "";

  constructor(public bookService: BookService, private router: Router, private loginService: LoginService) {
  }

  ngOnInit() {
    this.loginService.checkLogged().subscribe({
      next: r => {
        if (r) {
          this.loginService.getLoggedUser().subscribe({
            next: user => {
              if (!user.roles.includes("ADMIN")) {
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

  createBook(newTitle: string, newAuthor: string, newDescription: string, newDate: string,
             newRating: number, newSeries: string, newPageCount: string, newPublisher: string,
             newISBN: string, newGenre: string) {
    //Check required inputs
    this.repeatTitle = !newTitle
    this.repeatAuthor = !newAuthor
    this.repeatISBN = !newISBN
    this.repeatGenre = !newGenre

    //If all minimum requirements are met
    if (newTitle && newAuthor && newISBN && newGenre) {
      this.bookService.createBook({
        title: newTitle, authorString: newAuthor, description: newDescription, releaseDate: newDate,
        averageRating: newRating, series: newSeries, pageCount: +newPageCount, publisher: newPublisher,
        isbn: newISBN, genre: newGenre
      }).subscribe({
        next: r => { //After successful creation
          let inputBut = document.getElementById("inputImageFile") as HTMLInputElement; //Get image
          if (inputBut.files && inputBut.files.length > 0) { //Check if image is available to upload
            let file = inputBut.files[0]; //send file
            this.uploadImage(file, r)
          } else { //if no image was added, go to book page
            this.router.navigate(["/book/" + r.ID])
          }
        },
        error: r => {
          console.log("error creando libro");
        }
      });
    }
  }

  uploadImage(newImage: File, book: Book) {
    let fileSizeInMB = newImage.size / 1024 / 1024; //Checks image size
    if (fileSizeInMB < 5) {
      this.bookService.uploadImage(book.ID, newImage).subscribe({
        error: n => { //If error
          console.log(n);
        },
        complete: () => { //When image is finished uploading...
          this.router.navigate(["/book/" + book.ID])
        }
      })
    } else {
      console.error("Error uploading image!") //If there was an error uploading the image...
    }
  }

  updateImage() {
    let inputBut = document.getElementById("inputImageFile") as HTMLInputElement; //Get image
    if (inputBut.files && inputBut.files.length > 0) { //Check if image is available to upload
      let file = inputBut.files[0];
      let fileSizeInMB = file.size / 1024 / 1024;
      if (fileSizeInMB < 5) {
        let fileByteArray = [];
        let reader = new FileReader();
        reader.readAsArrayBuffer(file);
        reader.onloadend = function (e) {
          console.log("aqui")
        }
        reader.onload = (e) => {
          if (reader.readyState === FileReader.DONE) {
            let arrayBuffer = reader.result as ArrayBuffer;
            let array = new Uint8Array(arrayBuffer);
            for (let i = 0; i < array.length; i++) {
              fileByteArray.push(array[i]);
            }
            this.bookImage = btoa(fileByteArray.map((v) => {
              return String.fromCharCode(v)
            }).join(""))
            this.differentImage = true
            this.repeatImage = false;
          } else { //Image unavailable
            this.repeatImage = true;
            this.differentImage = false;
            this.bookImage = ""
          }
        }
      } else { //Image too big
        this.repeatImage = true;
        this.differentImage = false;
        this.bookImage = ""
      }
    } else { //File is null
      this.repeatImage = true;
      this.differentImage = false;
      this.bookImage = ""
    }
  }
}

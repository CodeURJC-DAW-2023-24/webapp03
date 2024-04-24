import {Component} from "@angular/core";
import {BookService} from "../../services/book.service";
import {Chart, registerables} from "chart.js";
import {Router} from "@angular/router";
import {Book} from "../../models/book.model";


Chart.register(...registerables);

@Component({
  selector: "app-createbook",
  templateUrl: "./createbook.component.html",
  styleUrls: ["./createbook.component.css", "../../../animations.css"]
})

export class CreateBookComponent {
  title = "Bookmarks"
  //isAdmin = false;
  repeatTitle = false;
  repeatAuthor = false;
  repeatISBN = false;
  repeatGenre = false;
  constructor(public bookService:BookService, private router:Router) {
  }

  ngOnInit() {
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
            this.uploadImage(file,r)
          }else{ //if no image was added, go to book page
            this.router.navigate(["/book/"+r.ID])
          }
        },
        error: r => {
          console.log("error creando libro");
        }
      });
    }
  }

  uploadImage(newImage:File, book:Book){
    let fileSizeInMB = newImage.size / 1024 / 1024; //Checks image size
    if (fileSizeInMB < 5) {
      this.bookService.uploadImage(book.ID,newImage).subscribe({
        error: n =>{ //If error
          console.log(n);
        },
        complete: () => { //When image is finished uploading...
          this.router.navigate(["/book/"+book.ID])
        }
      })
    } else {
      console.error("Error uploading image!") //If there was an error uploading the image...
    }
  }
}

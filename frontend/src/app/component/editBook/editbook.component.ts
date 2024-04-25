import {Component} from "@angular/core";
import {BookService} from "../../services/book.service";
import {Chart, registerables} from "chart.js";
import {ActivatedRoute, Router} from "@angular/router";
import {Book} from "../../models/book.model";


Chart.register(...registerables);

@Component({
  selector: "app-createbook",
  templateUrl: "./editbook.component.html",
  styleUrls: ["./editbook.component.css", "../../../animations.css"]
})

export class EditBookComponent {
  title = "Bookmarks"
  bookData = {
    title: "",
    author: "",
    isbn: "",
    pageCount: 0,
    genre: "",
    realeaseDate: "",
    publisher: "",
    saga: "",
    desc: "",
    currentRating: 0
  }
  bookImage = ""
  bookID: number = 0
  repeatTitle = false;
  repeatAuthor = false;
  repeatISBN = false;
  repeatGenre = false;
  repeatImage = false;
  differentImage = false;
  constructor(public bookService:BookService, private router:Router, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.bookID = Number(this.route.snapshot.paramMap.get('id')); // Obtiene el ID del libro desde la URL
    this.getInfoBook(); //Rellenar la info del libro
  }

  editBook(newTitle: string, newAuthor: string, newDescription: string, newDate: string,
             newRating: number, newSeries: string, newPageCount: string, newPublisher: string,
             newISBN: string, newGenre: string) {

    //Check required inputs
    this.repeatTitle = !newTitle
    this.repeatAuthor = !newAuthor
    this.repeatISBN = !newISBN
    this.repeatGenre = !newGenre

    //If all minimum requirements are met
    if (newTitle && newAuthor && newISBN && newGenre) {
      this.bookService.editBook({title: newTitle,authorString: newAuthor,description: newDescription,
                                releaseDate: newDate, averageRating: this.bookData.currentRating, genre: newGenre,
                                isbn: newISBN, pageCount: +newPageCount, publisher:newPublisher, series: newSeries},this.bookID).subscribe({
        next: b => {
          if (this.differentImage) { //check if image changed
            let inputBut = document.getElementById("inputImageFile") as HTMLInputElement; //Get image
            if (inputBut.files && inputBut.files.length > 0) { //Check if image is available to upload
              let file = inputBut.files[0]; //send file
              this.uploadImage(file,b)
            }else{ //if no image was added, go to book page
              this.router.navigate(["/book/"+this.bookID])
            }
          }else{
            this.router.navigate(["/book/"+this.bookID])
          }
        },
        error: r => {
          // route to error page
          this.router.navigate(['/error']);
        }
      })
    }
  }

  uploadImage(newImage:File, book:Book){
    let fileSizeInMB = newImage.size / 1024 / 1024; //Checks image size
    if (fileSizeInMB < 5) {
      this.bookService.uploadImage(this.bookID,newImage).subscribe({
        error: n =>{ //If error
          console.log(n);
        },
        complete: () => { //When image is finished uploading...
          this.router.navigate(["/book/"+this.bookID])
        }
      })
    } else {
      console.error("Error uploading image!") //If there was an error uploading the image...
    }
  }

  getInfoBook() {
    this.bookService.getBook(this.bookID).subscribe({
      next: b => {
        this.bookData.title = b.title;
        this.bookData.author = b.authorString;
        this.bookData.isbn = b.ISBN;
        this.bookData.pageCount = b.pageCount;
        // @ts-ignore
        this.bookData.genre = b.genre?.name;
        this.bookData.realeaseDate = b.releaseDate;
        this.bookData.publisher = b.publisher;
        this.bookData.saga = b.series;
        this.bookData.desc = b.description;
        this.bookData.currentRating = b.averageRating
        this.bookImage = this.bookService.downloadCover(this.bookID)
        if (!this.bookImage) { //In case image get fails
          this.bookImage = "https://fissac.com/wp-content/uploads/2020/11/placeholder.png"
        }
      },
      error: e => {
        // route to error page
        this.router.navigate(['/error']);
      }
    })
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
        reader.onloadend = (e) => {
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
            this.bookImage = this.bookService.downloadCover(this.bookID)
          }
        }
      }else{ //Image too big
        this.repeatImage = true;
        this.differentImage = false;
        this.bookImage = this.bookService.downloadCover(this.bookID)
      }
    }else{ //File is null
      this.repeatImage = true;
      this.differentImage = false;
      this.bookImage = this.bookService.downloadCover(this.bookID)
    }
  }
}

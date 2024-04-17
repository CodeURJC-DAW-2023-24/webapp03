import {Genre} from "./genre.model";

export interface Book {
  ID: number; // ? means optional
  genre?: Genre;
  title: string;
  authorString: string;
  description: string;
  releaseDate: string;
  ISBN: string;
  averageRating: number;
  series: string;
  pageCount: number;
  publisher: string;
}

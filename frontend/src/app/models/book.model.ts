export interface Book {
  ID?: number; // ? means optional
  genre?: object;
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

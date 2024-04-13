export interface Review {
  ID?: number; // ? means optional
  title: string;
  authorName?: string;
  rating: number;
  content: string;
}

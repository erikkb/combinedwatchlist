export interface Movie {
    id: number;
    title: string;
    poster_path: string | null;
    release_date: string;
  }
  
  export interface Show {
    id: number;
    name: string;
    poster_path: string | null;
    first_air_date: string;
  }

  export interface User {
    userId: number;
    username: string;
    role: string;
    email?: string;
  }
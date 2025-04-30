import { useEffect, useState } from 'react';

interface Movie {
  id: number;
  original_title: string;
}

export default function MovieSearch() {
  const [movies, setMovies] = useState<Movie[]>([]);

  useEffect(() => {
    const token = document.cookie
      .split('; ')
      .find((row) => row.startsWith('XSRF-TOKEN='))
      ?.split('=')[1];

    fetch(`${import.meta.env.VITE_BACKEND_BASE_URL}/api/movies/search?movieName=batman`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'X-XSRF-TOKEN': token } : {})
      },
      credentials: 'include'
    })
      .then((res) => res.json())
      .then(setMovies)
      .catch((err) => console.error(err));
  }, []);

  return (
    <div>
      <h1>Search Result for "Batman"</h1>
      <ul>
        {movies.map((movie) => (
          <li key={movie.id}>{movie.original_title}</li>
        ))}
      </ul>
    </div>
  );
}

import { useState } from "react";
import { Movie, Show } from "../../types.ts";
import "./SearchResults.css";

interface SearchResultsProps {
  movies: Movie[];
  shows: Show[];
  onAddMovie: (movie: Movie) => void;
  onAddShow: (show: Show) => void;
  onRemoveMovie: (movieId: number) => void;
  onRemoveShow: (showId: number) => void;
  watchlist: { movie_ids: number[]; show_ids: number[] };
}

export default function SearchResults({
  movies,
  shows,
  onAddMovie,
  onAddShow,
  onRemoveMovie,
  onRemoveShow,
  watchlist
}: SearchResultsProps) {
  const [activeMovieId, setActiveMovieId] = useState<number | null>(null);
  const [activeShowId, setActiveShowId] = useState<number | null>(null);
  const imageBaseUrl = "https://image.tmdb.org/t/p/w500";

  if (movies.length === 0 && shows.length === 0) {
    return null; // don't render anything if no search request was sent
  }

  const handleMovieClick = (id: number) => {
    if (window.innerWidth <= 600) {
      setActiveMovieId(prev => (prev === id ? null : id));
    }
  };

  const handleShowClick = (id: number) => {
    if (window.innerWidth <= 600) {
      setActiveShowId(prev => (prev === id ? null : id));
    }
  };

  return (
    <div className="search-results">
      <div className="results-column">
        <h2>Movies</h2>
        <div className="results-grid">
          {movies.map((movie, index) => (
            <div key={movie.id} className="result-item" style={{ animationDelay: `${index * 50}ms` }}>
              <div
                className="poster-container"
                onClick={() => handleMovieClick(movie.id)}
              >
                {movie.poster_path ? (
                  <img src={`${imageBaseUrl}${movie.poster_path}`} alt={movie.title} />
                ) : (
                  <div className="poster-placeholder" aria-label={movie.title}></div>
                )}
                <div className={`overlay ${(activeMovieId === movie.id) ? "show" : ""}`}>
                  <span>{movie.title}</span>
                  <span>{movie.release_date ? ` (${new Date(movie.release_date).getFullYear()})` : ""}</span>
                  {/* <button
                    disabled={watchlist.movie_ids.includes(movie.id)}
                    onClick={(e) => {
                      e.stopPropagation();
                      if (!watchlist.movie_ids.includes(movie.id)) {
                        onAddMovie(movie);
                      }
                    }}
                    style={watchlist.movie_ids.includes(movie.id) ? { color: "var(--ctp-mocha-red)" } : {}}
                    >
                    {watchlist.movie_ids.includes(movie.id) ? "✓" : "+"}
                  </button> */}
                  {watchlist.movie_ids.includes(movie.id) ? (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onRemoveMovie(movie.id);
                      }}
                      style={{ background: "var(--ctp-mocha-red)", color: "var(--ctp-mocha-base)" }}
                    >
                      ×
                    </button>
                  ) : (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onAddMovie(movie);
                      }}
                    >
                      +
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="results-column">
        <h2>Shows</h2>
        <div className="results-grid">
          {shows.map((show, index) => (
            <div key={show.id} className="result-item" style={{ animationDelay: `${index * 50}ms` }}>
              <div
                className="poster-container"
                onClick={() => handleShowClick(show.id)}
              >
                {show.poster_path ? (
                  <img src={`${imageBaseUrl}${show.poster_path}`} alt={show.name} />
                ) : (
                  <div className="poster-placeholder" aria-label={show.name}></div>
                )}
                <div className={`overlay ${(activeShowId === show.id) ? "show" : ""}`}>
                  <span>{show.name}</span>
                  <span>{show.first_air_date ? ` (${new Date(show.first_air_date).getFullYear()})` : ""}</span>
                  {/* <button
                    disabled={watchlist.show_ids.includes(show.id)}
                    onClick={(e) => {
                      e.stopPropagation();
                      if (!watchlist.show_ids.includes(show.id)) {
                        onAddShow(show);
                      }
                    }}
                    style={watchlist.show_ids.includes(show.id) ? { color: "var(--ctp-mocha-green)" } : {}}
                    >
                    {watchlist.show_ids.includes(show.id) ? "✓" : "+"}
                  </button> */}
                  {watchlist.show_ids.includes(show.id) ? (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onRemoveShow(show.id);
                      }}
                      style={{ background: "var(--ctp-mocha-red)", color: "var(--ctp-mocha-base)" }}
                    >
                      ×
                    </button>
                  ) : (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onAddShow(show);
                      }}
                    >
                      +
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

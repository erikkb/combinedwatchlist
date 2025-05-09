import { useState } from "react";
import { Show, Movie } from "../../types";
import WatchlistItem from "../WatchlistItem/WatchlistItem";
import "./WatchlistColumns.css"

interface Props {
    movies: Movie[];
    shows: Show[];
    selectedCountry: string;
    selectedProviders: string[];
    watchlist: { movie_ids: number[]; show_ids: number[] };
    onAddMovie: (movie: Movie) => void;
    onAddShow: (show: Show) => void;
    onRemoveMovie: (movieId: number) => void;
    onRemoveShow: (showId: number) => void;
  }
  
  export default function WatchlistColumns({
    movies,
    shows,
    selectedCountry,
    selectedProviders,
    watchlist,
    onAddMovie,
    onAddShow,
    onRemoveMovie,
    onRemoveShow,
  }: Props) {
    const [activeItemId, setActiveItemId] = useState<number | null>(null);

    return (
      <div className="watchlist-columns">
        {movies.length > 0 &&(
          <div className="watchlist-column">
            <h2>Movies</h2>
            <span className="separator"></span>
            {movies.map((movie) => (
              <WatchlistItem
                key={`movie-${movie.id}`}
                movie={movie}
                selectedCountry={selectedCountry}
                selectedProviders={selectedProviders}
                watchlist={watchlist}
                onAddMovie={onAddMovie}
                onAddShow={onAddShow}
                onRemoveMovie={onRemoveMovie}
                onRemoveShow={onRemoveShow}
                activeItemId={activeItemId}
                setActiveItemId={setActiveItemId}
              />
            ))}
          </div>
        )}

        {shows.length > 0 && (
          <div className="watchlist-column">
            <h2>Shows</h2>
            <span className="separator"></span>
            {shows.map((show) => (
              <WatchlistItem
                key={`show-${show.id}`}
                show={show}
                selectedCountry={selectedCountry}
                selectedProviders={selectedProviders}
                watchlist={watchlist}
                onAddMovie={onAddMovie}
                onAddShow={onAddShow}
                onRemoveMovie={onRemoveMovie}
                onRemoveShow={onRemoveShow}
                activeItemId={activeItemId}
                setActiveItemId={setActiveItemId}
              />
            ))}
          </div>
        )}
      </div>
    );
  }
  
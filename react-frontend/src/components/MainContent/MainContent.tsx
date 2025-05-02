// src/components/MainContent/MainContent.tsx
import { useState, useEffect } from "react";
import SearchResults from "../SearchResults/SearchResults";
import { Movie, Show } from "../../types.ts";
import { getCookie } from "../../utils/cookies";
import { useUser } from "../../context/UserContext";
import "./MainContent.css";

export default function MainContent() {
  const { user } = useUser();
  const [movies, setMovies] = useState<Movie[]>([]);
  const [shows, setShows] = useState<Show[]>([]);
  const [query, setQuery] = useState("");
  const [watchlist, setWatchlist] = useState<{ movie_ids: number[]; show_ids: number[] }>({ movie_ids: [], show_ids: [] });
  const baseUrl = import.meta.env.VITE_BACKEND_BASE_URL;

  useEffect(() => {
    async function fetchWatchlist() {
      // const csrfToken = getCookie("XSRF-TOKEN");
      const isLoggedIn = !!user;
      const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
      try {
        const res = await fetch(getUrl, { credentials: "include" });
        if (res.ok) {
          const data = await res.json();
          setWatchlist({ movie_ids: data.movie_ids || [], show_ids: data.show_ids || [] });
        }
      } catch (err) {
        console.error("Failed to load watchlist", err);
      }
    }
    fetchWatchlist();
  }, [user]);  

  async function handleSearch(e: React.FormEvent) {
    e.preventDefault();
    const [movieData, showData] = await Promise.all([
      fetch(`${baseUrl}/api/movies/search?movieName=${encodeURIComponent(query)}`).then(res => res.json()),
      fetch(`${baseUrl}/api/shows/search?showName=${encodeURIComponent(query)}`).then(res => res.json())
    ]);
    setMovies(movieData);
    setShows(showData);
  }

  async function handleAddMovie(movie: Movie) {
    const csrfToken = getCookie("XSRF-TOKEN");
    try {
      // Check if movie exists in DB
      const res = await fetch(`${baseUrl}/api/movies/${movie.id}`, { credentials: "include" });
      if (res.ok) {
        console.log("Movie exists in DB, adding to watchlist...");
        await addMovieToWatchlist(movie.id, csrfToken);
      } else if (res.status === 404) {
        // Fetch providers
        const provRes = await fetch(`${baseUrl}/api/movies/search/providers?movieId=${movie.id}`, { credentials: "include" });
        if (!provRes.ok) throw new Error("Failed to fetch movie providers");

        const { first: providers, second: providerinfo_lastupdate } = await provRes.json();
        const fullMovie = { ...movie, providers, providerinfo_lastupdate };

        // Add movie to DB
        const postRes = await fetch(`${baseUrl}/api/movies`, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}),
          },
          body: JSON.stringify(fullMovie),
        });
        if (!postRes.ok) throw new Error("Failed to add movie to DB");

        console.log("Movie added to DB, now adding to watchlist...");
        await addMovieToWatchlist(movie.id, csrfToken);
      } else {
        throw new Error("Unexpected error checking movie");
      }
    } catch (err) {
      console.error(err);
      alert("Failed to add movie");
    } finally {
      setMovies(prev => prev.filter(m => m.id !== movie.id)); // remove from search
    }
  }

  async function addMovieToWatchlist(movieId: number, csrfToken: string | null) {
    const isLoggedIn = !!user;
    const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
    
    const watchlistRes = await fetch(getUrl, { credentials: "include" });
    if (!watchlistRes.ok) throw new Error("Failed to fetch watchlist");
  
    const watchlist = await watchlistRes.json();
    const putUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/${watchlist.id}`;
  
    if (watchlist.movie_ids.includes(movieId)) {
      alert("Movie already in watchlist");
      return;
    }
  
    const updated = { ...watchlist, movie_ids: [...watchlist.movie_ids, movieId] };
    const putRes = await fetch(putUrl, {
      method: "PUT",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}),
      },
      body: JSON.stringify(updated),
    });


    if (!putRes.ok) {throw new Error("Failed to update watchlist");} else {
      setWatchlist(prev => ({
        ...prev,
        movie_ids: [...prev.movie_ids, movieId]
      }));
      console.log("Watchlist updated with movie");      
    }
  }
  
  async function handleAddShow(show: Show) {
    const csrfToken = getCookie("XSRF-TOKEN");
    try {
      const res = await fetch(`${baseUrl}/api/shows/${show.id}`, { credentials: "include" });
      if (res.ok) {
        console.log("Show exists in DB, adding to watchlist...");
        await addShowToWatchlist(show.id, csrfToken);
      } else if (res.status === 404) {
        const provRes = await fetch(`${baseUrl}/api/shows/search/providers?showId=${show.id}`, { credentials: "include" });
        if (!provRes.ok) throw new Error("Failed to fetch show providers");

        const { first: providers, second: providerinfo_lastupdate } = await provRes.json();
        const fullShow = { ...show, providers, providerinfo_lastupdate };

        const postRes = await fetch(`${baseUrl}/api/shows`, {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}),
          },
          body: JSON.stringify(fullShow),
        });
        if (!postRes.ok) throw new Error("Failed to add show to DB");

        console.log("Show added to DB, now adding to watchlist...");
        await addShowToWatchlist(show.id, csrfToken);
      } else {
        throw new Error("Unexpected error checking show");
      }
    } catch (err) {
      console.error(err);
      alert("Failed to add show");
    } finally {
      setShows(prev => prev.filter(s => s.id !== show.id)); // remove from search
    }
  }

  async function addShowToWatchlist(showId: number, csrfToken: string | null) {
    const isLoggedIn = !!user;
    const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
    
    const watchlistRes = await fetch(getUrl, { credentials: "include" });
    if (!watchlistRes.ok) throw new Error("Failed to fetch watchlist");
  
    const watchlist = await watchlistRes.json();
    const putUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/${watchlist.id}`;
  
    if (watchlist.show_ids.includes(showId)) {
      alert("Movie already in watchlist");
      return;
    }
  
    const updated = { ...watchlist, show_ids: [...watchlist.show_ids, showId] };
    const putRes = await fetch(putUrl, {
      method: "PUT",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}),
      },
      body: JSON.stringify(updated),
    });

    if (!putRes.ok) {throw new Error("Failed to update watchlist");} else {
      setWatchlist(prev => ({
        ...prev,
        show_ids: [...prev.show_ids, showId]
      }));
      console.log("Watchlist updated with show");      
    }
  }
  

  return (
    <main className="main-content">
      <div className="description-wrapper">
        <p className="description">
          A tool to search for TV shows and movies, manage a personal watchlist, and identify which streaming platforms provide full access to selected content.
        </p>
        <p className="description">
        Only services that offer complete access to a title for subscribers will be shown. Titles with limited availability (e.g., partial seasons) or those available only for rental or purchase are excluded.
        </p>
      </div>
      <form className="search-form" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search for Movies or Shows"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <button type="submit">Search</button>
      </form>

      <SearchResults
        movies={movies}
        shows={shows}
        onAddMovie={handleAddMovie}
        onAddShow={handleAddShow}
        watchlist={watchlist}
      />
    </main>
  );
}

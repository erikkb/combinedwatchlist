import { useState, useEffect } from "react";
import { useUser } from "../../context/UserContext";
import CountrySelect from "../CountrySelect/CountrySelect.tsx";
import ProviderSelect from "../ProviderSelect/ProviderSelect.tsx";
import WatchlistColumns from "../WatchlistColumns/WatchlistColumns.tsx";
import { Movie, Show } from "../../types.ts";
import { getCookie } from "../../utils/cookies";
import "./WatchlistPage.css";

export default function WatchlistPage() {
  const { user } = useUser();
  const [movies, setMovies] = useState<Movie[]>([]);
  const [shows, setShows] = useState<Show[]>([]);
  const [selectedCountry, setSelectedCountry] = useState(() => {
    return localStorage.getItem("selectedCountry") || "VPN";
  });
  const [selectedProviders, setSelectedProviders] = useState<string[]>([]);
  const [hasOutdatedProviders, setHasOutdatedProviders] = useState(false);
  const [watchlist, setWatchlist] = useState<{ movie_ids: number[]; show_ids: number[] }>({ movie_ids: [], show_ids: [] });

  const baseUrl = import.meta.env.VITE_BACKEND_BASE_URL;

  useEffect(() => {
    async function fetchWatchlist() {
      const isLoggedIn = !!user;
      const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;

      const res = await fetch(getUrl, { credentials: "include" });
      if (!res.ok) throw new Error("Failed to fetch watchlist");

      const data = await res.json();
      setWatchlist({ movie_ids: data.movie_ids || [], show_ids: data.show_ids || [] });

      const moviePromises = data.movie_ids.map((id: number) =>
        fetch(`${baseUrl}/api/movies/${id}`, { credentials: "include" }).then((res) => res.json())
      );
      const showPromises = data.show_ids.map((id: number) =>
        fetch(`${baseUrl}/api/shows/${id}`, { credentials: "include" }).then((res) => res.json())
      );

      const fetchedMovies = await Promise.all(moviePromises);
      const fetchedShows = await Promise.all(showPromises);
      setMovies(fetchedMovies);
      setShows(fetchedShows);

      const now = new Date();
      const outdated = [...fetchedMovies, ...fetchedShows].some((item) => {
        const lastUpdate = new Date(item.providerinfo_lastupdate);
        return now.getTime() - lastUpdate.getTime() > 24 * 60 * 60 * 1000;
      });
      setHasOutdatedProviders(outdated);
    }

    fetchWatchlist();
  }, [user]);

  async function handleAddMovie(movie: Movie) {
    const csrfToken = getCookie("XSRF-TOKEN");
    try {
      const res = await fetch(`${baseUrl}/api/movies/${movie.id}`, { credentials: "include" });
      if (res.ok) {
        await addMovieToWatchlist(movie.id, csrfToken);
      } else if (res.status === 404) {
        const provRes = await fetch(`${baseUrl}/api/movies/search/providers?movieId=${movie.id}`, { credentials: "include" });
        const { first: providers, second: providerinfo_lastupdate } = await provRes.json();
        const fullMovie = { ...movie, providers, providerinfo_lastupdate };

        const postRes = await fetch(`${baseUrl}/api/movies`, {
          method: "POST",
          credentials: "include",
          headers: { "Content-Type": "application/json", ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}) },
          body: JSON.stringify(fullMovie),
        });
        if (!postRes.ok) throw new Error("Failed to add movie to DB");

        await addMovieToWatchlist(movie.id, csrfToken);
      } else {
        throw new Error("Unexpected error");
      }
    } catch (err) {
      console.error(err);
      alert("Failed to add movie");
    }
  }

  async function addMovieToWatchlist(movieId: number, csrfToken: string | null) {
    const isLoggedIn = !!user;
    const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
    const res = await fetch(getUrl, { credentials: "include" });
    const wl = await res.json();
    const putUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/${wl.id}`;

    if (wl.movie_ids.includes(movieId)) {
      alert("Movie already in watchlist");
      return;
    }

    const updated = { ...wl, movie_ids: [...wl.movie_ids, movieId] };
    const putRes = await fetch(putUrl, {
      method: "PUT",
      credentials: "include",
      headers: { "Content-Type": "application/json", ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}) },
      body: JSON.stringify(updated),
    });

    if (!putRes.ok) throw new Error("Failed to update watchlist");

    setWatchlist((prev) => ({ ...prev, movie_ids: [...prev.movie_ids, movieId] }));
  }

  async function removeMovieFromWatchlist(movieId: number) {
    const isLoggedIn = !!user;
    const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
    const res = await fetch(getUrl, { credentials: "include" });
    const wl = await res.json();
    const putUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/${wl.id}`;

    const updated = { ...wl, movie_ids: wl.movie_ids.filter((id: number) => id !== movieId) };
    const putRes = await fetch(putUrl, {
      method: "PUT",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updated),
    });

    if (!putRes.ok) throw new Error("Failed to update watchlist");

    setWatchlist((prev) => ({ ...prev, movie_ids: prev.movie_ids.filter((id) => id !== movieId) }));
  }

  async function handleAddShow(show: Show) {
    const csrfToken = getCookie("XSRF-TOKEN");
    try {
      const res = await fetch(`${baseUrl}/api/shows/${show.id}`, { credentials: "include" });
      if (res.ok) {
        await addShowToWatchlist(show.id, csrfToken);
      } else if (res.status === 404) {
        const provRes = await fetch(`${baseUrl}/api/shows/search/providers?showId=${show.id}`, { credentials: "include" });
        const { first: providers, second: providerinfo_lastupdate } = await provRes.json();
        const fullShow = { ...show, providers, providerinfo_lastupdate };

        const postRes = await fetch(`${baseUrl}/api/shows`, {
          method: "POST",
          credentials: "include",
          headers: { "Content-Type": "application/json", ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}) },
          body: JSON.stringify(fullShow),
        });
        if (!postRes.ok) throw new Error("Failed to add show to DB");

        await addShowToWatchlist(show.id, csrfToken);
      } else {
        throw new Error("Unexpected error");
      }
    } catch (err) {
      console.error(err);
      alert("Failed to add show");
    }
  }

  async function addShowToWatchlist(showId: number, csrfToken: string | null) {
    const isLoggedIn = !!user;
    const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
    const res = await fetch(getUrl, { credentials: "include" });
    const wl = await res.json();
    const putUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/${wl.id}`;

    if (wl.show_ids.includes(showId)) {
      alert("Show already in watchlist");
      return;
    }

    const updated = { ...wl, show_ids: [...wl.show_ids, showId] };
    const putRes = await fetch(putUrl, {
      method: "PUT",
      credentials: "include",
      headers: { "Content-Type": "application/json", ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}) },
      body: JSON.stringify(updated),
    });

    if (!putRes.ok) throw new Error("Failed to update watchlist");

    setWatchlist((prev) => ({ ...prev, show_ids: [...prev.show_ids, showId] }));
  }

  async function removeShowFromWatchlist(showId: number) {
    const isLoggedIn = !!user;
    const getUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/user/${user.userId}`;
    const res = await fetch(getUrl, { credentials: "include" });
    const wl = await res.json();
    const putUrl = !isLoggedIn ? `${baseUrl}/api/watchlist` : `${baseUrl}/api/watchlist/${wl.id}`;

    const updated = { ...wl, show_ids: wl.show_ids.filter((id: number) => id !== showId) };
    const putRes = await fetch(putUrl, {
      method: "PUT",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updated),
    });

    if (!putRes.ok) throw new Error("Failed to update watchlist");

    setWatchlist((prev) => ({ ...prev, show_ids: prev.show_ids.filter((id) => id !== showId) }));
  }

  function handleCountryChange(newCountry: string) {
    setSelectedCountry(newCountry);
    localStorage.setItem("selectedCountry", newCountry);
  }

  async function handleUpdateProviders() {
    const csrfToken = getCookie("XSRF-TOKEN");
    const now = new Date();
  
    const outdatedMovies = movies.filter(movie => {
      const lastUpdate = new Date(movie.providerinfo_lastupdate!);
      return now.getTime() - lastUpdate.getTime() > 24 * 60 * 60 * 1000;
    });
  
    const outdatedShows = shows.filter(show => {
      const lastUpdate = new Date(show.providerinfo_lastupdate!);
      return now.getTime() - lastUpdate.getTime() > 24 * 60 * 60 * 1000;
    });
  
    try {
      await Promise.all(outdatedMovies.map(async (movie) => {
        const res = await fetch(`${baseUrl}/api/movies/search/providers?movieId=${movie.id}`, { credentials: "include" });
        if (!res.ok) throw new Error("Providerinfo was not outdated")
        const { first: providers, second: providerinfo_lastupdate } = await res.json();
  
        const updatedMovie = { ...movie, providers, providerinfo_lastupdate };
  
        const putRes = await fetch(`${baseUrl}/api/movies/${movie.id}`, {
          method: "PUT",
          credentials: "include",
          headers: { 
            "Content-Type": "application/json", 
            ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}) 
          },
          body: JSON.stringify(updatedMovie)
        });
  
        if (!putRes.ok) throw new Error(`Failed updating movie ${movie.id}`);
      }));
  
      await Promise.all(outdatedShows.map(async (show) => {
        const res = await fetch(`${baseUrl}/api/shows/search/providers?showId=${show.id}`, { credentials: "include" });
        if (!res.ok) throw new Error("Providerinfo was not outdated")
        const { first: providers, second: providerinfo_lastupdate } = await res.json();
  
        const updatedShow = { ...show, providers, providerinfo_lastupdate };
  
        const putRes = await fetch(`${baseUrl}/api/shows/${show.id}`, {
          method: "PUT",
          credentials: "include",
          headers: { 
            "Content-Type": "application/json", 
            ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}) 
          },
          body: JSON.stringify(updatedShow)
        });
  
        if (!putRes.ok) throw new Error(`Failed updating show ${show.id}`);
      }));
  
      window.location.reload();
    } catch (err) {
      console.error(err);
      alert("Some updates failed.");
    }
  }
  
  
  return (
    <main className="watchlist-page">
      <div className="select-row">
        <CountrySelect selectedCountry={selectedCountry} onChange={handleCountryChange} />
        <ProviderSelect selectedProviders={selectedProviders} onChange={setSelectedProviders} />
      </div>

      {hasOutdatedProviders && (
        <div className="update-notice">
          Some provider information is older than 24 hours - click to update: <button onClick={handleUpdateProviders}>Update Providers</button>
        </div>
      )}

      <WatchlistColumns
        movies={movies}
        shows={shows}
        selectedCountry={selectedCountry}
        selectedProviders={selectedProviders}
        watchlist={watchlist}
        onAddMovie={handleAddMovie}
        onAddShow={handleAddShow}
        onRemoveMovie={removeMovieFromWatchlist}
        onRemoveShow={removeShowFromWatchlist}
      />
    </main>
  );
}

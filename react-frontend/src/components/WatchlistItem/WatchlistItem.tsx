import { useEffect } from "react";
import { Show, Movie } from "../../types";
import "./WatchlistItem.css";

interface Props {
  movie?: Movie;
  show?: Show;
  selectedCountry: string;
  selectedProviders: string[];
  watchlist: { movie_ids: number[]; show_ids: number[] };
  onAddMovie: (movie: Movie) => void;
  onAddShow: (show: Show) => void;
  onRemoveMovie: (movieId: number) => void;
  onRemoveShow: (showId: number) => void;
  activeItemId: number | null;
  setActiveItemId: (id: number | null) => void;
}

export default function WatchlistItem({
  movie,
  show,
  selectedCountry,
  selectedProviders,
  watchlist,
  onAddMovie,
  onAddShow,
  onRemoveMovie,
  onRemoveShow,
  activeItemId,
  setActiveItemId
}: Props) {
  if (!movie && !show) throw new Error("Either movie or show required");

  useEffect(() => {
    if (window.innerWidth <= 600) {
      const handleOutsideClick = (e: MouseEvent) => {
        if (!(e.target as HTMLElement).closest(".poster-wrapper")) {
          setActiveItemId(null);
        }
      };
      document.addEventListener("click", handleOutsideClick);
      return () => document.removeEventListener("click", handleOutsideClick);
    }
  }, []);
  
  const item = (movie ?? show)!;

  function isMovieType(item: Movie | Show): item is Movie {
    return (item as Movie).title !== undefined;
  }

  const isInWatchlist = isMovieType(item)
    ? watchlist.movie_ids.includes(item.id)
    : watchlist.show_ids.includes(item.id);

    const providersForCountry = selectedCountry !== "VPN"
    ? item.providers?.[selectedCountry]?.flatrate ?? []
    : Object.values(item.providers ?? {}).flatMap(country => country.flatrate ?? []);
  
    const highlight =
    selectedProviders.length > 0 &&
    providersForCountry.some((p) =>
      selectedProviders.some(selected =>
        p.providerName.toLowerCase().includes(selected.toLowerCase())
      )
    );

  const isActive = activeItemId === item.id;

  const imageBaseUrl = "https://image.tmdb.org/t/p/w500";

  const providerMap: Record<string, string[]> = {};
  if (selectedCountry === "VPN") {
    for (const [country, { flatrate }] of Object.entries(item.providers ?? {})) {
      for (const provider of flatrate ?? []) {
        if (!providerMap[provider.providerName]) {
          providerMap[provider.providerName] = [];
        }
        providerMap[provider.providerName].push(country);
      }
    }
  }

  const handleClick = () => {
    if (window.innerWidth <= 600) {
      setActiveItemId(isActive ? null : item.id);
    }
  };
  
  return (
    <div
      className={`watchlist-item ${
        highlight ? "highlighted" : (selectedProviders.length > 0 ? "dimmed" : "")
      }`}
    >
      <div className="watchlist-item-left">
        <div className="poster-wrapper" onClick={handleClick}>
          {item.poster_path ? (
            <img
              src={`${imageBaseUrl}${item.poster_path}`}
              alt={isMovieType(item) ? item.title : item.name}
            />
          ) : (
            <div className="poster-placeholder" />
          )}
          <div className={`overlay ${isActive ? "show" : ""}`}>
            <span>{isMovieType(item) ? item.title : item.name}</span>
            <span>
              {isMovieType(item)
                ? item.release_date ? `(${item.release_date.slice(0, 4)})` : ""
                : item.first_air_date ? `(${item.first_air_date.slice(0, 4)})` : ""}
            </span>
            {isInWatchlist ? (
              <button
                onClick={() =>
                  isMovieType(item)
                    ? onRemoveMovie(item.id)
                    : onRemoveShow(item.id)
                }
              >
                ×
              </button>
            ) : (
              <button
                onClick={() =>
                  isMovieType(item) ? onAddMovie(item) : onAddShow(item)
                }
                style={{
                  background: "var(--ctp-mocha-blue)",
                  color: "var(--ctp-mocha-base)",
                }}
              >
                +
              </button>
            )}
          </div>
        </div>
      </div>
      <div className="watchlist-item-right">
        {selectedCountry !== "VPN" ? (
          providersForCountry.length > 0 ? (
            providersForCountry.map((p) => (
              <div key={p.providerId}>• {p.providerName}</div>
            ))
          ) : (
            <div>• No Providers</div>
          )
        ) : Object.keys(providerMap).length > 0 ? (
          Object.entries(providerMap).map(([providerName, countries]) => (
            <div key={providerName}>
              • {providerName}{" "}
              <span className="provider-country" title={countries.join(", ")}>
                ({countries.join(", ")})
              </span>
            </div>
          ))
        ) : (
          <div>• No Providers</div>
        )}
      </div>
    </div>
  );
}

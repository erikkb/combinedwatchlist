import { useState, useRef, useEffect } from "react";
import { useUser } from "../../context/UserContext";
import ProfileModal from "../ProfileModal/ProfileModal";
import LoginModal from "../LoginModal/LoginModal";
import RegisterModal from "../RegisterModal/RegisterModal";
import RequestResetModal from "../RequestResetModal/RequestResetModal";

import "./Header.css";

export default function Header({ minimal = false }: { minimal?: boolean }) {
  const { user, setUser } = useUser();
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showReset, setShowReset] = useState(false);
  const [showProfile, setShowProfile] = useState(false);
  const [showMobileMenu, setShowMobileMenu] = useState(false);

  const menuRef = useRef<HTMLDivElement>(null);

  const isAnyModalOpen = showLogin || showRegister || showReset || showProfile;

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setShowMobileMenu(false);
      }
    }
    if (isAnyModalOpen) {
      setShowMobileMenu(false);
    }
    if (showMobileMenu && !isAnyModalOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [showMobileMenu, isAnyModalOpen]);
  
  async function handleLogout() {
    const csrfToken = document.cookie.split('; ').find(row => row.startsWith('XSRF-TOKEN='))
      ?.split('=')[1];

    try {
      const res = await fetch(`/api/users/logout`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: csrfToken ? `_csrf=${encodeURIComponent(csrfToken)}` : ""
      });

      if (!res.ok) throw new Error("Logout failed");

      setUser(null);
      console.log("Logged out");
      
      const watchlistRes = await fetch(`/api/watchlist`, {
        method: "POST",
        credentials: "include",
        headers: csrfToken ? { "X-XSRF-TOKEN": csrfToken } : {}
      });
  
      if (!watchlistRes.ok) {
        console.error("Failed to initialize guest watchlist after logout");
      } else {
        console.log("Guest watchlist initialized after logout");
      }
    } catch (err) {
      console.error(err);
    }

    // window.location.reload();
  }

  return (
    <>
      <header className={`${minimal ? 'header-minimal' : 'header'}`}>
        {!minimal && (
          <>
            <button className="hamburger" onClick={() => setShowMobileMenu(!showMobileMenu)}>
              <span className="hamburger-icon">☰</span> 
            </button>
            <div className="header-left">
              <a href="/" onClick={() => setShowMobileMenu(false)}>Search</a>
              <a href="/watchlist" onClick={() => setShowMobileMenu(false)}>Watchlist</a>
            </div>
          </>
        )}

        <div className="header-center">
          <h1>combinedwatchlist</h1>
        </div>

        {!minimal && (
          <>
            <div className="header-right">
              {user ? (
                <>
                  <button
                    onClick={() => { setShowProfile(true); }}
                    className="username-button"
                    title={user.username} // tooltip for full username
                  >
                    {user.username.length > 12 ? user.username.slice(0, 11) + "…" : user.username}
                  </button>
                  <button onClick={handleLogout}>Logout</button>
                </>
              ) : (
                <>
                  <button onClick={() => { setShowLogin(true); }}>Login</button>
                  <button onClick={() => { setShowRegister(true); }}>Register</button>
                </>
              )}
            </div>
          </>
        )}

        {showProfile && <ProfileModal onClose={() => setShowProfile(false)} setUser={setUser} />}
        {showLogin && <LoginModal 
          onClose={() => setShowLogin(false)}
          onForgotPassword={() => {
            setShowLogin(false);
            setShowReset(true);
          }}/>}
        {showRegister && <RegisterModal onClose={() => setShowRegister(false)} />}
        {showReset && <RequestResetModal onClose={() => setShowReset(false)} />}
        {showMobileMenu && (
          <div className="mobile-menu" ref={menuRef}>
            <a href="/">Search</a>
            <a href="/watchlist">Watchlist</a>
            {/* <br /> */}
            <span className="burger-separator"></span>
            {/* <br /> */}
            {user ? (
              <>
                <button 
                  onClick={() => setShowProfile(true)}
                  className="username-button"
                  title={user.username}
                >
                  {user.username.length > 12 ? user.username.slice(0, 11) + "…" : user.username}
                </button>
                <button onClick={handleLogout}>Logout</button>
              </>
            ) : (
              <>
                <button onClick={() => setShowLogin(true)}>Login</button>
                <button onClick={() => setShowRegister(true)}>Register</button>
              </>
            )}
          </div>
        )}
      </header>
    </>
  );
}


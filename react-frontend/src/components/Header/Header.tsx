import { useState } from "react";
import { useUser } from "../../context/UserContext";
import ProfileModal from "../ProfileModal/ProfileModal";
import LoginModal from "../LoginModal/LoginModal";
import RegisterModal from "../RegisterModal/RegisterModal";
import RequestResetModal from "../RequestResetModal/RequestResetModal";

import "./Header.css";

export default function Header() {
  const { user, setUser } = useUser();
  const [showLogin, setShowLogin] = useState(false);
  const [showRegister, setShowRegister] = useState(false);
  const [showReset, setShowReset] = useState(false);
  const [showProfile, setShowProfile] = useState(false);

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
        headers: csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}
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
      <header className="header">
        <div className="header-left">
          <a href="/">Search</a>
          <a href="/watchlist">Watchlist</a>
        </div>

        <div className="header-center">
          <h1>combinedwatchlist</h1>
        </div>

        <div className="header-right">
          {user ? (
            <>
              <span onClick={() => setShowProfile(true)} style={{ cursor: 'pointer' }}>
                {user.username}
              </span>
              <button onClick={handleLogout}>Logout</button>
            </>
          ) : (
            <>
              <button onClick={() => setShowLogin(true)}>Login</button>
              <button onClick={() => setShowRegister(true)}>Register</button>
            </>
          )}
        </div>

        {showProfile && <ProfileModal onClose={() => setShowProfile(false)} />}
        {showLogin && <LoginModal 
          onClose={() => setShowLogin(false)}
          onForgotPassword={() => {
            setShowLogin(false);
            setShowReset(true);
          }}/>}
        {showRegister && <RegisterModal onClose={() => setShowRegister(false)} />}
        {showReset && <RequestResetModal onClose={() => setShowReset(false)} />}
      </header>
    </>
  );
}


import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import { UserContext } from "./context/UserContext";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import MainContent from "./components/MainContent/MainContent";
import ResetPasswordPage from "./components/ResetPasswordPage/ResetPasswordPage.tsx";
import { User } from "./types.ts"
import { getCookie } from "./utils/cookies.ts";
import "./App.css";

function App() {
  const [user, setUser] = useState<User | null>(null);
  const baseUrl = import.meta.env.VITE_BACKEND_BASE_URL;

  useEffect(() => {
    const csrfToken = getCookie('XSRF-TOKEN');

    // Check user login status
    fetch(`${baseUrl}/api/users/me`, {
      method: "GET",
      headers: csrfToken ? { 'X-CSRF-TOKEN': csrfToken } : {},
      credentials: 'include'
      })
      .then((res) => {
        if (!res.ok) throw new Error("Not logged in");
        return res.json();
      })
      .then((data) => {
        setUser(data);
        console.log("Logged in user:", data);
      })
      .catch(() => {
        console.log("User is a guest");
      });

    // Ensure watchlist exists
    fetch(`${baseUrl}/api/watchlist`, {
      method: "GET",
      headers: csrfToken ? { 'X-CSRF-TOKEN': csrfToken } : {},
      credentials: 'include'
    })
      .then(res => {
        if (!res.ok) throw new Error("Failed to create watchlist");
        console.log("Watchlist ensured");
      })
      .catch(err => console.error(err));
    
  }, [baseUrl]);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<><Header /><MainContent /><Footer /></>} />
          <Route path="/reset-password" element={<><Header minimal /><ResetPasswordPage /><Footer /></>} />
          {/* <Route path="/watchlist" element={<><Header /><div>TODO Watchlist Page</div><Footer /></>} /> */}
        </Routes>
      </BrowserRouter>
    </UserContext.Provider>
  );
}

export default App;

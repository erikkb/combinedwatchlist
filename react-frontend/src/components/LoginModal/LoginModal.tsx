import { useState, useRef, useEffect } from "react";
import { useUser } from "../../context/UserContext";
import { getCookie } from "../../utils/cookies";
import "./LoginModal.css";

interface LoginModalProps {
  onClose: () => void;
  onForgotPassword: () => void;
}

export default function LoginModal({ onClose, onForgotPassword }: LoginModalProps) {
  const { setUser } = useUser();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const modalRef = useRef<HTMLDivElement>(null);
  const baseUrl = import.meta.env.VITE_BACKEND_BASE_URL;

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (modalRef.current && !modalRef.current.contains(event.target as Node)) {
        onClose();
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [onClose]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError("");
    const csrfToken = getCookie('XSRF-TOKEN');

    try {
      const res = await fetch(`${baseUrl}/api/users/login`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {})
        },
        body: new URLSearchParams({ username, password }).toString()
      });

      if (!res.ok) throw new Error("Invalid credentials");

      const userRes = await fetch(`${baseUrl}/api/users/me`, {
        method: "GET",
        credentials: "include",
        headers: csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {},
      });

      if (!userRes.ok) throw new Error("Failed to fetch user info");

      const userData = await userRes.json();
      setUser(userData);
      onClose();
    } catch (err: any) {
      console.error(err);
      setError(err.message || "Login failed");
    }
  }

  return (
    <div className="dropdown">
      <div className="dropdown-content" ref={modalRef}>
        <h2>Login</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            maxLength={255}
            onChange={e => setUsername(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            maxLength={255}
            onChange={e => setPassword(e.target.value)}
            required
          />
          <button type="submit">Login</button>
          {error && <div className="error"><br />{error}</div>}
          <p className="forgot-password">
            <a href="#" onClick={(e) => { e.preventDefault(); onForgotPassword(); }}>
              Forgot Password?
            </a>
          </p>
        </form>
      </div>
    </div>
  );
}

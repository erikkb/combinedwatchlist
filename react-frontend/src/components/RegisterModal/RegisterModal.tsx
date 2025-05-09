import { useState, useRef, useEffect } from "react";
import { useUser } from "../../context/UserContext";
import { getCookie } from "../../utils/cookies";
import "./RegisterModal.css";

interface RegisterModalProps {
  onClose: () => void;
}

export default function RegisterModal({ onClose }: RegisterModalProps) {
  const { setUser } = useUser();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
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
    setSuccess("");

    const usernameRegex = /^[a-zA-Z0-9_]+$/;

    if (!username.trim()) {
      setError("Username cannot be blank.");
      return;
    }
    if (!usernameRegex.test(username)) {
      setError("Username can only contain letters, numbers, and underscores.");
      return;
    }
    if (username.length > 255) {
      setError("Username can't be longer than 255 characters.");
      return;
    }
  
    if (!password.trim()) {
      setError("Password cannot be blank.");
      return;
    }

    if (password.length > 255) {
      setError("Password can't be longer than 255 characters.");
      return;
    }
  
    if (email && email.length > 255) {
      setError("Email can't be longer than 255 characters.");
      return;
    }

    const csrfToken = getCookie("XSRF-TOKEN");

    const payload = { username, password, email: email || null };

    try {
      const res = await fetch(`${baseUrl}/api/users/register`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {})
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const json = await res.json().catch(() => null);
        throw new Error(json?.error || "Registration failed");
      }

      const userRes = await fetch(`${baseUrl}/api/users/me`, {
        method: "GET",
        credentials: "include",
        headers: csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {},
      });

      if (!userRes.ok) throw new Error("Failed to fetch user info");

      const userData = await userRes.json();
      setUser(userData);

      setSuccess("Registration successful!");
      setUsername("");
      setPassword("");
      setEmail("");
      onClose();
    } catch (err: any) {
      console.error(err);
      setError(err.message || "Registration failed");
    }
  }

  return (
    <div className="dropdown">
      <div className="dropdown-content" ref={modalRef}>
        <h2>Register</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            required
            maxLength={255}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            required
            maxLength={255}
            onChange={(e) => setPassword(e.target.value)}
          />
          <input
            type="email"
            placeholder="Email (optional)"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button type="submit">Register</button>
          {error && <div className="error"><br />{error}</div>}
          {success && <div className="success"><br />{success}</div>}
        </form>
      </div>
    </div>
  );
}

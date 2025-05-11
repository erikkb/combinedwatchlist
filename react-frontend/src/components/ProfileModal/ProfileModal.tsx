import { useState, useRef, useEffect } from "react";
import { getCookie } from "../../utils/cookies";
import { User } from "../../types";
import "./ProfileModal.css";

interface ProfileModalProps {
  onClose: () => void;
  setUser: (user: User | null) => void;
}

export default function ProfileModal({ onClose, setUser }: ProfileModalProps) {
  const [newPassword, setNewPassword] = useState("");
  const [newEmail, setNewEmail] = useState("");
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

    const payload: Record<string, string> = {};
    if (newPassword) payload.password = newPassword;
    if (newEmail) payload.email = newEmail;

    if (!newPassword && !newEmail) {
      setError("Please enter at least a new password or email.");
      return;
    }

    if (newEmail && newEmail.length > 255) {
      setError("Email can't be longer than 255 characters.");
      return;
    }

    try {
      //Fetch first to refresh CSRF token cookie
      await fetch(`${baseUrl}/api/users/me`, { credentials: "include" });

      const csrfToken = getCookie("XSRF-TOKEN");

      const res = await fetch(`${baseUrl}/api/users/me`, {
        method: "PATCH",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          ...(csrfToken ? { "X-XSRF-TOKEN": csrfToken } : {}),
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error("Failed to update profile.");

      setSuccess("Profile updated successfully!");
      setNewPassword("");
      setNewEmail("");
    } catch (err) {
      console.error(err);
      setError("Failed to update profile.");
    }
  }

  async function handleDelete() {
    if (!confirm("Are you sure you want to delete your account? This cannot be undone.")) {
      return;
    }
  
    try {
      // Refresh CSRF
      await fetch(`${baseUrl}/api/users/me`, { credentials: "include" });
  
      const csrfToken = getCookie("XSRF-TOKEN");
  
      // Delete user (also deletes watchlist)
      const deleteRes = await fetch(`${baseUrl}/api/users/me`, {
        method: "DELETE",
        credentials: "include",
        headers: csrfToken ? { "X-XSRF-TOKEN": csrfToken } : {},
      });
  
      if (!deleteRes.ok) throw new Error("Failed to delete account");
  
      console.log("Account deleted");
  
      // Logout session
      const logoutRes = await fetch(`${baseUrl}/api/users/logout`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: csrfToken ? `_csrf=${encodeURIComponent(csrfToken)}` : "",
      });
  
      if (!logoutRes.ok) throw new Error("Logout failed");
  
      console.log("Logged out");
  
      // Initialize guest watchlist
      const watchlistRes = await fetch(`${baseUrl}/api/watchlist`, {
        method: "POST",
        credentials: "include",
        headers: csrfToken ? { "X-XSRF-TOKEN": csrfToken } : {},
      });
  
      if (!watchlistRes.ok) {
        console.error("Failed to initialize guest watchlist after account deletion");
      } else {
        console.log("Guest watchlist initialized after account deletion");
      }
  
      setUser(null);
      onClose();
      alert("Your account was deleted. You are now browsing as a guest.");
    } catch (err) {
      console.error(err);
      alert("An error occurred while deleting your account.");
    }
  }
  

  return (
    <div className="dropdown">
      <div className="dropdown-content" ref={modalRef}>
        <h2>Edit Account</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="password"
            placeholder="New Password"
            value={newPassword}
            maxLength={255}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <input
            type="email"
            placeholder="New Email"
            value={newEmail}
            onChange={(e) => setNewEmail(e.target.value)}
          />
          <button type="submit">Save Changes</button>
          {error && <div className="error"><br />{error}</div>}
          {success && <div className="success"><br />{success}</div>}
        </form>
        <br />
        <br />
        <button onClick={handleDelete} className="delete-account-button">Delete Account</button>
      </div>
    </div>
  );
}

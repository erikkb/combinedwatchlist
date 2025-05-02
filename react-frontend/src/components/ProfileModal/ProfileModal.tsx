import { useState, useRef, useEffect } from "react";
import { getCookie } from "../../utils/cookies";
import "./ProfileModal.css";

interface ProfileModalProps {
  onClose: () => void;
}

export default function ProfileModal({ onClose }: ProfileModalProps) {
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

    try {
      //Fetch first to refresh CSRF token cookie
      await fetch(`${baseUrl}/api/users/me`, { credentials: "include" });

      const csrfToken = getCookie("XSRF-TOKEN");

      const res = await fetch(`${baseUrl}/api/users/me`, {
        method: "PATCH",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}),
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

  return (
    <div className="dropdown">
      <div className="dropdown-content" ref={modalRef}>
        <h2>Edit Profile</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="password"
            placeholder="New Password"
            value={newPassword}
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
      </div>
    </div>
  );
}

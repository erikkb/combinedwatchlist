import { useSearchParams } from "react-router-dom";
import { useState } from "react";
import { getCookie } from "../../utils/cookies";
import "./ResetPasswordPage.css"

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const baseUrl = import.meta.env.VITE_BACKEND_BASE_URL;

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!token) {
      setMessage("Invalid or missing token.");
      return;
    }

    const csrfToken = getCookie("XSRF-TOKEN");

    try {
      const res = await fetch(`${baseUrl}/api/users/reset-password`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}),
        },
        body: new URLSearchParams({ token, newPassword }).toString(),
      });

      if (!res.ok) throw new Error("Reset failed");

      setMessage("Password reset successful! Redirecting...");
      setTimeout(() => {
        window.location.href = "/";
      }, 2000);
    } catch (err) {
      console.error(err);
      setMessage("Failed to reset password. Link may have expired.");
    }
  }

  if (!token) {
    return <p>Invalid password reset link.</p>;
  }

  return (
    <main style={{ textAlign: "center", padding: "2rem" }}>
      <h2>Reset Password</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="password"
          placeholder="Enter new password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          required
        />
        <button type="submit">Reset</button>
      </form>
      {message && <p>{message}</p>}
    </main>
  );
}

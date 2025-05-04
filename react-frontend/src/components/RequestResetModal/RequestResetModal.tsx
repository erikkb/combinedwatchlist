import { useState, useRef, useEffect } from "react";
import { getCookie } from "../../utils/cookies";
import "./RequestResetModal.css";

interface RequestResetModalProps {
  onClose: () => void;
}

export default function RequestResetModal({ onClose }: RequestResetModalProps) {
  const [email, setEmail] = useState("");
  const [status, setStatus] = useState<"idle" | "sending" | "done">("idle");
  const [message, setMessage] = useState("");
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
    setStatus("sending");
    setMessage("");
    const csrfToken = getCookie("XSRF-TOKEN");

    setTimeout(() => {
      if (status === "sending") {
        setStatus("idle");
      }
    }, 5000);

    try {
      const res = await fetch(`${baseUrl}/api/users/request-password-reset`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
          ...(csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {})
        },
        body: JSON.stringify({ email }),
      });

      if (!res.ok) throw new Error("Failed to send reset email");

      setStatus("done");
      setMessage("If your account has an email, a reset link was sent.");
      setEmail("");
    } catch (err) {
      console.error(err);
      setStatus("idle");
      setMessage("Failed to send reset email. Please try again later.");
    }
  }

  return (
    <div className="dropdown">
      <div className="dropdown-content" ref={modalRef}>
        <h2>Reset Password</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Enter your email"
            value={email}
            required
            onChange={(e) => setEmail(e.target.value)}
          />
          <button type="submit" disabled={status === "sending"}>
            {status === "sending" ? "Sending..." : "Send Reset Link"}
          </button>
          {message && <div className="info"><br />{message}</div>}
        </form>
      </div>
    </div>
  );
}

import { FaGithub } from "react-icons/fa";
import "./Footer.css";

export default function Footer() {
  return (
    <footer className="footer">
      <p>
        <a href="https://www.themoviedb.org/" target="_blank" rel="noreferrer">All Data provided by TMDB</a>
        <span className="footer-separator"></span>
        <a href="https://github.com/catppuccin/catppuccin" target="_blank" rel="noopener noreferrer">Theme: Catppuccin Mocha</a>
        <span className="footer-separator"></span>
        <a href="/privacy-policy" target="_self" rel="noopener noreferrer">Privacy Policy</a>
        <span className="footer-separator"></span>
        <a href="https://github.com/erikkb/combinedwatchlist" target="_blank" rel="noreferrer"> 
          <FaGithub />
        </a>
      </p>
    </footer>
  );
}

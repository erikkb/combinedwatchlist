import "./PrivacyPolicyPage.css"

export default function PrivacyPolicyPage() {
    const ownerName = import.meta.env.VITE_OWNER_NAME || "missing .env.local!";
    const address1 = import.meta.env.VITE_OWNER_ADDRESS1 || "missing .env.local!";
    const address2 = import.meta.env.VITE_OWNER_ADDRESS2 || "missing .env.local!";
    const address3 = import.meta.env.VITE_OWNER_ADDRESS3 || "missing .env.local!";
    const address4 = import.meta.env.VITE_OWNER_ADDRESS4 || "missing .env.local!";
    const ownerEmail = import.meta.env.VITE_OWNER_EMAIL || "missing .env.local!";


    return (
        <main className="main">
            <div className="policy-wrapper">
                <h2>Privacy Policy</h2>
                <p>Privacy Policy for combinedwatchlist.<br />Effective date: 05.05.2025.</p>
                <h3>1. Controller</h3>
                <p>The controller responsible for data processing on this website is: <br /> <br /> {ownerName} <br /> {address1} <br /> {address2} <br /> {address3} <br /> {address4} <br /> <br /> <a href={`mailto:${ownerEmail}`}>{ownerEmail}</a></p>
                <h3>General Information</h3>
                <p>This privacy policy explains how we collect, use, and store your personal data when you use the website combinedwatchlist.</p>
                <p>We take the protection of your personal data seriously and process it in accordance with the General Data Protection Regulation (GDPR) and other applicable laws.</p>
                <h3>3. What Data We Collect</h3>
                <p>When you use this website, we process the following data:</p>
                <h4>3.1 Account Data</h4>
                <p>When you register for an account, we collect:</p>
                <ul>
                    <li>Your username (required)</li>
                    <li>Your password (stored encrypted/hashed)</li>
                    <li>Your email address (optional; used only if you provide it for password recovery)</li>
                </ul>
                <p>Email addresses are only used for sending password reset links if requested.</p>
                <h4>3.2 Watchlist Data</h4>
                <p>If you add movies or TV shows to your watchlist, the system saves:</p>
                <ul>
                    <li>The IDs of the selected movies/shows in your personal watchlist</li>
                    <li>This data is stored either in a temporary session (guest users) or in the database linked to your account (registered users)</li>
                </ul>
                <h4>3.3 Cookies</h4>
                <p>This website uses two essential cookies and one optional cookie:</p>
                <ul>
                    <li>1. <strong>SESSION</strong> – required to maintain your session (guest or logged-in); expires after 72 hours.</li>
                    <li>2. <strong>XSRF-TOKEN</strong> – required for security to prevent CSRF attacks; expires at the end of the session.</li>
                    <li>3. <strong>remember-me</strong> – optional cookie that allows you to stay logged in across browser sessions; set only if you check the “Remember Me” option during login; valid for up to 30 days or until you log out.</li>
                </ul>
                <p>The SESSION and XSRF-TOKEN cookies are strictly necessary for the technical operation and security of the site. The remember-me cookie is an optional functional cookie, stored only with your consent by selecting “Remember Me” at login. No analytics, tracking, marketing, or third-party cookies are used. Because only essential and user-requested functional cookies are used, no cookie banner is displayed.</p>
                <h4>3.4 Local Storage</h4>
                <p>We use browser-based storage mechanisms (e.g., localStorage) to store your last selected country preference for usability purposes. This data is not shared or transmitted to any third parties and is cleared manually by the user.</p>
                <h3>4. Purpose of Processing</h3>
                <p>Your personal data is processed solely for:</p>
                <ul>
                    <li>Registering and managing your user account</li>
                    <li>Allowing you to create and manage a personal watchlist</li>
                    <li>Ensuring secure website operation (via CSRF token and session handling)</li>
                    <li>Sending password reset emails if requested</li>
                </ul>
                <p>Your data is not shared with third parties, except if legally required.</p>
                <h3>5. Legal Basis</h3>
                <p>Data processing is based on:</p>
                <ul>
                    <li>Art. 6(1)(b) GDPR – processing necessary for the performance of a contract (providing your account/watchlist)</li>
                    <li>Art. 6(1)(f) GDPR – processing necessary to ensure website security and functionality</li>
                    <li>Art. 6(1)(c) GDPR – compliance with legal obligations (if applicable)</li>
                </ul>
                <h3>6. Storage Duration</h3>
                <ul>
                    <li>Account data is stored as long as you maintain your account.</li>
                    <li>Watchlist data is stored as long as your account exists or until you clear your guest session.</li>
                    <li>Essential cookies expire automatically or at the end of your session.</li>
                </ul>
                <p>You may delete your account at any time.</p>
                <h3>7. Hosting</h3>
                <p>This website is hosted by Amazon Web Services (AWS). AWS may technically process server logs (e.g., IP address, browser, time of access) for operational and security purposes. This is based on Art. 6(1)(f) GDPR. No personal data beyond what is described is intentionally collected or shared by us.</p>
                <h3>8. Your Rights</h3>
                <p>Under GDPR, you have the right to:</p>
                <ul>
                    <li>Access your stored personal data</li>
                    <li>Request correction or deletion</li>
                    <li>Restrict or object to processing</li>
                    <li>Receive a copy of your data (data portability)</li>
                    <li>Lodge a complaint with a supervisory authority</li>
                </ul>
                <p>To exercise any of these rights, contact us at <a href={`mailto:${ownerEmail}`}>{ownerEmail}</a>.</p>
                <h3>9. Changes to This Policy</h3>
                <p>We may update this privacy policy if required by law or changes in functionality. The latest version will always be available on this website.</p>
                <h3>Legal Notice (Impressum)</h3>
                <p>Required under §5 TMG (Germany):</p>
                <p>Site Owner:</p>
                <p>{ownerName} <br /> {address1} <br /> {address2} <br /> {address3} <br /> {address4} <br /> <br /> <a href={`mailto:${ownerEmail}`}>{ownerEmail}</a></p>
            </div>
        </main>
    )
}
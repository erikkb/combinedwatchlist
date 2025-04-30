# combinedwatchlist
A tool to search for TV shows and movies, manage a personal watchlist, and identify which streaming platforms provide full access to selected content - helping users choose the most suitable subscription service.

Only services that offer complete access to a title for subscribers will be shown. Titles with limited availability (e.g., partial seasons) or those available only for rental or purchase are excluded.

## Requirements
- Docker Desktop
- A configuration file located at combined-watchlist/src/main/resources/credentials.properties containing:
    ```properties
    TMDB_KEY=get here: https://www.themoviedb.org/documentation/api
    admin.username=admin
    admin.password=verysecret
    admin.email=admin@provider.com
    spring.mail.username=youremailadress@gmail.com
    spring.mail.password=yourapppassword
    ```
This file must include a valid TMDB API key, Spring Security admin credentials, and Gmail SMTP credentials (App Password required).

## Frontend
### Rudimentary (integrated) frontend
If `app.react-mode=false` is set in `combined-watchlist/src/main/resources/application.properties`, the backend will serve a basic frontend accessible at: [http://localhost:8080](http://localhost:8080).

### React frontend (development mode)
To use the standalone React frontend:

1. Set `app.react-mode=true`.
2. Navigate to the frontend directory (`/react-frontend`).
3. Install dependencies (only needed once):
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ```
The React frontend will be available at http://localhost:5173. The backend must be running in parallel at http://localhost:8080 for API access.
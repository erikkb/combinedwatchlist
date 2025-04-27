# combinedwatchlist
Search for TV shows and movies, add them to your watchlist and see which streaming services have them on offer to make an informed decision on what best to subscribe to.

Currently assumes the users location is in Germany, multi-region and users with VPN functionality will be added at a later date.


## Requirements
- Docker Desktop
- a file /resources/credentials.properties containing a valid TMDB API key, credentials for a Spring Security admin user and a @gmail account with a valid App password:
```properties
TMDB_KEY=get here: https://www.themoviedb.org/documentation/api
admin.username=admin
admin.password=verysecret
admin.email=admin@provider.com
spring.mail.username=youremailadress@gmail.com
spring.mail.password=yourapppassword
```

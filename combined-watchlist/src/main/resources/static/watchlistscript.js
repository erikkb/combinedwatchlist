// todo: refactor this file to use built-in JS (fetch) instead of jQuery

$(document).ready(function() {
    // Fetch movies
    $.ajax({
        url: '/api/movies',
        type: 'GET',
        success: function(movies) {
            const moviesList = $('#movies-list');
            movies.forEach(function(movie) {
                const movieItem = $('<li>').text(movie.original_title);
                const providersDiv = $('<div>').addClass('providers');
                movie.provider_names.forEach(function(provider) {
                    providersDiv.append($('<span>').text(provider).addClass('indent provider-name')[0]);
                });
                movieItem.append(providersDiv);

                // Add hidden inputs for movie fields
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'original_title').val(movie.original_title));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'id').val(movie.id));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'adult').val(movie.adult));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'backdrop_path').val(movie.backdrop_path));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'genre_ids').val(movie.genre_ids));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'original_language').val(movie.original_language));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'overview').val(movie.overview));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'popularity').val(movie.popularity));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'poster_path').val(movie.poster_path));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'release_date').val(movie.release_date));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'title').val(movie.title));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'video').val(movie.video));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'vote_average').val(movie.vote_average));
                movieItem.append($('<input>').attr('type', 'hidden').attr('name', 'vote_count').val(movie.vote_count));

                // Add delete button
                const deleteButton = $('<button>').text('Delete').click(function() {
                    $.ajax({
                        url: '/api/movies/' + movie.id,
                        type: 'DELETE',
                        success: function() {
                            movieItem.remove();
                        },
                        error: function() {
                            alert('Failed to delete movie');
                        }
                    });
                });
                movieItem.append(deleteButton.addClass('indent'));

                moviesList.append(movieItem);
            });
        },
        error: function(error) {
            alert('Failed to fetch movies');
        }
    });

    // Fetch shows
    $.ajax({
        url: '/api/shows',
        type: 'GET',
        success: function(shows) {
            const showsList = $('#shows-list');
            shows.forEach(function(show) {
                const showItem = $('<li>').text(show.original_name);
                const providersDiv = $('<div>').addClass('providers');
                show.provider_names.forEach(function(provider) {
                    providersDiv.append($('<span>').text(provider).addClass('indent provider-name')[0]);
                });
                showItem.append(providersDiv);

                // Add hidden inputs for show fields
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'original_name').val(show.original_name));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'id').val(show.id));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'adult').val(show.adult));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'backdrop_path').val(show.backdrop_path));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'genre_ids').val(show.genre_ids));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'origin_country').val(show.origin_country));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'original_language').val(show.original_language));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'overview').val(show.overview));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'popularity').val(show.popularity));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'poster_path').val(show.poster_path));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'first_air_date').val(show.first_air_date));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'name').val(show.name));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'vote_average').val(show.vote_average));
                showItem.append($('<input>').attr('type', 'hidden').attr('name', 'vote_count').val(show.vote_count));

                // Add delete button
                const deleteButton = $('<button>').text('Delete').click(function() {
                    $.ajax({
                        url: '/api/shows/' + show.id,
                        type: 'DELETE',
                        success: function() {
                            showItem.remove();
                        },
                        error: function() {
                            alert('Failed to delete show');
                        }
                    });
                });
                showItem.append(deleteButton.addClass('indent'));

                showsList.append(showItem);
            });
        },
        error: function(error) {
            alert('Failed to fetch shows');
        }
    });

    // Update providers button click event
    document.getElementById('update-providers-button').addEventListener('click', function() {
        console.log("update providers button clicked");
        const movieItems = document.querySelectorAll('#movies-list li');
        const showItems = document.querySelectorAll('#shows-list li');
        console.log(movieItems);
        console.log(showItems);

        movieItems.forEach(item => {
            const movieId = item.querySelector('input[name="id"]').value;

            fetch(`/api/movies/search/providers?movieId=${movieId}`)
                .then(response => response.json())
                .then(providers => {
                    console.log('Movie providers fetched:', providers);
                    const providerNames = providers.map(provider => provider.first);
                    const providerLogos = providers.map(provider => provider.second);

                    const movie = {
                        id: movieId,
                        original_title: 'it worked',
                        adult: item.querySelector('input[name="adult"]').value,
                        backdrop_path: item.querySelector('input[name="backdrop_path"]').value,
                        genre_ids: item.querySelector('input[name="genre_ids"]').value.replace(/[\[\]\s]/g, '').split(',').map(Number),
                        original_language: item.querySelector('input[name="original_language"]').value,
                        overview: item.querySelector('input[name="overview"]').value,
                        popularity: item.querySelector('input[name="popularity"]').value,
                        poster_path: item.querySelector('input[name="poster_path"]').value,
                        release_date: item.querySelector('input[name="release_date"]').value,
                        title: item.querySelector('input[name="title"]').value,
                        video: item.querySelector('input[name="video"]').value,
                        vote_average: item.querySelector('input[name="vote_average"]').value,
                        vote_count: item.querySelector('input[name="vote_count"]').value,
                        provider_names: providerNames,
                        provider_logos: providerLogos
                    };

                    console.log('Updating movie:', movie);

                    fetch(`/api/movies/${movieId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(movie)
                    }).then(response => {
                        if (!response.ok) {
                            alert('Failed to update movie');
                        } else {
                            // Update the DOM with new provider names
                            const providersDiv = item.querySelector('.providers');
                            providersDiv.innerHTML = '';
                            providerNames.forEach(provider => {
                                providersDiv.append($('<span>').text(provider).addClass('indent provider-name')[0]);
                            });
                            console.log('Movie updated successfully');
                        }
                    }).catch(error => {
                        console.error('Error updating movie:', error);
                    });
                }).catch(error => {
                console.error('Error fetching movie providers:', error);
            });
        });

        showItems.forEach(item => {
            const showId = item.querySelector('input[name="id"]').value;

            fetch(`/api/shows/search/providers?showId=${showId}`)
                .then(response => response.json())
                .then(providers => {
                    console.log('Show providers fetched:', providers);
                    const providerNames = providers.map(provider => provider.first);
                    const providerLogos = providers.map(provider => provider.second);

                    const show = {
                        id: showId,
                        adult: item.querySelector('input[name="adult"]').value,
                        backdrop_path: item.querySelector('input[name="backdrop_path"]').value,
                        genre_ids: item.querySelector('input[name="genre_ids"]').value.replace(/[\[\]\s]/g, '').split(',').filter(Boolean).map(Number),
                        origin_country: item.querySelector('input[name="origin_country"]').value.replace(/[\[\]\s]/g, '').split(',').filter(Boolean).map(String),
                        original_language: item.querySelector('input[name="original_language"]').value,
                        original_name: 'it worked for show as well',
                        overview: item.querySelector('input[name="overview"]').value,
                        popularity: item.querySelector('input[name="popularity"]').value,
                        poster_path: item.querySelector('input[name="poster_path"]').value,
                        first_air_date: item.querySelector('input[name="first_air_date"]').value,
                        name: item.querySelector('input[name="name"]').value,
                        vote_average: item.querySelector('input[name="vote_average"]').value,
                        vote_count: item.querySelector('input[name="vote_count"]').value,
                        provider_names: providerNames,
                        provider_logos: providerLogos
                    };

                    console.log('Updating show:', show);

                    fetch(`/api/shows/${showId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(show)
                    }).then(response => {
                        if (!response.ok) {
                            alert('Failed to update show');
                        } else {
                            // Update the DOM with new provider names
                            const providersDiv = item.querySelector('.providers');
                            providersDiv.innerHTML = '';
                            providerNames.forEach(provider => {
                                providersDiv.append($('<span>').text(provider).addClass('indent provider-name')[0]);
                            });
                            console.log('Show updated successfully');
                        }
                    }).catch(error => {
                        console.error('Error updating show:', error);
                    });
                }).catch(error => {
                console.error('Error fetching show providers:', error);
            });
        });
    });
});

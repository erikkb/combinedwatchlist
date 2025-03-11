$(document).ready(function() {
    // Fetch movies
    $.ajax({
        url: '/api/movies',
        type: 'GET',
        success: function(movies) {
            const moviesList = $('#movies-list');
            movies.forEach(function(movie) {
                const movieItem = $('<li>').text(movie.original_title);
                const providersList = $('<ul>');
                movie.provider_names.forEach(function(provider) {
                    providersList.append($('<li>').text(provider));
                });
                movieItem.append(providersList);

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
                movieItem.append(deleteButton);

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
                const providersList = $('<ul>');
                show.provider_names.forEach(function(provider) {
                    providersList.append($('<li>').text(provider));
                });
                showItem.append(providersList);

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
                showItem.append(deleteButton);

                showsList.append(showItem);
            });
        },
        error: function(error) {
            alert('Failed to fetch shows');
        }
    });
});
$(document).ready(function() {
    // Fetch watchlist from session
    $.ajax({
        url: '/api/watchlist',
        type: 'GET',
        success: function(watchlist) {
            const movieIds = watchlist.movie_ids;
            const showIds = watchlist.show_ids;

            // Fetch and display movies
            movieIds.forEach(function(movieId) {
                $.ajax({
                    url: `/api/movies/${movieId}`,
                    type: 'GET',
                    success: function(movie) {
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
                        const deleteButton = $('<button>').text('Remove').click(function() {
                            // Remove from DB
                            // $.ajax({
                            //     url: '/api/movies/' + movie.id,
                            //     type: 'DELETE',
                            //     success: function() {
                            //         movieItem.remove();
                            //     },
                            //     error: function() {
                            //         alert('Failed to delete movie');
                            //     }
                            // });
                            // Remove from watchlist
                            $.ajax({
                                url: '/api/watchlist',
                                type: 'GET',
                                success: function(watchlist) {
                                    const index = watchlist.movie_ids.indexOf(movie.id);
                                    if (index > -1) {
                                        watchlist.movie_ids.splice(index, 1);
                                        $.ajax({
                                            url: '/api/watchlist',
                                            type: 'PUT',
                                            contentType: 'application/json',
                                            data: JSON.stringify(watchlist),
                                            success: function() {
                                                movieItem.remove();
                                            },
                                            error: function() {
                                                alert('Failed to update watchlist');
                                            }
                                        });
                                    }
                                },
                                error: function() {
                                    alert('Failed to fetch watchlist');
                                }
                            });
                        });
                        movieItem.append(deleteButton.addClass('indent'));

                        $('#movies-list').append(movieItem);
                    },
                    error: function() {
                        alert('Failed to fetch movie');
                    }
                });
            });

            // Fetch and display shows
            showIds.forEach(function(showId) {
                $.ajax({
                    url: `/api/shows/${showId}`,
                    type: 'GET',
                    success: function(show) {
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
                        const deleteButton = $('<button>').text('Remove').click(function() {
                            // Remove from DB
                            // $.ajax({
                            //     url: '/api/shows/' + show.id,
                            //     type: 'DELETE',
                            //     success: function() {
                            //         showItem.remove();
                            //     },
                            //     error: function() {
                            //         alert('Failed to delete show');
                            //     }
                            // });
                            // Remove from watchlist
                            $.ajax({
                                url: '/api/watchlist',
                                type: 'GET',
                                success: function(watchlist) {
                                    const index = watchlist.show_ids.indexOf(show.id);
                                    if (index > -1) {
                                        watchlist.show_ids.splice(index, 1);
                                        $.ajax({
                                            url: '/api/watchlist',
                                            type: 'PUT',
                                            contentType: 'application/json',
                                            data: JSON.stringify(watchlist),
                                            success: function() {
                                                showItem.remove();
                                            },
                                            error: function() {
                                                alert('Failed to update watchlist');
                                            }
                                        });
                                    }
                                },
                                error: function() {
                                    alert('Failed to fetch watchlist');
                                }
                            });
                        });
                        showItem.append(deleteButton.addClass('indent'));

                        $('#shows-list').append(showItem);
                    },
                    error: function() {
                        alert('Failed to fetch show');
                    }
                });
            });
        },
        error: function() {
            alert('Failed to fetch watchlist');
        }
    });

// Update providers button click event
    $('#update-providers-button').on('click', function() {
        console.log("update providers button clicked");
        const movieItems = $('#movies-list li');
        const showItems = $('#shows-list li');
        console.log(movieItems);
        console.log(showItems);

        movieItems.each(function() {
            const item = $(this);
            const movieId = item.find('input[name="id"]').val();

            $.ajax({
                url: `/api/movies/search/providers`,
                type: 'GET',
                data: { movieId: movieId },
                success: function(providers) {
                    console.log('Movie providers fetched:', providers);
                    const providerNames = providers.map(provider => provider.first);
                    const providerLogos = providers.map(provider => provider.second);

                    const movie = {
                        id: movieId,
                        original_title: item.find('input[name="original_title"]').val(),
                        adult: item.find('input[name="adult"]').val(),
                        backdrop_path: item.find('input[name="backdrop_path"]').val(),
                        genre_ids: item.find('input[name="genre_ids"]').val().replace(/[\[\]\s]/g, '').split(',').map(Number),
                        original_language: item.find('input[name="original_language"]').val(),
                        overview: item.find('input[name="overview"]').val(),
                        popularity: item.find('input[name="popularity"]').val(),
                        poster_path: item.find('input[name="poster_path"]').val(),
                        release_date: item.find('input[name="release_date"]').val(),
                        title: item.find('input[name="title"]').val(),
                        video: item.find('input[name="video"]').val(),
                        vote_average: item.find('input[name="vote_average"]').val(),
                        vote_count: item.find('input[name="vote_count"]').val(),
                        provider_names: providerNames,
                        provider_logos: providerLogos
                    };

                    console.log('Updating movie:', movie);

                    $.ajax({
                        url: `/api/movies/${movieId}`,
                        type: 'PUT',
                        contentType: 'application/json',
                        data: JSON.stringify(movie),
                        success: function() {
                            const providersDiv = item.find('.providers');
                            providersDiv.empty();
                            providerNames.forEach(provider => {
                                providersDiv.append($('<span>').text(provider).addClass('indent provider-name'));
                                // providersDiv.append($('<span>').text("hellotest").addClass('indent provider-name'));
                            });
                            console.log('Movie updated successfully');
                        },
                        error: function() {
                            alert('Failed to update movie');
                        }
                    });
                },
                error: function() {
                    console.error('Error fetching movie providers');
                }
            });
        });

        showItems.each(function() {
            const item = $(this);
            const showId = item.find('input[name="id"]').val();

            $.ajax({
                url: `/api/shows/search/providers`,
                type: 'GET',
                data: { showId: showId },
                success: function(providers) {
                    console.log('Show providers fetched:', providers);
                    const providerNames = providers.map(provider => provider.first);
                    const providerLogos = providers.map(provider => provider.second);

                    const show = {
                        id: showId,
                        adult: item.find('input[name="adult"]').val(),
                        backdrop_path: item.find('input[name="backdrop_path"]').val(),
                        genre_ids: item.find('input[name="genre_ids"]').val().replace(/[\[\]\s]/g, '').split(',').filter(Boolean).map(Number),
                        origin_country: item.find('input[name="origin_country"]').val().replace(/[\[\]\s]/g, '').split(',').filter(Boolean).map(String),
                        original_language: item.find('input[name="original_language"]').val(),
                        original_name: item.find('input[name="original_name"]').val(),
                        overview: item.find('input[name="overview"]').val(),
                        popularity: item.find('input[name="popularity"]').val(),
                        poster_path: item.find('input[name="poster_path"]').val(),
                        first_air_date: item.find('input[name="first_air_date"]').val(),
                        name: item.find('input[name="name"]').val(),
                        vote_average: item.find('input[name="vote_average"]').val(),
                        vote_count: item.find('input[name="vote_count"]').val(),
                        provider_names: providerNames,
                        provider_logos: providerLogos
                    };

                    console.log('Updating show:', show);

                    $.ajax({
                        url: `/api/shows/${showId}`,
                        type: 'PUT',
                        contentType: 'application/json',
                        data: JSON.stringify(show),
                        success: function() {
                            const providersDiv = item.find('.providers');
                            providersDiv.empty();
                            providerNames.forEach(provider => {
                                providersDiv.append($('<span>').text(provider).addClass('indent provider-name'));
                            });
                            console.log('Show updated successfully');
                        },
                        error: function() {
                            alert('Failed to update show');
                        }
                    });
                },
                error: function() {
                    console.error('Error fetching show providers');
                }
            });
        });
    });
});

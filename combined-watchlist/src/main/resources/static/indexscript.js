$(document).ready(function() {
    // Create watchlist if it doesn't exist
    $.ajax({
        url: '/api/watchlist',
        type: 'POST',
        success: function() {
            console.log('Watchlist created or already exists');
        },
        error: function(error) {
            console.error('Failed to create watchlist', error);
        }
    });

    $('form.search-form').on('submit', function(event) {
        event.preventDefault();
        const form = $(this);
        const query = form.find('input[name="query"]').val();

        $.ajax({
            url: '/api/movies/search',
            type: 'GET',
            data: { movieName: query },
            success: function(response) {
                const searchResults = response.map(function(movie) {
                    return `
                        <li>
                            <form class="add-movie-form">
                                <span>${movie.original_title}</span>
                                <input type="hidden" name="id" value="${movie.id}">
                                <input type="hidden" name="original_title" value="${movie.original_title}">
                                <input type="hidden" name="adult" value="${movie.adult}">
                                <input type="hidden" name="backdrop_path" value="${movie.backdrop_path}">
                                <input type="hidden" name="genre_ids" value="${movie.genre_ids}">
                                <input type="hidden" name="original_language" value="${movie.original_language}">
                                <input type="hidden" name="overview" value="${movie.overview}">
                                <input type="hidden" name="popularity" value="${movie.popularity}">
                                <input type="hidden" name="poster_path" value="${movie.poster_path}">
                                <input type="hidden" name="release_date" value="${movie.release_date}">
                                <input type="hidden" name="title" value="${movie.title}">
                                <input type="hidden" name="video" value="${movie.video}">
                                <input type="hidden" name="vote_average" value="${movie.vote_average}">
                                <input type="hidden" name="vote_count" value="${movie.vote_count}">
                                <button type="submit">Add</button>
                            </form>
                        </li>
                    `;
                }).join('');
                $('#search-results-movies').html(searchResults);
            },
            error: function(error) {
                alert('Failed to fetch search movie results');
            }
        });

        $.ajax({
            url: '/api/shows/search',
            type: 'GET',
            data: { showName: query },
            success: function(response) {
                const searchResults = response.map(function(show) {
                    return `
                        <li>
                            <form class="add-show-form">
                                <span>${show.original_name}</span>
                                <input type="hidden" name="id" value="${show.id}">
                                <input type="hidden" name="adult" value="${show.adult}">
                                <input type="hidden" name="backdrop_path" value="${show.backdrop_path}">
                                <input type="hidden" name="genre_ids" value="${show.genre_ids}">
                                <input type="hidden" name="origin_country" value="${show.origin_country}">
                                <input type="hidden" name="original_language" value="${show.original_language}">
                                <input type="hidden" name="original_name" value="${show.original_name}">
                                <input type="hidden" name="overview" value="${show.overview}">
                                <input type="hidden" name="popularity" value="${show.popularity}">
                                <input type="hidden" name="poster_path" value="${show.poster_path}">
                                <input type="hidden" name="first_air_date" value="${show.first_air_date}">
                                <input type="hidden" name="name" value="${show.name}">
                                <input type="hidden" name="vote_average" value="${show.vote_average}">
                                <input type="hidden" name="vote_count" value="${show.vote_count}">
                                <button type="submit">Add</button>
                            </form>
                        </li>
                    `;
                }).join('');
                $('#search-results-shows').html(searchResults);
            },
            error: function(error) {
                alert('Failed to fetch search show results');
            }
        });
    });

    $(document).on('submit', 'form.add-movie-form', function(event) {
        event.preventDefault();
        const form = $(this);
        const movieId = form.find('input[name="id"]').val();

        // Check if the movie already exists in the database
        $.ajax({
            url: `/api/movies/${movieId}`,
            type: 'GET',
            success: function(response) {
                console.log('Movie already exists in the database, adding to watchlist');
                $.ajax({
                    url: '/api/watchlist',
                    type: 'GET',
                    success: function(watchlist) {
                        console.log('Watchlist:', watchlist);
                        if (!watchlist.movie_ids.includes(movieId)) {
                            watchlist.movie_ids.push(movieId);
                            $.ajax({
                                url: '/api/watchlist',
                                type: 'PUT',
                                contentType: 'application/json',
                                data: JSON.stringify(watchlist),
                                success: function() {
                                    console.log('Watchlist updated with new movie');
                                },
                                error: function(error) {
                                    console.error('Failed to update watchlist', error);
                                }
                            });
                        }
                    },
                    error: function(error) {
                        console.error('Failed to fetch watchlist', error);
                    }
                });
            },
            error: function(error) {
                if (error.status === 404) {
                    // Movie does not exist, proceed with adding the movie
                    $.ajax({
                        url: '/api/movies/search/providers',
                        type: 'GET',
                        data: { movieId: movieId },
                        success: function(response) {
                            const providerNames = response.map(function(provider) { return provider.first; });
                            const providerLogos = response.map(function(provider) { return provider.second; });

                            const movie = {
                                id: movieId,
                                original_title: form.find('input[name="original_title"]').val(),
                                adult: form.find('input[name="adult"]').val(),
                                backdrop_path: form.find('input[name="backdrop_path"]').val(),
                                genre_ids: form.find('input[name="genre_ids"]').val().replace(/[\[\]\s]/g, '').split(',').map(Number),
                                original_language: form.find('input[name="original_language"]').val(),
                                overview: form.find('input[name="overview"]').val(),
                                popularity: form.find('input[name="popularity"]').val(),
                                poster_path: form.find('input[name="poster_path"]').val(),
                                release_date: form.find('input[name="release_date"]').val(),
                                title: form.find('input[name="title"]').val(),
                                video: form.find('input[name="video"]').val(),
                                vote_average: form.find('input[name="vote_average"]').val(),
                                vote_count: form.find('input[name="vote_count"]').val(),
                                provider_names: providerNames,
                                provider_logos: providerLogos
                            };

                            $.ajax({
                                url: '/api/movies',
                                type: 'POST',
                                contentType: 'application/json',
                                data: JSON.stringify(movie),
                                success: function(response) {
                                    form.closest('li').remove();
                                    // Add movie ID to watchlist
                                    $.ajax({
                                        url: '/api/watchlist',
                                        type: 'GET',
                                        success: function(watchlist) {
                                            console.log('Watchlist:', watchlist);
                                            if (!watchlist.movie_ids.includes(movieId)) {
                                                watchlist.movie_ids.push(movieId);
                                                $.ajax({
                                                    url: '/api/watchlist',
                                                    type: 'PUT',
                                                    contentType: 'application/json',
                                                    data: JSON.stringify(watchlist),
                                                    success: function() {
                                                        console.log('Watchlist updated with new movie');
                                                    },
                                                    error: function(error) {
                                                        console.error('Failed to update watchlist', error);
                                                    }
                                                });
                                            }
                                        },
                                        error: function(error) {
                                            console.error('Failed to fetch watchlist', error);
                                        }
                                    });
                                },
                                error: function(error) {
                                    alert('Failed to add movie');
                                }
                            });
                        },
                        error: function(error) {
                            alert('Failed to fetch providers');
                        }
                    });
                } else {
                    alert('Failed to check if movie exists');
                }
            }
        });
    });

    $(document).on('submit', 'form.add-show-form', function(event) {
        event.preventDefault();
        const form = $(this);
        const showId = form.find('input[name="id"]').val();

        // Check if the movie already exists in the database
        $.ajax({
            url: `/api/shows/${showId}`,
            type: 'GET',
            success: function(response) {
                console.log('Show already exists in the database, adding to watchlist');
                $.ajax({
                    url: '/api/watchlist',
                    type: 'GET',
                    success: function(watchlist) {
                        if (!watchlist.show_ids.includes(showId)) {
                            watchlist.show_ids.push(showId);
                            $.ajax({
                                url: '/api/watchlist',
                                type: 'PUT',
                                contentType: 'application/json',
                                data: JSON.stringify(watchlist),
                                success: function() {
                                    console.log('Watchlist updated with new show');
                                },
                                error: function(error) {
                                    console.error('Failed to update watchlist', error);
                                }
                            });
                        }
                    },
                    error: function(error) {
                        console.error('Failed to fetch watchlist', error);
                    }
                });
            },
            error: function(error) {
                if (error.status === 404) {
                    // Show does not exist, proceed with adding the movie
                    $.ajax({
                        url: '/api/shows/search/providers',
                        type: 'GET',
                        data: { showId: showId },
                        success: function(response) {
                            const providerNames = response.map(function (provider) { return provider.first; });
                            const providerLogos = response.map(function (provider) { return provider.second; });

                            const show = {
                                id: showId,
                                adult: form.find('input[name="adult"]').val(),
                                backdrop_path: form.find('input[name="backdrop_path"]').val(),
                                genre_ids: form.find('input[name="genre_ids"]').val().replace(/[\[\]\s]/g, '').split(',').filter(Boolean).map(Number),
                                origin_country: form.find('input[name="origin_country"]').val().replace(/[\[\]\s]/g, '').split(',').filter(Boolean).map(String),
                                original_language: form.find('input[name="original_language"]').val(),
                                original_name: form.find('input[name="original_name"]').val(),
                                overview: form.find('input[name="overview"]').val(),
                                popularity: form.find('input[name="popularity"]').val(),
                                poster_path: form.find('input[name="poster_path"]').val(),
                                first_air_date: form.find('input[name="first_air_date"]').val(),
                                name: form.find('input[name="name"]').val(),
                                vote_average: form.find('input[name="vote_average"]').val(),
                                vote_count: form.find('input[name="vote_count"]').val(),
                                provider_names: providerNames,
                                provider_logos: providerLogos
                            };

                            $.ajax({
                                url: '/api/shows',
                                type: 'POST',
                                contentType: 'application/json',
                                data: JSON.stringify(show),
                                success: function(response) {
                                    form.closest('li').remove();
                                    // Add show ID to watchlist
                                    $.ajax({
                                        url: '/api/watchlist',
                                        type: 'GET',
                                        success: function(watchlist) {
                                            if (!watchlist.show_ids.includes(showId)) {
                                                watchlist.show_ids.push(showId);
                                                $.ajax({
                                                    url: '/api/watchlist',
                                                    type: 'PUT',
                                                    contentType: 'application/json',
                                                    data: JSON.stringify(watchlist),
                                                    success: function() {
                                                        console.log('Watchlist updated with new show');
                                                    },
                                                    error: function(error) {
                                                        console.error('Failed to update watchlist', error);
                                                    }
                                                });
                                            }
                                        },
                                        error: function(error) {
                                            console.error('Failed to fetch watchlist', error);
                                        }
                                    });
                                },
                                error: function(error) {
                                    alert('Failed to add show');
                                }
                            });
                        },
                        error: function(error) {
                            alert('Failed to fetch providers');
                        }
                    });
                } else {
                    alert('Failed to check if show exists');
                }
            }
        });
    });
});
//placeholder frontend, just making it work for now -> replace with react frontend later

$(document).ready(function() {
    $('#login-form').hide();
    $('#register-form').hide();
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

    window.currentUser = null;

    let isLoggedIn = null;
    let getUrl = null;

    function setUserStatus(loggedIn, user) {
        const statusDiv = $('#user-status');
        statusDiv.empty();

        if (loggedIn) {
            window.currentUser = user;
            statusDiv.append(`<span>Logged in as: <strong>${user.username}</strong></span>`);
            statusDiv.append(` <button id="logout-btn">Logout</button>`);

            $('#logout-btn').on('click', function () {
                $.post('/api/users/logout', function () {
                    location.reload();
                });
            });
            $('#auth-forms').hide();
            isLoggedIn = true;
        } else {
            window.currentUser = null;
            statusDiv.append(`<span>Logged in as: <strong>Guest</strong></span>`);
            statusDiv.append(`<button id="login-btn">Login</button>`);
            statusDiv.append(`<button id="register-btn">Register</button>`);

            $('#login-btn').on('click', function () {
                console.log("Login button clicked");
                $('#register-form').hide(); // hide other
                $('#login-form').toggle();  // toggle this
            });
            $('#register-btn').on('click', function () {
                $('#login-form').hide(); // hide other
                $('#register-form').toggle(); // toggle this
            });
            isLoggedIn = false;
        }

        getUrl = !isLoggedIn ? '/api/watchlist' : `/api/watchlist/user/${user.userId}`;
        console.log('getUrl:', getUrl);
    }

    // Detect login status
    $.ajax({
        url: '/api/users/me',
        type: 'GET',
        success: function (data) {
            setUserStatus(true, data);
        },
        error: function () {
            setUserStatus(false, null);
        }
    });

    $('#login').on('submit', function (e) {
        e.preventDefault();
        const formData = $(this).serialize(); // encodes username=...&password=...

        $.ajax({
            url: '/api/users/login',
            type: 'POST',
            data: formData,
            success: function () {
                location.reload();
            },
            error: function () {
                alert('Login failed. Please check your credentials.');
            }
        });
    });

    $('#register').on('submit', function (e) {
        e.preventDefault();

        const payload = {
            username: $('#register input[name="username"]').val(),
            password: $('#register input[name="password"]').val(),
            email: $('#register input[name="email"]').val() || null
        };

        $.ajax({
            url: '/api/users/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function () {
                alert('Registration successful! You are now logged in.');
                location.reload();
            },
            error: function (xhr) {
                let errorMessage = 'Registration failed';
                if (xhr.responseText) {
                    try {
                        const json = JSON.parse(xhr.responseText);
                        errorMessage = json.error || errorMessage;
                    } catch (e) {
                        // Not valid JSON — keep default error message
                    }
                }
                alert(errorMessage);
            }
        });
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
                    url: getUrl,
                    type: 'GET',
                    success: function(watchlist) {
                        const putUrl = !isLoggedIn ? '/api/watchlist' : `/api/watchlist/${watchlist.id}`;
                        console.log('Watchlist:', watchlist);
                        if (!watchlist.movie_ids.includes(Number(movieId))) {
                            watchlist.movie_ids.push(Number(movieId));
                            $.ajax({
                                url: putUrl,
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
                        } else {
                            alert("Movie already in watchlist");
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
                            console.log('Movie providers fetched:', response);

                            const providers = response.first; // This is the List<Pair<String, String>>
                            const providerInfoLastUpdate = response.second; // This is the LocalDateTime

                            const providerNames = providers.map(provider => provider.first);
                            const providerLogos = providers.map(provider => provider.second);

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
                                provider_logos: providerLogos,
                                providerinfo_lastupdate: providerInfoLastUpdate
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
                                        url: getUrl,
                                        type: 'GET',
                                        success: function(watchlist) {
                                            const putUrl = !isLoggedIn ? '/api/watchlist' : `/api/watchlist/${watchlist.id}`;
                                            console.log('Watchlist:', watchlist);
                                            if (!watchlist.movie_ids.includes(movieId)) {
                                                watchlist.movie_ids.push(movieId);
                                                $.ajax({
                                                    url: putUrl,
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
                        error: function(xhr) {
                            if (xhr.status === 400) {
                                console.log('Provider info was updated less than 24 hours ago for movie:' + movieId);
                            } else {
                                console.error('Error fetching movie providers', xhr.status, xhr.responseText);
                            }
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
                    url: getUrl,
                    type: 'GET',
                    success: function(watchlist) {
                        const putUrl = !isLoggedIn ? '/api/watchlist' : `/api/watchlist/${watchlist.id}`;
                        if (!watchlist.show_ids.includes(Number(showId))) {
                            watchlist.show_ids.push(Number(showId));
                            $.ajax({
                                url: putUrl,
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
                        } else {
                            alert("Show already in watchlist");
                        }
                    },
                    error: function(error) {
                        console.error('Failed to fetch watchlist', error);
                    }
                });
            },
            error: function(error) {
                if (error.status === 404) {
                    // Show does not exist, proceed with adding the show
                    $.ajax({
                        url: '/api/shows/search/providers',
                        type: 'GET',
                        data: { showId: showId },
                        success: function(response) {
                            console.log('Show providers fetched:', response);

                            const providers = response.first; // This is the List<Pair<String, String>>
                            const providerInfoLastUpdate = response.second; // This is the LocalDateTime

                            const providerNames = providers.map(provider => provider.first);
                            const providerLogos = providers.map(provider => provider.second);

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
                                provider_logos: providerLogos,
                                providerinfo_lastupdate: providerInfoLastUpdate
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
                                        url: getUrl,
                                        type: 'GET',
                                        success: function(watchlist) {
                                            const putUrl = !isLoggedIn ? '/api/watchlist' : `/api/watchlist/${watchlist.id}`;
                                            if (!watchlist.show_ids.includes(showId)) {
                                                watchlist.show_ids.push(showId);
                                                $.ajax({
                                                    url: putUrl,
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
                        error: function(xhr) {
                            if (xhr.status === 400) {
                                console.log('Provider info was updated less than 24 hours ago for show:' + showId);
                            } else {
                                console.error('Error fetching show providers');
                            }
                        }
                    });
                } else {
                    alert('Failed to check if show exists');
                }
            }
        });
    });
});
$(document).ready(function() {
    $('form.add-movie-form').on('submit', function(event) {
        event.preventDefault();
        const form = $(this);
        const movieId = form.find('input[name="id"]').val();

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
                    },
                    error: function(error) {
                        alert('Failed to add movie, it might already be in your watchlist');
                    }
                });
            },
            error: function(error) {
                alert('Failed to fetch providers');
            }
        });
    });
});
$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (!token) {
        alert('Invalid password reset link.');
        $('form').hide();
        return;
    }

    $('#reset-password-form').on('submit', function(e) {
        e.preventDefault();
        const newPassword = $('input[name="newPassword"]').val();

        $.ajax({
            url: '/api/users/reset-password',
            type: 'POST',
            data: {
                token: token,
                newPassword: newPassword
            },
            success: function() {
                alert('Password reset successful! You can now log in.');
                window.location.href = '/index.html';
            },
            error: function() {
                alert('Failed to reset password. Link may have expired.');
            }
        });
    });
});

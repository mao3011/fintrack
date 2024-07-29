/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('password-reset-form');
    const errorContainer = document.getElementById('error-message-container');

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const email = document.getElementById('email').value;

        fetch('/api/password-reset', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: email })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                errorContainer.textContent = 'パスワードリセットリンクをメールで送信しました。';
                errorContainer.style.display = 'block';
                errorContainer.style.color = 'green';
            } else {
                errorContainer.textContent = data.message || 'パスワードリセットに失敗しました。';
                errorContainer.style.display = 'block';
                errorContainer.style.color = 'red';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            errorContainer.textContent = 'エラーが発生しました。後でもう一度お試しください。';
            errorContainer.style.display = 'block';
            errorContainer.style.color = 'red';
        });
    });
});
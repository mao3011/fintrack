document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');
    const errorContainer = document.getElementById('error-message-container');

    loginForm.addEventListener('submit', function(e) {
        if (!validateForm()) {
            e.preventDefault(); // フォーム送信を中止
        }
    });

    function validateForm() {
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        let isValid = true;

        // 基本的なクライアントサイドバリデーション
        if (username === '') {
            showError('username', 'ユーザー名を入力してください');
            isValid = false;
        } else {
            clearError('username');
        }

        if (password === '') {
            showError('password', 'パスワードを入力してください');
            isValid = false;
        } else {
            clearError('password');
        }

        return isValid;
    }

    function showError(field, message) {
        const errorElement = document.querySelector(`#${field} + .error-message`);
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }

    function clearError(field) {
        const errorElement = document.querySelector(`#${field} + .error-message`);
        errorElement.textContent = '';
        errorElement.style.display = 'none';
    }

    // サーバーからのエラーメッセージを表示（必要に応じて）
    if (errorContainer) {
        const urlParams = new URLSearchParams(window.location.search);
        const errorMsg = urlParams.get('error');
        if (errorMsg) {
            errorContainer.textContent = decodeURIComponent(errorMsg);
            errorContainer.style.display = 'block';
        }
    }
});
function onSignIn(googleUser) {
    const id_token = googleUser.getAuthResponse().id_token;

    fetch('/api/google-login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token: id_token })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            window.location.href = '/index.html';  // ログイン成功時のリダイレクト
        } else {
            const errorContainer = document.getElementById('error-message-container');
            errorContainer.textContent = data.message || 'Googleログインに失敗しました。';
            errorContainer.style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        const errorContainer = document.getElementById('error-message-container');
        errorContainer.textContent = 'エラーが発生しました。後でもう一度お試しください。';
        errorContainer.style.display = 'block';
    });
}
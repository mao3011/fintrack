/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('register-form');
    const errorContainer = document.getElementById('error-message-container');

    registerForm.addEventListener('submit', function(e) {
        if (!validateForm()) {
            e.preventDefault(); // フォーム送信を中止
        }
    });

    function validateForm() {
        const username = document.getElementById('username').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirm-password').value;
        let isValid = true;

        // ユーザー名のバリデーション
        if (username === '') {
            showError('username', 'ユーザー名を入力してください');
            isValid = false;
        } else {
            clearError('username');
        }

        // メールアドレスのバリデーション
        if (email === '') {
            showError('email', 'メールアドレスを入力してください');
            isValid = false;
        } else if (!isValidEmail(email)) {
            showError('email', '有効なメールアドレスを入力してください');
            isValid = false;
        } else {
            clearError('email');
        }

        // パスワードのバリデーション
        if (password === '') {
            showError('password', 'パスワードを入力してください');
            isValid = false;
        } else if (password.length < 8) {
            showError('password', 'パスワードは8文字以上で入力してください');
            isValid = false;
        } else {
            clearError('password');
        }

        // パスワード確認のバリデーション
        if (confirmPassword === '') {
            showError('confirm-password', 'パスワード（確認）を入力してください');
            isValid = false;
        } else if (password !== confirmPassword) {
            showError('confirm-password', 'パスワードが一致しません');
            isValid = false;
        } else {
            clearError('confirm-password');
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

    function isValidEmail(email) {
        const re = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
        return re.test(email);
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
/**
 * 
 */
// utils.js ファイルを作成し、以下の関数を追加
export function handleError(error, elementId) {
    console.error('Error:', error);
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
        errorElement.textContent = `エラーが発生しました: ${error.message}`;
        errorElement.style.display = 'block';
    }
}

// 各JSファイルで以下のように使用
import { handleError } from './utils.js';

// ... 

fetch('/api/some-endpoint')
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        // データ処理
    })
    .catch(error => handleError(error, 'error-message-container'));
    // utils.js に以下の関数を追加
export function showLoading(elementId) {
    const loadingElement = document.getElementById(elementId);
    if (loadingElement) {
        loadingElement.style.display = 'block';
    }
}

export function hideLoading(elementId) {
    const loadingElement = document.getElementById(elementId);
    if (loadingElement) {
        loadingElement.style.display = 'none';
    }
}

// 各JSファイルで以下のように使用
import { showLoading, hideLoading } from './utils.js';

// ...

showLoading('loading-spinner');
fetch('/api/some-endpoint')
    .then(response => response.json())
    .then(data => {
        // データ処理
    })
    .catch(error => handleError(error, 'error-message-container'))
    .finally(() => hideLoading('loading-spinner'));
    // utils.js に以下の関数を追加
export function validateForm(form) {
    const inputs = form.querySelectorAll('input, select, textarea');
    let isValid = true;

    inputs.forEach(input => {
        if (input.hasAttribute('required') && !input.value.trim()) {
            showError(input, '必須項目です');
            isValid = false;
        } else if (input.type === 'email' && !isValidEmail(input.value)) {
            showError(input, '有効なメールアドレスを入力してください');
            isValid = false;
        } else {
            clearError(input);
        }
    });

    return isValid;
}

function showError(input, message) {
    const errorElement = input.nextElementSibling;
    if (errorElement && errorElement.classList.contains('error-message')) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}

function clearError(input) {
    const errorElement = input.nextElementSibling;
    if (errorElement && errorElement.classList.contains('error-message')) {
        errorElement.textContent = '';
        errorElement.style.display = 'none';
    }
}

function isValidEmail(email) {
    const re = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    return re.test(email);
}

// フォーム送信時に使用
import { validateForm } from './utils.js';

form.addEventListener('submit', function(event) {
    event.preventDefault();
    if (validateForm(this)) {
        // フォーム送信処理
    }
});
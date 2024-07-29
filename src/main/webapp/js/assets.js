/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    loadAllAssets();
    setupFormValidation();
});

function setupFormValidation() {
    const forms = document.querySelectorAll('.asset-form');
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input');
        inputs.forEach(input => {
            input.addEventListener('input', function() {
                validateInput(this);
            });
        });

        form.addEventListener('submit', function(e) {
            e.preventDefault();
            if (validateForm(this)) {
                addAsset(this);
            }
        });
    });
}

function validateInput(input) {
    const errorElement = input.nextElementSibling;
    if (input.validity.valid) {
        errorElement.textContent = '';
        errorElement.classList.remove('active');
        input.classList.remove('invalid');
    } else {
        showError(input, errorElement);
    }
}

function showError(input, errorElement) {
    if (input.validity.valueMissing) {
        errorElement.textContent = 'この項目は必須です。';
    } else if (input.validity.typeMismatch) {
        errorElement.textContent = '正しい形式で入力してください。';
    } else if (input.validity.rangeUnderflow) {
        errorElement.textContent = '0以上の値を入力してください。';
    }
    errorElement.classList.add('active');
    input.classList.add('invalid');
}

function validateForm(form) {
    let isValid = true;
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        if (!input.validity.valid) {
            isValid = false;
            showError(input, input.nextElementSibling);
        }
    });
    return isValid;
}

function addAsset(form) {
    const formData = new FormData(form);
    const categoryId = form.id.replace('-form', '');
    formData.append('categoryId', categoryId);

    fetch('/api/assets', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            loadAllAssets();
            form.reset();
            showFeedback(form, '資産が正常に追加されました。', 'success');
        } else {
            showFeedback(form, '資産の追加に失敗しました。', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showFeedback(form, 'エラーが発生しました。', 'error');
    });
}

function showFeedback(form, message, type) {
    const feedbackElement = form.querySelector('.feedback');
    if (!feedbackElement) {
        const newFeedback = document.createElement('div');
        newFeedback.className = `feedback ${type}`;
        form.appendChild(newFeedback);
    }
    feedbackElement.textContent = message;
    feedbackElement.classList.add(type);
    setTimeout(() => {
        feedbackElement.textContent = '';
        feedbackElement.classList.remove(type);
    }, 3000);
}

function deleteAsset(categoryId, assetId) {
    if (confirm('この資産を削除してもよろしいですか？この操作は取り消せません。')) {
        fetch(`/api/assets/${assetId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadAllAssets();
                showFeedback(document.getElementById(`${categoryId}-form`), '資産が正常に削除されました。', 'success');
            } else {
                showFeedback(document.getElementById(`${categoryId}-form`), '資産の削除に失敗しました。', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showFeedback(document.getElementById(`${categoryId}-form`), 'エラーが発生しました。', 'error');
        });
    }
}

function loadAllAssets() {
    fetch('/api/assets')
        .then(response => response.json())
        .then(data => {
            updateAssetsSummary(data.summary);
            updateAssetCategories(data.categories);
        })
        .catch(error => console.error('Error:', error));
}

function updateAssetsSummary(summary) {
    const summaryElement = document.getElementById('assets-summary');
    summaryElement.innerHTML = `
        <div class="summary-item">
            <h3>総資産</h3>
            <div class="amount">${formatCurrency(summary.totalAssets)}</div>
        </div>
        <div class="summary-item">
            <h3>総負債</h3>
            <div class="amount">${formatCurrency(summary.totalLiabilities)}</div>
        </div>
        <div class="summary-item">
            <h3>純資産</h3>
            <div class="amount">${formatCurrency(summary.netWorth)}</div>
        </div>
    `;
}

function updateAssetCategories(categories) {
    categories.forEach(category => {
        const listElement = document.getElementById(`${category.id}-list`);
        if (listElement) {
            listElement.innerHTML = '';
            category.assets.forEach(asset => {
                const li = document.createElement('li');
                li.className = 'asset-item';
                li.innerHTML = `
                    <span>${asset.name}: ${formatCurrency(asset.balance)}</span>
                    <button class="delete-btn" data-id="${asset.id}">削除</button>
                `;
                li.querySelector('.delete-btn').addEventListener('click', () => deleteAsset(category.id, asset.id));
                listElement.appendChild(li);
            });
        }

        const form = document.getElementById(`${category.id}-form`);
        if (form) {
            form.onsubmit = (e) => {
                e.preventDefault();
                addAsset(category.id);
            };
        }
    });
}

function addAsset(categoryId) {
    const form = document.getElementById(`${categoryId}-form`);
    const formData = new FormData(form);
    formData.append('categoryId', categoryId);

    fetch('/api/assets', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            loadAllAssets();
            form.reset();
        } else {
            alert('資産の追加に失敗しました。');
        }
    })
    .catch(error => console.error('Error:', error));
}

function deleteAsset(_categoryId, assetId) {
    if (confirm('この資産を削除してもよろしいですか？')) {
        fetch(`/api/assets/${assetId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadAllAssets();
            } else {
                alert('資産の削除に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
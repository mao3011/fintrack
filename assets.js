document.addEventListener('DOMContentLoaded', function() {
    loadAllAssets();
});

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

function deleteAsset(categoryId, assetId) {
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
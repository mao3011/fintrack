document.addEventListener('DOMContentLoaded', function() {
    setupEventListeners();
    loadAllAssets();
});

function setupEventListeners() {
    const forms = document.querySelectorAll('.asset-form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            event.preventDefault();
            const category = this.id.replace('-form', '');
            addAsset(category);
        });
    });
}

function addAsset(category) {
    const form = document.getElementById(`${category}-form`);
    const name = form.querySelector('input[name="name"]').value;
    const balance = parseFloat(form.querySelector('input[name="balance"]').value);

    if (name && !isNaN(balance)) {
        const assets = JSON.parse(localStorage.getItem(category)) || [];
        assets.push({ id: Date.now(), name, balance });
        localStorage.setItem(category, JSON.stringify(assets));
        loadAssets(category);
        form.reset();
    }
}

function loadAllAssets() {
    const categories = ['bank', 'card', 'security', 'emoney', 'point', 'liability'];
    categories.forEach(loadAssets);
    updateAssetsSummary();
}

function loadAssets(category) {
    const assets = JSON.parse(localStorage.getItem(category)) || [];
    const list = document.getElementById(`${category}-list`);
    list.innerHTML = '';

    assets.forEach(asset => {
        const li = document.createElement('li');
        li.className = 'asset-item';
        li.innerHTML = `
            <span>${asset.name}: ${formatCurrency(asset.balance)}</span>
            <button class="delete-btn" data-id="${asset.id}">削除</button>
        `;
        li.querySelector('.delete-btn').addEventListener('click', () => deleteAsset(category, asset.id));
        list.appendChild(li);
    });
}

function deleteAsset(category, id) {
    const assets = JSON.parse(localStorage.getItem(category)) || [];
    const updatedAssets = assets.filter(asset => asset.id !== id);
    localStorage.setItem(category, JSON.stringify(updatedAssets));
    loadAssets(category);
    updateAssetsSummary();
}

function updateAssetsSummary() {
    const summary = {
        totalAssets: 0,
        totalLiabilities: 0,
        netWorth: 0
    };

    ['bank', 'card', 'security', 'emoney', 'point'].forEach(category => {
        const assets = JSON.parse(localStorage.getItem(category)) || [];
        summary.totalAssets += assets.reduce((total, asset) => total + asset.balance, 0);
    });

    const liabilities = JSON.parse(localStorage.getItem('liability')) || [];
    summary.totalLiabilities = liabilities.reduce((total, liability) => total + liability.balance, 0);

    summary.netWorth = summary.totalAssets - summary.totalLiabilities;

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

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
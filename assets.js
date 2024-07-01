let assetsChart;

document.addEventListener('DOMContentLoaded', function() {
    loadAssets();
    setupEventListeners();
    initializeChart();
});

function setupEventListeners() {
    document.getElementById('add-savings').addEventListener('click', addSavings);
    document.getElementById('add-debt').addEventListener('click', addDebt);
}

function loadAssets() {
    loadSavings();
    loadDebts();
}

function loadSavings() {
    const savings = JSON.parse(localStorage.getItem('savings')) || [];
    const savingsList = document.getElementById('savings-list');
    savingsList.innerHTML = '';
    savings.forEach(item => {
        const li = createAssetElement(item, 'savings');
        savingsList.appendChild(li);
    });
}

function loadDebts() {
    const debts = JSON.parse(localStorage.getItem('debts')) || [];
    const debtList = document.getElementById('debt-list');
    debtList.innerHTML = '';
    debts.forEach(item => {
        const li = createAssetElement(item, 'debt');
        debtList.appendChild(li);
    });
}

function createAssetElement(item, type) {
    const li = document.createElement('li');
    li.className = 'asset-item';
    li.innerHTML = `
        <span class="name">${item.name} (${item.account || item.type})</span>
        <span class="amount">${formatCurrency(item.amount)}</span>
        <button class="delete-btn" data-type="${type}" data-id="${item.id}">削除</button>
    `;
    li.querySelector('.delete-btn').addEventListener('click', deleteAsset);
    return li;
}

function addSavings() {
    const bank = document.getElementById('savings-bank').value;
    const account = document.getElementById('savings-account').value;
    const amount = document.getElementById('savings-amount').value;
    if (bank && account && amount) {
        const savings = JSON.parse(localStorage.getItem('savings')) || [];
        savings.push({ id: Date.now(), name: bank, account: account, amount: parseFloat(amount) });
        localStorage.setItem('savings', JSON.stringify(savings));
        loadSavings();
        clearInputs('savings');
    }
}

function addDebt() {
    const name = document.getElementById('debt-name').value;
    const type = document.getElementById('debt-type').value;
    const amount = document.getElementById('debt-amount').value;
    if (name && type && amount) {
        const debts = JSON.parse(localStorage.getItem('debts')) || [];
        debts.push({ id: Date.now(), name: name, type: type, amount: parseFloat(amount) });
        localStorage.setItem('debts', JSON.stringify(debts));
        loadDebts();
        clearInputs('debt');
    }
}

function deleteAsset(event) {
    const type = event.target.getAttribute('data-type');
    const id = parseInt(event.target.getAttribute('data-id'));
    const key = type === 'savings' ? 'savings' : 'debts';
    const items = JSON.parse(localStorage.getItem(key)) || [];
    const updatedItems = items.filter(item => item.id !== id);
    localStorage.setItem(key, JSON.stringify(updatedItems));
    if (type === 'savings') {
        loadSavings();
    } else {
        loadDebts();
    }
}

function clearInputs(type) {
    if (type === 'savings') {
        document.getElementById('savings-bank').value = '';
        document.getElementById('savings-account').value = '';
        document.getElementById('savings-amount').value = '';
    } else {
        document.getElementById('debt-name').value = '';
        document.getElementById('debt-type').value = '';
        document.getElementById('debt-amount').value = '';
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
function initializeChart() {
    const ctx = document.getElementById('assets-chart').getContext('2d');
    assetsChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [
                    '#36A2EB', '#FFCE56', '#FF6384', '#4BC0C0', '#9966FF', '#FF9F40'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                position: 'bottom'
            }
        }
    });
}

function updateAssetsOverview() {
    const savings = JSON.parse(localStorage.getItem('savings')) || [];
    const debts = JSON.parse(localStorage.getItem('debts')) || [];

    const totalAssets = savings.reduce((sum, item) => sum + item.amount, 0);
    const totalDebts = debts.reduce((sum, item) => sum + item.amount, 0);
    const netWorth = totalAssets - totalDebts;

    document.getElementById('total-assets').textContent = formatCurrency(totalAssets);
    document.getElementById('total-debts').textContent = formatCurrency(totalDebts);
    document.getElementById('net-worth').textContent = formatCurrency(netWorth);

    updateChart(savings, debts);
}

function updateChart(savings, debts) {
    const labels = [];
    const data = [];

    savings.forEach(item => {
        labels.push(item.name);
        data.push(item.amount);
    });

    debts.forEach(item => {
        labels.push(item.name + ' (負債)');
        data.push(-item.amount); // 負債は負の値として表示
    });

    assetsChart.data.labels = labels;
    assetsChart.data.datasets[0].data = data;
    assetsChart.update();
}

// loadSavings と loadDebts 関数を修正
function loadSavings() {
    const savings = JSON.parse(localStorage.getItem('savings')) || [];
    const savingsList = document.getElementById('savings-list');
    savingsList.innerHTML = '';
    savings.forEach(item => {
        const li = createAssetElement(item, 'savings');
        savingsList.appendChild(li);
    });
    updateAssetsOverview(); // 追加
}

function loadDebts() {
    const debts = JSON.parse(localStorage.getItem('debts')) || [];
    const debtList = document.getElementById('debt-list');
    debtList.innerHTML = '';
    debts.forEach(item => {
        const li = createAssetElement(item, 'debt');
        debtList.appendChild(li);
    });
    updateAssetsOverview(); // 追加
}
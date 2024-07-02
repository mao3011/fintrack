document.addEventListener('DOMContentLoaded', function() {
    initializeChart();
    loadBalanceSheet();
    setupEventListeners();
});

let assetChangeChart;

function initializeChart() {
    const ctx = document.getElementById('asset-change-chart').getContext('2d');
    assetChangeChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: '資産総額',
                data: [],
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function setupEventListeners() {
    document.getElementById('monthly-btn').addEventListener('click', () => updateChart('monthly'));
    document.getElementById('yearly-btn').addEventListener('click', () => updateChart('yearly'));
}

function updateChart(period) {
    fetch(`/api/asset-change?period=${period}`)
        .then(response => response.json())
        .then(data => {
            assetChangeChart.data.labels = data.labels;
            assetChangeChart.data.datasets[0].data = data.values;
            assetChangeChart.update();
        })
        .catch(error => console.error('Error:', error));
}

function loadBalanceSheet() {
    fetch('/api/balance-sheet')
        .then(response => response.json())
        .then(data => updateBalanceSheet(data))
        .catch(error => console.error('Error:', error));
}

function updateBalanceSheet(data) {
    for (const [id, value] of Object.entries(data)) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = formatCurrency(value);
        }
    }

    const totalAssets = calculateTotal(['cash-deposits', 'notes-receivable', 'accounts-receivable', 'securities', 'inventory', 'buildings', 'land']) - data['allowance-doubtful'];
    const totalLiabilities = calculateTotal(['notes-payable', 'accounts-payable', 'short-term-loans', 'long-term-loans']);
    const totalEquity = calculateTotal(['capital-stock', 'retained-earnings']);

    document.getElementById('total-assets').textContent = formatCurrency(totalAssets);
    document.getElementById('total-liabilities').textContent = formatCurrency(totalLiabilities);
    document.getElementById('total-equity').textContent = formatCurrency(totalEquity);
    document.getElementById('total-liabilities-equity').textContent = formatCurrency(totalLiabilities + totalEquity);
}

function calculateTotal(ids) {
    return ids.reduce((total, id) => {
        const element = document.getElementById(id);
        return total + (element ? parseFloat(element.textContent.replace(/[¥,]/g, '')) || 0 : 0);
    }, 0);
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
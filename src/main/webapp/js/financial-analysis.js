/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    initializeCharts();
    setupEventListeners();
    updateDashboard('monthly'); // 初期表示は月次データ
});

let assetChangeChart;
let expenseCategoryChart;

function initializeCharts() {
    // 資産変動グラフの初期化
    const assetCtx = document.getElementById('asset-change-chart').getContext('2d');
    assetChangeChart = new Chart(assetCtx, {
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
                    beginAtZero: true,
                    ticks: {
                        callback: function(value, _index) {
                            return '¥' + value.toLocaleString();
                        }
                    }
                }
            }
        }
    });

    // 支出カテゴリー別円グラフの初期化
    const expenseCtx = document.getElementById('expense-category-chart').getContext('2d');
    expenseCategoryChart = new Chart(expenseCtx, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [
                    'rgb(255, 99, 132)',
                    'rgb(54, 162, 235)',
                    'rgb(255, 206, 86)',
                    'rgb(75, 192, 192)',
                    'rgb(153, 102, 255)',
                    'rgb(255, 159, 64)'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'right',
                }
            }
        }
    });
}

function setupEventListeners() {
    document.getElementById('monthly-btn').addEventListener('click', () => updateDashboard('monthly'));
    document.getElementById('yearly-btn').addEventListener('click', () => updateDashboard('yearly'));
}

function updateDashboard(period) {
    // ローディングスピナーを表示
    document.getElementById('loading-spinner').style.display = 'block';

    // APIからデータを取得
    fetch(`/api/financial-data?period=${period}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            updateAssetChangeChart(data.assetChange);
            updateExpenseCategoryChart(data.expenseCategories);
            updateBalanceSheet(data.balanceSheet);
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('error-message-container').textContent = 'データの取得中にエラーが発生しました。';
            document.getElementById('error-message-container').style.display = 'block';
        })
        .finally(() => {
            // ローディングスピナーを非表示
            document.getElementById('loading-spinner').style.display = 'none';
        });
}

function updateAssetChangeChart(data) {
    assetChangeChart.data.labels = data.labels;
    assetChangeChart.data.datasets[0].data = data.values;
    assetChangeChart.update();
}

function updateExpenseCategoryChart(data) {
    expenseCategoryChart.data.labels = data.labels;
    expenseCategoryChart.data.datasets[0].data = data.values;
    expenseCategoryChart.update();
}

function updateBalanceSheet(data) {
    // 貸借対照表の更新ロジック
    for (const [id, value] of Object.entries(data)) {
        const element = document.getElementById(id);
        if (element) {
            element.textContent = formatCurrency(value);
        }
    }

    // 合計の計算と更新
    updateTotals();
}

function updateTotals() {
    const totalAssets = calculateTotal(['cash-deposits', 'notes-receivable', 'accounts-receivable', 'securities', 'inventory', 'buildings', 'land']);
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
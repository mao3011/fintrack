let currentDate = new Date();
let expenseChart;

document.addEventListener('DOMContentLoaded', function() {
    updateDateDisplay();
    setupEventListeners();
    initializeChart();
    updateDashboard();
    renderCalendar();
});

function updateDateDisplay() {
    const dateElement = document.getElementById('current-date');
    dateElement.textContent = `${currentDate.getFullYear()}年${currentDate.getMonth() + 1}月`;
}

function setupEventListeners() {
    document.querySelector('.prev-month').addEventListener('click', () => changeMonth(-1));
    document.querySelector('.next-month').addEventListener('click', () => changeMonth(1));
}

function changeMonth(delta) {
    currentDate.setMonth(currentDate.getMonth() + delta);
    updateDateDisplay();
    updateDashboard();
    renderCalendar();
}

function initializeChart() {
    const ctx = document.getElementById('expense-chart').getContext('2d');
    expenseChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [
                    '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'
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

function updateDashboard() {
    const transactions = getMonthTransactions(currentDate);
    const { income, expense } = calculateTotals(transactions);
    const balance = income - expense;

    document.getElementById('income-amount').textContent = formatCurrency(income);
    document.getElementById('expense-amount').textContent = formatCurrency(expense);
    const balanceElement = document.getElementById('balance-amount');
    balanceElement.textContent = formatCurrency(Math.abs(balance));
    balanceElement.classList.toggle('negative', balance < 0);

    updateExpenseChart(transactions);
}

function getMonthTransactions(date) {
    // この関数は、指定された月のトランザクションデータを返します
    // 実際の実装では、ローカルストレージやデータベースからデータを取得します
    return JSON.parse(localStorage.getItem('transactions')) || [];
}

function calculateTotals(transactions) {
    return transactions.reduce((totals, transaction) => {
        if (transaction.type === '収入') {
            totals.income += transaction.amount;
        } else {
            totals.expense += transaction.amount;
        }
        return totals;
    }, { income: 0, expense: 0 });
}

function updateExpenseChart(transactions) {
    const categories = {};
    transactions.forEach(transaction => {
        if (transaction.type === '支出') {
            categories[transaction.category] = (categories[transaction.category] || 0) + transaction.amount;
        }
    });

    expenseChart.data.labels = Object.keys(categories);
    expenseChart.data.datasets[0].data = Object.values(categories);
    expenseChart.update();
}

function renderCalendar() {
    const calendarElement = document.getElementById('monthly-calendar');
    calendarElement.innerHTML = '';

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    for (let i = 0; i < firstDay; i++) {
        calendarElement.appendChild(createCalendarDay());
    }

    for (let day = 1; day <= daysInMonth; day++) {
        calendarElement.appendChild(createCalendarDay(day));
    }
}

function createCalendarDay(day = null) {
    const dayElement = document.createElement('div');
    dayElement.className = 'calendar-day';

    if (day !== null) {
        dayElement.innerHTML = `
            <div class="calendar-day-header">${day}</div>
            <div class="calendar-day-content"></div>
            <div class="calendar-day-total"></div>
        `;

        const transactions = getDayTransactions(new Date(currentDate.getFullYear(), currentDate.getMonth(), day));
        const { income, expense } = calculateTotals(transactions);
        const balance = income - expense;

        const contentElement = dayElement.querySelector('.calendar-day-content');
        transactions.forEach(transaction => {
            contentElement.innerHTML += `${transaction.category}: ${formatCurrency(transaction.amount)}<br>`;
        });

        const totalElement = dayElement.querySelector('.calendar-day-total');
        totalElement.textContent = formatCurrency(Math.abs(balance));
        totalElement.classList.add(balance >= 0 ? 'positive' : 'negative');
    }

    return dayElement;
}

function getDayTransactions(date) {
    // この関数は、指定された日のトランザクションデータを返します
    // 実際の実装では、ローカルストレージやデータベースからデータを取得します
    return JSON.parse(localStorage.getItem('transactions')) || [];
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
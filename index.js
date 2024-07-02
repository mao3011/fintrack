document.addEventListener('DOMContentLoaded', function() {
    updateDateDisplay();
    setupEventListeners();
    if (document.getElementById('expense-chart')) {
        initializeChart();
        updateDashboard();
    }
    if (document.getElementById('transaction-form')) {
        setupTransactionForm();
    }
    renderCalendar();
});

let currentDate = new Date();
let expenseChart;

function updateDateDisplay() {
    const dateElement = document.getElementById('current-date');
    if (dateElement) {
        dateElement.textContent = `${currentDate.getFullYear()}年${currentDate.getMonth() + 1}月`;
    }
}

function setupEventListeners() {
    const prevMonth = document.querySelector('.prev-month');
    const nextMonth = document.querySelector('.next-month');
    if (prevMonth && nextMonth) {
        prevMonth.addEventListener('click', () => changeMonth(-1));
        nextMonth.addEventListener('click', () => changeMonth(1));
    }
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
    fetch(`/api/dashboard?year=${currentDate.getFullYear()}&month=${currentDate.getMonth() + 1}`)
        .then(response => response.json())
        .then(data => {
            updateSummaryCards(data);
            updateExpenseChart(data.expenses);
        })
        .catch(error => console.error('Error:', error));
}

function updateSummaryCards(data) {
    document.getElementById('income-amount').textContent = formatCurrency(data.income);
    document.getElementById('expense-amount').textContent = formatCurrency(data.totalExpense);
    const balance = data.income - data.totalExpense;
    const balanceElement = document.getElementById('balance-amount');
    balanceElement.textContent = formatCurrency(Math.abs(balance));
    balanceElement.classList.toggle('negative', balance < 0);
}

function updateExpenseChart(expenses) {
    expenseChart.data.labels = expenses.map(item => item.category);
    expenseChart.data.datasets[0].data = expenses.map(item => item.amount);
    expenseChart.update();
}

function setupTransactionForm() {
    const form = document.getElementById('transaction-form');
    const typeButtons = document.querySelectorAll('.type-button');
    const typeInput = document.getElementById('type');

    typeButtons.forEach(button => {
        button.addEventListener('click', function() {
            typeButtons.forEach(btn => btn.classList.remove('selected'));
            this.classList.add('selected');
            typeInput.value = this.getAttribute('data-type');
        });
    });

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        addTransaction();
    });
}

function addTransaction() {
    const form = document.getElementById('transaction-form');
    const formData = new FormData(form);

    fetch('/api/transactions', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            updateDashboard();
            renderCalendar();
            form.reset();
            document.querySelectorAll('.type-button').forEach(btn => btn.classList.remove('selected'));
        } else {
            alert('トランザクションの追加に失敗しました。');
        }
    })
    .catch(error => console.error('Error:', error));
}

function renderCalendar() {
    const calendarElement = document.getElementById('monthly-calendar');
    if (!calendarElement) return;

    fetch(`/api/calendar?year=${currentDate.getFullYear()}&month=${currentDate.getMonth() + 1}`)
        .then(response => response.json())
        .then(data => {
            calendarElement.innerHTML = '';
            const daysInMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();
            const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1).getDay();

            for (let i = 0; i < firstDay; i++) {
                calendarElement.appendChild(createCalendarDay());
            }

            for (let day = 1; day <= daysInMonth; day++) {
                calendarElement.appendChild(createCalendarDay(day, data[day]));
            }
        })
        .catch(error => console.error('Error:', error));
}

function createCalendarDay(day = null, transactions = []) {
    const dayElement = document.createElement('div');
    dayElement.className = 'calendar-day';

    if (day !== null) {
        dayElement.innerHTML = `
            <div class="calendar-day-header">${day}</div>
            <div class="calendar-day-content"></div>
            <div class="calendar-day-total"></div>
        `;

        const contentElement = dayElement.querySelector('.calendar-day-content');
        let dayTotal = 0;

        transactions.forEach(transaction => {
            contentElement.innerHTML += `${transaction.category}: ${formatCurrency(transaction.amount)}<br>`;
            dayTotal += transaction.type === 'income' ? transaction.amount : -transaction.amount;
        });

        const totalElement = dayElement.querySelector('.calendar-day-total');
        totalElement.textContent = formatCurrency(Math.abs(dayTotal));
        totalElement.classList.add(dayTotal >= 0 ? 'positive' : 'negative');
    }

    return dayElement;
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
let currentDate = new Date();
let expenseChart;

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
});

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
    const data = getDummyData();
    updateSummaryCards(data);
    updateExpenseChart(data);
    updateCategoryList(data);
}

function updateSummaryCards(data) {
    document.getElementById('income-amount').textContent = formatCurrency(data.income);
    document.getElementById('expense-amount').textContent = formatCurrency(data.totalExpense);
    document.getElementById('balance-amount').textContent = formatCurrency(data.income - data.totalExpense);
}

function updateExpenseChart(data) {
    expenseChart.data.labels = data.expenses.map(item => item.category);
    expenseChart.data.datasets[0].data = data.expenses.map(item => item.amount);
    expenseChart.update();
}

function updateCategoryList(data) {
    const categoryList = document.getElementById('category-list');
    if (categoryList) {
        categoryList.innerHTML = '';
        data.expenses.forEach(item => {
            const li = document.createElement('li');
            li.innerHTML = `
                <span>${item.category}</span>
                <span>${formatCurrency(item.amount)}</span>
            `;
            categoryList.appendChild(li);
        });
    }
}

function setupTransactionForm() {
    const form = document.getElementById('transaction-form');
    const tableBody = document.getElementById('transaction-table-body');
    const typeButtons = document.querySelectorAll('.type-button');
    const typeInput = document.getElementById('type');

    typeButtons.forEach(button => {
        button.addEventListener('click', function() {
            typeButtons.forEach(btn => btn.classList.remove('selected'));
            button.classList.add('selected');
            typeInput.value = button.getAttribute('data-type');
        });
    });

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const date = form.date.value;
        const category = form.category.value;
        const amount = form.amount.value;
        const type = form.type.value;

        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${date}</td>
            <td>${category}</td>
            <td>${formatCurrency(amount)}</td>
            <td>${type}</td>
        `;

        tableBody.prepend(newRow);

        form.reset();
        document.getElementById('date').valueAsDate = new Date();
        typeButtons.forEach(btn => btn.classList.remove('selected'));
    });
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}

function getDummyData() {
    return {
        income: 300000,
        totalExpense: 250000,
        expenses: [
            { category: '住宅', amount: 80000 },
            { category: '食費', amount: 60000 },
            { category: '交通費', amount: 30000 },
            { category: '光熱費', amount: 20000 },
            { category: '娯楽', amount: 40000 },
            { category: 'その他', amount: 20000 }
        ]
    };
}
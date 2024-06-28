let currentDate = new Date();
let transactions = [];

document.addEventListener('DOMContentLoaded', function() {
    setupEventListeners();
    updateView();
});

function setupEventListeners() {
    const form = document.getElementById('transaction-form');
    const typeButtons = document.querySelectorAll('.type-button');
    const typeInput = document.getElementById('type');
    const addCategoryBtn = document.getElementById('add-category');
    const listViewBtn = document.getElementById('list-view-btn');
    const calendarViewBtn = document.getElementById('calendar-view-btn');

    typeButtons.forEach(button => {
        button.addEventListener('click', function() {
            typeButtons.forEach(btn => btn.classList.remove('selected'));
            button.classList.add('selected');
            typeInput.value = button.getAttribute('data-type');
        });
    });

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        addTransaction();
    });

    addCategoryBtn.addEventListener('click', addCategory);
    listViewBtn.addEventListener('click', () => switchView('list'));
    calendarViewBtn.addEventListener('click', () => switchView('calendar'));
}

function addTransaction() {
    const form = document.getElementById('transaction-form');
    const date = form.date.value;
    const category = form.category.value;
    const amount = form.amount.value;
    const type = form.type.value;

    const transaction = {
        date: new Date(date),
        category: category,
        amount: parseFloat(amount),
        type: type
    };

    transactions.push(transaction);
    updateView();
    form.reset();
    document.querySelectorAll('.type-button').forEach(btn => btn.classList.remove('selected'));
}

function updateView() {
    const currentView = document.querySelector('.view-toggle button.active').id === 'list-view-btn' ? 'list' : 'calendar';
    if (currentView === 'list') {
        updateListView();
    } else {
        updateCalendarView();
    }
}

function updateListView() {
    const tableBody = document.getElementById('transaction-table-body');
    tableBody.innerHTML = '';

    transactions.sort((a, b) => b.date - a.date).forEach(transaction => {
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${formatDate(transaction.date)}</td>
            <td>${transaction.category}</td>
            <td class="${transaction.type === '収入' ? 'income' : 'expense'}">
                ${formatCurrency(transaction.amount, transaction.type)}
            </td>
            <td>${transaction.type}</td>
        `;
        tableBody.appendChild(newRow);
    });
}

function updateCalendarView() {
    const calendarView = document.getElementById('calendar-view');
    calendarView.innerHTML = '';

    const calendar = document.createElement('div');
    calendar.className = 'calendar';

    const daysInMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0).getDate();
    const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1).getDay();

    // Add day headers
    const days = ['日', '月', '火', '水', '木', '金', '土'];
    days.forEach(day => {
        const dayHeader = document.createElement('div');
        dayHeader.className = 'calendar-day-header';
        dayHeader.textContent = day;
        calendar.appendChild(dayHeader);
    });

    // Add empty cells for days before the first day of the month
    for (let i = 0; i < firstDay; i++) {
        calendar.appendChild(document.createElement('div'));
    }

    // Add cells for each day of the month
    for (let i = 1; i <= daysInMonth; i++) {
        const dayCell = document.createElement('div');
        dayCell.className = 'calendar-day';
        dayCell.innerHTML = `<div class="calendar-day-header">${i}</div>`;

        const dayTransactions = transactions.filter(t => 
            t.date.getFullYear() === currentDate.getFullYear() &&
            t.date.getMonth() === currentDate.getMonth() &&
            t.date.getDate() === i
        );

        dayTransactions.forEach(transaction => {
            const transactionElement = document.createElement('div');
            transactionElement.className = `calendar-transaction ${transaction.type === '収入' ? 'income' : 'expense'}`;
            transactionElement.textContent = `${transaction.category}: ${formatCurrency(transaction.amount, transaction.type)}`;
            dayCell.appendChild(transactionElement);
        });

        calendar.appendChild(dayCell);
    }

    calendarView.appendChild(calendar);
}

function switchView(view) {
    const listView = document.getElementById('list-view');
    const calendarView = document.getElementById('calendar-view');
    const listViewBtn = document.getElementById('list-view-btn');
    const calendarViewBtn = document.getElementById('calendar-view-btn');

    if (view === 'list') {
        listView.style.display = 'block';
        calendarView.style.display = 'none';
        listViewBtn.classList.add('active');
        calendarViewBtn.classList.remove('active');
    } else {
        listView.style.display = 'none';
        calendarView.style.display = 'block';
        listViewBtn.classList.remove('active');
        calendarViewBtn.classList.add('active');
    }

    updateView();
}

function addCategory() {
    const newCategory = prompt('新しいカテゴリー名を入力してください:');
    if (newCategory) {
        const categorySelect = document.getElementById('category');
        const option = document.createElement('option');
        option.value = newCategory;
        option.textContent = newCategory;
        categorySelect.appendChild(option);
    }
}

function formatDate(date) {
    return `${date.getFullYear()}/${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getDate().toString().padStart(2, '0')}`;
}

function formatCurrency(amount, type) {
    const formatter = new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' });
    return type === '支出' ? `-${formatter.format(amount)}` : formatter.format(amount);
}
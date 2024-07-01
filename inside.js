document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    loadPaymentMethods();
    setupEventListeners();
    loadTransactions();
});

function loadCategories() {
    const categories = JSON.parse(localStorage.getItem('categories')) || {};
    const mainCategorySelect = document.getElementById('main-category');
    const subCategorySelect = document.getElementById('sub-category');

    mainCategorySelect.innerHTML = '<option value="">選択してください</option>';
    Object.keys(categories).forEach(mainCategory => {
        mainCategorySelect.innerHTML += `<option value="${mainCategory}">${mainCategory}</option>`;
    });

    mainCategorySelect.addEventListener('change', function() {
        const selectedMainCategory = this.value;
        subCategorySelect.innerHTML = '<option value="">選択してください</option>';
        if (selectedMainCategory && categories[selectedMainCategory]) {
            categories[selectedMainCategory].forEach(subCategory => {
                subCategorySelect.innerHTML += `<option value="${subCategory}">${subCategory}</option>`;
            });
        }
    });
}

function loadPaymentMethods() {
    const paymentMethods = JSON.parse(localStorage.getItem('paymentMethods')) || ['現金', 'クレジットカード', '電子マネー'];
    const paymentMethodSelect = document.getElementById('payment-method');
    paymentMethodSelect.innerHTML = paymentMethods.map(method => `<option value="${method}">${method}</option>`).join('');
}

function setupEventListeners() {
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
    const transaction = {
        id: Date.now(),
        date: form.date.value,
        mainCategory: form['main-category'].value,
        subCategory: form['sub-category'].value,
        amount: parseFloat(form.amount.value),
        paymentMethod: form['payment-method'].value,
        memo: form.memo.value,
        type: form.type.value
    };

    const transactions = JSON.parse(localStorage.getItem('transactions')) || [];
    transactions.push(transaction);
    localStorage.setItem('transactions', JSON.stringify(transactions));

    loadTransactions();
    form.reset();
    document.querySelectorAll('.type-button').forEach(btn => btn.classList.remove('selected'));
}

function loadTransactions() {
    const transactions = JSON.parse(localStorage.getItem('transactions')) || [];
    const tableBody = document.querySelector('#transaction-table tbody');
    tableBody.innerHTML = '';

    transactions.sort((a, b) => new Date(b.date) - new Date(a.date)).forEach(transaction => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${transaction.date}</td>
            <td>${transaction.mainCategory} > ${transaction.subCategory}</td>
            <td class="transaction-${transaction.type.toLowerCase()}">${formatCurrency(transaction.amount)}</td>
            <td>${transaction.type}</td>
            <td>${transaction.paymentMethod}</td>
            <td>${transaction.memo}</td>
            <td>
                <button onclick="editTransaction(${transaction.id})">編集</button>
                <button onclick="deleteTransaction(${transaction.id})">削除</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function editTransaction(id) {
    // 編集機能の実装（モーダルウィンドウを使用するなど）
    console.log('Edit transaction:', id);
}

function deleteTransaction(id) {
    if (confirm('この取引を削除してもよろしいですか？')) {
        const transactions = JSON.parse(localStorage.getItem('transactions')) || [];
        const updatedTransactions = transactions.filter(t => t.id !== id);
        localStorage.setItem('transactions', JSON.stringify(updatedTransactions));
        loadTransactions();
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
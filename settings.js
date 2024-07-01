document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    loadBudget();
    loadFixedCosts();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('add-category').addEventListener('click', addCategory);
    document.getElementById('set-budget').addEventListener('click', setBudget);
    document.getElementById('add-fixed-cost').addEventListener('click', addFixedCost);
}

function loadCategories() {
    const categories = JSON.parse(localStorage.getItem('categories')) || ['住宅', '食費', '交通費', '光熱費', '娯楽', 'その他'];
    const categoryList = document.getElementById('category-list');
    categoryList.innerHTML = '';
    categories.forEach(category => {
        const li = createCategoryElement(category);
        categoryList.appendChild(li);
    });
}

function createCategoryElement(category) {
    const li = document.createElement('li');
    li.className = 'category-item';
    li.innerHTML = `
        <span>${category}</span>
        <button class="delete-btn" data-category="${category}">削除</button>
    `;
    li.querySelector('.delete-btn').addEventListener('click', deleteCategory);
    return li;
}

function addCategory() {
    const newCategoryInput = document.getElementById('new-category');
    const category = newCategoryInput.value.trim();
    if (category) {
        const categories = JSON.parse(localStorage.getItem('categories')) || [];
        if (!categories.includes(category)) {
            categories.push(category);
            localStorage.setItem('categories', JSON.stringify(categories));
            const categoryList = document.getElementById('category-list');
            categoryList.appendChild(createCategoryElement(category));
            newCategoryInput.value = '';
        } else {
            alert('このカテゴリーは既に存在します。');
        }
    }
}

function deleteCategory(event) {
    const category = event.target.getAttribute('data-category');
    const categories = JSON.parse(localStorage.getItem('categories')) || [];
    const updatedCategories = categories.filter(c => c !== category);
    localStorage.setItem('categories', JSON.stringify(updatedCategories));
    event.target.closest('.category-item').remove();
}

function loadBudget() {
    const budget = localStorage.getItem('monthlyBudget') || 0;
    document.getElementById('current-budget').textContent = `現在の月間予算: ${formatCurrency(budget)}`;
}

function setBudget() {
    const budgetInput = document.getElementById('monthly-budget');
    const budget = budgetInput.value.trim();
    if (budget && !isNaN(budget)) {
        localStorage.setItem('monthlyBudget', budget);
        document.getElementById('current-budget').textContent = `現在の月間予算: ${formatCurrency(budget)}`;
        budgetInput.value = '';
    } else {
        alert('有効な数値を入力してください。');
    }
}

function loadFixedCosts() {
    const fixedCosts = JSON.parse(localStorage.getItem('fixedCosts')) || [];
    const fixedCostsList = document.getElementById('fixed-costs-list');
    const fixedCostCategory = document.getElementById('fixed-cost-category');
    
    fixedCostsList.innerHTML = '';
    fixedCostCategory.innerHTML = '<option value="">カテゴリーを選択</option>';
    
    const categories = JSON.parse(localStorage.getItem('categories')) || [];
    categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category;
        option.textContent = category;
        fixedCostCategory.appendChild(option);
    });

    fixedCosts.forEach(cost => {
        const li = createFixedCostElement(cost);
        fixedCostsList.appendChild(li);
    });
}

function createFixedCostElement(cost) {
    const li = document.createElement('li');
    li.className = 'fixed-cost-item';
    li.innerHTML = `
        <span class="category">${cost.category}</span>
        <span class="amount">${formatCurrency(cost.amount)}</span>
        <button class="delete-btn" data-category="${cost.category}">削除</button>
    `;
    li.querySelector('.delete-btn').addEventListener('click', deleteFixedCost);
    return li;
}

function addFixedCost() {
    const category = document.getElementById('fixed-cost-category').value;
    const amount = document.getElementById('fixed-cost-amount').value;
    if (category && amount && !isNaN(amount)) {
        const fixedCosts = JSON.parse(localStorage.getItem('fixedCosts')) || [];
        const existingIndex = fixedCosts.findIndex(cost => cost.category === category);
        if (existingIndex !== -1) {
            fixedCosts[existingIndex].amount = parseFloat(amount);
        } else {
            fixedCosts.push({ category, amount: parseFloat(amount) });
        }
        localStorage.setItem('fixedCosts', JSON.stringify(fixedCosts));
        loadFixedCosts();
        document.getElementById('fixed-cost-amount').value = '';
        document.getElementById('fixed-cost-category').value = '';
    } else {
        alert('有効なカテゴリーと金額を入力してください。');
    }
}

function deleteFixedCost(event) {
    const category = event.target.getAttribute('data-category');
    if (confirm(`${category}の固定費を削除してもよろしいですか？`)) {
        const fixedCosts = JSON.parse(localStorage.getItem('fixedCosts')) || [];
        const updatedCosts = fixedCosts.filter(cost => cost.category !== category);
        localStorage.setItem('fixedCosts', JSON.stringify(updatedCosts));
        loadFixedCosts();
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
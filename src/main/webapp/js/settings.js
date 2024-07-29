/**
 * 
 */
document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    loadPaymentMethods();
    loadBudget();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('add-main-category').addEventListener('click', addMainCategory);
    document.getElementById('add-payment-method').addEventListener('click', addPaymentMethod);
    document.getElementById('set-budget').addEventListener('click', setBudget);
}

function loadCategories() {
    fetch('/api/categories')
        .then(response => response.json())
        .then(categories => {
            const categoriesList = document.getElementById('categories-list');
            categoriesList.innerHTML = '';

            categories.forEach(category => {
                const categoryDiv = document.createElement('div');
                categoryDiv.className = 'category-item';
                categoryDiv.innerHTML = `
                    <span>${category.name}</span>
                    <button class="btn add-sub-category" data-main="${category.id}">副カテゴリー追加</button>
                    <button class="delete-btn" data-main="${category.id}">削除</button>
                `;
                
                if (category.subCategories.length > 0) {
                    const subList = document.createElement('ul');
                    subList.className = 'sub-category-list';
                    category.subCategories.forEach(subCategory => {
                        subList.innerHTML += `
                            <li>
                                ${subCategory.name}
                                <button class="delete-btn" data-main="${category.id}" data-sub="${subCategory.id}">削除</button>
                            </li>
                        `;
                    });
                    categoryDiv.appendChild(subList);
                }

                categoriesList.appendChild(categoryDiv);
            });

            setupCategoryEventListeners();
        })
        .catch(error => console.error('Error:', error));
}

function setupCategoryEventListeners() {
    document.querySelectorAll('.add-sub-category').forEach(btn => {
        btn.addEventListener('click', () => addSubCategory(btn.getAttribute('data-main')));
    });
    document.querySelectorAll('.delete-btn').forEach(btn => {
        if (btn.hasAttribute('data-sub')) {
            btn.addEventListener('click', () => deleteSubCategory(btn.getAttribute('data-main'), btn.getAttribute('data-sub')));
        } else {
            btn.addEventListener('click', () => deleteMainCategory(btn.getAttribute('data-main')));
        }
    });
}

function addMainCategory() {
    const newCategory = document.getElementById('new-main-category').value.trim();
    if (newCategory) {
        fetch('/api/categories', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: newCategory })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadCategories();
                document.getElementById('new-main-category').value = '';
            } else {
                alert('カテゴリーの追加に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function addSubCategory(mainCategoryId) {
    const subCategory = prompt('新しい副カテゴリーを入力してください:');
    if (subCategory && subCategory.trim()) {
        fetch(`/api/categories/${mainCategoryId}/subcategories`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: subCategory })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadCategories();
            } else {
                alert('副カテゴリーの追加に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function deleteMainCategory(categoryId) {
    if (confirm('このカテゴリーとそのすべての副カテゴリーを削除してもよろしいですか？')) {
        fetch(`/api/categories/${categoryId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadCategories();
            } else {
                alert('カテゴリーの削除に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function deleteSubCategory(mainCategoryId, subCategoryId) {
    if (confirm('この副カテゴリーを削除してもよろしいですか？')) {
        fetch(`/api/categories/${mainCategoryId}/subcategories/${subCategoryId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadCategories();
            } else {
                alert('副カテゴリーの削除に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function loadPaymentMethods() {
    fetch('/api/payment-methods')
        .then(response => response.json())
        .then(methods => {
            const paymentMethodsList = document.getElementById('payment-methods-list');
            paymentMethodsList.innerHTML = '';

            methods.forEach(method => {
                const li = document.createElement('li');
                li.className = 'payment-method-item';
                li.innerHTML = `
                    ${method.name}
                    <button class="delete-btn" data-method="${method.id}">削除</button>
                `;
                paymentMethodsList.appendChild(li);
            });

            setupPaymentMethodEventListeners();
        })
        .catch(error => console.error('Error:', error));
}

function setupPaymentMethodEventListeners() {
    document.querySelectorAll('#payment-methods-list .delete-btn').forEach(btn => {
        btn.addEventListener('click', () => deletePaymentMethod(btn.getAttribute('data-method')));
    });
}

function addPaymentMethod() {
    const newMethod = document.getElementById('new-payment-method').value.trim();
    if (newMethod) {
        fetch('/api/payment-methods', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name: newMethod })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadPaymentMethods();
                document.getElementById('new-payment-method').value = '';
            } else {
                alert('支払い方法の追加に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function deletePaymentMethod(methodId) {
    if (confirm('この支払い方法を削除してもよろしいですか？')) {
        fetch(`/api/payment-methods/${methodId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadPaymentMethods();
            } else {
                alert('支払い方法の削除に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

function loadBudget() {
    fetch('/api/budget')
        .then(response => response.json())
        .then(data => {
            document.getElementById('current-budget').textContent = `現在の月間予算: ${formatCurrency(data.budget)}`;
        })
        .catch(error => console.error('Error:', error));
}

function setBudget() {
    const budget = document.getElementById('monthly-budget').value;
    if (budget && !isNaN(budget)) {
        fetch('/api/budget', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ budget: parseFloat(budget) })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadBudget();
                document.getElementById('monthly-budget').value = '';
            } else {
                alert('予算の設定に失敗しました。');
            }
        })
        .catch(error => console.error('Error:', error));
    } else {
        alert('有効な数値を入力してください。');
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('ja-JP', { style: 'currency', currency: 'JPY' }).format(amount);
}
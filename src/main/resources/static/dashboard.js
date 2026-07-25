const token = localStorage.getItem('token');
if (!token) window.location.href = 'index.html';

document.getElementById('welcomeUser').innerText = 'Hi, ' + localStorage.getItem('username');

function authHeaders() {
  return { 'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json' };
}

function logout() {
  localStorage.clear();
  window.location.href = 'index.html';
}

async function saveExpense() {
  const id = document.getElementById('editingId').value;
  const payload = {
    title: document.getElementById('title').value,
    amount: parseFloat(document.getElementById('amount').value),
    currency: document.getElementById('currency').value,
    category: document.getElementById('category').value,
    date: document.getElementById('date').value
  };

  const url = id ? `/api/expenses/${id}` : '/api/expenses';
  const method = id ? 'PUT' : 'POST';

  const res = await fetch(url, { method, headers: authHeaders(), body: JSON.stringify(payload) });
  if (res.ok) {
    resetForm();
    loadAll();
  } else {
    alert('Failed to save expense');
  }
}

function resetForm() {
  document.getElementById('editingId').value = '';
  document.getElementById('formTitle').innerText = 'Add Expense';
  ['title', 'amount', 'category', 'date'].forEach(id => document.getElementById(id).value = '');
}

function editExpense(exp) {
  document.getElementById('editingId').value = exp.id;
  document.getElementById('formTitle').innerText = 'Edit Expense';
  document.getElementById('title').value = exp.title;
  document.getElementById('amount').value = exp.amount;
  document.getElementById('currency').value = exp.currency;
  document.getElementById('category').value = exp.category;
  document.getElementById('date').value = exp.date;
  window.scrollTo(0, 0);
}

async function deleteExpense(id) {
  if (!confirm('Delete this expense?')) return;
  await fetch(`/api/expenses/${id}`, { method: 'DELETE', headers: authHeaders() });
  loadAll();
}

async function loadAll() {
  const res = await fetch('/api/expenses', { headers: authHeaders() });
  const data = await res.json();
  renderExpenses(data);
  loadSummary();
}

async function loadByMonth() {
  const year = document.getElementById('filterYear').value;
  const month = document.getElementById('filterMonth').value;
  const res = await fetch(`/api/expenses/month?year=${year}&month=${month}`, { headers: authHeaders() });
  const data = await res.json();
  renderExpenses(data);
}

function renderExpenses(data) {
  const body = document.getElementById('expenseTableBody');
  body.innerHTML = '';
  data.forEach(exp => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${exp.title}</td>
      <td>${exp.amount} ${exp.currency}</td>
      <td>${exp.category}</td>
      <td>${exp.date}</td>
      <td>
        <button class="btn btn-sm btn-outline-primary" onclick='editExpense(${JSON.stringify(exp)})'>Edit</button>
        <button class="btn btn-sm btn-outline-danger" onclick="deleteExpense(${exp.id})">Delete</button>
      </td>`;
    body.appendChild(row);
  });
}

async function loadSummary() {
  const res = await fetch('/api/expenses/summary', { headers: authHeaders() });
  const data = await res.json();
  document.getElementById('totalUSD').innerText = data.totalUSD.toFixed(2);
  renderChart(data.byCategory);
}

loadAll();
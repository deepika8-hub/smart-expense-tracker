document.querySelectorAll('#authTabs .nav-link').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('#authTabs .nav-link').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    const tab = btn.dataset.tab;
    document.getElementById('loginForm').classList.toggle('d-none', tab !== 'login');
    document.getElementById('registerForm').classList.toggle('d-none', tab !== 'register');
  });
});

async function login() {
  const username = document.getElementById('loginUsername').value;
  const password = document.getElementById('loginPassword').value;
  const res = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  if (!res.ok) {
    document.getElementById('authError').innerText = 'Login failed. Check your credentials.';
    return;
  }
  const data = await res.json();
  localStorage.setItem('token', data.token);
  localStorage.setItem('username', data.username);
  window.location.href = 'dashboard.html';
}

async function register() {
  const username = document.getElementById('regUsername').value;
  const email = document.getElementById('regEmail').value;
  const password = document.getElementById('regPassword').value;
  const res = await fetch('/api/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, email, password })
  });
  if (!res.ok) {
    const err = await res.json();
    document.getElementById('authError').innerText = err.error || 'Registration failed.';
    return;
  }
  const data = await res.json();
  localStorage.setItem('token', data.token);
  localStorage.setItem('username', data.username);
  window.location.href = 'dashboard.html';
}
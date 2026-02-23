document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        // If already logged in, redirect to index
        if (localStorage.getItem('token')) {
            window.location.href = '/index.html';
        }

        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const correo = document.getElementById('correo').value;
            const contrasena = document.getElementById('contrasena').value;
            const errorMsg = document.getElementById('errorMsg');

            try {
                const response = await fetch('/api/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ correo, contrasena })
                });

                const data = await response.json();

                if (response.ok && data.success) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('usuario', JSON.stringify(data.user));
                    window.location.href = '/index.html';
                } else {
                    errorMsg.textContent = data.message || 'Credenciales inválidas';
                    errorMsg.style.display = 'block';
                }
            } catch (err) {
                errorMsg.textContent = 'Error de conexión con el servidor.';
                errorMsg.style.display = 'block';
            }
        });
    }
});

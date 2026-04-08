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

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btnRegister = document.getElementById('btnRegister');
            const errorMsg = document.getElementById('regErrorMsg');

            const nombre = document.getElementById('nombre').value;
            const username = document.getElementById('username').value;
            const correo = document.getElementById('correo').value;
            const telefono = document.getElementById('telefono').value;
            const fechaNacimiento = document.getElementById('fechaNacimiento').value;
            const sexo = document.getElementById('sexo').value;
            const contrasena = document.getElementById('contrasena').value;

            btnRegister.disabled = true;
            btnRegister.textContent = 'Procesando...';
            errorMsg.style.display = 'none';

            try {
                const response = await fetch('/api/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ nombre, username, correo, telefono, fechaNacimiento, sexo, contrasena })
                });

                if (response.ok) {
                    document.getElementById('successModal').style.display = 'flex';
                } else {
                    const errorText = await response.text();
                    errorMsg.textContent = errorText || 'Error al registrar la cuenta.';
                    errorMsg.style.display = 'block';
                }
            } catch (err) {
                errorMsg.textContent = 'Error de conexión con el servidor.';
                errorMsg.style.display = 'block';
            } finally {
                btnRegister.disabled = false;
                btnRegister.textContent = 'Registrarse';
            }
        });
    }

    const recuperarForm = document.getElementById('recuperarForm');
    if (recuperarForm) {
        recuperarForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const btn = document.getElementById('btnRecuperar');
            const errorMsg = document.getElementById('recErrorMsg');
            const successMsg = document.getElementById('recSuccessMsg');

            const correo = document.getElementById('rec_correo').value;
            const telefono = document.getElementById('rec_telefono').value;
            const nuevaContrasena = document.getElementById('rec_nueva_contrasena').value;

            btn.disabled = true;
            btn.textContent = 'Procesando...';
            errorMsg.style.display = 'none';
            successMsg.style.display = 'none';

            try {
                const response = await fetch('/api/recuperar-contrasena', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ correo, telefono, nuevaContrasena })
                });

                const text = await response.text();

                if (response.ok) {
                    successMsg.style.display = 'block';
                    setTimeout(() => {
                        window.location.href = 'login.html';
                    }, 2000);
                } else {
                    errorMsg.textContent = text || 'Hubo un problema al restablecer la contraseña.';
                    errorMsg.style.display = 'block';
                    btn.disabled = false;
                    btn.textContent = 'Restablecer Contraseña';
                }
            } catch (err) {
                errorMsg.textContent = 'Error de conexión con el servidor.';
                errorMsg.style.display = 'block';
                btn.disabled = false;
                btn.textContent = 'Restablecer Contraseña';
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    const usuarioRaw = localStorage.getItem('usuario');

    if (!token || !usuarioRaw) {
        window.location.href = '/login.html';
        return;
    }

    const usuario = JSON.parse(usuarioRaw);
    document.getElementById('userName').textContent = usuario.nombre;
    document.getElementById('userRole').textContent = usuario.rol;

    // Show add task button only for ADMIN or HOST
    if (usuario.rol === 'ADMIN' || usuario.rol === 'HOST') {
        document.getElementById('btnNuevaTarea').style.display = 'inline-block';
        document.getElementById('btnGestionarUsuarios').style.display = 'inline-block';
    }

    document.getElementById('logoutBtn').addEventListener('click', () => {
        localStorage.clear();
        window.location.href = '/login.html';
    });

    // Modal Logic
    const modal = document.getElementById('tareaModal');
    const btnNew = document.getElementById('btnNuevaTarea');
    const btnClose = document.getElementById('closeModal');
    const selectAsignado = document.getElementById('tAsignado');

    async function cargarEmpleados() {
        if (!selectAsignado) return;
        try {
            const res = await fetch('/api/usuarios', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (res.ok) {
                const usuarios = await res.json();
                selectAsignado.innerHTML = '<option value="">Sin asignar</option>';
                usuarios.forEach(u => {
                    if (u.rol === 'EMPLEADO' || u.rol === 'ADMIN' || u.rol === 'HOST') {
                        const opt = document.createElement('option');
                        opt.value = u.id;
                        opt.textContent = `${u.nombre} (${u.rol})`;
                        selectAsignado.appendChild(opt);
                    }
                });
            }
        } catch (err) {
            console.error('Error cargando empleados', err);
        }
    }

    if (btnNew) {
        btnNew.addEventListener('click', () => {
            modal.style.display = 'flex';
            cargarEmpleados();
        });
    }
    btnClose.addEventListener('click', () => modal.style.display = 'none');

    // User Management Modal
    const usuariosModal = document.getElementById('usuariosModal');
    const btnGestionarUsuarios = document.getElementById('btnGestionarUsuarios');
    const closeUsuariosModal = document.getElementById('closeUsuariosModal');

    if (btnGestionarUsuarios) {
        btnGestionarUsuarios.addEventListener('click', () => {
            usuariosModal.style.display = 'flex';
            cargarTablaUsuarios();
        });
    }
    if (closeUsuariosModal) {
        closeUsuariosModal.addEventListener('click', () => {
            usuariosModal.style.display = 'none';
        });
    }

    async function cargarTablaUsuarios() {
        try {
            const res = await fetch('/api/usuarios', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (res.ok) {
                const usuarios = await res.json();
                const tbody = document.getElementById('usuariosTableBody');
                tbody.innerHTML = '';

                usuarios.forEach(u => {
                    // Prevenir que un ADMIN cambie rol de un HOST (seguridad en UI)
                    const disableHostSelect = (usuario.rol === 'ADMIN' && u.rol === 'HOST') ? 'disabled' : '';

                    const isHost = u.rol === 'HOST' ? 'selected' : '';
                    const isAdmin = u.rol === 'ADMIN' ? 'selected' : '';
                    const isEmpleado = u.rol === 'EMPLEADO' ? 'selected' : '';
                    const isUsuario = u.rol === 'USUARIO' ? 'selected' : '';

                    const tr = document.createElement('tr');
                    tr.style.borderBottom = '1px solid rgba(255,255,255,0.05)';
                    tr.innerHTML = `
                        <td style="padding: 10px;">${u.nombre}</td>
                        <td style="padding: 10px; font-size:12px; color:var(--text-muted);">${u.correo}</td>
                        <td style="padding: 10px;">
                            <select id="roleSelect_${u.id}" ${disableHostSelect} style="padding: 5px; background: rgba(0,0,0,0.3); color: white; border: 1px solid rgba(255,255,255,0.2); border-radius: 4px;">
                                ${(usuario.rol === 'HOST') ? `<option value="HOST" ${isHost}>HOST</option>` : ''}
                                <option value="ADMIN" ${isAdmin}>ADMIN</option>
                                <option value="EMPLEADO" ${isEmpleado}>EMPLEADO</option>
                                <option value="USUARIO" ${isUsuario}>USUARIO</option>
                            </select>
                        </td>
                        <td style="padding: 10px;">
                            <button onclick="window.actualizarRol(${u.id})" class="btn-primary" style="padding: 5px 10px; font-size: 12px; min-width: auto;" ${disableHostSelect}>Guardar</button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }
        } catch (err) {
            console.error('Error cargando usuarios para gestión', err);
        }
    }

    window.actualizarRol = async function (usuarioId) {
        const select = document.getElementById(`roleSelect_${usuarioId}`);
        const nuevoRol = select.value;
        const btn = select.parentElement.nextElementSibling.querySelector('button');

        btn.textContent = '...';
        btn.disabled = true;

        try {
            const res = await fetch(`/api/usuarios/${usuarioId}/rol`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ rol: nuevoRol })
            });

            if (res.ok) {
                btn.textContent = 'OK';
                btn.style.background = 'var(--success)';
                btn.style.borderColor = 'var(--success)';
                setTimeout(() => {
                    btn.textContent = 'Guardar';
                    btn.style.background = 'var(--primary)';
                    btn.style.borderColor = 'var(--primary)';
                    btn.disabled = false;
                }, 2000);
            } else {
                alert('No se pudo cambiar el rol.');
                btn.textContent = 'Guardar';
                btn.disabled = false;
            }
        } catch (err) {
            console.error(err);
            alert('Error de conexión.');
            btn.textContent = 'Guardar';
            btn.disabled = false;
        }
    };

    // Load Tasks
    cargarTareas();

    // Create Task
    document.getElementById('tareaForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const titulo = document.getElementById('tTitulo').value;
        const descripcion = document.getElementById('tDesc').value;
        const urgencia = document.getElementById('tUrgencia').value;
        const asignadoId = document.getElementById('tAsignado').value;
        const bodyObj = {
            titulo,
            descripcion,
            urgencia,
            estado: 'PENDIENTE',
            asignadoA: asignadoId ? { id: parseInt(asignadoId) } : null
        };

        try {
            const res = await fetch('/api/tareas', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(bodyObj)
            });

            if (res.ok) {
                modal.style.display = 'none';
                e.target.reset();
                cargarTareas();
            } else {
                alert('Error al crear la tarea');
            }
        } catch (err) {
            console.error(err);
        }
    });

    async function cargarTareas() {
        try {
            const res = await fetch('/api/tareas', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (!res.ok) {
                if (res.status === 401 || res.status === 403) {
                    localStorage.clear();
                    window.location.href = '/login.html';
                }
                return;
            }
            const tareas = await res.json();
            renderTareas(tareas);
        } catch (err) {
            console.error(err);
        }
    }

    function renderTareas(tareas) {
        document.querySelectorAll('.task-list').forEach(list => list.innerHTML = '');

        let counts = { PENDIENTE: 0, EN_PROCESO: 0, HECHO: 0 };

        tareas.forEach(tarea => {
            const estado = tarea.estado || 'PENDIENTE';
            counts[estado]++;

            const list = document.querySelector(`.kanban-column[data-status="${estado}"] .task-list`);
            if (list) {
                const card = document.createElement('div');
                card.className = 'task-card';
                card.draggable = true;
                card.dataset.id = tarea.id;

                const asignadoText = tarea.asignadoA ? `Asig: ${tarea.asignadoA.nombre}` : 'Sin asignar';

                card.innerHTML = `
                    <div class="task-title">${tarea.titulo}</div>
                    <div class="task-desc">${tarea.descripcion}</div>
                    <div class="task-meta">
                        <span class="badge urgency-${tarea.urgencia}">${tarea.urgencia}</span>
                    </div>
                    <div class="task-meta" style="margin-top: 6px;">
                        <span style="color:var(--text-muted); font-size: 11px;">ID: ${tarea.id} | ${asignadoText}</span>
                    </div>
                `;

                // Drag Events
                card.addEventListener('dragstart', (e) => {
                    card.classList.add('dragging');
                    e.dataTransfer.setData('text/plain', tarea.id);
                });

                card.addEventListener('dragend', () => {
                    card.classList.remove('dragging');
                });

                list.appendChild(card);
            }
        });

        // Update counts
        document.querySelector('#col-pendiente .count').textContent = counts.PENDIENTE;
        document.querySelector('#col-proceso .count').textContent = counts.EN_PROCESO;
        document.querySelector('#col-hecho .count').textContent = counts.HECHO;
    }

    // Drop logic
    const columns = document.querySelectorAll('.kanban-column');
    columns.forEach(col => {
        col.addEventListener('dragover', (e) => {
            e.preventDefault();
            const afterElement = getDragAfterElement(col.querySelector('.task-list'), e.clientY);
            const draggable = document.querySelector('.dragging');
            const list = col.querySelector('.task-list');
            if (afterElement == null) {
                list.appendChild(draggable);
            } else {
                list.insertBefore(draggable, afterElement);
            }
        });

        col.addEventListener('drop', async (e) => {
            e.preventDefault();
            const taskId = e.dataTransfer.getData('text/plain');
            const newStatus = col.dataset.status;

            // Update on server
            try {
                const res = await fetch(`/api/tareas/${taskId}/estado`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(newStatus) // Sending string mapped to enum
                });
                if (!res.ok) {
                    cargarTareas(); // Revert on UI
                    alert('Error al actualizar estado');
                } else {
                    cargarTareas(); // Re-render to update counts properly
                }
            } catch (err) {
                console.error(err);
                cargarTareas();
            }
        });
    });

    function getDragAfterElement(container, y) {
        const draggableElements = [...container.querySelectorAll('.task-card:not(.dragging)')];

        return draggableElements.reduce((closest, child) => {
            const box = child.getBoundingClientRect();
            const offset = y - box.top - box.height / 2;
            if (offset < 0 && offset > closest.offset) {
                return { offset: offset, element: child };
            } else {
                return closest;
            }
        }, { offset: Number.NEGATIVE_INFINITY }).element;
    }
});

window.onload = init;

const connectionStatus = document.querySelector('#connection-status');
const listaDisponibles = document.querySelector('#lista-disponibles');
const listaSeguimiento = document.querySelector('#lista-seguimiento');

let socket;

function init() {
    connectWebSocket();
}

function connectWebSocket() {
    socket = new WebSocket("ws://localhost:8080/websocketexample/stock-updates");
    
    socket.onopen = function() {
        console.log("Conexión WebSocket establecida");
        connectionStatus.textContent = "Conectado";
        connectionStatus.style.color = "#5eb85e";
    };
    
    socket.onmessage = onMessage;
    
    socket.onerror = function(error) {
        console.error("Error en WebSocket:", error);
        connectionStatus.textContent = "Error de conexión";
        connectionStatus.style.color = "#db524d";
    };
    
    socket.onclose = function() {
        console.log("Conexión WebSocket cerrada");
        connectionStatus.textContent = "Desconectado";
        connectionStatus.style.color = "#c8cccf";
    };
}

function onMessage(event) {
    const message = JSON.parse(event.data);
    console.log("Mensaje recibido:", message);
    
    if (message.action === "empresa-disponible") {
        agregarEmpresaDisponible(message);
    } else if (message.action === "actualizar-precio") {
        actualizarPrecioEmpresa(message);
    } else if (message.action === "seguir") {
        moverAEmpresaSeguimiento(message);
    } else if (message.action === "dejar-seguir") {
        moverAEmpresaDisponible(message);
    }
}

function moverAEmpresaSeguimiento(message) {
    const empresaDisponible = document.getElementById('disponible-' + message.id);
    if (empresaDisponible) {
        empresaDisponible.remove();
    }
    
    const empresaSeguimiento = document.getElementById('seguimiento-' + message.id);
    if (!empresaSeguimiento) {
        agregarEmpresaSeguimiento(message);
    }
}

function moverAEmpresaDisponible(message) {
    const empresaSeguimiento = document.getElementById('seguimiento-' + message.id);
    if (empresaSeguimiento) {
        empresaSeguimiento.remove();
        
        if (listaSeguimiento.children.length === 0) {
            listaSeguimiento.innerHTML = '<p class="mensaje-vacio">No hay empresas en seguimiento. Selecciona una empresa de la lista superior.</p>';
        }
    }
    
    const empresaDisponible = document.getElementById('disponible-' + message.id);
    if (!empresaDisponible) {
        const empresaData = {
            id: message.id,
            nombreEmpresa: message.nombreEmpresa,
            icono: message.icono,
            enSeguimiento: false
        };
        agregarEmpresaDisponible(empresaData);
    }
}

function agregarEmpresaDisponible(message) {
    if (document.getElementById('disponible-' + message.id) || 
        document.getElementById('seguimiento-' + message.id) || 
        message.enSeguimiento) {
        return;
    }
    
    const empresaDiv = document.createElement("div");
    empresaDiv.setAttribute("id", "disponible-" + message.id);
    empresaDiv.setAttribute("class", "empresa-card disponible");
    empresaDiv.setAttribute("data-empresa-id", message.id);
    
    empresaDiv.innerHTML = `
        <div class="empresa-nombre">${message.nombreEmpresa}</div>
        <div class="empresa-info">
            <span><b>Icono:</b> ${message.icono || '📊'}</span>
            <span><b>ID:</b> ${message.id}</span>
        </div>
        <div class="empresa-acciones">
            <button class="btn-seguir" onclick="seguirEmpresa(${message.id})">Seguir</button>
        </div>
    `;
    
    listaDisponibles.appendChild(empresaDiv);
}

function seguirEmpresa(empresaId) {
    console.log("Seguir empresa:", empresaId);
    
    const message = {
        action: "seguir",
        empresaId: empresaId
    };
    
    socket.send(JSON.stringify(message));
}

function dejarSeguirEmpresa(empresaId) {
    console.log("Dejar de seguir empresa:", empresaId);
    
    const message = {
        action: "dejar-seguir",
        empresaId: empresaId
    };
    
    socket.send(JSON.stringify(message));
}

function agregarEmpresaSeguimiento(message) {
    const mensajeVacio = listaSeguimiento.querySelector('.mensaje-vacio');
    if (mensajeVacio) {
        mensajeVacio.remove();
    }
    
    const empresaDiv = document.createElement("div");
    empresaDiv.setAttribute("id", "seguimiento-" + message.id);
    empresaDiv.setAttribute("class", "empresa-card seguimiento");
    empresaDiv.setAttribute("data-empresa-id", message.id);
    
    const ahora = new Date();
    const horaActualizacion = ahora.getHours().toString().padStart(2, '0') + ':' + 
                              ahora.getMinutes().toString().padStart(2, '0') + ':' + 
                              ahora.getSeconds().toString().padStart(2, '0');
    
    empresaDiv.innerHTML = `
        <div class="empresa-nombre">${message.nombreEmpresa}</div>
        <div class="empresa-info">
            <span><b>Icono:</b> ${message.icono || '📊'}</span>
            <span class="precio-actual"><b>Precio actual:</b> <span class="precio">${(message.precioAccion || 0).toFixed(2)} €</span></span>
            <span class="ultima-actualizacion"><b>Última actualización:</b> ${message.ultimaActualizacion || horaActualizacion}</span>
        </div>
        <div class="empresa-acciones">
            <button class="btn-dejar-seguir" onclick="dejarSeguirEmpresa(${message.id})">Dejar de seguir</button>
        </div>
    `;
    
    listaSeguimiento.appendChild(empresaDiv);
}

function actualizarPrecioEmpresa(message) {
    const empresaSeguimiento = document.getElementById('seguimiento-' + message.id);
    
    if (empresaSeguimiento) {
        const precioElement = empresaSeguimiento.querySelector('.precio');
        const precioAnterior = parseFloat(precioElement.textContent);
        const precioNuevo = message.precioAccion;
        
        precioElement.textContent = precioNuevo.toFixed(2) + ' €';
        
        const precioActualSpan = empresaSeguimiento.querySelector('.precio-actual');
        precioActualSpan.classList.remove('precio-sube', 'precio-baja');
        
        if (precioNuevo > precioAnterior) {
            precioActualSpan.classList.add('precio-sube');
        } else if (precioNuevo < precioAnterior) {
            precioActualSpan.classList.add('precio-baja');
        }
        
        empresaSeguimiento.querySelector('.ultima-actualizacion').innerHTML = 
            `<b>Última actualización:</b> ${message.ultimaActualizacion}`;
        
        setTimeout(() => {
            precioActualSpan.classList.remove('precio-sube', 'precio-baja');
        }, 1000);
    }
}


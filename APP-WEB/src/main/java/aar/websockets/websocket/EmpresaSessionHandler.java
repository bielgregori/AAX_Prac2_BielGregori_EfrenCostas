package aar.websockets.websocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.websocket.Session;
import jakarta.json.JsonObject;
import jakarta.json.spi.JsonProvider;

import aar.websockets.model.Empresa;
import aar.websockets.services.EmpresaApiService;

public class EmpresaSessionHandler {

    private static final EmpresaSessionHandler instance = new EmpresaSessionHandler();
    
    private final Set<Session> sessions = new HashSet<>();
    private final Map<Long, Empresa> empresasDisponibles = new HashMap<>();
    private final Set<Long> empresasEnSeguimiento = new HashSet<>();
    
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int INTERVALO_ACTUALIZACION = 5;

    private EmpresaSessionHandler() {
        cargarEmpresasDesdeAPI();
        iniciarActualizacionPeriodica();
    }

    public static EmpresaSessionHandler getInstance() {
        return instance;
    }

    private void cargarEmpresasDesdeAPI() {
        List<Empresa> empresas = EmpresaApiService.obtenerTodasLasEmpresas();
        for (Empresa empresa : empresas) {
            empresasDisponibles.put(empresa.getId(), empresa);
        }
        System.out.println("Cargadas " + empresas.size() + " empresas desde la API");
    }

    private void iniciarActualizacionPeriodica() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                actualizarPreciosEmpresasEnSeguimiento();
            } catch (Exception e) {
                Logger.getLogger(EmpresaSessionHandler.class.getName())
                    .log(Level.SEVERE, "Error en actualización periódica", e);
            }
        }, INTERVALO_ACTUALIZACION, INTERVALO_ACTUALIZACION, TimeUnit.SECONDS);
        
        System.out.println("Actualizador periódico iniciado (cada " + INTERVALO_ACTUALIZACION + " segundos)");
    }

    private void actualizarPreciosEmpresasEnSeguimiento() {
        if (empresasEnSeguimiento.isEmpty() || sessions.isEmpty()) {
            return;
        }

        for (Long empresaId : empresasEnSeguimiento) {
            Empresa empresaActualizada = EmpresaApiService.obtenerEmpresaPorId(empresaId);
            
            if (empresaActualizada != null) {
                Empresa empresaLocal = empresasDisponibles.get(empresaId);
                
                if (empresaLocal != null) {
                    empresaLocal.setPrecioAccion(empresaActualizada.getPrecioAccion());
                    empresaLocal.actualizarFecha();
                    
                    
                    enviarActualizacionEmpresa(empresaLocal);
                }
            }
        }
    }

    public void addSession(Session session) {
        sessions.add(session);
        
        enviarListadoEmpresas(session);
        
        for (Long empresaId : empresasEnSeguimiento) {
            Empresa empresa = empresasDisponibles.get(empresaId);
            if (empresa != null) {
                empresa.setEnSeguimiento(true);
                enviarEmpresaASesion(session, empresa, "seguir");
            }
        }
    }


    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void seguirEmpresa(Long empresaId) {
        if (!empresasDisponibles.containsKey(empresaId)) {
            return;
        }

        if (empresasEnSeguimiento.add(empresaId)) {
            Empresa empresa = empresasDisponibles.get(empresaId);
            empresa.setEnSeguimiento(true);
            
            Empresa empresaActualizada = EmpresaApiService.obtenerEmpresaPorId(empresaId);
            if (empresaActualizada != null) {
                empresa.setPrecioAccion(empresaActualizada.getPrecioAccion());
                empresa.actualizarFecha();
            }
            
            JsonProvider provider = JsonProvider.provider();
            JsonObject mensaje = provider.createObjectBuilder()
                .add("action", "seguir")
                .add("id", empresa.getId())
                .add("nombreEmpresa", empresa.getNombreEmpresa())
                .add("icono", empresa.getIcono() != null ? empresa.getIcono() : "")
                .add("precioAccion", empresa.getPrecioAccion())
                .add("ultimaActualizacion", empresa.getUltimaActualizacion())
                .build();
            
            sendToAllConnectedSessions(mensaje);
            
            System.out.println("Empresa " + empresa.getNombreEmpresa() + " añadida al seguimiento para TODOS los clientes");
        }
    }

    public void dejarDeSeguirEmpresa(Long empresaId) {
        if (empresasEnSeguimiento.remove(empresaId)) {
            Empresa empresa = empresasDisponibles.get(empresaId);
            if (empresa != null) {
                empresa.setEnSeguimiento(false);
                
                JsonProvider provider = JsonProvider.provider();
                JsonObject mensaje = provider.createObjectBuilder()
                    .add("action", "dejar-seguir")
                    .add("id", empresaId)
                    .add("nombreEmpresa", empresa.getNombreEmpresa())
                    .add("icono", empresa.getIcono() != null ? empresa.getIcono() : "")
                    .build();
                
                sendToAllConnectedSessions(mensaje);
                
                System.out.println("Empresa " + empresa.getNombreEmpresa() + " eliminada del seguimiento para TODOS los clientes");
            }
        }
    }

    private void enviarListadoEmpresas(Session session) {
        for (Empresa empresa : empresasDisponibles.values()) {
            if (!empresasEnSeguimiento.contains(empresa.getId())) {
                JsonProvider provider = JsonProvider.provider();
                JsonObject mensaje = provider.createObjectBuilder()
                    .add("action", "empresa-disponible")
                    .add("id", empresa.getId())
                    .add("nombreEmpresa", empresa.getNombreEmpresa())
                    .add("icono", empresa.getIcono() != null ? empresa.getIcono() : "")
                    .add("enSeguimiento", false)
                    .build();
                
                sendToSession(session, mensaje);
            }
        }
    }

    private void enviarEmpresaASesion(Session session, Empresa empresa, String action) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject mensaje = provider.createObjectBuilder()
            .add("action", action)
            .add("id", empresa.getId())
            .add("nombreEmpresa", empresa.getNombreEmpresa())
            .add("icono", empresa.getIcono() != null ? empresa.getIcono() : "")
            .add("precioAccion", empresa.getPrecioAccion())
            .add("ultimaActualizacion", empresa.getUltimaActualizacion())
            .build();
        
        sendToSession(session, mensaje);
    }

    private void enviarActualizacionEmpresa(Empresa empresa) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject mensaje = provider.createObjectBuilder()
            .add("action", "actualizar-precio")
            .add("id", empresa.getId())
            .add("precioAccion", empresa.getPrecioAccion())
            .add("ultimaActualizacion", empresa.getUltimaActualizacion())
            .build();
        
        sendToAllConnectedSessions(mensaje);
    }

    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message.toString());
            }
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(EmpresaSessionHandler.class.getName())
                .log(Level.SEVERE, "Error enviando mensaje a sesión", ex);
        }
    }
}
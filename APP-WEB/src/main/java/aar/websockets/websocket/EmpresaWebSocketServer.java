package aar.websockets.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonObject;

@ServerEndpoint("/stock-updates")
public class EmpresaWebSocketServer {
    
    private final EmpresaSessionHandler sessionHandler = EmpresaSessionHandler.getInstance();
    
    public EmpresaWebSocketServer() {
        System.out.println("EmpresaWebSocketServer loaded: " + this.getClass());
    }
    
    @OnOpen
    public void onOpen(Session session) {
        sessionHandler.addSession(session);
        System.out.println("Cliente conectado, sesión activa: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {   
        sessionHandler.removeSession(session);
        System.out.println("Cliente desconectado, sesión eliminada: " + session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(EmpresaWebSocketServer.class.getName())
                .log(Level.SEVERE, "Error en WebSocket", error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            String action = jsonMessage.getString("action");

            if ("seguir".equals(action)) {
                Long empresaId = jsonMessage.getJsonNumber("empresaId").longValue();
                sessionHandler.seguirEmpresa(empresaId);
                System.out.println("Cliente solicita seguir empresa: " + empresaId);
            }

            if ("dejar-seguir".equals(action)) {
                Long empresaId = jsonMessage.getJsonNumber("empresaId").longValue();
                sessionHandler.dejarDeSeguirEmpresa(empresaId);
                System.out.println("Cliente solicita dejar de seguir empresa: " + empresaId);
            }
        } catch (Exception e) {
            Logger.getLogger(EmpresaWebSocketServer.class.getName())
                    .log(Level.SEVERE, "Error procesando mensaje: " + message, e);
        }
    }
}
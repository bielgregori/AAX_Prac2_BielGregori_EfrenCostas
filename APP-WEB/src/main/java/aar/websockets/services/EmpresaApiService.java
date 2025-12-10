package aar.websockets.services;

import aar.websockets.model.Empresa;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EmpresaApiService {
    
    private static final String API_BASE_URL = "http://localhost:8080/restwsexample/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Logger logger = Logger.getLogger(EmpresaApiService.class.getName());

    /**
     * Obtiene todas las empresas de la API REST
     */
    public static List<Empresa> obtenerTodasLasEmpresas() {
        List<Empresa> empresas = new ArrayList<>();
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/empresa"))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                empresas = parsearEmpresas(response.body());
            } else {
                logger.log(Level.WARNING, "Error al obtener empresas. Status: " + response.statusCode());
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al conectar con la API REST", e);
        }
        
        return empresas;
    }

    /**
     * Obtiene una empresa específica por ID
     */
    public static Empresa obtenerEmpresaPorId(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/empresa/" + id))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parsearEmpresa(response.body());
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener empresa " + id, e);
        }
        
        return null;
    }

    /**
     * Parsea el JSON de una lista de empresas
     */
    private static List<Empresa> parsearEmpresas(String jsonString) {
        List<Empresa> empresas = new ArrayList<>();
        
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            JsonArray jsonArray = reader.readArray();
            
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonEmpresa = jsonArray.getJsonObject(i);
                empresas.add(crearEmpresaDesdeJson(jsonEmpresa));
            }
        }
        
        return empresas;
    }

    /**
     * Parsea el JSON de una empresa individual
     */
    private static Empresa parsearEmpresa(String jsonString) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            JsonObject jsonEmpresa = reader.readObject();
            return crearEmpresaDesdeJson(jsonEmpresa);
        }
    }

    /**
     * Crea un objeto Empresa desde un JsonObject
     */
    private static Empresa crearEmpresaDesdeJson(JsonObject jsonEmpresa) {
        Long id = (long) jsonEmpresa.getInt("id");
        String nombre = jsonEmpresa.getString("nombreEmpresa");
        String icono = jsonEmpresa.getString("icono", "");
        Double precio = jsonEmpresa.getJsonNumber("precioAccion").doubleValue();
        
        return new Empresa(id, nombre, icono, precio);
    }
}


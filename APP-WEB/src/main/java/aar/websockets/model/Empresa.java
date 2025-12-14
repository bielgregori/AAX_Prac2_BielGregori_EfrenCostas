package aar.websockets.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Empresa {

    private Long id;
    private String nombreEmpresa;
    private String simbolo;
    private Double precioAccion;
    private String ultimaActualizacion;
    private boolean enSeguimiento;

    public Empresa() {
    }

    public Empresa(Long id, String nombreEmpresa, String simbolo, Double precioAccion) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.simbolo = simbolo;
        this.precioAccion = precioAccion;
        this.enSeguimiento = false;
        actualizarFecha();
    }

    public void actualizarFecha() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.ultimaActualizacion = LocalDateTime.now().format(formatter);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public Double getPrecioAccion() {
        return precioAccion;
    }

    public void setPrecioAccion(Double precioAccion) {
        this.precioAccion = precioAccion;
        actualizarFecha();
    }

    public String getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(String ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public boolean isEnSeguimiento() {
        return enSeguimiento;
    }

    public void setEnSeguimiento(boolean enSeguimiento) {
        this.enSeguimiento = enSeguimiento;
    }
}

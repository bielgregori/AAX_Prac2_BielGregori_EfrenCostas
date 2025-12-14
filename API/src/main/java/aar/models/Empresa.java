package aar.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({ "id", "nombreEmpresa", "simbolo", "precioAccion", "ultimaActualizacion" })

@Entity
@Table(name = "`empresa`") // Escapado por si 'empresa' fuera reservado en H2
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombreEmpresa")
    private String nombreEmpresa;

    @Column(name = "simbolo")
    private String simbolo;   // Símbolo de la empresa

    @Column(name = "precioAccion")
    private Double precioAccion;

    @Column(name = "ultimaActualizacion")
    private String ultimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "bolsa_id")
    private Bolsa bolsa;

    public Empresa() {}

    public Empresa(String nombreEmpresa, String simbolo) {
        this.nombreEmpresa = nombreEmpresa;
        this.simbolo = simbolo;
        this.precioAccion = generarPrecioInicial();
    }

    public double generarPrecioInicial() {
        return ThreadLocalRandom.current().nextDouble(1,100);
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
        actualizarFechaHora();
    }

    public String getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(String ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    private void actualizarFechaHora() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.ultimaActualizacion = LocalDateTime.now().format(formatter);
    }

    public Bolsa getBolsa() {
        return bolsa;
    }

    public void setBolsa(Bolsa bolsa) {
        this.bolsa = bolsa;
    }
}

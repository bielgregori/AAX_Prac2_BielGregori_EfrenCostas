package aar.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.concurrent.ThreadLocalRandom;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({ "id", "nombreEmpresa", "icono", "precioAccion" })

@Entity
@Table(name = "`empresa`") // Escapado por si 'empresa' fuera reservado en H2
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombreEmpresa")
    private String nombreEmpresa;

    @Column(name = "icono")
    private String icono;   // URL del icono

    @Column(name = "precioAccion")
    private Double precioAccion;

    @ManyToOne
    @JoinColumn(name = "bolsa_id")
    private Bolsa bolsa;

    public Empresa() {}

    public Empresa(String nombreEmpresa, String icono) {
        this.nombreEmpresa = nombreEmpresa;
        this.icono = icono;
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

    public String getIcono() { 
        return icono; 
    }

    public void setIcono(String icono) { 
        this.icono = icono; 
    }

    public Double getPrecioAccion() { 
        return precioAccion; 
    }

    public void setPrecioAccion(Double precioAccion) { 
        this.precioAccion = precioAccion; 
    }


    public Bolsa getBolsa() {
        return bolsa;
    }

    public void setBolsa(Bolsa bolsa) {
        this.bolsa = bolsa;
    }
}

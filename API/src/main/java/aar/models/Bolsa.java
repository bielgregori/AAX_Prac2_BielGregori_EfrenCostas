package aar.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;

@JsonbPropertyOrder({ "id", "nombreBolsa", "empresas" })

@Entity
@Table(name = "`bolsa`") // From newer versions of H2, bolsa is a reserved word and needs to be escaped
public class Bolsa {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "nombreBolsa")
   private String nombreBolsa;

   @OneToMany(mappedBy = "bolsa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
   private List<Empresa> empresas;

   public Bolsa() {}

   public Bolsa(String nombreBolsa) {
      this.nombreBolsa = nombreBolsa;
      this.empresas = new ArrayList<>();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNombreBolsa() {
      return nombreBolsa;
   }

   public void setNombreBolsa(String nombreBolsa) {
      this.nombreBolsa = nombreBolsa;
   }

   @JsonbTransient
   public List<Empresa> getEmpresas() {
      return empresas;
   }

   public void agregarEmpresa(Empresa e){
      empresas.add(e);
      e.setBolsa(this);
   }
}

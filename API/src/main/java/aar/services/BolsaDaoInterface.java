package aar.services;
import aar.models.Bolsa;
import java.util.List;

public interface BolsaDaoInterface {
    void create(Bolsa bolsa);
    List<Bolsa> findAll();
    Bolsa findById(Long id);
    void update(Bolsa bolsa);
    void delete(Long id);
    void init();
}

package aar.services;
import aar.models.Empresa;
import java.util.List;

public interface EmpresaDaoInterface {
    void create(Empresa empresa);
    List<Empresa> findAll();
    Empresa findById(Long id);
    void update(Empresa empresa);
    void delete(Long id);
    void init();
}

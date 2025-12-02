package aar.services;

import java.util.List;

import aar.models.Empresa;

public class EmpresaService {

    private final EmpresaDaoInterface empresaDao;

    //inyeccion de dependencias
    public EmpresaService(EmpresaDaoInterface empresaDao) {
        this.empresaDao = empresaDao;
        empresaDao.init();
    }

    public void createEmpresa(Empresa empresa) {
        empresaDao.create(empresa);
    }

    public List<Empresa> getAllEmpresas() {
        return empresaDao.findAll();
    }

    public Empresa getEmpresaById(Long id) {
        return empresaDao.findById(id);
    }

    public void updateEmpresa(Empresa empresa) {
        empresaDao.update(empresa);
    }

    public void deleteEmpresa(Long id) {
        empresaDao.delete(id);
    }
}

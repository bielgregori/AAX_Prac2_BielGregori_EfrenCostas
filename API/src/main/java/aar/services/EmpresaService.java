package aar.services;

import java.util.List;

import aar.models.Empresa;
import aar.repository.EmpresaDaoImpl;

public class EmpresaService {

    private final EmpresaDaoInterface empresaDao;
    private static StockPriceUpdater priceUpdater;
    private static boolean isUpdaterStarted = false;

    //inyeccion de dependencias
    public EmpresaService(EmpresaDaoInterface empresaDao) {
        this.empresaDao = empresaDao;
        empresaDao.init();
        
        // Inicializar el actualizador de precios solo una vez
        if (!isUpdaterStarted && empresaDao instanceof EmpresaDaoImpl) {
            priceUpdater = new StockPriceUpdater((EmpresaDaoImpl) empresaDao);
            isUpdaterStarted = true;
        }
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

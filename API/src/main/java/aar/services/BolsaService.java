package aar.services;

import java.util.List;

import aar.models.Bolsa;

public class BolsaService {

    private final BolsaDaoInterface bolsaDao;

    public BolsaService(BolsaDaoInterface bolsaDao) {
        this.bolsaDao = bolsaDao;
        bolsaDao.init();
    }

    public void createBolsa(Bolsa bolsa) {
        bolsaDao.create(bolsa);
    }

    public List<Bolsa> getAllBolsas() {
        return bolsaDao.findAll();
    }

    public Bolsa getBolsaById(Long id) {
        return bolsaDao.findById(id);
    }

    public void updateBolsa(Bolsa bolsa) {
        bolsaDao.update(bolsa);
    }

    public void deleteBolsa(Long id) {
        bolsaDao.delete(id);
    }
}

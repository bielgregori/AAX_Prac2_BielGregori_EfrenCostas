package aar.repository;

import aar.models.Bolsa;
import aar.models.Empresa;
import aar.services.BolsaService;
import aar.services.EmpresaDaoInterface;
import aar.services.StockPriceUpdater;

import java.util.List;

public class EmpresaDaoImpl implements EmpresaDaoInterface{
    @Override
    public void create(Empresa empresa) {
        JpaExecutor.executeInTransaction(em -> {
            em.persist(empresa);
            return null;
        });
    }

    @Override
    public List<Empresa> findAll() {
        return JpaExecutor.execute(em ->
            em.createQuery("SELECT u FROM Empresa u", Empresa.class).getResultList()
        );
    }

    @Override
    public Empresa findById(Long id) {
        return JpaExecutor.execute(em ->
            em.find(Empresa.class, id)
        );
    }

    @Override
    public void update(Empresa empresa) {
        JpaExecutor.executeInTransaction(em -> {
            em.merge(empresa);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        JpaExecutor.executeInTransaction(em -> {
            Empresa empresa = em.find(Empresa.class, id);
            
            if (empresa != null) {
                Bolsa bolsa = empresa.getBolsa();
                if (bolsa != null) {
                    bolsa.getEmpresas().remove(empresa);
                    empresa.setBolsa(null);
                }
                em.remove(empresa);
            }
            return null;
        });
    }

    @Override
    public void init() {
        if (findAll().isEmpty()) {
            BolsaService bolsaService = new BolsaService(new BolsaDaoImpl());

            Bolsa bolsa1 = bolsaService.getBolsaById(1L);
            Bolsa bolsa2 = bolsaService.getBolsaById(2L);
            Bolsa bolsa3 = bolsaService.getBolsaById(3L);

            Empresa e1 = new Empresa("Netflix", "https://images.ctfassets.net/4cd45et68cgf/Rx83JoRDMkYNlMC9MKzcB/2b14d5a59fc3937afd3f03191e19502d/Netflix-Symbol.png?w=700&h=456");
            e1.setBolsa(bolsa1);
            bolsa1.agregarEmpresa(e1);
            create(e1);

            Empresa e2 = new Empresa("Spotify", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/19/Spotify_logo_without_text.svg/1024px-Spotify_logo_without_text.svg.png");
            e2.setBolsa(bolsa2);
            bolsa2.agregarEmpresa(e2);
            create(e2);

            Empresa e3 = new Empresa("Microsoft", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Microsoft_logo.svg/2048px-Microsoft_logo.svg.png");
            e3.setBolsa(bolsa3);
            bolsa3.agregarEmpresa(e3);
            create(e3);
        }
        new StockPriceUpdater(this);
    }
}

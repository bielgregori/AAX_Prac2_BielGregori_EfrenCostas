package aar.repository;

import aar.models.Bolsa;
import aar.models.Empresa;
import aar.services.BolsaService;
import aar.services.EmpresaDaoInterface;
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
            Bolsa bolsa4 = bolsaService.getBolsaById(4L);
            Bolsa bolsa5 = bolsaService.getBolsaById(5L);

            Empresa e1 = new Empresa("Netflix", "NFLX");
            e1.setBolsa(bolsa1);
            bolsa1.agregarEmpresa(e1);
            create(e1);

            Empresa e2 = new Empresa("Spotify", "SPOT");
            e2.setBolsa(bolsa2);
            bolsa2.agregarEmpresa(e2);
            create(e2);

            Empresa e3 = new Empresa("Microsoft", "MSFT");
            e3.setBolsa(bolsa3);
            bolsa3.agregarEmpresa(e3);
            create(e3);

            Empresa e4 = new Empresa("Apple", "AAPL");
            e4.setBolsa(bolsa1);
            bolsa1.agregarEmpresa(e4);
            create(e4);

            Empresa e5 = new Empresa("Amazon", "AMZN");
            e5.setBolsa(bolsa2);
            bolsa2.agregarEmpresa(e5);
            create(e5);

            Empresa e6 = new Empresa("Google", "GOOGL");
            e6.setBolsa(bolsa3);
            bolsa3.agregarEmpresa(e6);
            create(e6);

            Empresa e7 = new Empresa("Facebook", "META");
            e7.setBolsa(bolsa1);
            bolsa1.agregarEmpresa(e7);
            create(e7);

            Empresa e8 = new Empresa("Tesla", "TSLA");
            e8.setBolsa(bolsa2);
            bolsa2.agregarEmpresa(e8);
            create(e8);

            Empresa e9 = new Empresa("NVIDIA", "NVDA");
            e9.setBolsa(bolsa4);
            bolsa4.agregarEmpresa(e9);
            create(e9);

            Empresa e10 = new Empresa("Intel", "INTC");
            e10.setBolsa(bolsa5);
            bolsa5.agregarEmpresa(e10);
            create(e10);

            Empresa e11 = new Empresa("IBM", "IBM");
            e11.setBolsa(bolsa4);
            bolsa4.agregarEmpresa(e11);
            create(e11);

            Empresa e12 = new Empresa("Oracle", "ORCL");
            e12.setBolsa(bolsa5);
            bolsa5.agregarEmpresa(e12);
            create(e12);
        }
    }
}

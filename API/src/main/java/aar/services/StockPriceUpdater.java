package aar.services;

import aar.models.Empresa;
import aar.repository.EmpresaDaoImpl;

import java.util.List;
import java.util.Random;

public class StockPriceUpdater {

    private final EmpresaDaoImpl empresaDao;
    private final Random random = new Random();

    public StockPriceUpdater(EmpresaDaoImpl empresaDao) {
        this.empresaDao = empresaDao;
        startUpdaterThread();
    }

    private void startUpdaterThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    updatePrices();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        thread.setName("StockPriceUpdater");
        thread.start();
    }

    private void updatePrices() {
        List<Empresa> empresas = empresaDao.findAll();

        for (Empresa emp : empresas) {
            double old = emp.getPrecioAccion();
            double change = random.nextDouble() * 10 - 5;
            double nuevo = old + change;

            if (nuevo < 1) nuevo = 1;
            if (nuevo > 100) nuevo = 100;

            emp.setPrecioAccion(nuevo);
            empresaDao.update(emp);
        }
    }
}
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
            double precioActual = emp.getPrecioAccion();
            
            // Generar variación aleatoria entre -5 y +5 euros
            double variacion = (random.nextDouble() * 10.0) - 5.0;
            double nuevoPrecio = precioActual + variacion;

            // Mantener el precio dentro del rango [1, 100]
            if (nuevoPrecio < 1.0) {
                nuevoPrecio = 1.0;
            } else if (nuevoPrecio > 100.0) {
                nuevoPrecio = 100.0;
            }

            emp.setPrecioAccion(nuevoPrecio);
            empresaDao.update(emp);
            
            System.out.println("Precio actualizado: " + emp.getNombreEmpresa() + 
                             " | Anterior: " + String.format("%.2f", precioActual) + 
                             " | Variación: " + String.format("%.2f", variacion) + 
                             " | Nuevo: " + String.format("%.2f", nuevoPrecio));
        }
    }
}
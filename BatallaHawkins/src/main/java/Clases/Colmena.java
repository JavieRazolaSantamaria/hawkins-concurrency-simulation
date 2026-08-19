package Clases;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Colmena {
    // --- Atributos ---
    private final BlockingQueue<Nino> prisioneros = new LinkedBlockingQueue<>();
    private final AtomicInteger contadorCapturasTotal = new AtomicInteger(0);
    private final AtomicInteger contadorDemogorgons = new AtomicInteger(1);
    private AgrupacionZonas zonas;

    // --- Constructor ---
    public Colmena() {}

    public void setZonas(AgrupacionZonas zonas) {
        this.zonas = zonas;
    }

    // --- Lógica de Prisión y Generación ---
    public void depositarNino(Nino nino) {
        prisioneros.add(nino);
        contadorCapturasTotal.incrementAndGet();

        if (zonas != null && prisioneros.size() % 8 == 0) {
            Logs.getInstance().log("¡Los Demogorgons han capturado 8 niños! Nace un nuevo Demogorgon.");
            crearNuevoDemogorgon();
        }
    }

    private void crearNuevoDemogorgon() {
        int siguienteId = contadorDemogorgons.getAndIncrement();
        String idFormateado = String.format("D%04d", siguienteId);
        
        Demogorgon nuevo = new Demogorgon(idFormateado, this.zonas);
        this.zonas.getUpsidedown().registrarDemogorgon(nuevo);
        nuevo.start();
    }

    // --- Lógica de Rescate ---
    public int liberarNinos(int cantidad) {
        int liberados = 0;
        
        while (liberados < cantidad && !prisioneros.isEmpty()) {
            Nino rescatado = prisioneros.poll();
            if (rescatado != null) {
                rescatado.serRescatado();
                liberados++;
            }
        }
        
        registrarLogRescate(liberados);
        return liberados;
    }

    // --- Utilidades ---
    private void registrarLogRescate(int liberados) {
        if (liberados > 0) {
            Logs.getInstance().log(" ELEVEN ha usado sus poderes y ha rescatado a " + liberados + " niños de la Colmena.");
        } else {
            Logs.getInstance().log(" ELEVEN ha llegado a la Colmena, pero estaba vacía.");
        }
    }

    public int getNumPrisioneros() {
        return prisioneros.size();
    }

    public int getCapturasHistoricas() {
        return contadorCapturasTotal.get();
    }
}
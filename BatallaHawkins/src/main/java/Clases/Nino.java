package Clases;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Nino extends Thread {
    // --- Atributos de Identificación y Estado ---
    private String idNino;
    private boolean vivo = true;
    private boolean capturado = false;
    private boolean inmune = false;
    private int sangreRecolectada = 0;
    
    private final AgrupacionZonas zonas;
    
    // --- Herramientas de concurrencia---
    private final AtomicBoolean bajoAtaque = new AtomicBoolean(false);
    private final Semaphore resolucionCombate = new Semaphore(0);
    private final Lock cerrojoRescate = new ReentrantLock();
    private final Condition condicionRescate = cerrojoRescate.newCondition();

    // --- Constructor ---
    public Nino(String idNino, AgrupacionZonas zonas) {
        this.idNino = idNino;
        this.zonas = zonas;
    }

    // --- Ciclo de Vida Principal ---
    @Override
    public void run() {
        
        try {
            zonas.esperarSiPausado();
            zonas.getCallePrincipal().inicio(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return; 
        }

        // El niño repite su rutina mientras siga vivo
        while (vivo) {
            try {
                // 1. Preparación en la base
                zonas.esperarSiPausado();
                zonas.getSotanoByers().entrarZona(this);
                Thread.sleep(1000 + (long) (Math.random() * 1001));
                zonas.esperarSiPausado();
                zonas.getSotanoByers().salirZona(this);

                // 2. Cruce de Portal (Ida al peligro)
                int rutaElegida = (int) (Math.random() * 4);
                zonas.esperarSiPausado();
                zonas.getPortal(rutaElegida).cruzarAlUpsideDown(this);

                // 3. Upside Down (Fase crítica donde puede ser atacado)
                gestionarFaseUpsideDown(rutaElegida);

                // 4. Regreso a Hawkins (Prioridad absoluta en el portal)
                zonas.esperarSiPausado();
                zonas.getPortal(rutaElegida).cruzarAHawkins(this);

                // 5. Depositar sangre y descansar en la Radio
                gestionarFaseRadio();

                // 6. Camuflaje en la Calle Principal
                zonas.getCallePrincipal().deambular(this);

            } catch (InterruptedException e) {

            }
        }
        System.out.println(idNino + " ha finalizado su ejecución.");
    }

    // --- Lógica Interna de Fases ---
    private void gestionarFaseUpsideDown(int ruta) throws InterruptedException {
        ZonaInsegura zonaActual = zonas.getUpsidedown().getZonas().get(ruta);
        zonaActual.entrarNino(this);
        
        boolean sobreviveYEscapa = true;
        long tiempoRestante = 3000 + (long) (Math.random() * 2001);
        
        if (zonas.isTormentaUpsideDown()) tiempoRestante *= 2;

        while (tiempoRestante > 0 && sobreviveYEscapa) {
            long inicioExtraccion = System.currentTimeMillis();
            try {
                zonaActual.recolectarSangre(tiempoRestante);
                
                tiempoRestante = 0;
                this.sangreRecolectada = zonas.isTormentaUpsideDown() ? 2 : 1;
                zonaActual.registrarExtraccionGlobal(this.sangreRecolectada);
                Logs.getInstance().log(idNino + " ha recolectado sangre con éxito.");
                
            } catch (InterruptedException e) {
                long tiempoPasado = System.currentTimeMillis() - inicioExtraccion;
                tiempoRestante -= tiempoPasado;
                if (tiempoRestante < 0) tiempoRestante = 0;
                this.sangreRecolectada = 0;

                resolucionCombate.acquire();
                zonas.esperarSiPausado();

                if (capturado) {
                    sobreviveYEscapa = false;
                    
                    cerrojoRescate.lock();
                    try {
                        while (capturado) {
                            condicionRescate.await(); 
                        }
                    } finally {
                        cerrojoRescate.unlock();
                    }
                    
                    zonas.esperarSiPausado();
                    Logs.getInstance().log(idNino + " inicia retirada tras ser rescatado.");
                } else {
                    this.inmune = true; 
                    Logs.getInstance().log(idNino + " RESISTE con inmunidad.");
                }
            }
        }
        
        this.inmune = false; 
        zonaActual.salirNino(this);
    }

    private void gestionarFaseRadio() throws InterruptedException {
        zonas.esperarSiPausado();
        if (this.sangreRecolectada > 0) {
            zonas.getRadioWSQK().depositarSangre(this);
        }
        zonas.getRadioWSQK().entrarZona(this);
        Thread.sleep(2000 + (long) (Math.random() * 2001));
        zonas.esperarSiPausado();
        zonas.getRadioWSQK().salirZona(this);
    }

    public boolean intentarAtrapar() {
        return bajoAtaque.compareAndSet(false, true);
    }

    public void resolverAtaque(boolean fueCapturado) {
        this.capturado = fueCapturado;
        this.bajoAtaque.set(false);
        this.resolucionCombate.release(); 
    }

    public void serRescatado() {
        cerrojoRescate.lock();
        try {
            this.capturado = false;
            condicionRescate.signalAll(); 
        } finally {
            cerrojoRescate.unlock();
        }
    }

    // --- Getters y Setters ---
    public String getIdNino() { return idNino; }
    public boolean isVivo() { return vivo; }
    public void setVivo(boolean vivo) { this.vivo = vivo; }
    public boolean isCapturado() { return capturado; }
    public boolean isBajoAtaque() { return bajoAtaque.get(); }
    public int getSangreRecolectada() { return sangreRecolectada; }
    public void setSangreRecolectada(int sangreRecolectada) { this.sangreRecolectada = sangreRecolectada; }
    public boolean isInmune() { return inmune; }
}
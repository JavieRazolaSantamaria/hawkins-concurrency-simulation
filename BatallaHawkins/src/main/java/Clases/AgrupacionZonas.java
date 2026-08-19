package Clases;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AgrupacionZonas {

    // --- Atributos de Zonas y Entidades ---
    private final CallePrincipal callePrincipal;
    private final SotanoByers sotanoByers;
    private final RadioWSQK radioWSQK;
    private final UpsideDown upsidedown;
    private final Portal[] portales;
    
    // --- Atributos de Control y Concurrencia ---
    private final Lock cerrojoGlobal = new ReentrantLock();
    private final Condition condicionDetencion = cerrojoGlobal.newCondition();
    private final AtomicInteger tiempoRestanteEvento = new AtomicInteger(0);
    
    private volatile boolean simulacionDetenida = false;
    private boolean apagonLaboratorio = false;
    private boolean tormentaUpsideDown = false;
    private boolean intervencionEleven = false;
    private boolean redMental = false;

    // --- Constructor ---
    public AgrupacionZonas() {
        this.callePrincipal = new CallePrincipal(this);
        this.sotanoByers = new SotanoByers(this);
        this.radioWSQK = new RadioWSQK(this);
        
        this.upsidedown = new UpsideDown(this);
        this.upsidedown.getColmena().setZonas(this);
        
        this.portales = new Portal[4];
        this.portales[0] = new Portal("Bosque", 2, this);
        this.portales[1] = new Portal("Laboratorio", 3, this);
        this.portales[2] = new Portal("Centro Comercial", 4, this);
        this.portales[3] = new Portal("Alcantarillado", 2, this);
    }

    // --- Gestión de Pausa y Sincronización ---
    public void pausar() {
        cerrojoGlobal.lock();
        try {
            simulacionDetenida = true;
        } finally {
            cerrojoGlobal.unlock();
        }
    }

    public void reanudar() {
        cerrojoGlobal.lock();
        try {
            simulacionDetenida = false;
            condicionDetencion.signalAll();
        } finally {
            cerrojoGlobal.unlock();
        }
    }

    public void esperarSiPausado() throws InterruptedException {
        cerrojoGlobal.lock();
        try {
            while (simulacionDetenida) {
                condicionDetencion.await();
            }
        } finally {
            cerrojoGlobal.unlock();
        }
    }

    public void notificarFinEvento() {
        for (Portal p : portales) {
            if (p != null) {
                p.despertarHilos();
            }
        }
    }

    // --- Lógica de Estado y Reportes (Strings) ---
    public String getEstadoGlobalParaMonitor() {
        return getNinosTotalesHawkins() + ";" +
               getEventoActivo() + ";" +
               getTiempoRestanteEvento() + ";" +
               upsidedown.getColmena().getNumPrisioneros() + ";" +
               getEstadoPortalString(0) + ";" +
               getEstadoPortalString(1) + ";" +
               getEstadoPortalString(2) + ";" +
               getEstadoPortalString(3) + ";" +
               getEntidadesZonaString(0) + ";" +
               getEntidadesZonaString(1) + ";" +
               getEntidadesZonaString(2) + ";" +
               getEntidadesZonaString(3) + ";" +
               getTop3Demogorgons();
    }

    public String getEstadoPortalString(int index) {
        Portal p = portales[index];
        if (p == null) return "0,0,0";

        int esperandoIda = p.getNinosEsperandoAlUpsideDown().size();
        int enElInterior = p.getCruzando().size();
        int esperandoVuelta = p.getNinosEsperandoAHawkins().size();

        return esperandoIda + "," + enElInterior + "," + esperandoVuelta;
    }

    public String getEntidadesZonaString(int indice) {
        try {
            ZonaInsegura zona = upsidedown.getZonas().get(indice);
            int ninos = zona.getNinosEnZona().size();
            int demos = zona.getDemogorgonsEnZona().size();
            return ninos + "/" + demos;
        } catch (Exception e) {
            return "0/0";
        }
    }

    public String getTop3Demogorgons() {
        try {
            List<Demogorgon> lista = upsidedown.getListaDemogorgons();
            if (lista == null || lista.isEmpty()) return "No hay datos";

            return lista.stream()
                    .sorted((d1, d2) -> Integer.compare(d2.getCapturasRealizadas(), d1.getCapturasRealizadas()))
                    .limit(3)
                    .map(d -> d.getIdDemogorgon() + ": " + d.getCapturasRealizadas())
                    .reduce((a, b) -> a + " | " + b)
                    .orElse("Sin capturas");
        } catch (Exception e) {
            return "Error al calcular Top";
        }
    }

    public String getEventoActivo() {
        if (apagonLaboratorio) return "Apagón en el Laboratorio";
        if (tormentaUpsideDown) return "Tormenta en el Upside Down";
        if (intervencionEleven) return "Intervención de Eleven";
        if (redMental) return "Red Mental Activa";
        return "Sin evento activo";
    }

    // --- Getters de Zonas y Conteo ---
    public int getNinosTotalesHawkins() {
        return callePrincipal.getNumeroNinos() + 
               sotanoByers.getNumeroNinos() + 
               radioWSQK.getNumeroNinos();
    }

    public CallePrincipal getCallePrincipal() { return callePrincipal; }
    public SotanoByers getSotanoByers() { return sotanoByers; }
    public RadioWSQK getRadioWSQK() { return radioWSQK; }
    public UpsideDown getUpsidedown() { return upsidedown; }
    public Portal getPortal(int index) { return portales[index]; }
    public Portal[] getTodosLosPortales() { return portales; }

    // --- Getters y Setters de Eventos y Tiempo ---
    public boolean isPausado() { return simulacionDetenida; }
    
    public int getTiempoRestanteEvento() { return tiempoRestanteEvento.get(); }
    public void setTiempoRestanteEvento(int tiempo) { this.tiempoRestanteEvento.set(tiempo); }

    public boolean isApagonLaboratorio() { return apagonLaboratorio; }
    public void setApagonLaboratorio(boolean apagonLaboratorio) { this.apagonLaboratorio = apagonLaboratorio; }

    public boolean isTormentaUpsideDown() { return tormentaUpsideDown; }
    public void setTormentaUpsideDown(boolean tormentaUpsideDown) { this.tormentaUpsideDown = tormentaUpsideDown; }

    public boolean isIntervencionEleven() { return intervencionEleven; }
    public void setIntervencionEleven(boolean intervencionEleven) { this.intervencionEleven = intervencionEleven; }

    public boolean isRedMental() { return redMental; }
    public void setRedMental(boolean redMental) { this.redMental = redMental; }
}
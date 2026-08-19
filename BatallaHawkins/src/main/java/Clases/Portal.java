package Clases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Portal {
    private final String idPortal;
    private final int limiteGrupo;
    private final AgrupacionZonas gestorZonas;

    // --- Sincronización ---
    // Garantiza la exclusión mutua al modificar el estado o las listas del portal
    private final Lock mutex = new ReentrantLock();
    private final Condition condicionAcceso = mutex.newCondition();

    // --- Estado de Tránsito ---
    private boolean zonaTransitoOcupada = false;
    private Nino pasajeroActual = null;

    // --- Listas de Gestión ---
    private final List<Nino> colaSalida = new ArrayList<>();
    private final List<Nino> grupoAprobado = new ArrayList<>();
    private final List<Nino> colaRetorno = new ArrayList<>();

    public Portal(String idPortal, int limiteGrupo, AgrupacionZonas gestorZonas) {
        this.idPortal = idPortal;
        this.limiteGrupo = limiteGrupo;
        this.gestorZonas = gestorZonas;
    }
   
    // Flujo principal para cruzar de Hawkins al Upside Down (Ida)
    public void cruzarAlUpsideDown(Nino nino) throws InterruptedException {
        gestorZonas.esperarSiPausado();
        mutex.lock();
        try {
            registrarEnColaSalida(nino);
            esperarAprobacionIda(nino);
            iniciarCruceIda(nino);
        } finally {
            mutex.unlock();
        }
        simularTiempoCruce(nino, true);
    }

    private void registrarEnColaSalida(Nino nino) {
        if (!colaSalida.contains(nino) && !grupoAprobado.contains(nino)) {
            colaSalida.add(nino);
        }
    }

    private void esperarAprobacionIda(Nino nino) throws InterruptedException {
        while (true) {
            intentarFormarGrupo();
            if (tienePermisoParaIr(nino)) break;

            try {
                condicionAcceso.await();
                verificarPausaEnEspera();
            } catch (InterruptedException ex) {
                limpiarRastroIda(nino);
                throw ex; // Propaga la interrupción (ej. ataque de Demogorgon)
            }
        }
    }

    // Traspasa los hilos de la cola de espera al grupo de tránsito si hay capacidad
    private void intentarFormarGrupo() {
        if (grupoAprobado.isEmpty() && colaSalida.size() >= limiteGrupo) {
            for (int i = 0; i < limiteGrupo; i++) {
                grupoAprobado.add(colaSalida.remove(0));
            }
            condicionAcceso.signalAll(); // Avisa a los elegidos para que avancen
        }
    }

    private boolean tienePermisoParaIr(Nino nino) {
        boolean esLiderGrupo = !grupoAprobado.isEmpty() && grupoAprobado.get(0).equals(nino);
        boolean prioridadInversa = !colaRetorno.isEmpty();
        
        // La condición !prioridadInversa asegura que el retorno tenga prioridad absoluta
        return esLiderGrupo && !zonaTransitoOcupada && !prioridadInversa 
                && !gestorZonas.isApagonLaboratorio() && !gestorZonas.isPausado();
    }

    private void iniciarCruceIda(Nino nino) {
        zonaTransitoOcupada = true;
        pasajeroActual = nino;
        grupoAprobado.remove(nino);
        Logs.getInstance().log(">>> [PORTAL " + idPortal + "] " + nino.getIdNino() + " ENTRA (Ida)");
    }

    // Restaura el estado de las listas si un hilo es interrumpido mientras esperaba
    private void limpiarRastroIda(Nino nino) {
        colaSalida.remove(nino);
        if (grupoAprobado.remove(nino)) {
            colaSalida.addAll(grupoAprobado); // Deshace el grupo incompleto
            grupoAprobado.clear();
        }
        condicionAcceso.signalAll();
    }
    
    // Flujo de retorno. Los niños escapan de forma individual y con máxima prioridad
    public void cruzarAHawkins(Nino nino) throws InterruptedException {
        gestorZonas.esperarSiPausado();
        mutex.lock();
        try {
            registrarEnColaRetorno(nino);
            esperarAprobacionVuelta(nino);
            iniciarCruceVuelta(nino);
        } finally {
            mutex.unlock();
        }
        simularTiempoCruce(nino, false);
    }

    private void registrarEnColaRetorno(Nino nino) {
        if (!colaRetorno.contains(nino)) {
            colaRetorno.add(nino);
        }
    }

    private void esperarAprobacionVuelta(Nino nino) throws InterruptedException {
        while (true) {
            if (tienePermisoParaVolver(nino)) break;

            try {
                condicionAcceso.await();
                verificarPausaEnEspera();
            } catch (InterruptedException ex) {
                colaRetorno.remove(nino);
                condicionAcceso.signalAll();
                throw ex;
            }
        }
    }

    private boolean tienePermisoParaVolver(Nino nino) {
        // Garantiza un orden FIFO estricto para los retornos
        boolean esPrimero = !colaRetorno.isEmpty() && colaRetorno.get(0).equals(nino);
        return esPrimero && !zonaTransitoOcupada && !gestorZonas.isApagonLaboratorio() && !gestorZonas.isPausado();
    }

    private void iniciarCruceVuelta(Nino nino) {
        zonaTransitoOcupada = true;
        pasajeroActual = nino;
        colaRetorno.remove(nino);
        Logs.getInstance().log(">>> [PORTAL " + idPortal + "] " + nino.getIdNino() + " ENTRA (Vuelta)");
    }

    
    // Representa el tiempo físico dentro del portal. Libera el recurso al terminar.
    private void simularTiempoCruce(Nino nino, boolean esIda) throws InterruptedException {
        try {
            Thread.sleep(1000);
            gestorZonas.esperarSiPausado();
        } catch (InterruptedException e) {
            Logs.getInstance().log("[ALERTA] " + nino.getIdNino() + " fue interrumpido cruzando el portal " + idPortal);
            throw e;
        } finally {
            liberarAcceso();
            if (esIda) {
                Logs.getInstance().log("<<< [PORTAL " + idPortal + "] " + nino.getIdNino() + " SALE (Upside Down)");
            } else {
                Logs.getInstance().log("<<< [PORTAL " + idPortal + "] " + nino.getIdNino() + " A SALVO en Hawkins.");
            }
        }
    }

    // Desbloquea el portal y avisa a los siguientes hilos en espera
    private void liberarAcceso() {
        mutex.lock();
        try {
            zonaTransitoOcupada = false;
            pasajeroActual = null;
            condicionAcceso.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    public void despertarHilos() {
        mutex.lock();
        try {
            condicionAcceso.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    // Patrón de diseño para evitar deadlocks
    private void verificarPausaEnEspera() throws InterruptedException {
        if (gestorZonas.isPausado()) {
            mutex.unlock();
            try {
                gestorZonas.esperarSiPausado();
            } finally {
                mutex.lock();
            }
        }
    }

    // Getters
    public String getNombre() { return idPortal; }

    public List<Nino> getCruzando() {
        mutex.lock();
        try {
            List<Nino> l = new ArrayList<>();
            if (pasajeroActual != null) l.add(pasajeroActual);
            return l;
        } finally { mutex.unlock(); }
    }

    public List<Nino> getNinosEsperandoAlUpsideDown() {
        mutex.lock();
        try {
            List<Nino> l = new ArrayList<>(grupoAprobado);
            l.addAll(colaSalida);
            return l;
        } finally { mutex.unlock(); }
    }

    public List<Nino> getNinosEsperandoAHawkins() {
        mutex.lock();
        try { return new ArrayList<>(colaRetorno); } finally { mutex.unlock(); }
    }
}
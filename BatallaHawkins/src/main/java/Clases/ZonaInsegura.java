package Clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ZonaInsegura {

    // --- Atributos de Estado y Control ---
    private String nombre;
    private final AgrupacionZonas zonas;
    private final Random random = new Random();
    
    // Listas concurrentes para evitar ConcurrentModificationException durante ataques y movimientos
    private final List<Nino> ninosEnZona = new CopyOnWriteArrayList<>();
    private final List<Demogorgon> demogorgonsEnZona = new CopyOnWriteArrayList<>();
    
    // Contador thread-safe para estadísticas de la zona
    private final AtomicInteger sangreRecolectada = new AtomicInteger(0);

    // --- Constructor ---
    public ZonaInsegura(String nombre, AgrupacionZonas zonas) {
        this.nombre = nombre;
        this.zonas = zonas;
    }

    // --- Gestión de Entidades ---

    public void entrarNino(Nino n) throws InterruptedException {
        ninosEnZona.add(n);
        zonas.esperarSiPausado();
    }

    public void salirNino(Nino n) {
        ninosEnZona.remove(n);
    }

    public void entrarDemogorgon(Demogorgon d) {
        demogorgonsEnZona.add(d);
    }

    public void salirDemogorgon(Demogorgon d) {
        demogorgonsEnZona.remove(d);
    }

    // --- Lógica de Recolección ---

    /**
     * Simula el tiempo que el niño pasa extrayendo sangre.
     * Si el hilo es interrumpido aquí, es porque un Demogorgon ha iniciado un ataque.
     */
    public void recolectarSangre(long tiempoRestante) throws InterruptedException {
        Thread.sleep(tiempoRestante);
        zonas.esperarSiPausado();
    }

    /**
     * Registra el éxito de una extracción en el contador global de esta zona.
     */
    public void registrarExtraccionGlobal(int cantidad) {
        sangreRecolectada.addAndGet(cantidad);
    }

    // --- Lógica de Combate ---

    /**
     * Selecciona un niño de la zona que sea apto para ser atacado.
     * Filtra niños que ya están en combate o que gozan de inmunidad temporal.
     */
    public Nino seleccionarVictima() {
        try {
            if (ninosEnZona.isEmpty()) return null;

            List<Nino> objetivosValidos = new ArrayList<>();
            for (Nino n : ninosEnZona) {
                // Un niño solo es atacable si no está ya peleando y no es inmune
                if (!n.isBajoAtaque() && !n.isInmune()) {
                    objetivosValidos.add(n);
                }
            }

            if (objetivosValidos.isEmpty()) return null;

            int indice = random.nextInt(objetivosValidos.size());
            return objetivosValidos.get(indice);
            
        } catch (Exception e) {
            return null;
        }
    }

    // --- Getters y Consultas ---

    public String getNombre() {
        return nombre;
    }

    public List<Nino> getNinosEnZona() {
        return ninosEnZona;
    }

    public List<Demogorgon> getDemogorgonsEnZona() {
        return demogorgonsEnZona;
    }
    
    public int getTotalSangreExtraida() {
        return sangreRecolectada.get();
    }
}
package Clases;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RadioWSQK {

    // --- Atributos de Estado y Almacenamiento ---
    private final BlockingQueue<Nino> ninosEnRadio;
    private final AtomicInteger sangreTotalAlmacenada = new AtomicInteger(0);
    private final AgrupacionZonas zonas;
    private final Random rand = new Random();

    // --- Constructor ---
    public RadioWSQK(AgrupacionZonas zonas) {
        this.ninosEnRadio = new LinkedBlockingQueue<>();
        this.zonas = zonas;
    }

    // --- Gestión de Recursos (Sangre) ---

    /**
     * Transfiere la sangre recolectada por un niño al inventario global de la Radio.
     */
    public void depositarSangre(Nino nino) {
        int sangreTraida = nino.getSangreRecolectada();
        if (sangreTraida > 0) {
            int totalActual = sangreTotalAlmacenada.addAndGet(sangreTraida);
            nino.setSangreRecolectada(0); // El niño vacía su inventario personal
            Logs.getInstance().log(nino.getIdNino() + " ha depositado " + sangreTraida + 
                " unidad(es) de sangre. Total en RADIO: " + totalActual);
        }
    }

    /**
     * Reduce el inventario de sangre (generalmente invocado por Eleven durante un evento).
     */
    public void consumirSangre(int cantidad) {
        int actual = sangreTotalAlmacenada.get();
        int nuevaCantidad = Math.max(0, actual - cantidad);
        sangreTotalAlmacenada.set(nuevaCantidad);
    }

    // --- Lógica de Estancia de Entidades ---

    /**
     * Registra la entrada de un niño a la zona para su fase de descanso.
     */
    public void entrarZona(Nino nino) throws InterruptedException {
        if (!ninosEnRadio.contains(nino)) {
            ninosEnRadio.put(nino);
            Logs.getInstance().log(nino.getIdNino() + " ha ENTRADO a la Radio WSQK para descansar.");
        }
    }

    /**
     * Remueve al niño de la zona para que pueda continuar hacia la Calle Principal.
     */
    public void salirZona(Nino nino) {
        if (ninosEnRadio.remove(nino)) {
            Logs.getInstance().log(nino.getIdNino() + " ha terminado de descansar y SALE de la Radio WSQK.");
        }
    }

    // --- Consultas de Estado ---

    public int getNumeroNinos() {
        return ninosEnRadio.size();
    }

    public List<Nino> getNinosEnRadio() {
        return new ArrayList<>(ninosEnRadio);
    }

    public int getSangreTotalAlmacenada() {
        return sangreTotalAlmacenada.get();
    }
}
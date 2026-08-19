package Clases;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SotanoByers {

    // --- Atributos de Control y Estado ---
    private final BlockingQueue<Nino> ninosEnSotano;
    private final Random rand;
    private final AgrupacionZonas zonas;

    // --- Constructor ---
    public SotanoByers(AgrupacionZonas zonas) {
        this.ninosEnSotano = new LinkedBlockingQueue<>();
        this.rand = new Random();
        this.zonas = zonas;
    }

    // --- Lógica de Estancia ---

    /**
     * Registra la entrada de un niño al sótano y simula el tiempo de preparación.
     */
    public void entrarZona(Nino nino) throws InterruptedException {
        ninosEnSotano.put(nino);
        Logs.getInstance().log(nino.getIdNino() + " ha ENTRADO al Sótano Byers y empieza a prepararse.");
        
        // Tiempo de preparación aleatorio entre 1 y 2 segundos
        int tiempoEspera = rand.nextInt(1000) + 1000; 
        Thread.sleep(tiempoEspera);
        
        // Verificación de pausa tras el sueño para evitar comportamientos inconsistentes
        zonas.esperarSiPausado();
        
        Logs.getInstance().log(nino.getIdNino() + " ha terminado de prepararse en el Sótano Byers.");
    }

    /**
     * Extrae al niño de la zona para permitir su transición hacia los portales.
     */
    public void salirZona(Nino nino) {
        if (ninosEnSotano.remove(nino)) {
            Logs.getInstance().log(nino.getIdNino() + " ha SALIDO del Sótano Byers hacia los Portales.");
        }
    }

    // --- Consultas de Estado ---

    public int getNumeroNinos() {
        return ninosEnSotano.size();
    }

    /**
     * Retorna una copia de la lista actual de niños para su representación en la interfaz.
     */
    public List<Nino> getNinosEnSotano() {
        return new ArrayList<>(ninosEnSotano);
    }
}
package Clases;

import java.util.Random;

public class GeneradorEventos extends Thread {

    // --- Atributos de Control ---
    private final AgrupacionZonas zonas;
    private final Random random = new Random();

    // --- Constructor ---
    public GeneradorEventos(AgrupacionZonas zonas) {
        this.zonas = zonas;
    }

    // --- Ciclo de Vida del Generador ---
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                zonas.esperarSiPausado();

                // 1. Fase de Latencia: Espera entre 30 y 60 segundos antes de un evento
                int tiempoHastaEvento = 30000 + random.nextInt(30001);
                int tiempoTranscurrido = 0;

                while (tiempoTranscurrido < tiempoHastaEvento) {
                    Thread.sleep(1000);
                    zonas.esperarSiPausado();
                    tiempoTranscurrido += 1000;
                }

                // 2. Fase de Selección: Determina tipo y duración (5-10 segundos)
                int tipoEvento = random.nextInt(4);
                int duracionEvento = 5000 + random.nextInt(5001);

                activarEvento(tipoEvento, duracionEvento);

                // 3. Fase de Ejecución: Monitorización del tiempo restante
                int tiempoPasado = 0;
                while (tiempoPasado < duracionEvento) {
                    int segundosRestantes = (duracionEvento - tiempoPasado) / 1000;
                    zonas.setTiempoRestanteEvento(segundosRestantes);

                    Thread.sleep(500);
                    zonas.esperarSiPausado();
                    tiempoPasado += 500;
                }

                // 4. Fase de Cierre: Limpieza de estados
                finalizarEventos();
            }
        } catch (InterruptedException e) {
            System.out.println("Generador de Eventos interrumpido.");
            Thread.currentThread().interrupt();
        }
    }

    // --- Gestión de Estados de Evento ---

    /**
     * Modifica las flags globales en el monitor para alterar el comportamiento de los hilos.
     */
    private void activarEvento(int tipo, int duracion) {
        switch (tipo) {
            case 0:
                zonas.setApagonLaboratorio(true);
                Logs.getInstance().log("¡EVENTO! APAGÓN DEL LABORATORIO: Portales bloqueados por " + (duracion / 1000) + "s.");
                break;
            case 1:
                zonas.setTormentaUpsideDown(true);
                Logs.getInstance().log("¡EVENTO! TORMENTA UPSIDE DOWN: Sangre x2 y Demogorgons rápidos por " + (duracion / 1000) + "s.");
                break;
            case 2:
                zonas.setIntervencionEleven(true);
                Logs.getInstance().log("¡EVENTO! INTERVENCIÓN DE ELEVEN: Demogorgons paralizados por " + (duracion / 1000) + "s.");

                // Lógica de rescate inmediata basada en el recurso de sangre
                int sangreDisponible = zonas.getRadioWSQK().getSangreTotalAlmacenada();
                int rescatados = zonas.getUpsidedown().getColmena().liberarNinos(sangreDisponible);

                if (rescatados > 0) {
                    zonas.getRadioWSQK().consumirSangre(rescatados);
                    Logs.getInstance().log("Se han consumido " + rescatados + " unidades de sangre para el rescate.");
                }
                break;
            case 3:
                zonas.setRedMental(true);
                Logs.getInstance().log("¡EVENTO! LA RED MENTAL: Los Demogorgons van a la zona más poblada por " + (duracion / 1000) + "s.");
                break;
        }
    }

    /**
     * Restablece la normalidad del sistema y notifica a los hilos bloqueados.
     */
    private void finalizarEventos() {
        zonas.setTiempoRestanteEvento(0);
        zonas.setApagonLaboratorio(false);
        zonas.setTormentaUpsideDown(false);
        zonas.setIntervencionEleven(false);
        zonas.setRedMental(false);

        zonas.notificarFinEvento();
        Logs.getInstance().log("FIN DEL EVENTO GLOBAL. La normalidad vuelve a Hawkins.");
    }
}
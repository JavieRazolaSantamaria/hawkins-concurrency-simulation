package Clases;

import sockets.ServidorControl;
import Interfaz.Interfaz;
import java.awt.EventQueue;

public class Main {

    public static void main(String args[]) {
        
        System.out.println(">>> [SERVIDOR] INICIANDO SIMULACIÓN...");

        // Monitor de recursos compartido y lógica de red
        AgrupacionZonas zonas = new AgrupacionZonas();
        ServidorControl servidorSockets = new ServidorControl(zonas);
        servidorSockets.start();

        // Despliegue de la interfaz gráfica en el hilo de despacho de eventos (EDT)
        EventQueue.invokeLater(() -> {
            Interfaz ventanaPrincipal = new Interfaz(zonas);
            ventanaPrincipal.setLocationRelativeTo(null);
            ventanaPrincipal.setVisible(true);
        });

        // Inicio de procesos lógicos y entidad especial
        new GeneradorEventos(zonas).start();
        
        Demogorgon d0 = new Demogorgon("D0000", zonas);
        zonas.getUpsidedown().registrarDemogorgon(d0);
        d0.start();

        // Ciclo de instanciación de entidades con retardo variable
        for (int i = 1; i <= 1500; i++) {
            try {
                zonas.esperarSiPausado(); 
                
                String idNino = String.format("N%04d", i);
                new Nino(idNino, zonas).start();
                
                Thread.sleep(500 + (int)(Math.random() * 1500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
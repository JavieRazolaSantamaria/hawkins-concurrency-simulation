/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Interfaz;

import java.awt.EventQueue;

/**
 *
 * @author javir
 */
public class MainMonitor {

    public static void main(String[] args) {
        System.out.println(">>> [CLIENTE] INICIANDO MONITOR REMOTO...");

        EventQueue.invokeLater(() -> {
            // IMPORTANTE: Usamos el constructor vacío. 
            // El monitor no debe recibir el objeto "zonas" para ser un cliente real.
            VentanaMonitor monitor = new VentanaMonitor(); 
            monitor.setTitle("Monitor de Estadísticas (Conexión vía Socket)");
            monitor.setLocationRelativeTo(null);
            monitor.setVisible(true);
        });
    }
}
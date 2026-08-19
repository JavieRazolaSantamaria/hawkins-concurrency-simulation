/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sockets;

import Clases.AgrupacionZonas;
import sockets.ManejadorCliente;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorControl extends Thread {
    private AgrupacionZonas mundo;
    private int puerto = 5011;

    public ServidorControl(AgrupacionZonas mundo) {
        this.mundo = mundo;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(puerto)) {
            while (true) {
                Socket cliente = server.accept();
                new ManejadorCliente(cliente, mundo).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
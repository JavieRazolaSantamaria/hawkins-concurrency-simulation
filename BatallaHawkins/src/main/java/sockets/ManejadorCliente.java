package sockets;

import Clases.AgrupacionZonas;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ManejadorCliente extends Thread {
    private final Socket socket;
    private final AgrupacionZonas mundo;

    public ManejadorCliente(Socket s, AgrupacionZonas m) {
        this.socket = s;
        this.mundo = m;
    }

    @Override
    public void run() {
        // Usamos try-with-resources para asegurar que los flujos se cierren siempre
        try (DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream())) {

            String peticion = entrada.readUTF();

            switch (peticion) {
                case "GET_DATA":
                    // Envía toda la ristra de datos para el monitor
                    String respuesta = mundo.getEstadoGlobalParaMonitor();
                    salida.writeUTF(respuesta);
                    break;

                case "PAUSAR":
                    // Ejecuta la pausa en el monitor global
                    mundo.pausar();
                    salida.writeUTF("OK_PAUSA");
                    break;

                case "REANUDAR":
                    // Ejecuta la reanudación en el monitor global
                    mundo.reanudar();
                    salida.writeUTF("OK_REANUDAR");
                    break;

                default:
                    salida.writeUTF("ERROR: Comando no reconocido");
                    break;
            }

            socket.close();
        } catch (Exception e) {
            System.err.println("Error en la conexión con el cliente: " + e.getMessage());
        }
    }
}
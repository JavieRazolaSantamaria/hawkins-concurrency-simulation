package Clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Logs {

    
    private static final Logs instancia = new Logs();
    
    // Definición de la ruta 
    private static final Path RUTA_ARCHIVO = Paths.get("hawkins.txt");
    
    // Formateador para la marca de tiempo de cada evento
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Cerrojo explícito para garantizar la exclusión mutua a
    private final Lock cerrojo = new ReentrantLock();

    // Constructor 
    private Logs() {}

    // Devuelve la única instancia global de la clase
    public static Logs getInstance() {
        return instancia;
    }

    // Registra un evento en el archivo de forma segura para múltiples hilos
    public void log(String evento) {
        // Obtiene la hora actual formateada
        String tiempoActual = LocalDateTime.now().format(formatoHora);
        
        // Prepara la línea completa. El '%n' asegura un salto de línea compatible con cualquier sistema
        String lineaAEscribir = String.format("%s - %s%n", tiempoActual, evento);

        // Bloquea el acceso para que ningún otro hilo pueda escribir al mismo tiempo
        cerrojo.lock(); 
        try {
            // Escribe la línea usando Java NIO. Si no existe lo crea (CREATE), si existe añade al final (APPEND)
            Files.writeString(RUTA_ARCHIVO, lineaAEscribir, 
                              StandardOpenOption.CREATE, 
                              StandardOpenOption.APPEND);
        } catch (IOException e) {
            // Captura y muestra errores de entrada/salida sin detener el programa
            System.err.println("Fallo al escribir el log: " + e.getMessage());
        } finally {
            // Se coloca en el 'finally' para garantizar que el cerrojo SIEMPRE se libere, incluso si hay error
            cerrojo.unlock(); 
        }
    }
}
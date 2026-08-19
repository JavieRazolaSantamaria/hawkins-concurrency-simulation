package Clases;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class UpsideDown {

    // --- Atributos de Dimensión ---
    private final List<ZonaInsegura> zonasInseguras = new ArrayList<>();
    private final Colmena colmena; 
    private final Random random = new Random();
    
    // CopyOnWriteArrayList para permitir lectura concurrente del ranking mientras se añaden nuevos Demogorgons
    private final List<Demogorgon> listaDemogorgons = new CopyOnWriteArrayList<>();

    // --- Constructor ---
    public UpsideDown(AgrupacionZonas zonas) {
        // Inicialización de las subzonas del Upside Down
        zonasInseguras.add(new ZonaInsegura("BOSQUE", zonas));
        zonasInseguras.add(new ZonaInsegura("LABORATORIO", zonas));
        zonasInseguras.add(new ZonaInsegura("CENTRO COMERCIAL", zonas));
        zonasInseguras.add(new ZonaInsegura("ALCANTARILLADO", zonas));
        
        this.colmena = new Colmena();
        this.colmena.setZonas(zonas);
    }

    // --- Gestión de Movimiento y Selección ---

    public ZonaInsegura obtenerZonaAleatoria() {
        return zonasInseguras.get(random.nextInt(zonasInseguras.size()));
    }

    /**
     * Lógica de la 'Red Mental': Busca la zona con mayor presencia de hilos Nino.
     * Si no hay niños en ninguna zona, retorna una aleatoria para dispersar la búsqueda.
     */
    public ZonaInsegura obtenerZonaMasPoblada() {
        ZonaInsegura zonaMasPoblada = null;
        int maxNinos = 0; 

        for (ZonaInsegura zona : zonasInseguras) {
            int numNinos = zona.getNinosEnZona().size();
            if (numNinos > maxNinos) {
                maxNinos = numNinos;
                zonaMasPoblada = zona;
            }
        }

        return (zonaMasPoblada == null) ? obtenerZonaAleatoria() : zonaMasPoblada;
    }

    // --- Reportes y Datos ---

    public void registrarDemogorgon(Demogorgon d) {
        this.listaDemogorgons.add(d);
    }

    /**
     * Genera una cadena formateada para el servidor de monitoreo (Sockets).
     * Incluye estado de zonas y el Top 3 de Demogorgons más letales.
     */
    public String obtenerDatosSocket() {
        StringBuilder sb = new StringBuilder();
        
        // Datos de población por zona
        for (ZonaInsegura z : zonasInseguras) {
            sb.append(z.getNombre()).append(": N=").append(z.getNinosEnZona().size())
              .append(", D=").append(z.getDemogorgonsEnZona().size()).append(" | ");
        }

        // Cálculo del Ranking de Capturas
        sb.append(";RANKING:");
        List<Demogorgon> copiaDemos = new ArrayList<>(listaDemogorgons);
        copiaDemos.sort((d1, d2) -> Integer.compare(d2.getCapturasRealizadas(), d1.getCapturasRealizadas()));
        
        int limite = Math.min(3, copiaDemos.size());
        if (limite == 0) {
            sb.append("Sin datos");
        } else {
            for (int i = 0; i < limite; i++) {
                Demogorgon d = copiaDemos.get(i);
                sb.append(d.getIdDemogorgon()).append("(").append(d.getCapturasRealizadas()).append(")");
                if (i < limite - 1) sb.append(" - ");
            }
        }
        return sb.toString();
    }

    // --- Getters ---
    public ZonaInsegura getZona(String nombre) {
        for (ZonaInsegura z : zonasInseguras) {
            if (z.getNombre().equalsIgnoreCase(nombre)) return z;
        }
        return null;
    }

    public List<ZonaInsegura> getZonas() { return zonasInseguras; }
    public Colmena getColmena() { return colmena; }
    public List<Demogorgon> getListaDemogorgons() { return listaDemogorgons; }
}
# 🚲 La Batalla de Hawkins: Simulación Concurrente y Distribuida en Java

Simulación integral en **Java** ambientada en el universo de *Stranger Things* que implementa y pone a prueba conceptos avanzados de **programación concurrente, sincronización de hilos y arquitectura distribuida cliente-servidor**[cite: 1, 2].

El sistema orquesta la interacción en tiempo real de hasta **1.500 niños (hilos)** y múltiples **Demogorgons (hilos)**[cite: 1, 2], gestionando recursos compartidos mediante monitores[cite: 1], exclusión mutua[cite: 1], paso de mensajes[cite: 1], balanceo de prioridades y eventos globales asíncronos[cite: 1], complementado con un panel de **monitorización remota vía Sockets TCP/IP**[cite: 1, 2].

---

## 📌 Descripción del Proyecto

El sistema modela la ciudad de Hawkins (zona segura) conectada al *Upside Down* (dimensión hostil) a través de cuatro portales de paso restringido[cite: 1, 2]. Los niños deben adentrarse en territorio enemigo para recolectar sangre de Vecna y regresar con vida para depositarla en la base[cite: 1, 2].

### 🔄 Ciclo de Vida de los Actores

1. **Aparición y Preparación:** Los niños se instancian de forma escalonada en la `Calle Principal` y se preparan en el `Sótano Byers`[cite: 1, 2].
2. **Cruce Agrupado (Ida):** Seleccionan uno de los 4 portales y esperan en cola hasta formar un grupo exclusivo (2 a 4 miembros según el portal)[cite: 1, 2]. El cruce se efectúa de uno en uno[cite: 1, 2].
3. **Incursión Crítica (Upside Down):** Recolectan recursos en una de las 4 zonas inseguras (*Bosque*, *Laboratorio*, *Centro Comercial*, *Alcantarillado*)[cite: 1, 2].
4. **Combate & Captura:** Si un Demogorgon ataca, el hilo entra en forcejeo[cite: 1]. Si el monstruo vence (probabilidad de éxito del 33%), traslada al niño a la `Colmena` como prisionero[cite: 1, 2]. Por cada 8 capturas, se engendra un nuevo Demogorgon autónomo[cite: 1, 2].
5. **Retorno Prioritario (Vuelta):** Al regresar, los niños que huyen tienen **prioridad absoluta e individual** sobre los grupos de ida en los portales[cite: 1, 2].
6. **Almacenamiento & Recuperación:** Depositan la sangre en la `Radio WSQK`, descansan y deambulan por la calle antes de reiniciar el ciclo[cite: 1, 2].

> **Eventos Globales Asíncronos:** Un hilo `GeneradorEventos` altera periódicamente el comportamiento del ecosistema (*Apagón del Laboratorio*, *Tormenta del Upside Down*, *Intervención Psíquica de Eleven* y *La Red Mental*)[cite: 1, 2].

---

## ⚙️ Mecanismos de Concurrencia y Sincronización

| Mecanismo Java | Componente / Ubicación | Propósito Técnico |
| :--- | :--- | :--- |
| **`ReentrantLock` + `Condition`** | `Portal` | Control de salas de espera para formación de grupos y prioridad FIFO absoluta de retorno[cite: 1]. |
| **`ReentrantLock` + `Condition`** | `AgrupacionZonas` | Mecanismo de **Pausa/Reanudación global** y despertar coordinado tras eventos[cite: 1]. |
| **`LinkedBlockingQueue<Nino>`** | `CallePrincipal`, `SotanoByers`, `RadioWSQK`, `Colmena` | Gestión de flujos y colas seguras para subprocesos sin bloqueos manuales (*thread-safe FIFO*)[cite: 1]. |
| **`CopyOnWriteArrayList`** | `ZonaInsegura`, `UpsideDown` | Lectura concurrente de rankings y entidades evitando excepciones `ConcurrentModificationException`[cite: 1]. |
| **`Semaphore`** | `Nino` / `Demogorgon` | Control del tiempo de combate y bloqueo atómico del niño durante el forcejeo[cite: 1]. |
| **`AtomicBoolean` & `AtomicInteger`** | `Nino`, `RadioWSQK`, `Colmena` | Exclusividad atómica en selección de víctimas y conteo concurrente de sangre/capturas[cite: 1]. |
| **`Thread` / `Runnable`** | `Nino`, `Demogorgon`, `GeneradorEventos` | Entidades concurrentes con ciclos de vida independientes y manejo de interrupciones[cite: 1]. |
| **`ReentrantLock` (Singleton)** | `Logs` | Exclusión mutua en la persistencia de eventos en el fichero físico `hawkins.txt`[cite: 1]. |

---

## 🌐 Arquitectura Distribuida (Sockets TCP/IP)

El proyecto desacopla el núcleo de simulación de la interfaz gráfica y de control[cite: 1]:

* **Servidor de Control (`ServidorControl` en puerto 5011):** Permanece a la escucha e instancia un hilo independiente `ManejadorCliente` por cada monitor conectado, garantizando escalabilidad y nulo impacto en el rendimiento de la simulación[cite: 1].
* **Protocolo de Texto Ligero:** Comunicación basada en comandos transaccionales (`GET_DATA`, `PAUSAR`, `REANUDAR`)[cite: 1].
* **Monitor Remoto Desacoplado (`VentanaMonitor`):** Cliente gráfico independiente con sondeo periódico vía `javax.swing.Timer` para renderizado en tiempo real[cite: 1].

---

## 📁 Estructura del Proyecto

```text
src/
├── Clases/
│   ├── Main.java                 # Punto de entrada de la simulación y servidor
│   ├── AgrupacionZonas.java      # Monitor central y estado global compartido
│   ├── Nino.java                 # Hilo de entidad superviviente
│   ├── Demogorgon.java           # Hilo depredador y lógica de ataque
│   ├── GeneradorEventos.java     # Hilo director de eventos globales
│   ├── Portal.java               # Gestión de sincronización y cruce con prioridades
│   ├── Colmena.java              # Prisión de hilos y factoría de Demogorgons
│   ├── ZonaInsegura.java         # Áreas críticas con colecciones concurrentes
│   ├── RadioWSQK.java            # Depósito de recursos y descanso
│   ├── SotanoByers.java          # Sala de preparación
│   ├── CallePrincipal.java       # Punto de spawn y deambulación
│   └── Logs.java                 # Logger thread-safe (Patrón Singleton)
├── Interfaz/
│   ├── Interfaz.java             # GUI principal de simulación (Swing)
│   ├── MainMonitor.java          # Punto de entrada del cliente remoto
│   └── VentanaMonitor.java       # GUI del monitor distribuido
└── sockets/
    ├── ServidorControl.java      # Listener ServerSocket multihilo
    └── ManejadorCliente.java     # Procesador de peticiones del protocolo

# 🚲 La Batalla de Hawkins: Simulación Concurrente y Distribuida en Java

![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Architecture](https://img.shields.io/badge/Arquitectura-Cliente%2FServidor-blue?style=for-the-badge)
![Concurrency](https://img.shields.io/badge/Concurrencia-Monitores%20%7C%20Locks-green?style=for-the-badge)

Simulación integral en **Java** ambientada en el universo de *Stranger Things* que implementa y pone a prueba conceptos avanzados de **programación concurrente, sincronización de hilos y arquitectura distribuida cliente-servidor**.

El sistema orquesta la interacción en tiempo real de hasta **1.500 niños (hilos)** y múltiples **Demogorgons (hilos)**, gestionando recursos compartidos mediante monitores, exclusión mutua, paso de mensajes, balanceo de prioridades y eventos globales asíncronos. Todo esto se complementa con un panel de **monitorización remota vía Sockets TCP/IP**.

---

## 📌 Descripción del Proyecto

El sistema modela la ciudad de Hawkins (zona segura) conectada al *Upside Down* (dimensión hostil) a través de cuatro portales de paso restringido. Los niños deben adentrarse en territorio enemigo para recolectar sangre de Vecna y regresar con vida para depositarla en la base.

### 🔄 Ciclo de Vida de los Actores

1. **Aparición y Preparación:** Los niños se instancian de forma escalonada en la `Calle Principal` y se preparan en el `Sótano Byers`.
2. **Cruce Agrupado (Ida):** Seleccionan uno de los 4 portales y esperan en cola hasta formar un grupo exclusivo (2 a 4 miembros según el portal). El cruce se efectúa de uno en uno.
3. **Incursión Crítica (Upside Down):** Recolectan recursos en una de las 4 zonas inseguras (*Bosque*, *Laboratorio*, *Centro Comercial*, *Alcantarillado*).
4. **Combate & Captura:** Si un Demogorgon ataca, el hilo entra en forcejeo. Si el monstruo vence (probabilidad del 33%), traslada al niño a la `Colmena` como prisionero. Por cada 8 capturas, se engendra un nuevo Demogorgon autónomo.
5. **Retorno Prioritario (Vuelta):** Al regresar, los niños que huyen tienen **prioridad absoluta e individual** sobre los grupos de ida en los portales.
6. **Almacenamiento & Recuperación:** Depositan la sangre en la `Radio WSQK`, descansan y deambulan por la calle antes de reiniciar el ciclo.

> **⚠️ Eventos Globales Asíncronos:** Un hilo `GeneradorEventos` altera periódicamente el comportamiento del ecosistema (*Apagón del Laboratorio*, *Tormenta del Upside Down*, *Intervención Psíquica de Eleven* y *La Red Mental*).

---

## ⚙️ Mecanismos de Concurrencia y Sincronización

| Mecanismo Java | Componente / Ubicación | Propósito Técnico |
| :--- | :--- | :--- |
| **`ReentrantLock` + `Condition`** | `Portal` | Control de salas de espera para formación de grupos y prioridad FIFO absoluta de retorno. |
| **`ReentrantLock` + `Condition`** | `AgrupacionZonas` | Mecanismo de **Pausa/Reanudación global** y despertar coordinado tras eventos. |
| **`LinkedBlockingQueue<Nino>`** | `CallePrincipal`, `SotanoByers`, `RadioWSQK`, `Colmena` | Gestión de flujos y colas seguras para subprocesos sin bloqueos manuales (*thread-safe FIFO*). |
| **`CopyOnWriteArrayList`** | `ZonaInsegura`, `UpsideDown` | Lectura concurrente de rankings y entidades evitando excepciones `ConcurrentModificationException`. |
| **`Semaphore`** | `Nino` / `Demogorgon` | Control del tiempo de combate y bloqueo atómico del niño durante el forcejeo. |
| **`AtomicBoolean` & `AtomicInteger`** | `Nino`, `RadioWSQK`, `Colmena` | Exclusividad atómica en selección de víctimas y conteo concurrente de sangre/capturas. |
| **`Thread` / `Runnable`** | `Nino`, `Demogorgon`, `GeneradorEventos` | Entidades concurrentes con ciclos de vida independientes y manejo de interrupciones. |
| **`ReentrantLock` (Singleton)** | `Logs` | Exclusión mutua en la persistencia de eventos en el fichero físico `hawkins.txt`. |

---

## 🌐 Arquitectura Distribuida (Sockets TCP/IP)

El proyecto desacopla el núcleo de simulación de la interfaz gráfica y de control:

* **Servidor de Control (`ServidorControl` en puerto 5011):** Permanece a la escucha e instancia un hilo independiente `ManejadorCliente` por cada monitor conectado, garantizando escalabilidad y nulo impacto en el rendimiento de la simulación.
* **Protocolo de Texto Ligero:** Comunicación basada en comandos transaccionales (`GET_DATA`, `PAUSAR`, `REANUDAR`).
* **Monitor Remoto Desacoplado (`VentanaMonitor`):** Cliente gráfico independiente con sondeo periódico vía `javax.swing.Timer` para renderizado en tiempo real.

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
```

---

## 📋 Requisitos

* **Java 21** o superior
* **Maven 3.x**
* IDE recomendado: **NetBeans** (el proyecto incluye `nbactions.xml` y `pom.xml`).

---

## 🛠️ Compilar y Ejecutar

**1. Compilar el proyecto:**
```bash
mvn clean package
```

**2. Ejecutar la simulación principal (Servidor + GUI + Hilos):**
```bash
mvn exec:java -Dexec.mainClass="Clases.Main"
```
*O ejecutando el JAR generado:*
```bash
java -jar target/BatallaHawkins-1.0-SNAPSHOT.jar
```

---

## 💻 Modo Distribuido (Monitor Remoto por Sockets)

Para visualizar las métricas y controlar la simulación en tiempo real desde una ventana independiente (en la misma máquina o en red local):

1. Inicia el servidor principal (`Clases.Main`), que quedará a la escucha en el puerto `5011`.
2. Arranca el cliente remoto:
```bash
mvn exec:java -Dexec.mainClass="Interfaz.MainMonitor"
```

> **Nota:** La `VentanaMonitor` consulta de forma automática el estado del servidor cada **500 ms** vía TCP/IP, mostrando el censo de entidades por portal/zona, prisioneros en la colmena, ranking de Demogorgons y permitiendo pausar/reanudar el flujo general.

---

## ⚙️ Configuración & Parámetros

* **Total de niños generados:** 1.500 entidades creadas de forma escalonada (intervalos de 0,5 a 2 segundos).
* **Demogorgon Alpha:** Se inicia con el hilo `D0000` y se engendra un nuevo monstruo por cada 8 niños capturados en la Colmena.
* **Eventos Globales:** Se disparan asíncronamente cada 30–60 segundos con una duración de 5–10 segundos (Apagón, Tormenta, Eleven, Red Mental).
* **Persistencia / Logs:** Registro concurrente en tiempo real en el archivo físico `hawkins.txt` ubicado en la raíz del proyecto.

---

## 🗺️ Estructura de Zonas (GUI)

### 🟢 Hawkins (Zonas Seguras)
* **CALLE_PRINCIPAL:** Área de aparición (spawn) y deambulación para camuflaje.
* **SOTANO_BYERS:** Punto de preparación previa al cruce de portales.
* **RADIO_WSQK:** Almacén común de sangre recolectada y descanso de supervivientes.

### 🟡 Portales (Zonas de Tránsito Crítico)
* Cuatro pasos de cruce exclusivo (**Bosque [2]**, **Laboratorio [3]**, **Centro Comercial [4]**, **Alcantarillado [2]**) con prioridad absoluta e individual de retorno.

### 🔴 Upside Down (Zonas Críticas)
* **Bosque, Laboratorio, Centro Comercial, Alcantarillado:** Áreas de extracción de recursos bajo amenaza de patrullas.
* **COLMENA:** Depósito de prisioneros y punto de rescate psíquico.

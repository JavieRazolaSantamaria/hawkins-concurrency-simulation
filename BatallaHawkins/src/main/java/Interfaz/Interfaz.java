/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaz;

import Clases.Logs;

public class Interfaz extends javax.swing.JFrame {

    /**
     * Creates new form Interfaz
     */
// 1. Atributo para acceder a las zonas
    private Clases.AgrupacionZonas zonas;

    // --- CONSTRUCTORES ---
    public Interfaz() {
        initComponents();
    }

    public Interfaz(Clases.AgrupacionZonas zonas) {
        this.zonas = zonas;
        initComponents(); 

        iniciarActualizadorSotano();
        iniciarActualizadorRadio();
        iniciarActualizadorCalle();
        iniciarActualizadorZonasInseguras();
        configurarScrollBarsPortales(); 
        iniciarActualizadoresPortales();
    }


    //       ACTUALIZADOR: SÓTANO BYERS
public void actualizarSotanoGUI() {
        actualizarListaNinos(zonas.getSotanoByers().getNinosEnSotano(), jTextAreaSotano);
    }

    private void iniciarActualizadorSotano() {
        new javax.swing.Timer(500, e -> {
            actualizarSotanoGUI();
        }).start();
    }


    //       ACTUALIZADOR: RADIO WSQK
public void actualizarRadioGUI() {
        actualizarListaNinos(zonas.getRadioWSQK().getNinosEnRadio(), jTextAreaRadio);
    }

    private void iniciarActualizadorRadio() {
        new javax.swing.Timer(500, e -> {
            actualizarRadioGUI();
            actualizarSangreGUI();
        }).start();
    }


    //       ACTUALIZADOR: CALLE PRINCIPAL
public void actualizarCalleGUI() {
        actualizarListaNinos(zonas.getCallePrincipal().getNinosEnCalle(), jTextAreaCalle);
    }
    private void iniciarActualizadorCalle() {
        new javax.swing.Timer(500, e -> {
            actualizarCalleGUI();
        }).start();
    }
    //        ACTUALIZADOR: CONTADOR SANGRE
    public void actualizarSangreGUI() {
        if (zonas == null) return;
        
        int sangre = zonas.getRadioWSQK().getSangreTotalAlmacenada();
        
        Cantidad_Sangre.setText(String.valueOf(sangre));
    }
    
    //       ACTUALIZADOR: Zonas Inseguras
    // Zona Bosque
        public void actualizarBosqueGUI(){
            if (zonas == null || zonas.isPausado()) {
        return; 
        }
        Clases.ZonaInsegura bosque = zonas.getUpsidedown().getZona("BOSQUE");
        if (bosque == null) return;
        java.util.List<Clases.Demogorgon> demogorgons = bosque.getDemogorgonsEnZona();
        
        StringBuilder sb = new StringBuilder();
        for (Clases.Demogorgon d : demogorgons) {
        sb.append(d.getIdDemogorgon()).append("\n");
        }
        jTextAreaDemogorgonsBosque.setText(sb.toString());
        
        StringBuilder sbN = new StringBuilder();
        for (Clases.Nino n : bosque.getNinosEnZona()) {
        sbN.append(n.getIdNino()).append("\n");
        }
        jTextAreaNinosBosque.setText(sbN.toString());
        }
    // Zona Laboratorio
    public void actualizarLaboratorioGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.ZonaInsegura lab = zonas.getUpsidedown().getZona("LABORATORIO");
        if (lab == null) return;

        StringBuilder sb = new StringBuilder();
        for (Clases.Demogorgon d : lab.getDemogorgonsEnZona()) {
            sb.append(d.getIdDemogorgon()).append("\n");
        }
        jTextAreaDemogorgonsLaboratorio.setText(sb.toString());
        StringBuilder sbN = new StringBuilder();
        for (Clases.Nino n : lab.getNinosEnZona()) sbN.append(n.getIdNino()).append("\n");
        jTextAreaNinosLaboratorio.setText(sbN.toString());
    }

    // Zona Centro Comercial
    public void actualizarCentroComercialGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.ZonaInsegura cc = zonas.getUpsidedown().getZona("CENTRO COMERCIAL");
        if (cc == null) return;

        StringBuilder sb = new StringBuilder();
        for (Clases.Demogorgon d : cc.getDemogorgonsEnZona()) {
            sb.append(d.getIdDemogorgon()).append("\n");
        }
        jTextAreaDemogorgonsCentroComercial.setText(sb.toString());
        StringBuilder sbN = new StringBuilder();
        for (Clases.Nino n : cc.getNinosEnZona()) sbN.append(n.getIdNino()).append("\n");
        jTextAreaNinosCentroComercial.setText(sbN.toString());
    }

    // Zona Alcantarillado
    public void actualizarAlcantarilladoGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.ZonaInsegura alc = zonas.getUpsidedown().getZona("ALCANTARILLADO");
        if (alc == null) return;

        StringBuilder sb = new StringBuilder();
        for (Clases.Demogorgon d : alc.getDemogorgonsEnZona()) {
            sb.append(d.getIdDemogorgon()).append("\n");
        }
        jTextAreaDemogorgonsAlcantarillado.setText(sb.toString());
        StringBuilder sbN = new StringBuilder();
        for (Clases.Nino n : alc.getNinosEnZona()) sbN.append(n.getIdNino()).append("\n");
        jTextAreaNinosAlcantarillado.setText(sbN.toString());
    }
    private void iniciarActualizadorZonasInseguras() {
    // Un solo timer para todas las zonas de la dimensión paralela
    new javax.swing.Timer(500, e -> {
        actualizarBosqueGUI();
        actualizarLaboratorioGUI();
        actualizarCentroComercialGUI();
        actualizarAlcantarilladoGUI();
        actualizarContadorCapturadosGUI();
        actualizarEventoActivoGUI();
    }).start();
    }
    //
    
    public void actualizarContadorCapturadosGUI() {
    if (zonas == null) return;

    Clases.Colmena colmena = zonas.getUpsidedown().getColmena();
    
    if (colmena != null) {
        int total = colmena.getNumPrisioneros(); 

        Ninos_Capturados.setText(String.valueOf(total));
    }
}
    
    private void actualizarListaNinos(java.util.List<Clases.Nino> ninos, javax.swing.JTextArea textArea) {
        if (zonas == null || zonas.isPausado()) return;
        
        StringBuilder sb = new StringBuilder();
        for (Clases.Nino n : ninos) {
            sb.append(n.getIdNino()).append("\n");
        }
        textArea.setText(sb.toString());
    }

    private void actualizarNinoCruzando(Clases.Nino nino, javax.swing.JTextArea textArea) {
        if (zonas == null || zonas.isPausado()) return;
        
        if (nino != null) {
            textArea.setText(nino.getIdNino());
        } else {
            textArea.setText("");
        }
    }

    public void actualizarPortalBosqueGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.Portal portal = zonas.getPortal(0);
        
        actualizarListaNinos(portal.getNinosEsperandoAlUpsideDown(), jTextArea_Bosque_Dentro);
        actualizarListaNinos(portal.getCruzando(), TextArea_Bosque_Salida); // 
        actualizarListaNinos(portal.getNinosEsperandoAHawkins(), TextArea_Bosque_Entrada);
    }

    public void actualizarPortalLaboratorioGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.Portal portal = zonas.getPortal(1);

        actualizarListaNinos(portal.getNinosEsperandoAlUpsideDown(), jTextArea_Laboratorio_Dentro);

        actualizarListaNinos(portal.getCruzando(), jTextArea_Laboratorio_Salida);

        actualizarListaNinos(portal.getNinosEsperandoAHawkins(), jTextArea_Laboratorio_Entrada);
    }

    public void actualizarPortalCentroComercialGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.Portal portal = zonas.getPortal(2);
        
        actualizarListaNinos(portal.getNinosEsperandoAlUpsideDown(), jTextArea_Centro_Comercial_Dentro);

        actualizarListaNinos(portal.getCruzando(), jTextArea_Centro_Comercial_Salida);

        actualizarListaNinos(portal.getNinosEsperandoAHawkins(), jTextArea_Centro_Comercial_Entrada);
    }

    public void actualizarPortalAlcantarilladoGUI() {
        if (zonas == null || zonas.isPausado()) return;
        Clases.Portal portal = zonas.getPortal(3);
        
        actualizarListaNinos(portal.getNinosEsperandoAlUpsideDown(), jTextArea_Alcantarillado_Dentro);

        actualizarListaNinos(portal.getCruzando(), jTextArea_Alcantarillado_Salida);

        actualizarListaNinos(portal.getNinosEsperandoAHawkins(), jTextArea_Alcantarillado_Entrada);
    }

    private void iniciarActualizadoresPortales() {
        new javax.swing.Timer(500, e -> {
            actualizarPortalBosqueGUI();
            actualizarPortalLaboratorioGUI();
            actualizarPortalCentroComercialGUI();
            actualizarPortalAlcantarilladoGUI();
        }).start();
    }


    private void configurarScrollBarsPortales() {

        javax.swing.JScrollPane[] scrolls = {
            jScrollPaneBosque_Entrada, jScrollPaneBosque_Salida, jScrollPaneBosque_Dentro,
            jScrollPane_Laboratorio_Entrada, jScrollPane_Laboratorio_Salida, jScrollPane_Laboratorio_Dentro,
            jScrollPane_Centro_Comercial_Entrada, jScrollPane_Centro_Comercial_Salida, jScrollPane_Centro_Comercial_Dentro,
            jScrollPane_Alcantarillado_Entrada, jScrollPane_Alcantarillado_Salida, jScrollPane_Alcantarillado_Dentro,ScrollPanel_DemogorgonsAlcantarillado,
            ScrollPanel_DemogorgonsBosque,ScrollPanel_DemogorgonsCentroComercial,ScrollPanel_DemogorgonsLaboratorio,ScrollPanel_NiñosAlcantarillado,ScrollPanel_NiñosBosque,
            ScrollPanel_NiñosCentroComercial,ScrollPanel_NiñosLaboratorio,ScrollPanel_Radio_WSQK,ScrollPanel_Sotano_Byers
        };

        for (javax.swing.JScrollPane scroll : scrolls) {

            scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

            scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
    }
    
    
    //        ACTUALIZADOR: EVENTO ACTIVO
    public void actualizarEventoActivoGUI() {
        if (zonas == null) return;

        String textoEvento = "Ninguno";

        if (zonas.isApagonLaboratorio()) {
            textoEvento = "APAGÓN DEL LABORATORIO";
        } else if (zonas.isTormentaUpsideDown()) {
            textoEvento = "TORMENTA UPSIDE DOWN";
        } else if (zonas.isIntervencionEleven()) {
            textoEvento = "INTERVENCIÓN DE ELEVEN";
        } else if (zonas.isRedMental()) {
            textoEvento = "LA RED MENTAL";
        }


        jTextAreaEventoActivo.setText(textoEvento); 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Panel_Zona_Hawkins = new javax.swing.JPanel();
        ScrollPanel_Calle_Principal = new javax.swing.JScrollPane();
        jTextAreaCalle = new javax.swing.JTextArea();
        Panel_Radio_WSQK = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // Pon aquí el nombre exacto de tu imagen de fondo
                java.net.URL ruta = getClass().getResource("/Imagenes/Textura_Radio.png");
                if (ruta != null) {
                    java.awt.Image imagenFondo = new javax.swing.ImageIcon(ruta).getImage();
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("No se encontró la imagen del fondo");
                }
            }
        };
        ScrollPanel_Radio_WSQK = new javax.swing.JScrollPane();
        jTextAreaRadio = new javax.swing.JTextArea();
        Cantidad_Sangre = new javax.swing.JTextField();
        Label_SANGRE = new javax.swing.JLabel();
        HAWKINS = new javax.swing.JLabel();
        Calle_Principal = new javax.swing.JLabel();
        Sotano_Byers = new javax.swing.JLabel();
        Calle_Principal2 = new javax.swing.JLabel();
        Imagen_Hawkins = new javax.swing.JLabel();
        Panel_SotanoByers = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // Pon aquí el nombre exacto de tu imagen de fondo
                java.net.URL ruta = getClass().getResource("/Imagenes/Textura_Sotano.PNG");
                if (ruta != null) {
                    java.awt.Image imagenFondo = new javax.swing.ImageIcon(ruta).getImage();
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("No se encontró la imagen del fondo");
                }
            }
        };
        ScrollPanel_Sotano_Byers = new javax.swing.JScrollPane();
        jTextAreaSotano = new javax.swing.JTextArea();
        Panel_Zona_Portales = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // Pon aquí el nombre exacto de tu imagen de fondo
                java.net.URL ruta = getClass().getResource("/Imagenes/Textura_Panel_Portal.png");
                if (ruta != null) {
                    java.awt.Image imagenFondo = new javax.swing.ImageIcon(ruta).getImage();
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("No se encontró la imagen del fondo");
                }
            }
        };
        PORTALES = new javax.swing.JLabel();
        Panel_Portal_Bosque = new javax.swing.JPanel();
        jScrollPaneBosque_Entrada = new javax.swing.JScrollPane();
        TextArea_Bosque_Entrada = new javax.swing.JTextArea();
        jScrollPaneBosque_Salida = new javax.swing.JScrollPane();
        TextArea_Bosque_Salida = new javax.swing.JTextArea();
        jScrollPaneBosque_Dentro = new javax.swing.JScrollPane();
        jTextArea_Bosque_Dentro = new javax.swing.JTextArea();
        Panel_Portal_Laboratorio = new javax.swing.JPanel();
        jScrollPane_Laboratorio_Salida = new javax.swing.JScrollPane();
        jTextArea_Laboratorio_Salida = new javax.swing.JTextArea();
        jScrollPane_Laboratorio_Entrada = new javax.swing.JScrollPane();
        jTextArea_Laboratorio_Entrada = new javax.swing.JTextArea();
        jScrollPane_Laboratorio_Dentro = new javax.swing.JScrollPane();
        jTextArea_Laboratorio_Dentro = new javax.swing.JTextArea();
        Panel_Portal_Centro_Comercial = new javax.swing.JPanel();
        jScrollPane_Centro_Comercial_Dentro = new javax.swing.JScrollPane();
        jTextArea_Centro_Comercial_Dentro = new javax.swing.JTextArea();
        jScrollPane_Centro_Comercial_Salida = new javax.swing.JScrollPane();
        jTextArea_Centro_Comercial_Salida = new javax.swing.JTextArea();
        jScrollPane_Centro_Comercial_Entrada = new javax.swing.JScrollPane();
        jTextArea_Centro_Comercial_Entrada = new javax.swing.JTextArea();
        Panel_Portal_Alcantarillado = new javax.swing.JPanel();
        jScrollPane_Alcantarillado_Salida = new javax.swing.JScrollPane();
        jTextArea_Alcantarillado_Salida = new javax.swing.JTextArea();
        jScrollPane_Alcantarillado_Entrada = new javax.swing.JScrollPane();
        jTextArea_Alcantarillado_Entrada = new javax.swing.JTextArea();
        jScrollPane_Alcantarillado_Dentro = new javax.swing.JScrollPane();
        jTextArea_Alcantarillado_Dentro = new javax.swing.JTextArea();
        Panel_Zona_Upsidedown = new javax.swing.JPanel();
        UPSIDEDOWN = new javax.swing.JLabel();
        Bosque = new javax.swing.JLabel();
        Panel_Bosque = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // Pon aquí el nombre exacto de tu imagen de fondo
                java.net.URL ruta = getClass().getResource("/Imagenes/Textura_Bosque.jpg");
                if (ruta != null) {
                    java.awt.Image imagenFondo = new javax.swing.ImageIcon(ruta).getImage();
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("No se encontró la imagen del fondo");
                }
            }
        };
        ScrollPanel_NiñosBosque = new javax.swing.JScrollPane();
        jTextAreaNinosBosque = new javax.swing.JTextArea();
        ScrollPanel_DemogorgonsBosque = new javax.swing.JScrollPane();
        jTextAreaDemogorgonsBosque = new javax.swing.JTextArea();
        Laboratorio = new javax.swing.JLabel();
        Panel_Portal_Laboratorio1 = new javax.swing.JPanel();
        ScrollPanel_NiñosLaboratorio = new javax.swing.JScrollPane();
        jTextAreaNinosLaboratorio = new javax.swing.JTextArea();
        ScrollPanel_DemogorgonsLaboratorio = new javax.swing.JScrollPane();
        jTextAreaDemogorgonsLaboratorio = new javax.swing.JTextArea();
        Textura_Laboratorio = new javax.swing.JLabel();
        Centro_Comercial = new javax.swing.JLabel();
        Panel_Portal_Centro_Comercial1 = new javax.swing.JPanel();
        ScrollPanel_NiñosCentroComercial = new javax.swing.JScrollPane();
        jTextAreaNinosCentroComercial = new javax.swing.JTextArea();
        ScrollPanel_DemogorgonsCentroComercial = new javax.swing.JScrollPane();
        jTextAreaDemogorgonsCentroComercial = new javax.swing.JTextArea();
        Textura_CentroComercial = new javax.swing.JLabel();
        Alcantarillado = new javax.swing.JLabel();
        Panel_Portal_Alcantarillado1 = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // Pon aquí el nombre exacto de tu imagen de fondo
                java.net.URL ruta = getClass().getResource("/Imagenes/Textura_Alcantarillado.png");
                if (ruta != null) {
                    java.awt.Image imagenFondo = new javax.swing.ImageIcon(ruta).getImage();
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                } else {
                    System.err.println("No se encontró la imagen del fondo");
                }
            }
        };
        ScrollPanel_NiñosAlcantarillado = new javax.swing.JScrollPane();
        jTextAreaNinosAlcantarillado = new javax.swing.JTextArea();
        ScrollPanel_DemogorgonsAlcantarillado = new javax.swing.JScrollPane();
        jTextAreaDemogorgonsAlcantarillado = new javax.swing.JTextArea();
        Panel_Estadisticas = new javax.swing.JPanel();
        Ninos_Capturados = new javax.swing.JTextField();
        Texto_Eventos = new javax.swing.JLabel();
        Demogorgon = new javax.swing.JLabel();
        Vecna = new javax.swing.JLabel();
        Texto_NIÑOS_CAPTURADOS = new javax.swing.JLabel();
        jScrollPaneEventoActivo = new javax.swing.JScrollPane();
        jTextAreaEventoActivo = new javax.swing.JTextArea();
        Texto_Principal = new javax.swing.JLabel();
        Boton_Pausa = new javax.swing.JButton();
        Boton_Reanudar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Panel_Zona_Hawkins.setBackground(new java.awt.Color(204, 255, 204));
        Panel_Zona_Hawkins.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 0), 3));
        Panel_Zona_Hawkins.setPreferredSize(new java.awt.Dimension(300, 500));

        ScrollPanel_Calle_Principal.setPreferredSize(new java.awt.Dimension(260, 110));

        jTextAreaCalle.setEditable(false);
        jTextAreaCalle.setColumns(20);
        jTextAreaCalle.setRows(5);
        ScrollPanel_Calle_Principal.setViewportView(jTextAreaCalle);

        Panel_Radio_WSQK.setBackground(new java.awt.Color(255, 255, 255));
        Panel_Radio_WSQK.setOpaque(false);

        jTextAreaRadio.setEditable(false);
        jTextAreaRadio.setColumns(20);
        jTextAreaRadio.setRows(5);
        ScrollPanel_Radio_WSQK.setViewportView(jTextAreaRadio);

        Cantidad_Sangre.setEditable(false);
        Cantidad_Sangre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Cantidad_Sangre.setText("0");
        Cantidad_Sangre.setPreferredSize(new java.awt.Dimension(60, 40));
        Cantidad_Sangre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cantidad_SangreActionPerformed(evt);
            }
        });

        Label_SANGRE.setFont(new java.awt.Font("Segoe UI Emoji", 1, 12)); // NOI18N
        Label_SANGRE.setForeground(new java.awt.Color(255, 0, 0));
        Label_SANGRE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Label_SANGRE.setText("SANGRE");
        Label_SANGRE.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Label_SANGRE.setOpaque(true);

        javax.swing.GroupLayout Panel_Radio_WSQKLayout = new javax.swing.GroupLayout(Panel_Radio_WSQK);
        Panel_Radio_WSQK.setLayout(Panel_Radio_WSQKLayout);
        Panel_Radio_WSQKLayout.setHorizontalGroup(
            Panel_Radio_WSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Radio_WSQKLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(ScrollPanel_Radio_WSQK, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(Panel_Radio_WSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Cantidad_Sangre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Label_SANGRE, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        Panel_Radio_WSQKLayout.setVerticalGroup(
            Panel_Radio_WSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Radio_WSQKLayout.createSequentialGroup()
                .addGroup(Panel_Radio_WSQKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_Radio_WSQKLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(Cantidad_Sangre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_Radio_WSQKLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(Label_SANGRE, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_Radio_WSQKLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ScrollPanel_Radio_WSQK, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        HAWKINS.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        HAWKINS.setText("HAWKINS");

        Calle_Principal.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Calle_Principal.setText("Calle Principal");

        Sotano_Byers.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Sotano_Byers.setText("Sotano Byers");

        Calle_Principal2.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Calle_Principal2.setText("Radio WSQK");

        Imagen_Hawkins.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Cartel_Hawkins.jpg"))); // NOI18N

        Panel_SotanoByers.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ScrollPanel_Sotano_Byers.setPreferredSize(new java.awt.Dimension(260, 110));

        jTextAreaSotano.setEditable(false);
        jTextAreaSotano.setColumns(20);
        jTextAreaSotano.setRows(5);
        ScrollPanel_Sotano_Byers.setViewportView(jTextAreaSotano);

        javax.swing.GroupLayout Panel_SotanoByersLayout = new javax.swing.GroupLayout(Panel_SotanoByers);
        Panel_SotanoByers.setLayout(Panel_SotanoByersLayout);
        Panel_SotanoByersLayout.setHorizontalGroup(
            Panel_SotanoByersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPanel_Sotano_Byers, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );
        Panel_SotanoByersLayout.setVerticalGroup(
            Panel_SotanoByersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPanel_Sotano_Byers, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Panel_Zona_HawkinsLayout = new javax.swing.GroupLayout(Panel_Zona_Hawkins);
        Panel_Zona_Hawkins.setLayout(Panel_Zona_HawkinsLayout);
        Panel_Zona_HawkinsLayout.setHorizontalGroup(
            Panel_Zona_HawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                .addGroup(Panel_Zona_HawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(HAWKINS))
                    .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(Calle_Principal))
                    .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(ScrollPanel_Calle_Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(Imagen_Hawkins))
                    .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(Panel_Zona_HawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Panel_Radio_WSQK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Calle_Principal2)))
                    .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(Panel_Zona_HawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Panel_SotanoByers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Sotano_Byers))))
                .addGap(228, 228, 228))
        );
        Panel_Zona_HawkinsLayout.setVerticalGroup(
            Panel_Zona_HawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(HAWKINS)
                .addGap(4, 4, 4)
                .addComponent(Calle_Principal)
                .addGap(9, 9, 9)
                .addGroup(Panel_Zona_HawkinsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollPanel_Calle_Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel_Zona_HawkinsLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(Imagen_Hawkins, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(Sotano_Byers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_SotanoByers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Calle_Principal2)
                .addGap(18, 18, 18)
                .addComponent(Panel_Radio_WSQK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Panel_Zona_Portales.setBackground(new java.awt.Color(255, 153, 204));
        Panel_Zona_Portales.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 255), 3));
        Panel_Zona_Portales.setPreferredSize(new java.awt.Dimension(300, 500));

        PORTALES.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        PORTALES.setText("PORTALES");

        Panel_Portal_Bosque.setBackground(new java.awt.Color(156, 28, 156));
        Panel_Portal_Bosque.setOpaque(false);
        Panel_Portal_Bosque.setPreferredSize(new java.awt.Dimension(260, 100));

        TextArea_Bosque_Entrada.setEditable(false);
        TextArea_Bosque_Entrada.setColumns(20);
        TextArea_Bosque_Entrada.setRows(5);
        jScrollPaneBosque_Entrada.setViewportView(TextArea_Bosque_Entrada);

        TextArea_Bosque_Salida.setEditable(false);
        TextArea_Bosque_Salida.setColumns(20);
        TextArea_Bosque_Salida.setRows(5);
        jScrollPaneBosque_Salida.setViewportView(TextArea_Bosque_Salida);

        jTextArea_Bosque_Dentro.setEditable(false);
        jTextArea_Bosque_Dentro.setColumns(20);
        jTextArea_Bosque_Dentro.setRows(5);
        jScrollPaneBosque_Dentro.setViewportView(jTextArea_Bosque_Dentro);

        javax.swing.GroupLayout Panel_Portal_BosqueLayout = new javax.swing.GroupLayout(Panel_Portal_Bosque);
        Panel_Portal_Bosque.setLayout(Panel_Portal_BosqueLayout);
        Panel_Portal_BosqueLayout.setHorizontalGroup(
            Panel_Portal_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_BosqueLayout.createSequentialGroup()
                .addContainerGap(94, Short.MAX_VALUE)
                .addComponent(jScrollPaneBosque_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPaneBosque_Entrada, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(Panel_Portal_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_Portal_BosqueLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPaneBosque_Dentro, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(183, Short.MAX_VALUE)))
        );
        Panel_Portal_BosqueLayout.setVerticalGroup(
            Panel_Portal_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_BosqueLayout.createSequentialGroup()
                .addComponent(jScrollPaneBosque_Entrada, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_BosqueLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPaneBosque_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(Panel_Portal_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_Portal_BosqueLayout.createSequentialGroup()
                    .addComponent(jScrollPaneBosque_Dentro, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        Panel_Portal_Laboratorio.setBackground(new java.awt.Color(156, 28, 156));
        Panel_Portal_Laboratorio.setOpaque(false);

        jTextArea_Laboratorio_Salida.setColumns(20);
        jTextArea_Laboratorio_Salida.setRows(5);
        jScrollPane_Laboratorio_Salida.setViewportView(jTextArea_Laboratorio_Salida);

        jTextArea_Laboratorio_Entrada.setColumns(20);
        jTextArea_Laboratorio_Entrada.setRows(5);
        jScrollPane_Laboratorio_Entrada.setViewportView(jTextArea_Laboratorio_Entrada);

        jTextArea_Laboratorio_Dentro.setColumns(20);
        jTextArea_Laboratorio_Dentro.setRows(5);
        jScrollPane_Laboratorio_Dentro.setViewportView(jTextArea_Laboratorio_Dentro);

        javax.swing.GroupLayout Panel_Portal_LaboratorioLayout = new javax.swing.GroupLayout(Panel_Portal_Laboratorio);
        Panel_Portal_Laboratorio.setLayout(Panel_Portal_LaboratorioLayout);
        Panel_Portal_LaboratorioLayout.setHorizontalGroup(
            Panel_Portal_LaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_LaboratorioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane_Laboratorio_Dentro, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane_Laboratorio_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_Laboratorio_Entrada, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        Panel_Portal_LaboratorioLayout.setVerticalGroup(
            Panel_Portal_LaboratorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_Laboratorio_Entrada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_LaboratorioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane_Laboratorio_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addComponent(jScrollPane_Laboratorio_Dentro)
        );

        Panel_Portal_Centro_Comercial.setBackground(new java.awt.Color(156, 28, 156));
        Panel_Portal_Centro_Comercial.setOpaque(false);

        jTextArea_Centro_Comercial_Dentro.setColumns(20);
        jTextArea_Centro_Comercial_Dentro.setRows(5);
        jScrollPane_Centro_Comercial_Dentro.setViewportView(jTextArea_Centro_Comercial_Dentro);

        jTextArea_Centro_Comercial_Salida.setColumns(20);
        jTextArea_Centro_Comercial_Salida.setRows(5);
        jScrollPane_Centro_Comercial_Salida.setViewportView(jTextArea_Centro_Comercial_Salida);

        jTextArea_Centro_Comercial_Entrada.setColumns(20);
        jTextArea_Centro_Comercial_Entrada.setRows(5);
        jScrollPane_Centro_Comercial_Entrada.setViewportView(jTextArea_Centro_Comercial_Entrada);

        javax.swing.GroupLayout Panel_Portal_Centro_ComercialLayout = new javax.swing.GroupLayout(Panel_Portal_Centro_Comercial);
        Panel_Portal_Centro_Comercial.setLayout(Panel_Portal_Centro_ComercialLayout);
        Panel_Portal_Centro_ComercialLayout.setHorizontalGroup(
            Panel_Portal_Centro_ComercialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_Centro_ComercialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane_Centro_Comercial_Dentro, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_Centro_Comercial_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_Centro_Comercial_Entrada, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addContainerGap())
        );
        Panel_Portal_Centro_ComercialLayout.setVerticalGroup(
            Panel_Portal_Centro_ComercialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_Centro_ComercialLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane_Centro_Comercial_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_Centro_ComercialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_Portal_Centro_ComercialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane_Centro_Comercial_Entrada, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(jScrollPane_Centro_Comercial_Dentro))
                .addContainerGap())
        );

        Panel_Portal_Alcantarillado.setBackground(new java.awt.Color(156, 28, 156));
        Panel_Portal_Alcantarillado.setOpaque(false);

        jTextArea_Alcantarillado_Salida.setColumns(20);
        jTextArea_Alcantarillado_Salida.setRows(5);
        jScrollPane_Alcantarillado_Salida.setViewportView(jTextArea_Alcantarillado_Salida);

        jTextArea_Alcantarillado_Entrada.setColumns(20);
        jTextArea_Alcantarillado_Entrada.setRows(5);
        jScrollPane_Alcantarillado_Entrada.setViewportView(jTextArea_Alcantarillado_Entrada);

        jTextArea_Alcantarillado_Dentro.setColumns(20);
        jTextArea_Alcantarillado_Dentro.setRows(5);
        jScrollPane_Alcantarillado_Dentro.setViewportView(jTextArea_Alcantarillado_Dentro);

        javax.swing.GroupLayout Panel_Portal_AlcantarilladoLayout = new javax.swing.GroupLayout(Panel_Portal_Alcantarillado);
        Panel_Portal_Alcantarillado.setLayout(Panel_Portal_AlcantarilladoLayout);
        Panel_Portal_AlcantarilladoLayout.setHorizontalGroup(
            Panel_Portal_AlcantarilladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_AlcantarilladoLayout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addComponent(jScrollPane_Alcantarillado_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane_Alcantarillado_Entrada, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(Panel_Portal_AlcantarilladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel_Portal_AlcantarilladoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane_Alcantarillado_Dentro, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(183, Short.MAX_VALUE)))
        );
        Panel_Portal_AlcantarilladoLayout.setVerticalGroup(
            Panel_Portal_AlcantarilladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_AlcantarilladoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane_Alcantarillado_Entrada, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_AlcantarilladoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane_Alcantarillado_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(Panel_Portal_AlcantarilladoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Portal_AlcantarilladoLayout.createSequentialGroup()
                    .addContainerGap(8, Short.MAX_VALUE)
                    .addComponent(jScrollPane_Alcantarillado_Dentro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout Panel_Zona_PortalesLayout = new javax.swing.GroupLayout(Panel_Zona_Portales);
        Panel_Zona_Portales.setLayout(Panel_Zona_PortalesLayout);
        Panel_Zona_PortalesLayout.setHorizontalGroup(
            Panel_Zona_PortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(Panel_Portal_Bosque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                .addGroup(Panel_Zona_PortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(PORTALES))
                    .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(Panel_Portal_Laboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(Panel_Portal_Centro_Comercial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(Panel_Portal_Alcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        Panel_Zona_PortalesLayout.setVerticalGroup(
            Panel_Zona_PortalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Zona_PortalesLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(PORTALES)
                .addGap(9, 9, 9)
                .addComponent(Panel_Portal_Bosque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(Panel_Portal_Laboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(Panel_Portal_Centro_Comercial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(Panel_Portal_Alcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Panel_Zona_Upsidedown.setBackground(new java.awt.Color(255, 102, 102));
        Panel_Zona_Upsidedown.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3));
        Panel_Zona_Upsidedown.setPreferredSize(new java.awt.Dimension(300, 500));

        UPSIDEDOWN.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        UPSIDEDOWN.setText("UPSIDEDOWN");

        Bosque.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Bosque.setText("Bosque");

        Panel_Bosque.setBackground(new java.awt.Color(153, 255, 153));
        Panel_Bosque.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTextAreaNinosBosque.setEditable(false);
        jTextAreaNinosBosque.setColumns(20);
        jTextAreaNinosBosque.setRows(5);
        ScrollPanel_NiñosBosque.setViewportView(jTextAreaNinosBosque);

        jTextAreaDemogorgonsBosque.setEditable(false);
        jTextAreaDemogorgonsBosque.setColumns(20);
        jTextAreaDemogorgonsBosque.setRows(5);
        ScrollPanel_DemogorgonsBosque.setViewportView(jTextAreaDemogorgonsBosque);

        javax.swing.GroupLayout Panel_BosqueLayout = new javax.swing.GroupLayout(Panel_Bosque);
        Panel_Bosque.setLayout(Panel_BosqueLayout);
        Panel_BosqueLayout.setHorizontalGroup(
            Panel_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_BosqueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPanel_NiñosBosque, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(ScrollPanel_DemogorgonsBosque, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        Panel_BosqueLayout.setVerticalGroup(
            Panel_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_BosqueLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_BosqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ScrollPanel_DemogorgonsBosque, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(ScrollPanel_NiñosBosque, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        Laboratorio.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Laboratorio.setText("Laboratorio");

        jTextAreaNinosLaboratorio.setEditable(false);
        jTextAreaNinosLaboratorio.setColumns(20);
        jTextAreaNinosLaboratorio.setRows(5);
        ScrollPanel_NiñosLaboratorio.setViewportView(jTextAreaNinosLaboratorio);

        jTextAreaDemogorgonsLaboratorio.setEditable(false);
        jTextAreaDemogorgonsLaboratorio.setColumns(20);
        jTextAreaDemogorgonsLaboratorio.setRows(5);
        ScrollPanel_DemogorgonsLaboratorio.setViewportView(jTextAreaDemogorgonsLaboratorio);

        Textura_Laboratorio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Textura_Laboratorio.png"))); // NOI18N

        javax.swing.GroupLayout Panel_Portal_Laboratorio1Layout = new javax.swing.GroupLayout(Panel_Portal_Laboratorio1);
        Panel_Portal_Laboratorio1.setLayout(Panel_Portal_Laboratorio1Layout);
        Panel_Portal_Laboratorio1Layout.setHorizontalGroup(
            Panel_Portal_Laboratorio1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_Laboratorio1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPanel_NiñosLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ScrollPanel_DemogorgonsLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(Panel_Portal_Laboratorio1Layout.createSequentialGroup()
                .addComponent(Textura_Laboratorio)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        Panel_Portal_Laboratorio1Layout.setVerticalGroup(
            Panel_Portal_Laboratorio1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_Laboratorio1Layout.createSequentialGroup()
                .addComponent(Textura_Laboratorio)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(Panel_Portal_Laboratorio1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_Portal_Laboratorio1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollPanel_DemogorgonsLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(ScrollPanel_NiñosLaboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        Centro_Comercial.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Centro_Comercial.setText("Centro Comercial");

        Panel_Portal_Centro_Comercial1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextAreaNinosCentroComercial.setEditable(false);
        jTextAreaNinosCentroComercial.setColumns(20);
        jTextAreaNinosCentroComercial.setRows(5);
        ScrollPanel_NiñosCentroComercial.setViewportView(jTextAreaNinosCentroComercial);

        Panel_Portal_Centro_Comercial1.add(ScrollPanel_NiñosCentroComercial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 70));

        jTextAreaDemogorgonsCentroComercial.setEditable(false);
        jTextAreaDemogorgonsCentroComercial.setColumns(20);
        jTextAreaDemogorgonsCentroComercial.setRows(5);
        ScrollPanel_DemogorgonsCentroComercial.setViewportView(jTextAreaDemogorgonsCentroComercial);

        Panel_Portal_Centro_Comercial1.add(ScrollPanel_DemogorgonsCentroComercial, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 100, 70));

        Textura_CentroComercial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Textura_CentroComerical.png"))); // NOI18N
        Panel_Portal_Centro_Comercial1.add(Textura_CentroComercial, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        Alcantarillado.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        Alcantarillado.setText("Alcantarillado");

        Panel_Portal_Alcantarillado1.setBackground(new java.awt.Color(153, 153, 153));

        jTextAreaNinosAlcantarillado.setEditable(false);
        jTextAreaNinosAlcantarillado.setColumns(20);
        jTextAreaNinosAlcantarillado.setRows(5);
        ScrollPanel_NiñosAlcantarillado.setViewportView(jTextAreaNinosAlcantarillado);

        jTextAreaDemogorgonsAlcantarillado.setEditable(false);
        jTextAreaDemogorgonsAlcantarillado.setColumns(20);
        jTextAreaDemogorgonsAlcantarillado.setRows(5);
        ScrollPanel_DemogorgonsAlcantarillado.setViewportView(jTextAreaDemogorgonsAlcantarillado);

        javax.swing.GroupLayout Panel_Portal_Alcantarillado1Layout = new javax.swing.GroupLayout(Panel_Portal_Alcantarillado1);
        Panel_Portal_Alcantarillado1.setLayout(Panel_Portal_Alcantarillado1Layout);
        Panel_Portal_Alcantarillado1Layout.setHorizontalGroup(
            Panel_Portal_Alcantarillado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_Alcantarillado1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ScrollPanel_NiñosAlcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(ScrollPanel_DemogorgonsAlcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        Panel_Portal_Alcantarillado1Layout.setVerticalGroup(
            Panel_Portal_Alcantarillado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Portal_Alcantarillado1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(Panel_Portal_Alcantarillado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ScrollPanel_DemogorgonsAlcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ScrollPanel_NiñosAlcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout Panel_Zona_UpsidedownLayout = new javax.swing.GroupLayout(Panel_Zona_Upsidedown);
        Panel_Zona_Upsidedown.setLayout(Panel_Zona_UpsidedownLayout);
        Panel_Zona_UpsidedownLayout.setHorizontalGroup(
            Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_Portal_Alcantarillado1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Bosque, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                            .addGap(70, 70, 70)
                            .addComponent(UPSIDEDOWN))
                        .addComponent(Laboratorio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Panel_Portal_Laboratorio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Centro_Comercial, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Panel_Portal_Centro_Comercial1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Alcantarillado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Panel_Bosque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        Panel_Zona_UpsidedownLayout.setVerticalGroup(
            Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                .addGroup(Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(Bosque))
                            .addComponent(UPSIDEDOWN)))
                    .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(Panel_Bosque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Laboratorio)
                    .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(Panel_Portal_Laboratorio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(Panel_Zona_UpsidedownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Centro_Comercial)
                    .addGroup(Panel_Zona_UpsidedownLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(Panel_Portal_Centro_Comercial1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(Alcantarillado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_Portal_Alcantarillado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Panel_Estadisticas.setBackground(new java.awt.Color(0, 255, 153));
        Panel_Estadisticas.setOpaque(false);

        Ninos_Capturados.setEditable(false);
        Ninos_Capturados.setBackground(new java.awt.Color(255, 153, 153));
        Ninos_Capturados.setFont(new java.awt.Font("Segoe UI Emoji", 1, 36)); // NOI18N
        Ninos_Capturados.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Ninos_Capturados.setText("0");
        Ninos_Capturados.setOpaque(true);
        Ninos_Capturados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ninos_CapturadosActionPerformed(evt);
            }
        });

        Texto_Eventos.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        Texto_Eventos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Texto_Eventos.setText("Evento Activo:");

        Demogorgon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Demogorgon.png"))); // NOI18N
        Demogorgon.setText("jLabel3");

        Vecna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Vecna.png"))); // NOI18N

        Texto_NIÑOS_CAPTURADOS.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        Texto_NIÑOS_CAPTURADOS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Texto_NIÑOS_CAPTURADOS.setText("Niños capturados");

        jTextAreaEventoActivo.setColumns(20);
        jTextAreaEventoActivo.setRows(5);
        jScrollPaneEventoActivo.setViewportView(jTextAreaEventoActivo);

        javax.swing.GroupLayout Panel_EstadisticasLayout = new javax.swing.GroupLayout(Panel_Estadisticas);
        Panel_Estadisticas.setLayout(Panel_EstadisticasLayout);
        Panel_EstadisticasLayout.setHorizontalGroup(
            Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                .addGroup(Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Texto_Eventos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jScrollPaneEventoActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                .addGroup(Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(Texto_NIÑOS_CAPTURADOS, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(Ninos_Capturados, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Demogorgon, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addComponent(Vecna))))
        );
        Panel_EstadisticasLayout.setVerticalGroup(
            Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                        .addComponent(Texto_NIÑOS_CAPTURADOS)
                        .addGap(9, 9, 9)
                        .addComponent(Ninos_Capturados, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel_EstadisticasLayout.createSequentialGroup()
                            .addComponent(Texto_Eventos)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPaneEventoActivo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Panel_EstadisticasLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addGroup(Panel_EstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Demogorgon)
                                .addComponent(Vecna)))))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        Texto_Principal.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        Texto_Principal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Texto_Principal.setText("Simulacion de Stranger Things");
        Texto_Principal.setToolTipText("");

        Boton_Pausa.setText("PAUSAR");
        Boton_Pausa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_PausaActionPerformed(evt);
            }
        });

        Boton_Reanudar.setText("REANUDAR");
        Boton_Reanudar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_ReanudarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Texto_Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Boton_Reanudar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Boton_Pausa, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(Panel_Zona_Hawkins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)
                            .addComponent(Panel_Zona_Portales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)
                            .addComponent(Panel_Zona_Upsidedown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(Panel_Estadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(Boton_Pausa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Texto_Principal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Boton_Reanudar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Panel_Zona_Hawkins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Panel_Zona_Portales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Panel_Zona_Upsidedown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Panel_Estadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Cantidad_SangreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cantidad_SangreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Cantidad_SangreActionPerformed

    private void Ninos_CapturadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ninos_CapturadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Ninos_CapturadosActionPerformed

    private void Boton_PausaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_PausaActionPerformed
        // TODO add your handling code here:
        if (zonas != null) {
            zonas.pausar();
            System.out.println("Simulación pausada.");
            Logs.getInstance().log("SIMULACIÓN PARADA");
        }
    }//GEN-LAST:event_Boton_PausaActionPerformed

    private void Boton_ReanudarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_ReanudarActionPerformed
        // TODO add your handling code here:
        if (zonas != null) {
            zonas.reanudar();
            System.out.println("Simulación reanudada.");
            Logs.getInstance().log("SIMULACIÓN REANUDADA");
        }
    }//GEN-LAST:event_Boton_ReanudarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Alcantarillado;
    private javax.swing.JLabel Bosque;
    private javax.swing.JButton Boton_Pausa;
    private javax.swing.JButton Boton_Reanudar;
    private javax.swing.JLabel Calle_Principal;
    private javax.swing.JLabel Calle_Principal2;
    private javax.swing.JTextField Cantidad_Sangre;
    private javax.swing.JLabel Centro_Comercial;
    private javax.swing.JLabel Demogorgon;
    private javax.swing.JLabel HAWKINS;
    private javax.swing.JLabel Imagen_Hawkins;
    private javax.swing.JLabel Label_SANGRE;
    private javax.swing.JLabel Laboratorio;
    private javax.swing.JTextField Ninos_Capturados;
    private javax.swing.JLabel PORTALES;
    private javax.swing.JPanel Panel_Bosque;
    private javax.swing.JPanel Panel_Estadisticas;
    private javax.swing.JPanel Panel_Portal_Alcantarillado;
    private javax.swing.JPanel Panel_Portal_Alcantarillado1;
    private javax.swing.JPanel Panel_Portal_Bosque;
    private javax.swing.JPanel Panel_Portal_Centro_Comercial;
    private javax.swing.JPanel Panel_Portal_Centro_Comercial1;
    private javax.swing.JPanel Panel_Portal_Laboratorio;
    private javax.swing.JPanel Panel_Portal_Laboratorio1;
    private javax.swing.JPanel Panel_Radio_WSQK;
    private javax.swing.JPanel Panel_SotanoByers;
    private javax.swing.JPanel Panel_Zona_Hawkins;
    private javax.swing.JPanel Panel_Zona_Portales;
    private javax.swing.JPanel Panel_Zona_Upsidedown;
    private javax.swing.JScrollPane ScrollPanel_Calle_Principal;
    private javax.swing.JScrollPane ScrollPanel_DemogorgonsAlcantarillado;
    private javax.swing.JScrollPane ScrollPanel_DemogorgonsBosque;
    private javax.swing.JScrollPane ScrollPanel_DemogorgonsCentroComercial;
    private javax.swing.JScrollPane ScrollPanel_DemogorgonsLaboratorio;
    private javax.swing.JScrollPane ScrollPanel_NiñosAlcantarillado;
    private javax.swing.JScrollPane ScrollPanel_NiñosBosque;
    private javax.swing.JScrollPane ScrollPanel_NiñosCentroComercial;
    private javax.swing.JScrollPane ScrollPanel_NiñosLaboratorio;
    private javax.swing.JScrollPane ScrollPanel_Radio_WSQK;
    private javax.swing.JScrollPane ScrollPanel_Sotano_Byers;
    private javax.swing.JLabel Sotano_Byers;
    private javax.swing.JTextArea TextArea_Bosque_Entrada;
    private javax.swing.JTextArea TextArea_Bosque_Salida;
    private javax.swing.JLabel Texto_Eventos;
    private javax.swing.JLabel Texto_NIÑOS_CAPTURADOS;
    private javax.swing.JLabel Texto_Principal;
    private javax.swing.JLabel Textura_CentroComercial;
    private javax.swing.JLabel Textura_Laboratorio;
    private javax.swing.JLabel UPSIDEDOWN;
    private javax.swing.JLabel Vecna;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneBosque_Dentro;
    private javax.swing.JScrollPane jScrollPaneBosque_Entrada;
    private javax.swing.JScrollPane jScrollPaneBosque_Salida;
    private javax.swing.JScrollPane jScrollPaneEventoActivo;
    private javax.swing.JScrollPane jScrollPane_Alcantarillado_Dentro;
    private javax.swing.JScrollPane jScrollPane_Alcantarillado_Entrada;
    private javax.swing.JScrollPane jScrollPane_Alcantarillado_Salida;
    private javax.swing.JScrollPane jScrollPane_Centro_Comercial_Dentro;
    private javax.swing.JScrollPane jScrollPane_Centro_Comercial_Entrada;
    private javax.swing.JScrollPane jScrollPane_Centro_Comercial_Salida;
    private javax.swing.JScrollPane jScrollPane_Laboratorio_Dentro;
    private javax.swing.JScrollPane jScrollPane_Laboratorio_Entrada;
    private javax.swing.JScrollPane jScrollPane_Laboratorio_Salida;
    private javax.swing.JTextArea jTextAreaCalle;
    private javax.swing.JTextArea jTextAreaDemogorgonsAlcantarillado;
    private javax.swing.JTextArea jTextAreaDemogorgonsBosque;
    private javax.swing.JTextArea jTextAreaDemogorgonsCentroComercial;
    private javax.swing.JTextArea jTextAreaDemogorgonsLaboratorio;
    private javax.swing.JTextArea jTextAreaEventoActivo;
    private javax.swing.JTextArea jTextAreaNinosAlcantarillado;
    private javax.swing.JTextArea jTextAreaNinosBosque;
    private javax.swing.JTextArea jTextAreaNinosCentroComercial;
    private javax.swing.JTextArea jTextAreaNinosLaboratorio;
    private javax.swing.JTextArea jTextAreaRadio;
    private javax.swing.JTextArea jTextAreaSotano;
    private javax.swing.JTextArea jTextArea_Alcantarillado_Dentro;
    private javax.swing.JTextArea jTextArea_Alcantarillado_Entrada;
    private javax.swing.JTextArea jTextArea_Alcantarillado_Salida;
    private javax.swing.JTextArea jTextArea_Bosque_Dentro;
    private javax.swing.JTextArea jTextArea_Centro_Comercial_Dentro;
    private javax.swing.JTextArea jTextArea_Centro_Comercial_Entrada;
    private javax.swing.JTextArea jTextArea_Centro_Comercial_Salida;
    private javax.swing.JTextArea jTextArea_Laboratorio_Dentro;
    private javax.swing.JTextArea jTextArea_Laboratorio_Entrada;
    private javax.swing.JTextArea jTextArea_Laboratorio_Salida;
    // End of variables declaration//GEN-END:variables
}

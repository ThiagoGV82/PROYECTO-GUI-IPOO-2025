import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;

public class GUIResto extends JFrame {

    // Atributos de la aplicación
    private Resto resto;
    private int numeroMesaSeleccionada;

    // Objetos Gráficos
    // PANEL MENU
    private JButton[] boton;
    private JLabel[] etiqueta;
    private JPanel panelMenu;

    // PANEL RESTO
    private JPanel panelResto;
    private JButton[] botonMesas;

    // PANEL MESA
    private JPanel panelMesa;
    private JLabel etiquetaMesaSeleccionada;
    private JPanel panelDetalle;
    private JLabel etiquetaDetallePedido;
    private JButton botonAgregarItem;
    private JPanel panelOcuparDesocupar;
    private JButton botonOcuparMesa;
    private JButton botonDesocuparMesa;

    public GUIResto(Resto r) {
        super("Bienvenido al IPOO-Resto: Mesas");
        resto = r;
        numeroMesaSeleccionada = -1;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 800));
        setVisible(true);

        inicializarpanelResto();
        inicializarPanelMenu();
        inicializarpanelMesa();
        armarBotones();
        armarEtiquetas();

        getContentPane().setLayout(new BorderLayout());

        // Agrego cada panel al panel de contenido
        getContentPane().add(panelResto, BorderLayout.CENTER);
        getContentPane().add(panelMesa, BorderLayout.EAST);
        getContentPane().add(panelMenu, BorderLayout.SOUTH);

        this.setVisible(true);
        this.setResizable(true); 
    }

    private void inicializarpanelResto() {

        // Crea el panel y setea el layout
        panelResto = new JPanel();
        panelResto.setLayout(new GridLayout(resto.cantMesas() / 4, 4));

        // Crea el arreglo de botones
        botonMesas = new JButton[resto.cantMesas()];
        for (int i = 0; i < resto.cantMesas(); i++) {
            botonMesas[i] = new JButton();
            botonMesas[i].setBackground(Color.WHITE);
            botonMesas[i].setPreferredSize(new Dimension(200, 200));
            botonMesas[i].setIcon(escalarIcono("imagenes/mesaLibre2.png", 200, 200));
            botonMesas[i].addActionListener(new OyenteMesa());
            botonMesas[i].setActionCommand(String.valueOf(i + 1));
            panelResto.add(botonMesas[i]);
        }
    }

    private void activarBotonesMesas(boolean activar) {
        for (int i = 0; i < resto.cantMesas(); i++)
            botonMesas[i].setEnabled(activar);
    }

    /*TODO
     * Implementar el método inicializarPanelMenu(). Crea los botones correspondientes a los distintos combos del menú, 
     * registra sus oyentes y los inserta en el panel del menú. Además, genera las etiquetas con las descripciones de cada combo y las agrega al mismo panel.
     */
    private void inicializarPanelMenu() {

        // Crear paneles y establecer diagramado
        int cant = resto.obtenerStockMenu().cantCombos();
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(2, cant)); 

        //Crear botones, registrar oyentes e insertar en el panel de Menu 
        boton = new JButton[cant];

        // PRIMERA FILA
        for (int i = 0; i < cant; i++) {
            boton[i] = new JButton();
            boton[i].addActionListener(new OyenteCombo()); 
            boton[i].setFocusable(false);
            boton[i].setBorder(new LineBorder(Color.BLACK, 1));
            boton[i].setBackground(Color.WHITE);
            boton[i].setContentAreaFilled(true);
            panelMenu.add(boton[i]);
        }

        //Crear etiquetas e insertarlas en el panel de Menu 
        etiqueta = new JLabel[cant];

        // SEGUNDA FILA 
        for (int i = 0; i < cant; i++) {
            etiqueta[i] = new JLabel("", SwingConstants.CENTER);
            panelMenu.add(etiqueta[i]);
        }

        // Al inicio, el panel del Menu no está visible
        panelMenu.setVisible(false);
    }
    // FIN TODO

    private void inicializarpanelMesa() {

        // Crear paneles y establecer diagramado
        panelMesa = new JPanel();
        panelMesa.setLayout(new BorderLayout());
        panelMesa.setBorder(new EmptyBorder(10, 0, 0, 10)); // top, left, bottom, right
        etiquetaMesaSeleccionada = new JLabel("Mesa seleccionada");

        panelDetalle = new JPanel();
        panelDetalle.setLayout(new BorderLayout());
        etiquetaDetallePedido = new JLabel("La mesa aún no ha realizado ningún pedido. ");
        botonAgregarItem = new JButton("Agregar pedido");

        botonAgregarItem.addActionListener(new OyenteAgregarItem());
        panelDetalle.add(etiquetaDetallePedido, BorderLayout.CENTER);
        panelDetalle.add(botonAgregarItem, BorderLayout.PAGE_END);

        panelOcuparDesocupar = new JPanel();

        /* TODO
         * Crear los botones botonOcuparMesa y botonDesocuparMesa e insertarlos en el panel correspondiente. 
         * Declarar, crear y registrar los oyentes para esos botones.*/
        botonOcuparMesa = new JButton("Ocupar");
        OyenteOcuparMesa ocuparMesa = new OyenteOcuparMesa();
        botonOcuparMesa.addActionListener(ocuparMesa);
        panelOcuparDesocupar.add(botonOcuparMesa);

        botonDesocuparMesa = new JButton("Liberar");
        OyenteLiberarMesa liberarMesa = new OyenteLiberarMesa();
        botonDesocuparMesa.addActionListener(liberarMesa);
        panelOcuparDesocupar.add(botonDesocuparMesa);
        // FIN TODO

        panelMesa.add(etiquetaMesaSeleccionada, BorderLayout.PAGE_START);
        panelMesa.add(panelDetalle, BorderLayout.CENTER);
        panelMesa.add(panelOcuparDesocupar, BorderLayout.PAGE_END);
        panelMesa.setVisible(false);

    }

    /*TODO
     * Implementar el método armarBotones(). Configura los botones creados en el método inicializarPanelMenu() asignándoles la 
     * información específica de cada combo, como la imagen, la descripción y otros datos relevantes. Además setea el actionCommand con el
     * nombre del combo.
     */
    private void armarBotones() {
        int cant = resto.obtenerStockMenu().cantCombos();
        for (int i = 0; i < cant; i++) {
            Combo c = resto.obtenerStockMenu().obtenerCombo(i);
            boton[i].setIcon(escalarIcono("imagenes/combo " + (i + 1) + ".png", 300, 200));
            boton[i].setText(c.getNombre());
            boton[i].setHorizontalTextPosition(SwingConstants.CENTER);
            boton[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            boton[i].setActionCommand(c.getNombre());
            boton[i].setText(c.getNombre() + ": " + c.getDescripcion());
        }
    }
    // FIN TODO

    /*TODO
     * Implementar el método armarEtiquetas(). Configura las etiquetas generadas en el método inicializarPanelMenu(), 
     * incorporando en cada una el precio del combo y la cantidad de unidades disponibles en stock.
     */
    private void armarEtiquetas() {
        int cant = resto.obtenerStockMenu().cantCombos();
        for (int i = 0; i < cant; i++) {
            Combo c = resto.obtenerStockMenu().obtenerCombo(i);
            etiqueta[i].setText("$" + c.getPrecio() + " quedan " + c.getCantidad());
            etiqueta[i].setHorizontalAlignment(SwingConstants.CENTER);
            etiqueta[i].setVerticalAlignment(SwingConstants.CENTER);
        }
    }
    // FIN TODO

    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        Image imagenOriginal = iconoOriginal.getImage();

        // Crear una imagen compatible con la pantalla
        BufferedImage imagenEscalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagenEscalada.createGraphics();

        // Dibujar imagen escalada
        g2d.drawImage(imagenOriginal, 0, 0, ancho, alto, null);
        g2d.dispose();

        // Activar interpolación de alta calidad
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        return new ImageIcon(imagenEscalada);
    }

    /*TODO
     * Implementar el oyente OyenteMesa de manera que al seleccionar una mesa, actualice las etiquetas con la información correspondiente, y
     * ajuste la visibilidad y el estado de los botones según si la mesa está ocupada o libre.
     */
    private class OyenteMesa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Obtener el número de la mesa seleccionada y la mesa del resto con dicho número
            numeroMesaSeleccionada = Integer.parseInt(e.getActionCommand());
            Mesa m = resto.obtenerMesa(numeroMesaSeleccionada);
            // Modificar la etiqueta con la mesa seleccionada
            etiquetaMesaSeleccionada.setText("Mesa Selecionada: " + numeroMesaSeleccionada);
            // Modificar la etiqueta con el detalle parcial
            if (m.estaOcupada()) {
                etiquetaDetallePedido.setText(m.generarDetalleParcial());
            }
            // Hacer visible el panel de la Mesa
            panelMesa.setVisible(true);
            //Si la mesa no está ocupada entonces se oculta el panel de detalle, y se setea la visibilidad de los botones ocupar/desocupar mesa 
            if (!m.estaOcupada()) {
                // MESA LIBRE
                panelDetalle.setVisible(false);
                botonAgregarItem.setVisible(false);
                botonOcuparMesa.setVisible(true);
                botonDesocuparMesa.setVisible(false);
            } else {
                // MESA OCUPADA
                panelDetalle.setVisible(true);
                botonAgregarItem.setVisible(true);
                botonOcuparMesa.setVisible(false);
                botonDesocuparMesa.setVisible(true);
            }
            //Sino se muestra el panel de detalle, y si la mesa no alcanzó el máximo de pedidos posibles, se setea la visibilidad del botón para agregar
            panelMenu.setVisible(false);
            activarBotonesMesas(true);
            //Un nuevo item. Además se setea la visibilidad de los botones ocupar/desocupar mesa
            if (m.estaOcupada()) {
                // SI LA MESA NO ALCANZO SU MAXIMO DE PEDIDOS, PERMITIR AGREGAR UN ITEM
                if (!m.alcanzoMaximoPedidos()) {
                    botonAgregarItem.setVisible(true);
                } else {
                    botonAgregarItem.setVisible(false);
                }
                // OPCIONES DE OCUPAR/DESOCUPAR
                botonOcuparMesa.setVisible(false);
                botonDesocuparMesa.setVisible(true);
            }
        }
    }
    // FIN TODO

     /*TODO 
      * Completar los oyentes OyenteOcuparMesa, OyenteLiberarMesa y OyenteCombo para que 
      * la aplicación opere conforme a las funcionalidades descritas.*/
    private class OyenteCombo implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            /*Vender el combo seleccionado, actualizar la etiqueta y si ya no quedan bandejas deshabilitar el botón*/

            //Obtener la mesa seleccionada y el combo seleccionado. Vender el combo seleccionado. Actualizar la etiqueta
            Mesa m = resto.obtenerMesa(numeroMesaSeleccionada);
            String nombreCombo = e.getActionCommand();
            Combo combo = resto.obtenerStockMenu().obtenerCombo(nombreCombo);
            combo.vender();
            armarEtiquetas();
            //Si luego de vender el combo no hay más stock, entonces se deshabilita el boton del combo correspondiente 
            if (combo.getCantidad() == 0) {
                for (int i = 0; i < boton.length; i++) {
                    if (boton[i].getActionCommand().equals(nombreCombo)) {
                        boton[i].setEnabled(false);
                    }
                }
            }
            // Se agrega el combo a la mesa seleccionada.
            m.obtenerPedido().agregarCombo(combo);
            //Si luego de agregar el combo, la mesa alcanzó el máximo de pedidos, se deshabilita el botón para agregar nuevos items.
            if (m.alcanzoMaximoPedidos()) {
                botonAgregarItem.setEnabled(false);
            }
            // Se setea la etiqueta con el detalle parcial del pedido
            etiquetaDetallePedido.setText(m.generarDetalleParcial());
            //Se activan los botones de las mesas, se visibiliza el boton para desocupar la mesa y se oculta el panel del menu.
            activarBotonesMesas(true);
            botonDesocuparMesa.setVisible(true);
            panelMenu.setVisible(false);
        }
    }
    // FIN TODO

    private class OyenteAgregarItem implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            panelMenu.setVisible(true);
            activarBotonesMesas(false);
            botonDesocuparMesa.setVisible(false);
        }
    }

     /*TODO 
      * Completar los oyentes OyenteOcuparMesa, OyenteLiberarMesa y OyenteCombo para que 
      * la aplicación opere conforme a las funcionalidades descritas.*/
    private class OyenteOcuparMesa implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            // Se ocupa la mesa seleccionada
            System.out.println("numeroMesaSeleccionada:" + numeroMesaSeleccionada); // para ver si es correcto
            if (!resto.obtenerMesa(numeroMesaSeleccionada).estaOcupada()) {
                resto.obtenerMesa(numeroMesaSeleccionada).ocupar();

                // Se visibiliza el boton para agregar nuevos ítems
                botonMesas[numeroMesaSeleccionada - 1].setIcon(escalarIcono("imagenes/mesaOcupada2.png", 200, 200));
                botonAgregarItem.setVisible(true);
                // Se actualiza el detalle del pedido
                etiquetaDetallePedido.setText(resto.obtenerMesa(numeroMesaSeleccionada).generarDetalleParcial());
                // Se visibiliza el panel del detalle y se setea la visibilidad de los botones ocupar/desocupar mesa
                panelDetalle.setVisible(true);
                botonOcuparMesa.setVisible(false);
                botonDesocuparMesa.setVisible(true);
                botonAgregarItem.setEnabled(resto.obtenerMesa(numeroMesaSeleccionada).obtenerPedido().cantCombos() < 10);
            }
        }
    }
    // FIN TODO

     /*TODO 
      * Completar los oyentes OyenteOcuparMesa, OyenteLiberarMesa y OyenteCombo para que 
      * la aplicación opere conforme a las funcionalidades descritas.*/
    private class OyenteLiberarMesa implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            //Se muestra un cuadro de diálogo con la información de la mesa a liberar de acuerdo a lo expuesto en el enunciado del proyecto. 
            JOptionPane.showMessageDialog(null, resto.obtenerMesa(numeroMesaSeleccionada).generarTicketCuenta(), "Cerrando Mesa " + numeroMesaSeleccionada, JOptionPane.INFORMATION_MESSAGE);

            botonMesas[numeroMesaSeleccionada - 1].setIcon(escalarIcono("imagenes/mesaLibre2.png", 200, 200));
            // Se libera la mesa seleccionada
            resto.obtenerMesa(numeroMesaSeleccionada).liberar();
            // Se oculta el panel de detalle, y se setean los botones para ocupar/desocupar la mesa
            panelDetalle.setVisible(false);
            botonOcuparMesa.setVisible(true);
            botonDesocuparMesa.setVisible(false);
            botonAgregarItem.setVisible(false);

            activarBotonesMesas(true);

        }
    }
    // FIN TODO
}

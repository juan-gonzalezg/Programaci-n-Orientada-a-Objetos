
package project.view;

import project.controller.AdministradorController;
import project.controller.EstadisticasController;
import project.model.entities.Cliente;
import project.model.entities.HistorialDeEntrega;
import project.model.entities.Pedido;
import project.model.entities.Repartidor;
import project.model.enums.Combo;
import project.model.enums.MetodoDePago;
import project.view.components.DetalleButtonEditor;
import project.view.components.DetalleButtonRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartPanel;

/*
 * Clase principal de la interfaz de usuario para el panel de administración.
 * Extiende de javax.swing.JFrame para crear la ventana principal de la aplicación.
 * Implementa principios de Clean Code para mejorar la legibilidad y mantenibilidad.
 */
public class AdministradorDashboardJFrame extends javax.swing.JFrame {

    // Campos de texto para la búsqueda en tablas
    private final JTextField txtBuscarCliente;
    private final JTextField txtBuscarRepartidor;
    private final JTextField txtBuscarPedido;
    private final JTextField txtBuscarHistorial;

    // Tablas para mostrar datos
    private final JTable jTableClientes;
    private final JTable jTableRepartidores;
    private final JTable jTablePedidos;
    private final JTable jTableHistorialEntregas;

    // ComboBoxes para selección de datos
    private final JComboBox<Cliente> clientejComboBox;
    private final JComboBox<Combo> comboBoxCombos;
    private final JComboBox<MetodoDePago> metodoPagoComboBox;
    private final JComboBox<Repartidor> repartidorjComboBox;

    // Controlador para la lógica de negocio del administrador
    private final AdministradorController adminController;

    /*
     * Constructor de la clase AdministradorDashboardJFrame.
     * Inicializa los componentes de la UI y configura los eventos y datos iniciales.
     */
    public AdministradorDashboardJFrame() {
        initComponents();
        // Inicialización de campos de texto y tablas
        txtBuscarCliente = jTextField1;
        txtBuscarRepartidor = jTextField2;
        txtBuscarPedido = jTextField5;
        txtBuscarHistorial = jTextField4;
        jTableClientes = jTable4;  
        jTableRepartidores = jTable5;
        jTablePedidos = jTable6;
        jTableHistorialEntregas = jTable7;
        clientejComboBox = ClientesPedidosjComboBox;
        comboBoxCombos = CombosjComboBox;
        metodoPagoComboBox = MetodoDePagojComboBox;
        repartidorjComboBox = RepartidoresPedidojComboBox;
        adminController = new AdministradorController();
        
        // Configuración del icono de la aplicación
        setupAppIcon();
        // Configuración inicial de la UI
        initializeUIComponents();
        // Visualización inicial de datos
        visualizarDatos();
    }

    /*
     * Configura el icono de la ventana de la aplicación.
     */
    private void setupAppIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Logo.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }
    }

    /*
     * Inicializa y configura los componentes de la interfaz de usuario.
     */
    private void initializeUIComponents() {
        configurarEventos();
        updateNavPanelColors(0); // Establece el dashboard como seleccionado por defecto
        inicializarFiltros();
        configurarPanelesNavegacion();
        configurarPlaceholders();
        integrarGraficaEstadisticas();
        actualizarKpiCards();
    }

    /*
     * Configura los paneles de navegación como botones.
     */
    private void configurarPanelesNavegacion() {
        configurarPanelComoBoton(Dashboard, 0);
        configurarPanelComoBoton(Cliente, 1);
        configurarPanelComoBoton(Repartidor, 2);
        configurarPanelComoBoton(Pedido, 3);
        configurarPanelComoBoton(HistorialDeEntrega, 4);
        configurarPanelComoBoton(Estadistica, 5);
    }

    /*
     * Integra la gráfica de estadísticas en el panel correspondiente.
     */
    private void integrarGraficaEstadisticas() {
        jPanel13.setLayout(new BorderLayout());
        EstadisticasController statsController = new EstadisticasController();
        ChartPanel chartPanel = statsController.generarGraficaEstadisticasPanel();
        if (chartPanel != null) {
            jPanel13.add(chartPanel, BorderLayout.CENTER);
        } else {
            jPanel13.add(new JLabel("No hay datos disponibles para la gráfica de estadísticas."), BorderLayout.CENTER);
        }
        jPanel13.revalidate();
        jPanel13.repaint();
    }

    /*
     * Actualiza los valores de las tarjetas KPI (Key Performance Indicator) en el dashboard.
     */
    private void actualizarKpiCards() {
        int pedidosEntregadosHoy = adminController.getPedidosEntregadosHoy();
        double otdPorcentaje = adminController.getOnTimeDeliveryPorcentaje();
        double tiempoPromedioEntrega = adminController.getTiempoPromedioEntrega();
        int pedidosEnCurso = adminController.getPedidosEnCurso();
        int repartidoresDisponibles = adminController.getRepartidoresDisponiblesActivos();

        jLabel45.setText(String.valueOf(pedidosEntregadosHoy));
        jLabel46.setText(String.format("%.1f%%", otdPorcentaje));
        jLabel47.setText(String.format("%.1f min", tiempoPromedioEntrega));
        jLabel48.setText(String.valueOf(pedidosEnCurso));
        jLabel49.setText(String.valueOf(repartidoresDisponibles));
    }

    /*
     * Configura los botones de detalle en las tablas.
     */
    private void configurarBotonesDetalle() {
        configurarBotonTabla(jTable1, "Acción", "pedido"); 
        configurarBotonTabla(jTable4, "Detalle", "cliente"); 
        configurarBotonTabla(jTable5, "Detalle", "repartidor"); 
        configurarBotonTabla(jTable6, "Acción", "pedido"); 
        configurarBotonTabla(jTable7, "Detalle", "historial"); 
    }

    /*
     * Configura un botón de detalle para una columna específica en una tabla.
     */
    private void configurarBotonTabla(JTable tabla, String nombreColumna, String tipoEntidad) {
        try {
            DetalleButtonEditor editor = new DetalleButtonEditor(new JCheckBox()) {
                @Override
                public void fireEditingStopped() {
                    super.fireEditingStopped();
                    int row = tabla.getSelectedRow();
                    if (row >= 0){
                        handleDetalleButtonClick(tabla, row, tipoEntidad);
                    }
                }
            };
            tabla.getColumn(nombreColumna).setCellRenderer(new DetalleButtonRenderer("Ver Detalle"));
            tabla.getColumn(nombreColumna).setCellEditor(editor); 
        } catch (IllegalArgumentException e) {
            System.err.println("Columna no encontrada: " + nombreColumna + " en tabla " + tabla.getName() + ". Asegúrese de que la columna exista y sea visible.");
        } catch (Exception e) {
            System.err.println("Error al configurar botón en tabla " + tabla.getName() + ": " + e.getMessage());
        }
    }

    /*
     * Maneja el evento de clic en el botón de detalle de una tabla.
     */
    private void handleDetalleButtonClick(JTable tabla, int row, String tipoEntidad) {
        String idEntidad;
        if ("cliente".equals(tipoEntidad) || "repartidor".equals(tipoEntidad)) {
            idEntidad = (String) tabla.getModel().getValueAt(row, 1); 
        } else {
            idEntidad = (String) tabla.getModel().getValueAt(row, 0);
        }

        switch (tipoEntidad) {
            case "cliente" -> {
                Cliente cliente = adminController.buscarClientePorId(idEntidad);
                if (cliente != null) mostrarVentanaDetalleCliente(cliente);
                else JOptionPane.showMessageDialog(tabla, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            case "repartidor" -> {
                Repartidor repartidor = adminController.buscarRepartidorPorId(idEntidad);
                if (repartidor != null) mostrarVentanaDetalleRepartidor(repartidor);
                else JOptionPane.showMessageDialog(tabla, "Repartidor no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            case "pedido" -> {
                Pedido pedido = adminController.buscarPedidoPorId(idEntidad);
                if (pedido != null) mostrarVentanaDetallePedido(pedido);
                else JOptionPane.showMessageDialog(tabla, "Pedido no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            case "historial" -> {
                HistorialDeEntrega historial = adminController.buscarHistorialDeEntregaPorId(idEntidad);
                if (historial != null) mostrarVentanaDetalleHistorialDeEntrega(historial);
                else JOptionPane.showMessageDialog(tabla, "Historial de entrega no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * Configura los textos de marcador de posición (placeholders) para los campos de texto.
     */
    private void configurarPlaceholders() {
        configurarPlaceholder(jTextField1, "Buscar cliente...");
        configurarPlaceholder(jTextField2, "Buscar repartidor...");
        configurarPlaceholder(jTextField5, "Buscar pedido...");
        configurarPlaceholder(jTextField4, "Buscar historial...");
        configurarPlaceholder(textNombreCliente, "Nombre y Apellido...");
        configurarPlaceholder(textCedulaIdentidadCliente, "Cedula de Identidad...");
        configurarPlaceholder(textNumeroTelefonoCliente, "Numero de telefono...");
        configurarPlaceholder(textDireccionCliente, "Direccion...");
        configurarPlaceholder(textNombreRepartidor, "Nombre y Apellido...");
        configurarPlaceholder(textCedulaIdentidadRepartidor, "Cedula de Identidad...");
        configurarPlaceholder(textNumeroTelefonoRepartidor, "Numero de telefono...");
        configurarPlaceholder(textCostoDeEntregaPedido, "Costo de Entrega...");
    }

    /*
     * Configura un marcador de posición (placeholder) para un campo de texto de tipo AWT.
     */
    private void configurarPlaceholder(java.awt.TextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    /*
     * Configura un marcador de posición (placeholder) para un campo de texto de tipo Swing.
     */
    private void configurarPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    /*
     * Inicializa los filtros de búsqueda para las tablas.
     */
    private void inicializarFiltros() {
        aplicarFiltro(txtBuscarCliente, jTableClientes);
        aplicarFiltro(txtBuscarRepartidor, jTableRepartidores);
        aplicarFiltro(txtBuscarPedido, jTablePedidos);
        aplicarFiltro(txtBuscarHistorial, jTableHistorialEntregas);
    }

    /*
     * Aplica un filtro de búsqueda a una tabla basado en el texto de un campo de texto.
     */
    private void aplicarFiltro(JTextField campoTexto, JTable tabla) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabla.getModel());
        tabla.setRowSorter(sorter);  
        campoTexto.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabla(campoTexto, sorter);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabla(campoTexto, sorter);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabla(campoTexto, sorter);
            }
        });
    }

    /*
     * Realiza el filtrado de la tabla basado en el texto del campo de búsqueda.
     */
    private void filtrarTabla(JTextField campoTexto, TableRowSorter<DefaultTableModel> sorter) {
        String texto = campoTexto.getText();
        if (isPlaceholderText(texto)) {
            sorter.setRowFilter(null);
        } else if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else { 
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }

    /*
     * Verifica si el texto de un campo es un marcador de posición.
     */
    private boolean isPlaceholderText(String text) {
        return text.equals("Buscar cliente...") || text.equals("Buscar repartidor...") ||
               text.equals("Buscar pedido...") || text.equals("Buscar historial...");
    }

    /*
     * Actualiza los colores de los paneles de navegación para indicar la selección actual.
     */
    private void updateNavPanelColors(int selectedIndex) {
        Color activeColor = new Color(240, 240, 240);
        Color inactiveColor = new Color(255, 210, 38);
        JPanel[] navegacionJPanels = {
            Dashboard,
            Cliente,
            Repartidor,
            Pedido,
            HistorialDeEntrega,
            Estadistica
        };
        for (int i = 0; i < navegacionJPanels.length; i++) {
            if (i == selectedIndex) {
                navegacionJPanels[i].setBackground(activeColor);
            } else {
                navegacionJPanels[i].setBackground(inactiveColor);
            }
        }
    }

    /*
     * Configura un JPanel para que funcione como un botón de navegación, cambiando de pestaña.
     */
    private void configurarPanelComoBoton(JPanel panel, int index) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTabbedPane1.setSelectedIndex(index);
                updateNavPanelColors(index);
                visualizarDatos();
                actualizarKpiCards();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    /*
     * Configura todos los listeners de eventos para los botones y componentes interactivos.
     */
    private void configurarEventos() {
        setupClienteRegistrationEvent();
        setupRepartidorRegistrationEvent();
        setupPedidoRegistrationEvent();
        configurarEventosRadioBotones();
        configurarEventoCliente();
        configurarEventoCombo();
    }

    /*
     * Configura el evento para el botón de registro de cliente.
     */
    private void setupClienteRegistrationEvent() {
        RegistrarClientejButton.addActionListener((ActionEvent e) -> {
            String nombre = textNombreCliente.getText();
            String cedula = textCedulaIdentidadCliente.getText();
            String telefono = textNumeroTelefonoCliente.getText();
            String direccion = textDireccionCliente.getText();
            String mensajeError = adminController.intentarRegistrarCliente(nombre, cedula, telefono, direccion);
            handleRegistrationResult(mensajeError, "Cliente registrado correctamente.", "Registro exitoso", this::limpiarCamposCliente);
        });
    }

    /*
     * Configura el evento para el botón de registro de repartidor.
     */
    private void setupRepartidorRegistrationEvent() {
        RegistrarRepartidorjButton.addActionListener(e -> {
            String nombre = textNombreRepartidor.getText();
            String cedula = textCedulaIdentidadRepartidor.getText();
            String telefono = textNumeroTelefonoRepartidor.getText();

            // Validar que los campos no estén vacíos o contengan el placeholder
            if (nombre == null || nombre.trim().isEmpty() || isPlaceholderText(nombre)) {
                mostrarMensajeErrorTemporizado(this, "El nombre del repartidor no puede estar vacío.", "Error de Validación", 3000);
                return;
            }
            if (cedula == null || cedula.trim().isEmpty() || isPlaceholderText(cedula)) {
                mostrarMensajeErrorTemporizado(this, "La cédula de identidad del repartidor no puede estar vacía.", "Error de Validación", 3000);
                return;
            }
            if (telefono == null || telefono.trim().isEmpty() || isPlaceholderText(telefono)) {
                mostrarMensajeErrorTemporizado(this, "El número de teléfono del repartidor no puede estar vacío.", "Error de Validación", 3000);
                return;
            }
            
            // El método intentarRegistrarRepartidor ahora devuelve la contraseña generada o un mensaje de error
            String resultadoRegistro = adminController.intentarRegistrarRepartidor(nombre, cedula, telefono, true);
            
            if (resultadoRegistro != null && (resultadoRegistro.startsWith("Ya existe") || resultadoRegistro.startsWith("El nombre") || resultadoRegistro.startsWith("La cédula") || resultadoRegistro.startsWith("El número"))) {
                // Si el resultado es un mensaje de error de validación o existencia
                mostrarMensajeErrorTemporizado(this, resultadoRegistro, "Error de Registro", 3000);
            } else {
                // Si el registro fue exitoso, resultadoRegistro es la contraseña generada
                mostrarMensajeTemporizado(AdministradorDashboardJFrame.this, "Repartidor registrado correctamente. Contraseña: " + resultadoRegistro, "Registro exitoso", 5000);
                limpiarCamposRepartidor(); // Limpia los campos de entrada
                visualizarDatos();
                actualizarKpiCards();
            }
        });
    }

    /*
     * Configura el evento para el botón de registro de pedido.
     */
    private void setupPedidoRegistrationEvent() {
        RegistrarPedidojButton.addActionListener((ActionEvent e) -> {
            Cliente clienteSeleccionado = (Cliente) clientejComboBox.getSelectedItem();
            Combo comboSeleccionado = (Combo) comboBoxCombos.getSelectedItem();
            MetodoDePago metodoPagoSeleccionado = (MetodoDePago) MetodoDePagojComboBox.getSelectedItem();
            Repartidor repartidorSeleccionado = (Repartidor) RepartidoresPedidojComboBox.getSelectedItem();
            String costoEntregaStr = textCostoDeEntregaPedido.getText().trim();
            String vueltoStr = textVueltoPedido.getText().trim();
            boolean requiereCambio = SIjRadioButton.isSelected();

            String mensajeError = adminController.intentarRegistrarPedido(
                clienteSeleccionado, comboSeleccionado, metodoPagoSeleccionado, 
                repartidorSeleccionado, requiereCambio, costoEntregaStr, vueltoStr
            );
            handleRegistrationResult(mensajeError, "Pedido registrado correctamente.", "Registro exitoso", this::limpiarCamposPedido);
        });
    }

    /*
     * Maneja el resultado de una operación de registro, mostrando mensajes y actualizando la UI.
     */
    private void handleRegistrationResult(String errorMessage, String successMessage, String successTitle, Runnable cleanupAction) {
        if (errorMessage != null) {
            mostrarMensajeErrorTemporizado(this, errorMessage, "Error de Registro", 3000);
        } else {
            cleanupAction.run();
            mostrarMensajeTemporizado(AdministradorDashboardJFrame.this, successMessage, successTitle, 3000);
            visualizarDatos();
            actualizarKpiCards();
        }
    }

    /*
     * Limpia los campos de texto del formulario de registro de cliente y restablece los placeholders.
     */
    private void limpiarCamposCliente() {
        textNombreCliente.setText("");
        textCedulaIdentidadCliente.setText("");
        textNumeroTelefonoCliente.setText("");
        textDireccionCliente.setText("");
        configurarPlaceholder(textNombreCliente, "Nombre y Apellido...");
        configurarPlaceholder(textCedulaIdentidadCliente, "Cedula de Identidad...");
        configurarPlaceholder(textNumeroTelefonoCliente, "Numero de telefono...");
        configurarPlaceholder(textDireccionCliente, "Direccion...");
    }

    /*
     * Limpia los campos de texto del formulario de registro de repartidor y restablece los placeholders.
     */
    private void limpiarCamposRepartidor() {
        textNombreRepartidor.setText("");
        textCedulaIdentidadRepartidor.setText("");
        textNumeroTelefonoRepartidor.setText("");
        // Se elimina la limpieza y configuración del placeholder para textContrasena
        configurarPlaceholder(textNombreRepartidor, "Nombre y Apellido...");
        configurarPlaceholder(textCedulaIdentidadRepartidor, "Cedula de Identidad...");
        configurarPlaceholder(textNumeroTelefonoRepartidor, "Numero de telefono...");
    }

    /*
     * Limpia los campos del formulario de registro de pedido y restablece los valores por defecto.
     */
    private void limpiarCamposPedido() {
        clientejComboBox.setSelectedIndex(0);
        comboBoxCombos.setSelectedIndex(0);
        MetodoDePagojComboBox.setSelectedIndex(0);
        RepartidoresPedidojComboBox.setSelectedIndex(0);
        textCostoDeEntregaPedido.setText("");
        textVueltoPedido.setText("");
        NOjRadioButton.setSelected(true);
        textVueltoPedido.setEnabled(false);
        textVueltoPedido.setText("0");
        textDireccionPedido.setText("");
        textPrecioPedido.setText("");
    }

    /*
     * Muestra un mensaje de error temporizado al usuario.
     */
    public void mostrarMensajeErrorTemporizado(Component parent, String mensaje, String titulo, int duracionMillis) {
        showMessageTemporized(parent, mensaje, titulo, duracionMillis, JOptionPane.ERROR_MESSAGE);
    }

    /*
     * Muestra un mensaje informativo temporizado al usuario.
     */
    public void mostrarMensajeTemporizado(Component parent, String mensaje, String titulo, int duracionMillis) {
        showMessageTemporized(parent, mensaje, titulo, duracionMillis, JOptionPane.PLAIN_MESSAGE);
    }

    /*
     * Método genérico para mostrar un mensaje temporizado (error o informativo).
     */
    private void showMessageTemporized(Component parent, String message, String title, int durationMillis, int messageType) {
        final JOptionPane optionPane = new JOptionPane(message, messageType);
        final JDialog dialog = optionPane.createDialog(parent, title);
        Timer timer = new Timer(durationMillis, (ActionEvent e) -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        timer.stop();
    }

    /*
     * Actualiza y visualiza todos los datos en los ComboBoxes y tablas de la interfaz.
     */
    public final void visualizarDatos() {
        // Actualizar modelos de ComboBoxes
        clientejComboBox.setModel(adminController.generarModeloComboBoxClientes());
        comboBoxCombos.setModel(adminController.generarModeloComboBoxCombos());
        MetodoDePagojComboBox.setModel(adminController.generarModeloComboBoxMetodosPago());
        RepartidoresPedidojComboBox.setModel(adminController.generarModeloComboBoxRepartidores());

        // Actualizar modelos de tablas del Dashboard
        jTable1.setModel(adminController.generarModeloTablaPedidosDashboard());
        jTable2.setModel(adminController.generarModeloTablaRepartidoresDashboard());
        jTable3.setModel(adminController.generarModeloTablaHistorialDeEntregaDashboard());

        // Actualizar modelos de tablas de gestión
        jTable4.setModel(adminController.generarModeloTablaClientes());
        jTable5.setModel(adminController.generarModeloTablaRepartidores());
        jTable6.setModel(adminController.generarModeloTablaPedidos());
        jTable7.setModel(adminController.generarModeloTablaHistorialDeEntrega());
        jTable8.setModel(adminController.generarModeloTablaTopRepartidores());

        // Reconfigurar botones de detalle y filtros después de actualizar los datos
        configurarBotonesDetalle();
        inicializarFiltros();
    }

    /*
     * Configura el evento para el ComboBox de selección de cliente en el formulario de pedido.
     * Al seleccionar un cliente, se actualiza automáticamente la dirección de entrega del pedido.
     */
    private void configurarEventoCliente() {
        if (clientejComboBox != null) {
            clientejComboBox.addActionListener(e -> {
                Cliente seleccionado = (Cliente) clientejComboBox.getSelectedItem();
                if (seleccionado != null && !seleccionado.getCedulaIdentidad().isEmpty()) {
                    textDireccionPedido.setText(seleccionado.getDireccion());
                } else {
                    textDireccionPedido.setText("");
                }
            });
        }
    }

    /*
     * Configura el evento para el ComboBox de selección de combo en el formulario de pedido.
     * Al seleccionar un combo, se actualiza automáticamente el precio del pedido.
     */
    private void configurarEventoCombo() {
        if (CombosjComboBox != null) {
            CombosjComboBox.addActionListener(e -> {
                Combo seleccionado = (Combo) CombosjComboBox.getSelectedItem();
                if (seleccionado != null) {
                    textPrecioPedido.setText(String.valueOf(seleccionado.getPrecio()));
                } else {
                    textPrecioPedido.setText("");
                }
            });
        }
    }

    /*
     * Configura los eventos para los radio botones de "Requiere cambio" en el formulario de pedido.
     * Controla la habilitación y el texto del campo de vuelto.
     */
    private void configurarEventosRadioBotones() {
        ButtonGroup grupoVuelto = new ButtonGroup();
        grupoVuelto.add(SIjRadioButton);
        grupoVuelto.add(NOjRadioButton);

        NOjRadioButton.setSelected(true);
        textVueltoPedido.setEnabled(false);
        textVueltoPedido.setText("0");

        SIjRadioButton.addActionListener(e -> {
            textVueltoPedido.setEnabled(true);
            textVueltoPedido.setText("");
            textVueltoPedido.requestFocus();
        });

        NOjRadioButton.addActionListener(e -> {
            textVueltoPedido.setEnabled(false);
            textVueltoPedido.setText("0");
        });
    }

    /*
     * Muestra una ventana de diálogo con los detalles de un cliente.
     * Permite eliminar el cliente.
     */
    private void mostrarVentanaDetalleCliente(Cliente cliente) {
        JDialog dialog = new JDialog(this, "Detalles del Cliente", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));

        addClientDetailsToDialog(dialog, cliente);
        addDeleteButtonToClientDialog(dialog, cliente);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /*
     * Añade los detalles del cliente al diálogo de detalles.
     */
    private void addClientDetailsToDialog(JDialog dialog, Cliente cliente) {
        dialog.add(new JLabel("ID Cliente:"));
        dialog.add(new JLabel(cliente.getCedulaIdentidad()));
        dialog.add(new JLabel("Nombre:"));
        dialog.add(new JLabel(cliente.getNombre()));
        dialog.add(new JLabel("Cédula de Identidad:"));
        dialog.add(new JLabel(cliente.getCedulaIdentidad()));
        dialog.add(new JLabel("Teléfono:"));
        dialog.add(new JLabel(cliente.getNumeroTelefono()));
        dialog.add(new JLabel("Dirección:"));
        dialog.add(new JLabel(cliente.getDireccion()));
    }

    /*
     * Añade el botón de eliminar cliente al diálogo de detalles del cliente.
     */
    private void addDeleteButtonToClientDialog(JDialog dialog, Cliente cliente) {
        JButton eliminarButton = new JButton("Eliminar Cliente");
        eliminarButton.setBackground(Color.RED);
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "¿Está seguro de que desea eliminar a " + cliente.getNombre() + "?\n" + "Esta acción es irreversible y también eliminará sus pedidos asociados.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = adminController.eliminarCliente(cliente.getCedulaIdentidad());
                if (eliminado) {
                    JOptionPane.showMessageDialog(dialog, "Cliente eliminado exitosamente.");
                    dialog.dispose();
                    visualizarDatos();
                    actualizarKpiCards();
                } else {
                    JOptionPane.showMessageDialog(dialog, "No se pudo eliminar el cliente. Verifique que no tenga pedidos asociados.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.add(eliminarButton);
    }

    /*
     * Muestra una ventana de diálogo con los detalles de un repartidor.
     */
    private void mostrarVentanaDetalleRepartidor(Repartidor repartidor) {
        JDialog dialog = new JDialog(this, "Detalles del Repartidor", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));

        addRepartidorDetailsToDialog(dialog, repartidor);
        addDeleteButtonToRepartidorDialog(dialog, repartidor);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /*
     * Añade los detalles del repartidor al diálogo de detalles.
     */
    private void addRepartidorDetailsToDialog(JDialog dialog, Repartidor repartidor) {
        dialog.add(new JLabel("ID Repartidor:"));
        dialog.add(new JLabel(repartidor.getCedulaIdentidad()));
        dialog.add(new JLabel("Nombre:"));
        dialog.add(new JLabel(repartidor.getNombre()));
        dialog.add(new JLabel("Cédula de Identidad:"));
        dialog.add(new JLabel(repartidor.getCedulaIdentidad()));
        dialog.add(new JLabel("Teléfono:"));
        dialog.add(new JLabel(repartidor.getNumeroTelefono()));
        dialog.add(new JLabel("Disponibilidad:"));
        dialog.add(new JLabel(repartidor.isDisponibilidad()? "Disponible" : "No Disponible"));
        dialog.add(new JLabel("Contraseña:")); // Muestra la contraseña
        dialog.add(new JLabel(repartidor.getContrasena())); // Muestra la contraseña
    }

    /*
     * Añade el botón de eliminar repartidor al diálogo de detalles del repartidor.
     */
    private void addDeleteButtonToRepartidorDialog(JDialog dialog, Repartidor repartidor) {
        JButton eliminarButton = new JButton("Eliminar Repartidor");
        eliminarButton.setBackground(Color.RED);
        eliminarButton.setForeground(Color.WHITE);
        eliminarButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "¿Está seguro de que desea eliminar a " + repartidor.getNombre() + "?\n" + "Esta acción es irreversible y también eliminará los pedidos que tenga asignados.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean eliminado = adminController.eliminarRepartidor(repartidor.getCedulaIdentidad());
                if (eliminado) {
                    JOptionPane.showMessageDialog(dialog, "Repartidor eliminado exitosamente.");
                    dialog.dispose();
                    visualizarDatos();
                    actualizarKpiCards();
                } else {
                    JOptionPane.showMessageDialog(dialog, "No se pudo eliminar el repartidor. Verifique que no tenga pedidos asignados.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.add(eliminarButton);
    }

    /*
     * Muestra una ventana de diálogo con los detalles de un pedido.
     * Permite cancelar el pedido o reasignar un repartidor.
     */
    public void mostrarVentanaDetallePedido(Pedido pedido) {
        System.out.println("Mostrando detalles del pedido: " + pedido.getIdPedido());
        JDialog dialogoDetalle = new JDialog(this, "Detalle del Pedido: " + pedido.getIdPedido(), true);
        JPanel panelContenido = new JPanel(new GridLayout(0, 2, 10, 10));

        addPedidoDetailsToPanel(panelContenido, pedido);
        dialogoDetalle.add(panelContenido);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addPedidoActionButtons(dialogoDetalle, panelBotones, pedido);
        dialogoDetalle.add(panelBotones, java.awt.BorderLayout.SOUTH);

        dialogoDetalle.pack();
        dialogoDetalle.setLocationRelativeTo(this);
        dialogoDetalle.setVisible(true);
    }

    /*
     * Añade los detalles de un pedido a un panel.
     */
    private void addPedidoDetailsToPanel(JPanel panel, Pedido pedido) {
        panel.add(new JLabel("ID Pedido:"));
        panel.add(new JLabel(pedido.getIdPedido()));
        panel.add(new JLabel("--- Datos del Cliente ---"));
        panel.add(new JLabel(""));
        addClientInfoToPedidoPanel(panel, pedido.getCliente());
        panel.add(new JLabel("--- Detalles del Pedido ---"));
        panel.add(new JLabel(""));
        addPedidoInfoToPanel(panel, pedido);
        panel.add(new JLabel("--- Datos del Repartidor ---"));
        panel.add(new JLabel(""));
        addRepartidorInfoToPedidoPanel(panel, pedido.getRepartidorAsignado());
    }

    /*
     * Añade la información del cliente de un pedido a un panel.
     */
    private void addClientInfoToPedidoPanel(JPanel panel, Cliente cliente) {
        if (cliente != null) {
            panel.add(new JLabel("Nombre Cliente:"));
            panel.add(new JLabel(cliente.getNombre()));
            panel.add(new JLabel("Cédula Cliente:"));
            panel.add(new JLabel(cliente.getCedulaIdentidad()));
            panel.add(new JLabel("Teléfono Cliente:"));
            panel.add(new JLabel(cliente.getNumeroTelefono()));
            panel.add(new JLabel("Dirección Cliente:"));
            panel.add(new JLabel(cliente.getDireccion()));
        } else {
            panel.add(new JLabel("Cliente:"));
            panel.add(new JLabel("N/A"));
        }
    }

    /*
     * Añade la información específica del pedido a un panel.
     */
    private void addPedidoInfoToPanel(JPanel panel, Pedido pedido) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        panel.add(new JLabel("Dirección de Entrega:"));
        panel.add(new JLabel(pedido.getDireccionEntrega()));
        panel.add(new JLabel("Combo:"));
        panel.add(new JLabel(pedido.getCombo() != null ? pedido.getCombo().toString() : "N/A"));
        panel.add(new JLabel("Precio del Combo:"));
        panel.add(new JLabel(String.valueOf(pedido.getPrecioCombo())));
        panel.add(new JLabel("Método de Pago:"));
        panel.add(new JLabel(pedido.getMetodoPago() != null ? pedido.getMetodoPago().name() : "N/A"));
        panel.add(new JLabel("Requiere Cambio:"));
        panel.add(new JLabel(pedido.isRequiereCambio() ? "Sí" : "No"));
        panel.add(new JLabel("Costo de Entrega:"));
        panel.add(new JLabel(String.valueOf(pedido.getCostoEntrega())));
        panel.add(new JLabel("Vuelto:"));
        panel.add(new JLabel(String.valueOf(pedido.getVuelto())));
        panel.add(new JLabel("Monto Total:"));
        panel.add(new JLabel(String.valueOf(pedido.getPrecioCombo() + pedido.getCostoEntrega())));
        panel.add(new JLabel("Estado:"));
        panel.add(new JLabel(pedido.getEstado()));
        panel.add(new JLabel("Fecha de Creación:"));
        panel.add(new JLabel(pedido.getFechaCreacion() != null ? dateFormat.format(pedido.getFechaCreacion()) : "N/A"));
        if (pedido.getFechaEntrega() != null) {
            panel.add(new JLabel("Fecha de Entrega:"));
            panel.add(new JLabel(dateFormat.format(pedido.getFechaEntrega())));
        }
    }

    /*
     * Añade la información del repartidor de un pedido a un panel.
     */
    private void addRepartidorInfoToPedidoPanel(JPanel panel, Repartidor repartidor) {
        if (repartidor != null) {
            panel.add(new JLabel("Nombre Repartidor:"));
            panel.add(new JLabel(repartidor.getNombre()));
            panel.add(new JLabel("Cédula Repartidor:"));
            panel.add(new JLabel(repartidor.getCedulaIdentidad()));
            panel.add(new JLabel("Teléfono Repartidor:"));
            panel.add(new JLabel(repartidor.getNumeroTelefono()));
            panel.add(new JLabel("Disponibilidad Repartidor:"));
            panel.add(new JLabel(repartidor.isDisponibilidad() ? "Disponible" : "No disponible"));
        } else {
            panel.add(new JLabel("Repartidor Asignado:"));
            panel.add(new JLabel("No asignado"));
        }
    }

    /*
     * Añade los botones de acción (Cancelar y Reasignar) al diálogo de detalles del pedido.
     */
    private void addPedidoActionButtons(JDialog dialogoDetalle, JPanel panelBotones, Pedido pedido) {
        JButton btnCancelarPedido = new JButton("Cancelar Pedido");
        JButton btnReasignarRepartidor = new JButton("Reasignar Repartidor");
        boolean pedidoFinalizado = "Entregado".equalsIgnoreCase(pedido.getEstado()) || "Cancelado".equalsIgnoreCase(pedido.getEstado());
        
        btnCancelarPedido.setEnabled(!pedidoFinalizado);
        btnReasignarRepartidor.setEnabled(!pedidoFinalizado);

        btnCancelarPedido.addActionListener((ActionEvent e) -> handleCancelPedido(dialogoDetalle, pedido));
        btnReasignarRepartidor.addActionListener((ActionEvent e) -> handleReasignarRepartidor(dialogoDetalle, pedido));
        
        panelBotones.add(btnCancelarPedido);
        panelBotones.add(btnReasignarRepartidor);
    }

    /*
     * Maneja la acción de cancelar un pedido.
     */
    private void handleCancelPedido(JDialog dialogoDetalle, Pedido pedido) {
        int confirmacion = JOptionPane.showConfirmDialog(dialogoDetalle, "¿Está seguro de que desea cancelar este pedido?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            String mensaje = adminController.cancelarPedido(pedido.getIdPedido());
            if (mensaje == null) {
                mostrarMensajeTemporizado(AdministradorDashboardJFrame.this, "Pedido cancelado exitosamente.", "Éxito", 2000);
                visualizarDatos();
                actualizarKpiCards();
                dialogoDetalle.dispose();
            } else {
                mostrarMensajeErrorTemporizado(AdministradorDashboardJFrame.this, mensaje, "Error al Cancelar", 3000);
            }
        }
    }

    /*
     * Maneja la acción de reasignar un repartidor a un pedido.
     */
    private void handleReasignarRepartidor(JDialog dialogoDetalle, Pedido pedido) {
        JComboBox<Repartidor> cbRepartidores = new JComboBox<>(adminController.generarModeloComboBoxRepartidoresActivos());
        if (pedido.getRepartidorAsignado() != null) {
            for (int i = 0; i < cbRepartidores.getItemCount(); i++) {
                Repartidor r = cbRepartidores.getItemAt(i);
                if (r != null && r.getCedulaIdentidad().equals(pedido.getRepartidorAsignado().getCedulaIdentidad())) {
                    cbRepartidores.setSelectedItem(r);
                    break;
                }
            }
        }

        int result = JOptionPane.showConfirmDialog(dialogoDetalle, cbRepartidores, "Seleccionar Nuevo Repartidor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Repartidor nuevoRepartidor = (Repartidor) cbRepartidores.getSelectedItem();
            if (nuevoRepartidor != null && !nuevoRepartidor.getCedulaIdentidad().isEmpty()) {
                String mensaje = adminController.reasignarRepartidorAPedido(pedido.getIdPedido(), nuevoRepartidor);
                if (mensaje == null) {
                    mostrarMensajeTemporizado(AdministradorDashboardJFrame.this, "Repartidor reasignado exitosamente.", "Éxito", 2000);
                    visualizarDatos();
                    actualizarKpiCards();
                    dialogoDetalle.dispose();
                } else {
                    mostrarMensajeErrorTemporizado(AdministradorDashboardJFrame.this, mensaje, "Error al Reasignar", 3000);
                }
            } else {
                mostrarMensajeErrorTemporizado(AdministradorDashboardJFrame.this, "Debe seleccionar un repartidor válido.", "Error de Selección", 3000);
            }
        }
    }

    /*
     * Muestra una ventana de diálogo con los detalles de un historial de entrega.
     */
    public void mostrarVentanaDetalleHistorialDeEntrega(HistorialDeEntrega historial) {
        System.out.println("Mostrando detalles del historial de entrega: " + historial.getIdHistorial());
        JDialog dialogoDetalle = new JDialog(this, "Detalle del Historial: " + historial.getIdHistorial(), true);
        JPanel panelContenido = new JPanel(new GridLayout(0, 2, 10, 10));

        addHistorialDetailsToPanel(panelContenido, historial);
        dialogoDetalle.add(panelContenido);

        dialogoDetalle.pack();
        dialogoDetalle.setLocationRelativeTo(this);
        dialogoDetalle.setVisible(true);
    }

    /*
     * Añade los detalles de un historial de entrega a un panel.
     */
   private void addHistorialDetailsToPanel(JPanel panel, HistorialDeEntrega historial) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      panel.add(new JLabel("ID Historial:"));
      panel.add(new JLabel(historial.getIdHistorial()));
      panel.add(new JLabel("Fecha Registro:"));
      panel.add(new JLabel(historial.getFechaRegistro() != null ? dateFormat.format(historial.getFechaRegistro()) : "N/A"));
      panel.add(new JLabel("Estado Entrega:"));
      panel.add(new JLabel(historial.getEstadoEntrega()));
      panel.add(new JLabel("Ubicación Entrega:"));
      panel.add(new JLabel(historial.getUbicacionEntrega()));
        
      panel.add(new JLabel("--- Datos del Repartidor ---"));
      panel.add(new JLabel(""));
      addRepartidorInfoToHistorialPanel(panel, historial.getRepartidor());
      
      panel.add(new JLabel("--- Datos del Pedido Asociado ---"));
      panel.add(new JLabel(""));
      addPedidoInfoToHistorialPanel(panel, historial.getPedidoAsociado());
   }

   /*
    * Añade la información del repartidor de un historial de entrega a un panel.
    */
   private void addRepartidorInfoToHistorialPanel(JPanel panel, Repartidor repartidor) {
      if (repartidor != null) {
         panel.add(new JLabel("Nombre Repartidor:"));
         panel.add(new JLabel(repartidor.getNombre()));
         panel.add(new JLabel("Cédula Repartidor:"));
         panel.add(new JLabel(repartidor.getCedulaIdentidad()));
         panel.add(new JLabel("Teléfono Repartidor:"));
         panel.add(new JLabel(repartidor.getNumeroTelefono()));
         panel.add(new JLabel("Contraseña Repartidor:")); // Muestra la contraseña
         panel.add(new JLabel(repartidor.getContrasena())); // Muestra la contraseña
      } else {
         panel.add(new JLabel("Repartidor:"));
         panel.add(new JLabel("N/A"));
      }
   }

   /*
    * Añade la información del pedido asociado a un historial de entrega a un panel.
    */
   private void addPedidoInfoToHistorialPanel(JPanel panel, Pedido pedido) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      if (pedido != null) {
         panel.add(new JLabel("ID Pedido Asociado:"));
         panel.add(new JLabel(pedido.getIdPedido()));
         addClientInfoToHistorialPedidoPanel(panel, pedido.getCliente());
         panel.add(new JLabel("Dirección Entrega Pedido:"));
         panel.add(new JLabel(pedido.getDireccionEntrega()));
         panel.add(new JLabel("Combo del Pedido:"));
         panel.add(new JLabel(pedido.getCombo() != null ? pedido.getCombo().name() : "N/A"));
         panel.add(new JLabel("Precio Combo Pedido:"));
         panel.add(new JLabel(String.valueOf(pedido.getPrecioCombo())));
         panel.add(new JLabel("Método Pago Pedido:"));
         panel.add(new JLabel(pedido.getMetodoPago() != null ? pedido.getMetodoPago().name() : "N/A"));
         panel.add(new JLabel("Requiere Cambio Pedido:"));
         panel.add(new JLabel(pedido.isRequiereCambio() ? "Sí" : "No"));
         panel.add(new JLabel("Costo Entrega Pedido:"));
         panel.add(new JLabel(String.valueOf(pedido.getCostoEntrega())));
         panel.add(new JLabel("Vuelto Pedido:"));
         panel.add(new JLabel(String.valueOf(pedido.getVuelto())));
         panel.add(new JLabel("Estado Pedido:"));
         panel.add(new JLabel(pedido.getEstado()));
         panel.add(new JLabel("Fecha Creación Pedido:"));
         panel.add(new JLabel(pedido.getFechaCreacion() != null ? dateFormat.format(pedido.getFechaCreacion()) : "N/A")); 
         if (pedido.getFechaEntrega() != null) {
            panel.add(new JLabel("Fecha Entrega Pedido:"));
            panel.add(new JLabel(dateFormat.format(pedido.getFechaEntrega())));
         }
      } else {
         panel.add(new JLabel("Pedido Asociado:"));
         panel.add(new JLabel("N/A"));
      }
   }

   /*
    * Añade la información del cliente de un pedido asociado a un historial a un panel.
    */
   private void addClientInfoToHistorialPedidoPanel(JPanel panel, Cliente cliente) {
      if (cliente != null) {
         panel.add(new JLabel("Nombre Cliente Pedido:"));
         panel.add(new JLabel(cliente.getNombre()));
         panel.add(new JLabel("Cédula Cliente Pedido:"));
         panel.add(new JLabel(cliente.getCedulaIdentidad()));
      } else {
         panel.add(new JLabel("Cliente del Pedido:"));
         panel.add(new JLabel("N/A"));
      }
   }

   /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      buttonGroup1 = new javax.swing.ButtonGroup();
      jPanel1 = new javax.swing.JPanel();
      Dashboard = new javax.swing.JPanel();
      jLabel2 = new javax.swing.JLabel();
      jLabel8 = new javax.swing.JLabel();
      Cliente = new javax.swing.JPanel();
      jLabel7 = new javax.swing.JLabel();
      jLabel13 = new javax.swing.JLabel();
      Repartidor = new javax.swing.JPanel();
      jLabel6 = new javax.swing.JLabel();
      jLabel15 = new javax.swing.JLabel();
      Pedido = new javax.swing.JPanel();
      jLabel5 = new javax.swing.JLabel();
      jLabel12 = new javax.swing.JLabel();
      HistorialDeEntrega = new javax.swing.JPanel();
      jLabel4 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jScrollPane8 = new javax.swing.JScrollPane();
      jTextArea1 = new javax.swing.JTextArea();
      Estadistica = new javax.swing.JPanel();
      jLabel37 = new javax.swing.JLabel();
      jLabel38 = new javax.swing.JLabel();
      jLabel1 = new javax.swing.JLabel();
      jTabbedPane1 = new javax.swing.JTabbedPane();
      PestañaDashboard = new javax.swing.JPanel();
      jPanel10 = new javax.swing.JPanel();
      jScrollPane3 = new javax.swing.JScrollPane();
      jTable3 = new javax.swing.JTable();
      jPanel9 = new javax.swing.JPanel();
      jScrollPane2 = new javax.swing.JScrollPane();
      jTable2 = new javax.swing.JTable();
      jPanel8 = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      jTable1 = new javax.swing.JTable();
      jLabel9 = new javax.swing.JLabel();
      jLabel10 = new javax.swing.JLabel();
      jLabel21 = new javax.swing.JLabel();
      PestañaCliente = new javax.swing.JPanel();
      jPanel12 = new javax.swing.JPanel();
      jScrollPane4 = new javax.swing.JScrollPane();
      jTable4 = new javax.swing.JTable();
      jTextField1 = new javax.swing.JTextField();
      jLabel31 = new javax.swing.JLabel();
      jPanel19 = new javax.swing.JPanel();
      RegistrarClientejButton = new javax.swing.JButton();
      textNumeroTelefonoCliente = new java.awt.TextField();
      textDireccionCliente = new java.awt.TextField();
      textNombreCliente = new java.awt.TextField();
      textCedulaIdentidadCliente = new java.awt.TextField();
      jLabel11 = new javax.swing.JLabel();
      jLabel14 = new javax.swing.JLabel();
      jLabel16 = new javax.swing.JLabel();
      jLabel17 = new javax.swing.JLabel();
      jLabel32 = new javax.swing.JLabel();
      PestañaRepartidor = new javax.swing.JPanel();
      jPanel20 = new javax.swing.JPanel();
      RegistrarRepartidorjButton = new javax.swing.JButton();
      textNumeroTelefonoRepartidor = new java.awt.TextField();
      textNombreRepartidor = new java.awt.TextField();
      textCedulaIdentidadRepartidor = new java.awt.TextField();
      jLabel18 = new javax.swing.JLabel();
      jLabel19 = new javax.swing.JLabel();
      jLabel20 = new javax.swing.JLabel();
      jPanel16 = new javax.swing.JPanel();
      jScrollPane5 = new javax.swing.JScrollPane();
      jTable5 = new javax.swing.JTable();
      jTextField2 = new javax.swing.JTextField();
      jLabel34 = new javax.swing.JLabel();
      jLabel33 = new javax.swing.JLabel();
      PestañaPedido = new javax.swing.JPanel();
      jPanel17 = new javax.swing.JPanel();
      jScrollPane6 = new javax.swing.JScrollPane();
      jTable6 = new javax.swing.JTable();
      jTextField5 = new javax.swing.JTextField();
      jLabel35 = new javax.swing.JLabel();
      jPanel21 = new javax.swing.JPanel();
      RegistrarPedidojButton = new javax.swing.JButton();
      textVueltoPedido = new java.awt.TextField();
      textCostoDeEntregaPedido = new java.awt.TextField();
      textDireccionPedido = new java.awt.TextField();
      textPrecioPedido = new java.awt.TextField();
      jLabel22 = new javax.swing.JLabel();
      jLabel23 = new javax.swing.JLabel();
      jLabel24 = new javax.swing.JLabel();
      jLabel25 = new javax.swing.JLabel();
      ClientesPedidosjComboBox = new javax.swing.JComboBox<>();
      MetodoDePagojComboBox = new javax.swing.JComboBox<>();
      jLabel27 = new javax.swing.JLabel();
      jLabel28 = new javax.swing.JLabel();
      jLabel29 = new javax.swing.JLabel();
      jLabel30 = new javax.swing.JLabel();
      RepartidoresPedidojComboBox = new javax.swing.JComboBox<>();
      CombosjComboBox = new javax.swing.JComboBox<>();
      SIjRadioButton = new javax.swing.JRadioButton();
      NOjRadioButton = new javax.swing.JRadioButton();
      jLabel26 = new javax.swing.JLabel();
      jLabel36 = new javax.swing.JLabel();
      PestañaHistorialDeEntrega = new javax.swing.JPanel();
      jPanel18 = new javax.swing.JPanel();
      jScrollPane7 = new javax.swing.JScrollPane();
      jTable7 = new javax.swing.JTable();
      jTextField4 = new javax.swing.JTextField();
      jLabel40 = new javax.swing.JLabel();
      PestañaEstadisticas = new javax.swing.JPanel();
      jPanel2 = new javax.swing.JPanel();
      jPanel3 = new javax.swing.JPanel();
      jLabel39 = new javax.swing.JLabel();
      jLabel49 = new javax.swing.JLabel();
      jPanel4 = new javax.swing.JPanel();
      jLabel41 = new javax.swing.JLabel();
      jLabel48 = new javax.swing.JLabel();
      jPanel5 = new javax.swing.JPanel();
      jLabel42 = new javax.swing.JLabel();
      jLabel47 = new javax.swing.JLabel();
      jPanel6 = new javax.swing.JPanel();
      jLabel43 = new javax.swing.JLabel();
      jLabel46 = new javax.swing.JLabel();
      jPanel7 = new javax.swing.JPanel();
      jLabel44 = new javax.swing.JLabel();
      jLabel45 = new javax.swing.JLabel();
      jPanel11 = new javax.swing.JPanel();
      jScrollPane9 = new javax.swing.JScrollPane();
      jTable8 = new javax.swing.JTable();
      jLabel50 = new javax.swing.JLabel();
      jPanel13 = new javax.swing.JPanel();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setLocation(new java.awt.Point(0, 0));
      getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jPanel1.setBackground(new java.awt.Color(255, 255, 255));
      jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      Dashboard.setBackground(new java.awt.Color(255, 210, 38));
      Dashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/dashboard.png"))); // NOI18N
      Dashboard.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

      jLabel8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      jLabel8.setForeground(new java.awt.Color(0, 0, 0));
      jLabel8.setText("Dashboard");
      Dashboard.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, -1, -1));

      jPanel1.add(Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 270, 60));

      Cliente.setBackground(new java.awt.Color(255, 210, 38));
      Cliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      jLabel7.setForeground(new java.awt.Color(0, 0, 0));
      jLabel7.setText("Cliente");
      Cliente.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, -1, -1));

      jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/cliente.png"))); // NOI18N
      Cliente.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

      jPanel1.add(Cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 270, 60));

      Repartidor.setBackground(new java.awt.Color(255, 210, 38));
      Repartidor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      jLabel6.setForeground(new java.awt.Color(0, 0, 0));
      jLabel6.setText("Repartidor");
      Repartidor.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, -1, -1));

      jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/repartidor.png"))); // NOI18N
      Repartidor.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

      jPanel1.add(Repartidor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 270, 60));

      Pedido.setBackground(new java.awt.Color(255, 210, 38));
      Pedido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      jLabel5.setForeground(new java.awt.Color(0, 0, 0));
      jLabel5.setText("Pedido");
      Pedido.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, -1, -1));

      jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/pedido.png"))); // NOI18N
      Pedido.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

      jPanel1.add(Pedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 270, 60));

      HistorialDeEntrega.setBackground(new java.awt.Color(255, 210, 38));
      HistorialDeEntrega.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      jLabel4.setForeground(new java.awt.Color(0, 0, 0));
      jLabel4.setText("Historial de Entrega");
      HistorialDeEntrega.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, -1, -1));

      jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/historialDeEntrega.png"))); // NOI18N
      HistorialDeEntrega.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

      jPanel1.add(HistorialDeEntrega, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 270, 60));

      jScrollPane8.setBorder(null);
      jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      jScrollPane8.setToolTipText("");
      jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      jScrollPane8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

      jTextArea1.setEditable(false);
      jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
      jTextArea1.setColumns(7);
      jTextArea1.setFont(new java.awt.Font("Segoe UI", 1, 125)); // NOI18N
      jTextArea1.setForeground(new java.awt.Color(0, 0, 0));
      jTextArea1.setRows(5);
      jTextArea1.setText("Gj");
      jScrollPane8.setViewportView(jTextArea1);

      jPanel1.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 170, 180));

      Estadistica.setBackground(new java.awt.Color(255, 210, 38));
      Estadistica.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jLabel37.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
      jLabel37.setForeground(new java.awt.Color(0, 0, 0));
      jLabel37.setText("Estadística");
      Estadistica.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 15, -1, -1));

      jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/donut_chart_icon_207653.png"))); // NOI18N
      Estadistica.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

      jPanel1.add(Estadistica, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 600, 270, 60));

      getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 700));

      jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/recource/solido-abstracto-de-fondo-amarillo-brillante-del-sitio-de-la-pared-del-estudio-de-la-pendiente.jpg"))); // NOI18N
      getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 1190, 90));

      PestañaDashboard.setBackground(new java.awt.Color(255, 137, 137));
      PestañaDashboard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jPanel10.setBackground(new java.awt.Color(255, 137, 137));

      jTable3.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
         },
         new String [] {
            "ID Historial", "ID Pedido", "Repartidor", "Fecha", "Estado", "Direccion"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jScrollPane3.setViewportView(jTable3);

      javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
      jPanel10.setLayout(jPanel10Layout);
      jPanel10Layout.setHorizontalGroup(
         jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1138, Short.MAX_VALUE)
            .addContainerGap())
      );
      jPanel10Layout.setVerticalGroup(
         jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
            .addContainerGap())
      );

      PestañaDashboard.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 1150, 210));

      jPanel9.setBackground(new java.awt.Color(255, 137, 137));

      jTable2.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null},
            {null, null},
            {null, null},
            {null, null}
         },
         new String [] {
            "Repartidor", "Disponibilidad"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jScrollPane2.setViewportView(jTable2);
      if (jTable2.getColumnModel().getColumnCount() > 0) {
         jTable2.getColumnModel().getColumn(0).setResizable(false);
         jTable2.getColumnModel().getColumn(0).setPreferredWidth(200);
         jTable2.getColumnModel().getColumn(1).setResizable(false);
         jTable2.getColumnModel().getColumn(1).setPreferredWidth(120);
      }

      javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
      jPanel9.setLayout(jPanel9Layout);
      jPanel9Layout.setHorizontalGroup(
         jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
            .addContainerGap())
      );
      jPanel9Layout.setVerticalGroup(
         jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel9Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
            .addContainerGap())
      );

      PestañaDashboard.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 60, 330, 270));

      jPanel8.setBackground(new java.awt.Color(255, 137, 137));

      jTable1.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
         },
         new String [] {
            "ID", "Cliente", "Tiempo", "Estado", "Acción"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, true
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jScrollPane1.setViewportView(jTable1);
      if (jTable1.getColumnModel().getColumnCount() > 0) {
         jTable1.getColumnModel().getColumn(0).setResizable(false);
         jTable1.getColumnModel().getColumn(1).setResizable(false);
         jTable1.getColumnModel().getColumn(2).setResizable(false);
         jTable1.getColumnModel().getColumn(3).setResizable(false);
         jTable1.getColumnModel().getColumn(4).setResizable(false);
      }

      javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
      jPanel8.setLayout(jPanel8Layout);
      jPanel8Layout.setHorizontalGroup(
         jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
            .addContainerGap())
      );
      jPanel8Layout.setVerticalGroup(
         jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
            .addContainerGap())
      );

      PestañaDashboard.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 800, 270));

      jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel9.setForeground(new java.awt.Color(0, 0, 0));
      jLabel9.setText("Historial de Pedidos");
      PestañaDashboard.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 350, 40));

      jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel10.setForeground(new java.awt.Color(0, 0, 0));
      jLabel10.setText("Pedidos Pendientes");
      PestañaDashboard.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 40));

      jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel21.setForeground(new java.awt.Color(0, 0, 0));
      jLabel21.setText("Repartidores Disponibles");
      PestañaDashboard.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 20, -1, 40));

      jTabbedPane1.addTab("Pestaña Dashboard", PestañaDashboard);

      PestañaCliente.setBackground(new java.awt.Color(255, 137, 137));

      jPanel12.setBackground(new java.awt.Color(255, 137, 137));

      jTable4.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
         },
         new String [] {
            "Nombre", "C.I.", "N° Teléfono", "Dirección", "Detalle"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, true
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jTable4.setColumnSelectionAllowed(true);
      jScrollPane4.setViewportView(jTable4);
      if (jTable4.getColumnModel().getColumnCount() > 0) {
         jTable4.getColumnModel().getColumn(0).setResizable(false);
         jTable4.getColumnModel().getColumn(1).setResizable(false);
         jTable4.getColumnModel().getColumn(2).setResizable(false);
         jTable4.getColumnModel().getColumn(3).setResizable(false);
         jTable4.getColumnModel().getColumn(4).setResizable(false);
      }

      jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
      jTextField1.setText("Buscar cliente...");
      jTextField1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField1ActionPerformed(evt);
         }
      });

      jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel31.setForeground(new java.awt.Color(0, 0, 0));
      jLabel31.setText("Lista de Clientes");

      javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
      jPanel12.setLayout(jPanel12Layout);
      jPanel12Layout.setHorizontalGroup(
         jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                  .addComponent(jLabel31)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      jPanel12Layout.setVerticalGroup(
         jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel31))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );

      jPanel19.setBackground(new java.awt.Color(255, 255, 255));

      RegistrarClientejButton.setText("Registrar");
      RegistrarClientejButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            RegistrarClientejButtonActionPerformed(evt);
         }
      });

      textNumeroTelefonoCliente.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

      textDireccionCliente.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
      textDireccionCliente.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            textDireccionClienteActionPerformed(evt);
         }
      });

      textNombreCliente.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

      textCedulaIdentidadCliente.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
      textCedulaIdentidadCliente.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            textCedulaIdentidadClienteActionPerformed(evt);
         }
      });

      jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel11.setForeground(new java.awt.Color(0, 0, 0));
      jLabel11.setText("Nombre");
      jLabel11.setToolTipText("");

      jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel14.setForeground(new java.awt.Color(0, 0, 0));
      jLabel14.setText("Cedula de Identidad");

      jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel16.setForeground(new java.awt.Color(0, 0, 0));
      jLabel16.setText("Numero de telefono");

      jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel17.setForeground(new java.awt.Color(0, 0, 0));
      jLabel17.setText("Direccion");

      javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
      jPanel19.setLayout(jPanel19Layout);
      jPanel19Layout.setHorizontalGroup(
         jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel19Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(textDireccionCliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(textNumeroTelefonoCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(textCedulaIdentidadCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(textNombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(jPanel19Layout.createSequentialGroup()
                  .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
         .addGroup(jPanel19Layout.createSequentialGroup()
            .addGap(119, 119, 119)
            .addComponent(RegistrarClientejButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(142, Short.MAX_VALUE))
      );
      jPanel19Layout.setVerticalGroup(
         jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel11)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel14)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textCedulaIdentidadCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel16)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textNumeroTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel17)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(RegistrarClientejButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel32.setForeground(new java.awt.Color(0, 0, 0));
      jLabel32.setText("Registrar Cliente");

      javax.swing.GroupLayout PestañaClienteLayout = new javax.swing.GroupLayout(PestañaCliente);
      PestañaCliente.setLayout(PestañaClienteLayout);
      PestañaClienteLayout.setHorizontalGroup(
         PestañaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(PestañaClienteLayout.createSequentialGroup()
            .addContainerGap(18, Short.MAX_VALUE)
            .addGroup(PestañaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel32))
            .addGap(18, 18, 18)
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(20, 20, 20))
      );
      PestañaClienteLayout.setVerticalGroup(
         PestañaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(PestañaClienteLayout.createSequentialGroup()
            .addGap(71, 71, 71)
            .addGroup(PestañaClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGroup(PestañaClienteLayout.createSequentialGroup()
                  .addGap(10, 10, 10)
                  .addComponent(jLabel32)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(64, Short.MAX_VALUE))
      );

      jTabbedPane1.addTab("Pestaña Cliente", PestañaCliente);

      PestañaRepartidor.setBackground(new java.awt.Color(255, 137, 137));

      jPanel20.setBackground(new java.awt.Color(255, 255, 255));

      RegistrarRepartidorjButton.setText("Registrar");
      RegistrarRepartidorjButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            RegistrarRepartidorjButtonActionPerformed(evt);
         }
      });

      textNumeroTelefonoRepartidor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

      textNombreRepartidor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

      textCedulaIdentidadRepartidor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
      textCedulaIdentidadRepartidor.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            textCedulaIdentidadRepartidorActionPerformed(evt);
         }
      });

      jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel18.setText("Nombre");
      jLabel18.setToolTipText("");

      jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel19.setText("Cedula de Identidad");

      jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel20.setText("Numero de telefono");

      javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
      jPanel20.setLayout(jPanel20Layout);
      jPanel20Layout.setHorizontalGroup(
         jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(textNumeroTelefonoRepartidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(textCedulaIdentidadRepartidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(textNombreRepartidor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addGroup(jPanel20Layout.createSequentialGroup()
                  .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
         .addGroup(jPanel20Layout.createSequentialGroup()
            .addGap(121, 121, 121)
            .addComponent(RegistrarRepartidorjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(140, Short.MAX_VALUE))
      );
      jPanel20Layout.setVerticalGroup(
         jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
            .addContainerGap(7, Short.MAX_VALUE)
            .addComponent(jLabel18)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textNombreRepartidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel19)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textCedulaIdentidadRepartidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel20)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textNumeroTelefonoRepartidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(19, 19, 19)
            .addComponent(RegistrarRepartidorjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(19, 19, 19))
      );

      jPanel16.setBackground(new java.awt.Color(255, 137, 137));

      jTable5.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
         },
         new String [] {
            "Nombre", "C.I.", "N° Teléfono", "Disponibilidad", "Detalle"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, true
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jTable5.setColumnSelectionAllowed(true);
      jScrollPane5.setViewportView(jTable5);
      if (jTable5.getColumnModel().getColumnCount() > 0) {
         jTable5.getColumnModel().getColumn(0).setResizable(false);
         jTable5.getColumnModel().getColumn(1).setResizable(false);
         jTable5.getColumnModel().getColumn(2).setResizable(false);
         jTable5.getColumnModel().getColumn(3).setResizable(false);
         jTable5.getColumnModel().getColumn(4).setResizable(false);
      }

      jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
      jTextField2.setText("Buscar repartidor...");
      jTextField2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField2ActionPerformed(evt);
         }
      });

      jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel34.setForeground(new java.awt.Color(0, 0, 0));
      jLabel34.setText("Lista de Repartidores");

      javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
      jPanel16.setLayout(jPanel16Layout);
      jPanel16Layout.setHorizontalGroup(
         jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel16Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
               .addGroup(jPanel16Layout.createSequentialGroup()
                  .addComponent(jLabel34)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      jPanel16Layout.setVerticalGroup(
         jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel34))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );

      jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel33.setForeground(new java.awt.Color(0, 0, 0));
      jLabel33.setText("Registrar Repartidor");

      javax.swing.GroupLayout PestañaRepartidorLayout = new javax.swing.GroupLayout(PestañaRepartidor);
      PestañaRepartidor.setLayout(PestañaRepartidorLayout);
      PestañaRepartidorLayout.setHorizontalGroup(
         PestañaRepartidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PestañaRepartidorLayout.createSequentialGroup()
            .addGap(15, 15, 15)
            .addGroup(PestañaRepartidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel33))
            .addGap(18, 18, 18)
            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(23, Short.MAX_VALUE))
      );
      PestañaRepartidorLayout.setVerticalGroup(
         PestañaRepartidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(PestañaRepartidorLayout.createSequentialGroup()
            .addGap(50, 50, 50)
            .addGroup(PestañaRepartidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGroup(PestañaRepartidorLayout.createSequentialGroup()
                  .addGap(6, 6, 6)
                  .addComponent(jLabel33)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(85, Short.MAX_VALUE))
      );

      jTabbedPane1.addTab("Pestaña Repartidor", PestañaRepartidor);

      PestañaPedido.setBackground(new java.awt.Color(255, 137, 137));

      jPanel17.setBackground(new java.awt.Color(255, 137, 137));

      jTable6.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null}
         },
         new String [] {
            "ID", "Cliente", "Fecha", "Total", "Estado", "Repartidor", "Método de Pago", "Acción"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, true
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jTable6.setColumnSelectionAllowed(true);
      jScrollPane6.setViewportView(jTable6);

      jTextField5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
      jTextField5.setForeground(new java.awt.Color(0, 0, 0));
      jTextField5.setText("Buscar Pedido...");
      jTextField5.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField5ActionPerformed(evt);
         }
      });

      jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel35.setForeground(new java.awt.Color(0, 0, 0));
      jLabel35.setText("Lista de Pedidos");

      javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
      jPanel17.setLayout(jPanel17Layout);
      jPanel17Layout.setHorizontalGroup(
         jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jScrollPane6)
               .addGroup(jPanel17Layout.createSequentialGroup()
                  .addComponent(jLabel35)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
                  .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      jPanel17Layout.setVerticalGroup(
         jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel35))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
            .addContainerGap())
      );

      jPanel21.setBackground(new java.awt.Color(255, 255, 255));

      RegistrarPedidojButton.setForeground(new java.awt.Color(0, 0, 0));
      RegistrarPedidojButton.setText("Registrar");
      RegistrarPedidojButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            RegistrarPedidojButtonActionPerformed(evt);
         }
      });

      textVueltoPedido.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

      textCostoDeEntregaPedido.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
      textCostoDeEntregaPedido.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            textCostoDeEntregaPedidoActionPerformed(evt);
         }
      });

      textDireccionPedido.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

      textPrecioPedido.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
      textPrecioPedido.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            textPrecioPedidoActionPerformed(evt);
         }
      });

      jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel22.setForeground(new java.awt.Color(0, 0, 0));
      jLabel22.setText("Cliente");
      jLabel22.setToolTipText("");

      jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel23.setForeground(new java.awt.Color(0, 0, 0));
      jLabel23.setText("Metodo de pago");

      jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel24.setForeground(new java.awt.Color(0, 0, 0));
      jLabel24.setText("Combo");

      jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel25.setForeground(new java.awt.Color(0, 0, 0));
      jLabel25.setText("Direccion");

      ClientesPedidosjComboBox.setForeground(new java.awt.Color(0, 0, 0));
      ClientesPedidosjComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());
      ClientesPedidosjComboBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            ClientesPedidosjComboBoxActionPerformed(evt);
         }
      });

      MetodoDePagojComboBox.setForeground(new java.awt.Color(0, 0, 0));
      MetodoDePagojComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());
      MetodoDePagojComboBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            MetodoDePagojComboBoxActionPerformed(evt);
         }
      });

      jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel27.setForeground(new java.awt.Color(0, 0, 0));
      jLabel27.setText("Requiere cambio");

      jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel28.setForeground(new java.awt.Color(0, 0, 0));
      jLabel28.setText("Costo de entrega");

      jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel29.setForeground(new java.awt.Color(0, 0, 0));
      jLabel29.setText("Repartidor");

      jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel30.setForeground(new java.awt.Color(0, 0, 0));
      jLabel30.setText("Vuelto");

      RepartidoresPedidojComboBox.setForeground(new java.awt.Color(0, 0, 0));
      RepartidoresPedidojComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());

      CombosjComboBox.setForeground(new java.awt.Color(0, 0, 0));
      CombosjComboBox.setModel(new javax.swing.DefaultComboBoxModel<>());

      buttonGroup1.add(SIjRadioButton);
      SIjRadioButton.setForeground(new java.awt.Color(0, 0, 0));
      SIjRadioButton.setText("SI");
      SIjRadioButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            SIjRadioButtonActionPerformed(evt);
         }
      });

      buttonGroup1.add(NOjRadioButton);
      NOjRadioButton.setForeground(new java.awt.Color(0, 0, 0));
      NOjRadioButton.setText("NO");
      NOjRadioButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            NOjRadioButtonActionPerformed(evt);
         }
      });

      jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel26.setForeground(new java.awt.Color(0, 0, 0));
      jLabel26.setText("Precio");

      javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
      jPanel21.setLayout(jPanel21Layout);
      jPanel21Layout.setHorizontalGroup(
         jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel21Layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(textDireccionPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(textVueltoPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(245, 245, 245))
                     .addComponent(ClientesPedidosjComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(RepartidoresPedidojComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                              .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))
                           .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
               .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel21Layout.createSequentialGroup()
                  .addGap(104, 104, 104)
                  .addComponent(RegistrarPedidojButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel21Layout.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(SIjRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(NOjRadioButton))
                     .addComponent(MetodoDePagojComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                           .addComponent(CombosjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addComponent(textPrecioPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                           .addGroup(jPanel21Layout.createSequentialGroup()
                              .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addGap(0, 0, Short.MAX_VALUE))))
                     .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(textCostoDeEntregaPedido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)))))
            .addContainerGap())
      );
      jPanel21Layout.setVerticalGroup(
         jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel22)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(ClientesPedidosjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel25)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(textDireccionPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel24)
               .addComponent(jLabel26))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel21Layout.createSequentialGroup()
                  .addComponent(CombosjComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel23)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(MetodoDePagojComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel27)
                     .addComponent(SIjRadioButton)
                     .addComponent(NOjRadioButton))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel28)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(textCostoDeEntregaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel30)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(textVueltoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(jLabel29)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(RepartidoresPedidojComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(RegistrarPedidojButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addComponent(textPrecioPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel36.setForeground(new java.awt.Color(0, 0, 0));
      jLabel36.setText("Crear Pedido");

      javax.swing.GroupLayout PestañaPedidoLayout = new javax.swing.GroupLayout(PestañaPedido);
      PestañaPedido.setLayout(PestañaPedidoLayout);
      PestañaPedidoLayout.setHorizontalGroup(
         PestañaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(PestañaPedidoLayout.createSequentialGroup()
            .addGap(26, 26, 26)
            .addGroup(PestañaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel36))
            .addGap(18, 18, 18)
            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(7, Short.MAX_VALUE))
      );
      PestañaPedidoLayout.setVerticalGroup(
         PestañaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(PestañaPedidoLayout.createSequentialGroup()
            .addContainerGap(23, Short.MAX_VALUE)
            .addGroup(PestañaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addGroup(PestañaPedidoLayout.createSequentialGroup()
                  .addGap(5, 5, 5)
                  .addComponent(jLabel36)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                  .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(15, 15, 15))
      );

      jTabbedPane1.addTab("Pestaña Pedido", PestañaPedido);

      PestañaHistorialDeEntrega.setBackground(new java.awt.Color(255, 137, 137));

      jPanel18.setBackground(new java.awt.Color(255, 137, 137));

      jTable7.setAutoCreateRowSorter(true);
      jTable7.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null}
         },
         new String [] {
            "ID Historial", "ID Pedido", "Combo", "Monto total", "Cliente", "Repartidor", "Metodo de Pago", "Estado", "Detalle"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false, false, false, true
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jTable7.setColumnSelectionAllowed(true);
      jScrollPane7.setViewportView(jTable7);
      if (jTable7.getColumnModel().getColumnCount() > 0) {
         jTable7.getColumnModel().getColumn(0).setResizable(false);
         jTable7.getColumnModel().getColumn(1).setResizable(false);
         jTable7.getColumnModel().getColumn(2).setResizable(false);
         jTable7.getColumnModel().getColumn(3).setResizable(false);
         jTable7.getColumnModel().getColumn(4).setResizable(false);
         jTable7.getColumnModel().getColumn(5).setResizable(false);
         jTable7.getColumnModel().getColumn(6).setResizable(false);
         jTable7.getColumnModel().getColumn(7).setResizable(false);
         jTable7.getColumnModel().getColumn(8).setResizable(false);
      }

      jTextField4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
      jTextField4.setText("Buscar Dato...");
      jTextField4.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField4ActionPerformed(evt);
         }
      });

      jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel40.setForeground(new java.awt.Color(0, 0, 0));
      jLabel40.setText("Historial Completo de Entregas");

      javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
      jPanel18.setLayout(jPanel18Layout);
      jPanel18Layout.setHorizontalGroup(
         jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel18Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1128, Short.MAX_VALUE)
               .addGroup(jPanel18Layout.createSequentialGroup()
                  .addComponent(jLabel40)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      jPanel18Layout.setVerticalGroup(
         jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel40))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
            .addContainerGap())
      );

      javax.swing.GroupLayout PestañaHistorialDeEntregaLayout = new javax.swing.GroupLayout(PestañaHistorialDeEntrega);
      PestañaHistorialDeEntrega.setLayout(PestañaHistorialDeEntregaLayout);
      PestañaHistorialDeEntregaLayout.setHorizontalGroup(
         PestañaHistorialDeEntregaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PestañaHistorialDeEntregaLayout.createSequentialGroup()
            .addContainerGap(27, Short.MAX_VALUE)
            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(23, 23, 23))
      );
      PestañaHistorialDeEntregaLayout.setVerticalGroup(
         PestañaHistorialDeEntregaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(PestañaHistorialDeEntregaLayout.createSequentialGroup()
            .addGap(22, 22, 22)
            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(15, Short.MAX_VALUE))
      );

      jTabbedPane1.addTab("Pestaña Historial de Entrega", PestañaHistorialDeEntrega);

      jPanel2.setBackground(new java.awt.Color(255, 137, 137));

      jPanel3.setBackground(new java.awt.Color(204, 204, 204));

      jLabel39.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
      jLabel39.setForeground(new java.awt.Color(0, 0, 0));
      jLabel39.setText("Repartidores Disponibles/Activos");

      jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
      jLabel49.setForeground(new java.awt.Color(0, 0, 0));
      jLabel49.setText("jLabel49");

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel39)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel49)
            .addContainerGap())
      );

      jPanel4.setBackground(new java.awt.Color(204, 204, 204));

      jLabel41.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
      jLabel41.setForeground(new java.awt.Color(0, 0, 0));
      jLabel41.setText("Pedidos en Curso");

      jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
      jLabel48.setForeground(new java.awt.Color(0, 0, 0));
      jLabel48.setText("jLabel48");

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      jPanel4Layout.setVerticalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel41)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel48)
            .addContainerGap())
      );

      jPanel5.setBackground(new java.awt.Color(204, 204, 204));

      jLabel42.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
      jLabel42.setForeground(new java.awt.Color(0, 0, 0));
      jLabel42.setText("Tiempo Promedio de Entrega");

      jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
      jLabel47.setForeground(new java.awt.Color(0, 0, 0));
      jLabel47.setText("jLabel47");

      javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
      jPanel5.setLayout(jPanel5Layout);
      jPanel5Layout.setHorizontalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      jPanel5Layout.setVerticalGroup(
         jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel42)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel47)
            .addContainerGap())
      );

      jPanel6.setBackground(new java.awt.Color(204, 204, 204));

      jLabel43.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
      jLabel43.setForeground(new java.awt.Color(0, 0, 0));
      jLabel43.setText("On-Time Delivery Porcentaje");

      jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
      jLabel46.setForeground(new java.awt.Color(0, 0, 0));
      jLabel46.setText("jLabel46");

      javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
      jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(29, Short.MAX_VALUE))
      );
      jPanel6Layout.setVerticalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel43)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel46)
            .addContainerGap())
      );

      jPanel7.setBackground(new java.awt.Color(204, 204, 204));

      jLabel44.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
      jLabel44.setForeground(new java.awt.Color(0, 0, 0));
      jLabel44.setText("Pedidos Entregados Hoy");

      jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
      jLabel45.setForeground(new java.awt.Color(0, 0, 0));
      jLabel45.setText("jLabel45");

      javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
      jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      jPanel7Layout.setVerticalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel44)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel45)
            .addContainerGap())
      );

      jPanel11.setBackground(new java.awt.Color(255, 137, 137));

      jTable8.setForeground(new java.awt.Color(0, 0, 0));
      jTable8.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null},
            {null, null, null}
         },
         new String [] {
            "Nombre del Repartidor", "Número de Entregas", "OTD%"
         }
      ) {
         Class[] types = new Class [] {
            java.lang.String.class, java.lang.String.class, java.lang.Double.class
         };
         boolean[] canEdit = new boolean [] {
            false, false, false
         };

         public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
         }

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jTable8.setRowHeight(40);
      jTable8.setRowMargin(5);
      jScrollPane9.setViewportView(jTable8);
      if (jTable8.getColumnModel().getColumnCount() > 0) {
         jTable8.getColumnModel().getColumn(0).setResizable(false);
         jTable8.getColumnModel().getColumn(1).setResizable(false);
         jTable8.getColumnModel().getColumn(2).setResizable(false);
      }

      jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel50.setForeground(new java.awt.Color(0, 0, 0));
      jLabel50.setText("Repartidores TOP");

      javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
      jPanel11.setLayout(jPanel11Layout);
      jPanel11Layout.setHorizontalGroup(
         jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel50))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      jPanel11Layout.setVerticalGroup(
         jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel50)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel13.setPreferredSize(new java.awt.Dimension(757, 475));

      javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
      jPanel13.setLayout(jPanel13Layout);
      jPanel13Layout.setHorizontalGroup(
         jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 757, Short.MAX_VALUE)
      );
      jPanel13Layout.setVerticalGroup(
         jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 475, Short.MAX_VALUE)
      );

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel2Layout.createSequentialGroup()
                  .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addGroup(jPanel2Layout.createSequentialGroup()
                  .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addGap(18, 18, 18)
                  .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGap(18, 18, 18)
                  .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(21, 21, 21)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
               .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
               .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      javax.swing.GroupLayout PestañaEstadisticasLayout = new javax.swing.GroupLayout(PestañaEstadisticas);
      PestañaEstadisticas.setLayout(PestañaEstadisticasLayout);
      PestañaEstadisticasLayout.setHorizontalGroup(
         PestañaEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      );
      PestañaEstadisticasLayout.setVerticalGroup(
         PestañaEstadisticasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      );

      jTabbedPane1.addTab("tab6", PestañaEstadisticas);

      getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 1190, 650));

      pack();
      setLocationRelativeTo(null);
   }// </editor-fold>//GEN-END:initComponents

   private void RegistrarClientejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegistrarClientejButtonActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_RegistrarClientejButtonActionPerformed

   private void textCedulaIdentidadClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCedulaIdentidadClienteActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_textCedulaIdentidadClienteActionPerformed

   private void RegistrarPedidojButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegistrarPedidojButtonActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_RegistrarPedidojButtonActionPerformed

   private void textCostoDeEntregaPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCostoDeEntregaPedidoActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_textCostoDeEntregaPedidoActionPerformed

   private void textPrecioPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textPrecioPedidoActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_textPrecioPedidoActionPerformed

   private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_jTextField2ActionPerformed

   private void MetodoDePagojComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MetodoDePagojComboBoxActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_MetodoDePagojComboBoxActionPerformed

   private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_jTextField4ActionPerformed

   private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_jTextField5ActionPerformed
      // TODO add your handling code here:
   private void ClientesPedidosjComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientesPedidosjComboBoxActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_ClientesPedidosjComboBoxActionPerformed

   private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_jTextField1ActionPerformed

   private void SIjRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SIjRadioButtonActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_SIjRadioButtonActionPerformed

   private void NOjRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NOjRadioButtonActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_NOjRadioButtonActionPerformed

   private void textDireccionClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textDireccionClienteActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_textDireccionClienteActionPerformed

   private void textCedulaIdentidadRepartidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCedulaIdentidadRepartidorActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_textCedulaIdentidadRepartidorActionPerformed

   private void RegistrarRepartidorjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegistrarRepartidorjButtonActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_RegistrarRepartidorjButtonActionPerformed

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JPanel Cliente;
   private javax.swing.JComboBox<Cliente> ClientesPedidosjComboBox;
   private javax.swing.JComboBox<Combo> CombosjComboBox;
   private javax.swing.JPanel Dashboard;
   private javax.swing.JPanel Estadistica;
   private javax.swing.JPanel HistorialDeEntrega;
   private javax.swing.JComboBox<MetodoDePago> MetodoDePagojComboBox;
   private javax.swing.JRadioButton NOjRadioButton;
   private javax.swing.JPanel Pedido;
   private javax.swing.JPanel PestañaCliente;
   private javax.swing.JPanel PestañaDashboard;
   private javax.swing.JPanel PestañaEstadisticas;
   private javax.swing.JPanel PestañaHistorialDeEntrega;
   private javax.swing.JPanel PestañaPedido;
   private javax.swing.JPanel PestañaRepartidor;
   private javax.swing.JButton RegistrarClientejButton;
   private javax.swing.JButton RegistrarPedidojButton;
   private javax.swing.JButton RegistrarRepartidorjButton;
   private javax.swing.JPanel Repartidor;
   private javax.swing.JComboBox<Repartidor> RepartidoresPedidojComboBox;
   private javax.swing.JRadioButton SIjRadioButton;
   private javax.swing.ButtonGroup buttonGroup1;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel10;
   private javax.swing.JLabel jLabel11;
   private javax.swing.JLabel jLabel12;
   private javax.swing.JLabel jLabel13;
   private javax.swing.JLabel jLabel14;
   private javax.swing.JLabel jLabel15;
   private javax.swing.JLabel jLabel16;
   private javax.swing.JLabel jLabel17;
   private javax.swing.JLabel jLabel18;
   private javax.swing.JLabel jLabel19;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel20;
   private javax.swing.JLabel jLabel21;
   private javax.swing.JLabel jLabel22;
   private javax.swing.JLabel jLabel23;
   private javax.swing.JLabel jLabel24;
   private javax.swing.JLabel jLabel25;
   private javax.swing.JLabel jLabel26;
   private javax.swing.JLabel jLabel27;
   private javax.swing.JLabel jLabel28;
   private javax.swing.JLabel jLabel29;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel30;
   private javax.swing.JLabel jLabel31;
   private javax.swing.JLabel jLabel32;
   private javax.swing.JLabel jLabel33;
   private javax.swing.JLabel jLabel34;
   private javax.swing.JLabel jLabel35;
   private javax.swing.JLabel jLabel36;
   private javax.swing.JLabel jLabel37;
   private javax.swing.JLabel jLabel38;
   private javax.swing.JLabel jLabel39;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel40;
   private javax.swing.JLabel jLabel41;
   private javax.swing.JLabel jLabel42;
   private javax.swing.JLabel jLabel43;
   private javax.swing.JLabel jLabel44;
   private javax.swing.JLabel jLabel45;
   private javax.swing.JLabel jLabel46;
   private javax.swing.JLabel jLabel47;
   private javax.swing.JLabel jLabel48;
   private javax.swing.JLabel jLabel49;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JLabel jLabel50;
   private javax.swing.JLabel jLabel6;
   private javax.swing.JLabel jLabel7;
   private javax.swing.JLabel jLabel8;
   private javax.swing.JLabel jLabel9;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel10;
   private javax.swing.JPanel jPanel11;
   private javax.swing.JPanel jPanel12;
   private javax.swing.JPanel jPanel13;
   private javax.swing.JPanel jPanel16;
   private javax.swing.JPanel jPanel17;
   private javax.swing.JPanel jPanel18;
   private javax.swing.JPanel jPanel19;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel20;
   private javax.swing.JPanel jPanel21;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel5;
   private javax.swing.JPanel jPanel6;
   private javax.swing.JPanel jPanel7;
   private javax.swing.JPanel jPanel8;
   private javax.swing.JPanel jPanel9;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JScrollPane jScrollPane4;
   private javax.swing.JScrollPane jScrollPane5;
   private javax.swing.JScrollPane jScrollPane6;
   private javax.swing.JScrollPane jScrollPane7;
   private javax.swing.JScrollPane jScrollPane8;
   private javax.swing.JScrollPane jScrollPane9;
   private javax.swing.JTabbedPane jTabbedPane1;
   private javax.swing.JTable jTable1;
   private javax.swing.JTable jTable2;
   private javax.swing.JTable jTable3;
   private javax.swing.JTable jTable4;
   private javax.swing.JTable jTable5;
   private javax.swing.JTable jTable6;
   private javax.swing.JTable jTable7;
   private javax.swing.JTable jTable8;
   private javax.swing.JTextArea jTextArea1;
   private javax.swing.JTextField jTextField1;
   private javax.swing.JTextField jTextField2;
   private javax.swing.JTextField jTextField4;
   private javax.swing.JTextField jTextField5;
   private java.awt.TextField textCedulaIdentidadCliente;
   private java.awt.TextField textCedulaIdentidadRepartidor;
   private java.awt.TextField textCostoDeEntregaPedido;
   private java.awt.TextField textDireccionCliente;
   private java.awt.TextField textDireccionPedido;
   private java.awt.TextField textNombreCliente;
   private java.awt.TextField textNombreRepartidor;
   private java.awt.TextField textNumeroTelefonoCliente;
   private java.awt.TextField textNumeroTelefonoRepartidor;
   private java.awt.TextField textPrecioPedido;
   private java.awt.TextField textVueltoPedido;
   // End of variables declaration//GEN-END:variables
}

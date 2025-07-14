
package project.view;

import project.controller.RepartidorController;
import project.model.entities.Pedido;
import project.model.entities.Repartidor;
import project.view.components.DetalleButtonEditor;
import project.view.components.DetalleButtonRenderer;
import project.view.components.EntregadoButtonEditor;
import project.view.components.EntregadoButtonRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/*
 * Clase principal de la interfaz de usuario para el dashboard del repartidor.
 * Permite al repartidor ver pedidos asignados, historial de entregas y cambiar su disponibilidad.
 */
public class RepartidorDashboardJFrame extends javax.swing.JFrame {
   // Controladores y entidades
   private final RepartidorController repartidorController = new RepartidorController();
   private final Repartidor repartidorActual;

   // Componentes de la UI
   private final JTextField txtBuscarPedido;
   private final JTable tablaPedidosAsignados;
   private final JTable tablaHistorialEntregas;
   private final JTextField txtNombreRepartidor;
   private final JToggleButton toggleDisponibilidad;
   
   /*
    * Constructor de la clase RepartidorDashboardJFrame.
    * Inicializa la interfaz de usuario y carga los datos del repartidor.
    */
   public RepartidorDashboardJFrame(Repartidor repartidor) {
      this.repartidorActual = repartidor;
      initComponents(); // Inicializa los componentes generados por NetBeans
      
      // Asigna los componentes de la UI a las variables de instancia
      txtBuscarPedido = jTextField2;
      tablaPedidosAsignados = jTable1;
      tablaHistorialEntregas = jTable2;
      txtNombreRepartidor = NombreRepartidorjTextField;
      toggleDisponibilidad = DisponibilidadjToggleButton;
      
      inicializarDatosRepartidor(); // Inicializa los datos del repartidor en la UI
      cargarIconoVentana(); // Carga el icono de la ventana
      actualizarEstadoDisponibilidad(); // Actualiza el texto y color del botón de disponibilidad
      cargarDatosYConfigurarTablas(); // Carga datos y configura las tablas
      inicializarFiltros(); // Inicializa los filtros de búsqueda para las tablas
   }
    
   /*
    * Inicializa los datos del repartidor en los campos de texto y el toggle de disponibilidad.
    */
   private void inicializarDatosRepartidor() {
      if (repartidorActual != null) {
         txtNombreRepartidor.setText(repartidorActual.getNombre());
         toggleDisponibilidad.setSelected(repartidorActual.isDisponibilidad());
      } else {
         txtNombreRepartidor.setText("N/A"); // Establece un mensaje por defecto o de error
         toggleDisponibilidad.setEnabled(false); // Deshabilita el toggle si no hay repartidor
         System.err.println("Advertencia: Repartidor actual es nulo en RepartidorDashboardJFrame.");
      }
   }

   /*
    * Carga el icono de la ventana de la aplicación.
    */
   private void cargarIconoVentana() {
      try {
         setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Logo.png")));
      } catch (Exception e) {
         System.err.println("Error al cargar el icono: " + e.getMessage());
      }
   }

   /*
    * Carga los datos en las tablas de pedidos asignados e historial de entregas
    * y configura los renderers y editores de botones para las columnas.
    */
   public final void cargarDatosYConfigurarTablas() {
      // Carga y configura la tabla de pedidos asignados
      DefaultTableModel modeloPedidos = repartidorController.obtenerModeloPedidosAsignados(repartidorActual);
      tablaPedidosAsignados.setModel(modeloPedidos);
      configurarBotonDetallePedidosAsignados(tablaPedidosAsignados, "Detalle");
      configurarBotonEntregadoPedidosAsignados(tablaPedidosAsignados, "Acción"); // Asegúrate de que esta columna exista

      // Carga y configura la tabla de historial de entregas
      DefaultTableModel modeloHistorial = repartidorController.obtenerModeloHistorialDeEntregas(repartidorActual);
      tablaHistorialEntregas.setModel(modeloHistorial);
   }
    
   /*
    * Configura la columna "Detalle" en la tabla de pedidos asignados para mostrar un botón
    * que al ser presionado, abre una ventana con los detalles del pedido.
    */
   private void configurarBotonDetallePedidosAsignados(JTable tabla, String nombreColumna) {
      try {
         int columnaBotonDetalleIndex = tabla.getColumnModel().getColumnIndex(nombreColumna);
         tabla.getColumnModel().getColumn(columnaBotonDetalleIndex).setCellRenderer(new DetalleButtonRenderer("Ver Detalle"));
         
         DetalleButtonEditor detalleEditor = new DetalleButtonEditor(new JCheckBox()) {
            @Override
            public void fireEditingStopped() {
               super.fireEditingStopped();
               int row = tabla.getSelectedRow();
               if (row >= 0) {
                  String idPedido = (String) tabla.getModel().getValueAt(row, 0);
                  Pedido pedido = repartidorController.obtenerDetallesPedido(idPedido);
                  if (pedido != null) {
                     mostrarVentanaDetallePedido(pedido);
                  } else {
                     JOptionPane.showMessageDialog(tabla, "Pedido no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                  }
               }
            }
         };
         tabla.getColumnModel().getColumn(columnaBotonDetalleIndex).setCellEditor(detalleEditor);
      } catch (IllegalArgumentException e) {
         System.err.println("Error: Columna '" + nombreColumna + "' no encontrada en la tabla de pedidos asignados. " + e.getMessage());
      }
   }

   /*
    * Configura la columna "Acción" en la tabla de pedidos asignados para mostrar un botón
    * que al ser presionado, permite marcar el pedido como entregado o cancelarlo.
    */
   private void configurarBotonEntregadoPedidosAsignados(JTable tabla, String nombreColumna) {
      try {
         int columnaBotonEntregadoIndex = tabla.getColumnModel().getColumnIndex(nombreColumna);
         tabla.getColumnModel().getColumn(columnaBotonEntregadoIndex).setCellRenderer(new EntregadoButtonRenderer());
         tabla.getColumnModel().getColumn(columnaBotonEntregadoIndex).setCellEditor(new EntregadoButtonEditor(new JCheckBox(), repartidorController, repartidorActual));
      } catch (IllegalArgumentException e) {
         System.err.println("Error: Columna '" + nombreColumna + "' no encontrada en la tabla de pedidos asignados. " + e.getMessage());
      }
   }
    
   /*
    * Muestra una ventana de diálogo con los detalles completos de un pedido.
    * Permite al repartidor ver la información del cliente, los detalles del combo,
    * el estado del pedido y los datos del repartidor asignado. También incluye botones
    * para cancelar o marcar el pedido como entregado.
    */
   public void mostrarVentanaDetallePedido(Pedido pedido) {
      JDialog dialogoDetalle = new JDialog(this, "Detalle del Pedido: " + pedido.getIdPedido(), true);
      JPanel panelContenido = new JPanel(new GridLayout(0, 2, 10, 5));
      
      // Añadir detalles del pedido al panel de contenido
      agregarDetallePedido(panelContenido, pedido);
      
      dialogoDetalle.add(panelContenido);
      
      // Configurar y añadir botones de acción
      JPanel panelBotones = crearPanelBotonesAccion(pedido, dialogoDetalle);
      dialogoDetalle.add(panelBotones, java.awt.BorderLayout.SOUTH);
      
      dialogoDetalle.pack();
      dialogoDetalle.setLocationRelativeTo(this);
      dialogoDetalle.setVisible(true);
   }

   /*
    * Agrega los detalles de un pedido al panel de contenido del diálogo.
    */
   private void agregarDetallePedido(JPanel panelContenido, Pedido pedido) {
      panelContenido.add(new JLabel("ID Pedido:"));
      panelContenido.add(new JLabel(pedido.getIdPedido()));
      
      agregarSeccionCliente(panelContenido, pedido);
      agregarSeccionDetallesPedido(panelContenido, pedido);
      agregarSeccionRepartidorAsignado(panelContenido, pedido);
   }

   /*
    * Agrega la sección de datos del cliente al panel de contenido del diálogo.
    */
   private void agregarSeccionCliente(JPanel panelContenido, Pedido pedido) {
      panelContenido.add(new JLabel("--- Datos del Cliente ---"));
      panelContenido.add(new JLabel(""));
      if (pedido.getCliente() != null) {
         panelContenido.add(new JLabel("Nombre Cliente:"));
         panelContenido.add(new JLabel(pedido.getCliente().getNombre()));
         panelContenido.add(new JLabel("Cédula Cliente:"));
         panelContenido.add(new JLabel(pedido.getCliente().getCedulaIdentidad()));
         panelContenido.add(new JLabel("Teléfono Cliente:"));
         panelContenido.add(new JLabel(pedido.getCliente().getNumeroTelefono()));
         panelContenido.add(new JLabel("Dirección Cliente:"));
         panelContenido.add(new JLabel(pedido.getCliente().getDireccion()));
      } else {
         panelContenido.add(new JLabel("Cliente:"));
         panelContenido.add(new JLabel("N/A"));
      }
   }

   /*
    * Agrega la sección de detalles del pedido al panel de contenido del diálogo.
    */
   private void agregarSeccionDetallesPedido(JPanel panelContenido, Pedido pedido) {
      panelContenido.add(new JLabel("--- Detalles del Pedido ---"));
      panelContenido.add(new JLabel(""));
      panelContenido.add(new JLabel("Dirección de Entrega:"));
      panelContenido.add(new JLabel(pedido.getDireccionEntrega()));
      panelContenido.add(new JLabel("Combo:"));
      panelContenido.add(new JLabel(pedido.getCombo() != null ? pedido.getCombo().toString() : "N/A"));
      panelContenido.add(new JLabel("Precio del Combo:"));
      panelContenido.add(new JLabel(String.valueOf(pedido.getPrecioCombo())));
      panelContenido.add(new JLabel("Método de Pago:"));
      panelContenido.add(new JLabel(pedido.getMetodoPago() != null ? pedido.getMetodoPago().name() : "N/A"));
      panelContenido.add(new JLabel("Requiere Cambio:"));
      panelContenido.add(new JLabel(pedido.isRequiereCambio() ? "Sí" : "No"));
      panelContenido.add(new JLabel("Costo de Entrega:"));
      panelContenido.add(new JLabel(String.valueOf(pedido.getCostoEntrega())));
      panelContenido.add(new JLabel("Vuelto:"));
      panelContenido.add(new JLabel(String.valueOf(pedido.getVuelto())));
      panelContenido.add(new JLabel("Monto Total:"));
      panelContenido.add(new JLabel(String.valueOf(pedido.getPrecioCombo() + pedido.getCostoEntrega())));
      panelContenido.add(new JLabel("Estado:"));
      panelContenido.add(new JLabel(pedido.getEstado()));
      panelContenido.add(new JLabel("Fecha de Creación:"));
      panelContenido.add(new JLabel(pedido.getFechaCreacion() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getFechaCreacion()) : "N/A"));
      if (pedido.getFechaEntrega() != null) {
         panelContenido.add(new JLabel("Fecha de Entrega:"));
         panelContenido.add(new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(pedido.getFechaEntrega())));
      }
   }

   /*
    * Agrega la sección de datos del repartidor asignado al panel de contenido del diálogo.
    */
   private void agregarSeccionRepartidorAsignado(JPanel panelContenido, Pedido pedido) {
      panelContenido.add(new JLabel("--- Datos del Repartidor Asignado ---"));
      panelContenido.add(new JLabel(""));
      if (pedido.getRepartidorAsignado() != null) {
         panelContenido.add(new JLabel("Nombre Repartidor:"));
         panelContenido.add(new JLabel(pedido.getRepartidorAsignado().getNombre()));
         panelContenido.add(new JLabel("Cédula Repartidor:"));
         panelContenido.add(new JLabel(pedido.getRepartidorAsignado().getCedulaIdentidad()));
         panelContenido.add(new JLabel("Teléfono Repartidor:"));
         panelContenido.add(new JLabel(pedido.getRepartidorAsignado().getNumeroTelefono()));
         panelContenido.add(new JLabel("Disponibilidad Repartidor:"));
         panelContenido.add(new JLabel(pedido.getRepartidorAsignado().isDisponibilidad() ? "Disponible" : "No disponible"));
      } else {
         panelContenido.add(new JLabel("Repartidor Asignado:"));
         panelContenido.add(new JLabel("No asignado"));
      }
   }

   /*
    * Crea y configura el panel de botones para cancelar y marcar como entregado un pedido.
    */
   private JPanel crearPanelBotonesAccion(Pedido pedido, JDialog dialogoDetalle) {
      JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JButton btnCancelarPedido = new JButton("Cancelar Pedido");
      JButton btnMarcarEntregado = new JButton("Marcar como Entregado");
      
      boolean pedidoFinalizado = "Entregado".equalsIgnoreCase(pedido.getEstado()) || "Cancelado".equalsIgnoreCase(pedido.getEstado());
      btnCancelarPedido.setEnabled(!pedidoFinalizado);
      btnMarcarEntregado.setEnabled(!pedidoFinalizado);
      
      configurarAccionCancelarPedido(btnCancelarPedido, pedido, dialogoDetalle);
      configurarAccionMarcarEntregado(btnMarcarEntregado, pedido, dialogoDetalle);
      
      panelBotones.add(btnCancelarPedido);
      panelBotones.add(btnMarcarEntregado);
      return panelBotones;
   }

   /*
    * Configura la acción para el botón de cancelar pedido.
    */
   private void configurarAccionCancelarPedido(JButton btnCancelarPedido, Pedido pedido, JDialog dialogoDetalle) {
      btnCancelarPedido.addActionListener((ActionEvent e) -> {
         int confirmacion = JOptionPane.showConfirmDialog(dialogoDetalle, "¿Está seguro de que desea cancelar este pedido?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
         if (confirmacion == JOptionPane.YES_OPTION) {
            boolean cancelado = repartidorController.cancelarPedido(pedido.getIdPedido());
            if (cancelado) {
               mostrarMensajeTemporizado(RepartidorDashboardJFrame.this, "Pedido cancelado exitosamente.", "Éxito", 2000);
               actualizarTablaPedidos();
               dialogoDetalle.dispose();
            } else {
               mostrarMensajeErrorTemporizado(RepartidorDashboardJFrame.this, "Error al cancelar el pedido.", "Error", 3000);
            }
         }
      });
   }

   /*
    * Configura la acción para el botón de marcar pedido como entregado.
    */
   private void configurarAccionMarcarEntregado(JButton btnMarcarEntregado, Pedido pedido, JDialog dialogoDetalle) {
      btnMarcarEntregado.addActionListener((ActionEvent e) -> {
         int confirmacion = JOptionPane.showConfirmDialog(dialogoDetalle, "¿Está seguro de que desea marcar este pedido como entregado?", "Confirmar Entrega", JOptionPane.YES_NO_OPTION);
         if (confirmacion == JOptionPane.YES_OPTION) {
            boolean entregado = repartidorController.marcarPedidoComoEntregado(pedido.getIdPedido());
            if (entregado) {
               mostrarMensajeTemporizado(RepartidorDashboardJFrame.this, "Pedido marcado como entregado exitosamente.", "Éxito", 2000);
               actualizarTablaPedidos();
               actualizarTablaHistorial();
               dialogoDetalle.dispose();
            } else {
               mostrarMensajeErrorTemporizado(RepartidorDashboardJFrame.this, "Error al marcar el pedido como entregado.", "Error", 3000);
            }
         }
      });
   }
    
   /*
    * Actualiza el texto y el color de fondo del botón de disponibilidad
    * según el estado actual de disponibilidad del repartidor.
    */
   private void actualizarEstadoDisponibilidad() {
      if (toggleDisponibilidad.isSelected()) {
         toggleDisponibilidad.setText("Disponible");
         toggleDisponibilidad.setBackground(new Color(144, 238, 144)); // Verde claro
      } else {
         toggleDisponibilidad.setText("No Disponible");
         toggleDisponibilidad.setBackground(new Color(255, 160, 122)); // Naranja claro
      }
   }
    
   /*
    * Muestra un mensaje de error temporizado en un JOptionPane.
    */
   public void mostrarMensajeErrorTemporizado(Component parent, String mensaje, String titulo, int duracionMillis) {
      mostrarMensajeTemporizadoGenerico(parent, mensaje, titulo, duracionMillis, JOptionPane.ERROR_MESSAGE);
   }

   /*
    * Muestra un mensaje informativo temporizado en un JOptionPane.
    */
   public void mostrarMensajeTemporizado(Component parent, String mensaje, String titulo, int duracionMillis) {
      mostrarMensajeTemporizadoGenerico(parent, mensaje, titulo, duracionMillis, JOptionPane.PLAIN_MESSAGE);
   }

   /*
    * Método genérico para mostrar mensajes temporizados.
    */
   private void mostrarMensajeTemporizadoGenerico(Component parent, String mensaje, String titulo, int duracionMillis, int tipoMensaje) {
      final JOptionPane optionPane = new JOptionPane(mensaje, tipoMensaje);
      final JDialog dialog = optionPane.createDialog(parent, titulo);
      Timer timer = new Timer(duracionMillis, (ActionEvent e) -> {
         dialog.dispose();
      });
      timer.setRepeats(false);
      timer.start();
      dialog.setVisible(true);
      // Detener el timer después de que el diálogo se haya cerrado (o se haya agotado el tiempo)
      // Esto es importante para liberar recursos del timer.
      timer.stop(); 
   }
    
   /*
    * Actualiza la tabla de pedidos asignados recargando los datos del controlador
    * y reconfigurando los botones de detalle y acción.
    */
   public void actualizarTablaPedidos() {
      DefaultTableModel modeloPedidos = repartidorController.obtenerModeloPedidosAsignados(repartidorActual);
      tablaPedidosAsignados.setModel(modeloPedidos);
      configurarBotonDetallePedidosAsignados(tablaPedidosAsignados, "Detalle");
      configurarBotonEntregadoPedidosAsignados(tablaPedidosAsignados, "Acción");
   }

   /*
    * Actualiza la tabla del historial de entregas recargando los datos del controlador.
    */
   public void actualizarTablaHistorial() {
      DefaultTableModel modeloHistorial = repartidorController.obtenerModeloHistorialDeEntregas(repartidorActual);
      tablaHistorialEntregas.setModel(modeloHistorial);
   }

   /*
    * Inicializa los filtros de búsqueda para las tablas.
    * Actualmente, solo aplica el filtro a la tabla de historial de entregas.
    */
   private void inicializarFiltros() {
      aplicarFiltro(txtBuscarPedido, tablaHistorialEntregas);
   }

   /*
    * Aplica un filtro de texto a una JTable utilizando un TableRowSorter.
    * El filtro se actualiza dinámicamente a medida que el usuario escribe en el campo de texto.
    */
   private void aplicarFiltro(JTextField campoTexto, JTable tabla) {
      TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabla.getModel());
      tabla.setRowSorter(sorter);  
      campoTexto.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            filtrar();
         }
         @Override
         public void removeUpdate(DocumentEvent e) {
            filtrar();
         }
         @Override
         public void changedUpdate(DocumentEvent e) {
            filtrar();
         }
         private void filtrar() {
            String texto = campoTexto.getText();
            if (texto.trim().isEmpty())
               sorter.setRowFilter(null);
            else
               // (?i) hace que la búsqueda sea insensible a mayúsculas/minúsculas
               sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
         }
      });
   }
   
   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPanel1 = new javax.swing.JPanel();
      DisponibilidadjToggleButton = new javax.swing.JToggleButton();
      jLabel2 = new javax.swing.JLabel();
      NombreRepartidorjTextField = new javax.swing.JTextField();
      jScrollPane8 = new javax.swing.JScrollPane();
      jTextArea1 = new javax.swing.JTextArea();
      jPanel2 = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      jTable1 = new javax.swing.JTable();
      jScrollPane2 = new javax.swing.JScrollPane();
      jTable2 = new javax.swing.JTable();
      jTextField2 = new javax.swing.JTextField();
      jLabel1 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
      getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      jPanel1.setBackground(new java.awt.Color(255, 255, 255));

      DisponibilidadjToggleButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      DisponibilidadjToggleButton.setText("Ponerme Disponible");
      DisponibilidadjToggleButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            DisponibilidadjToggleButtonActionPerformed(evt);
         }
      });

      jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      jLabel2.setForeground(new java.awt.Color(0, 0, 0));
      jLabel2.setText("Repartidor:");

      NombreRepartidorjTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
      NombreRepartidorjTextField.setForeground(new java.awt.Color(0, 0, 0));
      NombreRepartidorjTextField.setText("jTextField1");

      jScrollPane8.setBorder(null);
      jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      jScrollPane8.setToolTipText("");
      jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      jScrollPane8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

      jTextArea1.setEditable(false);
      jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
      jTextArea1.setColumns(7);
      jTextArea1.setFont(new java.awt.Font("Segoe UI", 1, 125)); // NOI18N
      jTextArea1.setRows(5);
      jTextArea1.setText("Gj");
      jScrollPane8.setViewportView(jTextArea1);

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addGap(70, 70, 70)
                  .addComponent(jLabel2)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(NombreRepartidorjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addContainerGap(136, Short.MAX_VALUE))
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(DisponibilidadjToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addContainerGap())))
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addGap(59, 59, 59)
                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(jLabel2)
                     .addComponent(NombreRepartidorjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                  .addComponent(DisponibilidadjToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
            .addContainerGap())
      );

      getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 200));

      jPanel2.setBackground(new java.awt.Color(255, 204, 51));

      jTable1.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
         },
         new String [] {
            "ID Pedido", "Combo", "Cliente", "Dirección", "Estado", "Detalle"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, false, true
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jScrollPane1.setViewportView(jTable1);

      jTable2.setModel(new javax.swing.table.DefaultTableModel(
         new Object [][] {
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null},
            {null, null, null, null, null, null}
         },
         new String [] {
            "ID Pedido", "Cliente", "Dirección", "Fecha de Entrega", "Estado", "Acción"
         }
      ) {
         boolean[] canEdit = new boolean [] {
            false, false, false, false, false, false
         };

         public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit [columnIndex];
         }
      });
      jScrollPane2.setViewportView(jTable2);

      jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
      jTextField2.setForeground(new java.awt.Color(0, 0, 0));
      jTextField2.setText("Buscar pedido...");
      jTextField2.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            jTextField2ActionPerformed(evt);
         }
      });

      jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel1.setForeground(new java.awt.Color(0, 0, 0));
      jLabel1.setText("Pedidos Pendientes");

      jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
      jLabel3.setForeground(new java.awt.Color(0, 0, 0));
      jLabel3.setText("Historial de Pedidos Entregados");

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap(32, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel1)
               .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addGroup(jPanel2Layout.createSequentialGroup()
                     .addComponent(jLabel3)
                     .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 736, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(32, Short.MAX_VALUE))
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(15, 15, 15)
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(36, 36, 36)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jLabel3))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(39, Short.MAX_VALUE))
      );

      getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 800, -1));

      pack();
      setLocationRelativeTo(null);
   }// </editor-fold>//GEN-END:initComponents

   private void DisponibilidadjToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DisponibilidadjToggleButtonActionPerformed
      boolean nuevoEstado = repartidorController.cambiarEstadoDisponibilidad(repartidorActual);
      toggleDisponibilidad.setSelected(nuevoEstado);
      actualizarEstadoDisponibilidad();
   }//GEN-LAST:event_DisponibilidadjToggleButtonActionPerformed

   private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_jTextField2ActionPerformed

   /**
    * @param args the command line arguments
    */

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JToggleButton DisponibilidadjToggleButton;
   private javax.swing.JTextField NombreRepartidorjTextField;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane8;
   private javax.swing.JTable jTable1;
   private javax.swing.JTable jTable2;
   private javax.swing.JTextArea jTextArea1;
   private javax.swing.JTextField jTextField2;
   // End of variables declaration//GEN-END:variables
}

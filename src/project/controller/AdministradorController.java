
package project.controller;

import project.model.entities.Cliente;
import project.model.entities.HistorialDeEntrega;
import project.model.entities.Pedido;
import project.model.entities.Repartidor;
import project.model.enums.Combo;
import project.model.enums.MetodoDePago;
import project.model.repositories.ClienteRepository;
import project.model.repositories.PedidoRepository;
import project.model.repositories.RepartidorRepository;
import project.model.repositories.HistorialEntregaRepository;
import project.util.ValidacionDeDato;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

/*
 * Controlador para la administración de clientes, repartidores y pedidos.
 * Proporciona métodos para registrar, eliminar, buscar y actualizar información,
 * así como para generar modelos de tabla y combobox para la interfaz de usuario.
 */
public class AdministradorController {
   // Repositorios para la gestión de datos
   private final ClienteRepository clienteRepo;
   private final PedidoRepository pedidoRepo;
   private final RepartidorRepository repartidorRepo;
   private final HistorialEntregaRepository historialRepo;
   // Umbral de tiempo para considerar una entrega "a tiempo"
   private static final long ON_TIME_THRESHOLD_MINUTES = 60;
   
   /*
    * Constructor de la clase AdministradorController.
    * Inicializa los repositorios necesarios para las operaciones de administración.
    */
   public AdministradorController() {
      this.clienteRepo = new ClienteRepository();
      this.pedidoRepo = new PedidoRepository();
      this.repartidorRepo = new RepartidorRepository();
      this.historialRepo = new HistorialEntregaRepository();
   }

   /*
    * Intenta registrar un nuevo cliente en el sistema.
    * Realiza validaciones sobre los datos de entrada antes de proceder con el registro.
    */
   public String intentarRegistrarCliente(String nombre, String cedula, String telefono, String direccion) {
      String validationError = validarDatosCliente(nombre, cedula, telefono, direccion);
      if (validationError != null) {
         return validationError;
      }
      if (clienteRepo.buscarPorId(cedula) != null) {
         return "Ya existe un cliente con esta cédula de identidad.";
      }
      Cliente nuevoCliente = new Cliente(nombre, cedula, telefono, direccion);
      clienteRepo.guardar(nuevoCliente);
      return null;
   }

   /*
    * Valida los datos de entrada para el registro de un cliente.
    */
   private String validarDatosCliente(String nombre, String cedula, String telefono, String direccion) {
      if (!ValidacionDeDato.esTextoValido(nombre)) 
         return "El nombre del cliente no puede estar vacío.";
      if (!ValidacionDeDato.esCedulaVenezolana(cedula) || !ValidacionDeDato.esCedulaExtranjera(cedula)) 
         return "La cédula de identidad debe ser numérica y no estar vacía.";
      if (!ValidacionDeDato.esTelefonoValido(telefono)) 
         return "El número de teléfono debe ser numérico y no estar vacío.";
      if (!ValidacionDeDato.esTextoValido(direccion)) 
         return "La dirección del cliente no puede estar vacía.";
      return null;
   }

   /*
    * Elimina un cliente del sistema, junto con todos los pedidos y historiales de entrega asociados.
    */
   public boolean eliminarCliente(String idCliente) {
      List<Pedido> pedidosCliente = pedidoRepo.obtenerTodos().stream()
                                             .filter(p -> p.getCliente() != null && p.getCliente().getCedulaIdentidad().equals(idCliente))
                                             .collect(Collectors.toList());
      for (Pedido pedido : pedidosCliente) {
         eliminarHistorialYPedido(pedido);
      }
      return clienteRepo.eliminar(idCliente);
   }

   /*
    * Elimina el historial de entrega y el pedido asociado.
    */
   private void eliminarHistorialYPedido(Pedido pedido) {
      HistorialDeEntrega historial = historialRepo.obtenerTodos().stream()
                                                 .filter(h -> h.getPedidoAsociado() != null && h.getPedidoAsociado().getIdPedido().equals(pedido.getIdPedido()))
                                                 .findFirst().orElse(null);
      if (historial != null) {
         historialRepo.eliminarPorId(historial.getIdHistorial());
      }
      pedidoRepo.eliminar(pedido.getIdPedido());
   }

   /*
    * Busca un cliente por su cédula de identidad.
    */
   public Cliente buscarClientePorId(String idCliente) {
      return clienteRepo.buscarPorId(idCliente);
   }

   /*
    * Intenta registrar un nuevo repartidor en el sistema.
    * Realiza validaciones sobre los datos de entrada antes de proceder con el registro.
    * Genera una contraseña alfanumérica para el repartidor.
    */
   public String intentarRegistrarRepartidor(String nombre, String cedula, String telefono, boolean disponibilidad) {
      String validationError = validarDatosRepartidor(nombre, cedula, telefono);
      if (validationError != null) {
         return validationError;
      }
      if (repartidorRepo.buscarPorId(cedula) != null) {
         return "Ya existe un repartidor con esta cédula de identidad.";
      }
      String contrasenaGenerada = generarContrasenaAlfanumerica(8); // Genera una contraseña de 8 caracteres
      Repartidor nuevoRepartidor = new Repartidor(nombre, cedula, telefono, disponibilidad, contrasenaGenerada);
      repartidorRepo.guardar(nuevoRepartidor);
      return contrasenaGenerada; // Retorna la contraseña generada para mostrarla en la UI
   }

   /*
    * Genera una contraseña alfanumérica aleatoria de la longitud especificada.
    */
   public String generarContrasenaAlfanumerica(int longitud) {
      String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      StringBuilder contrasena = new StringBuilder();
      Random random = new Random();
      for (int i = 0; i < longitud; i++) {
         int index = random.nextInt(caracteres.length());
         contrasena.append(caracteres.charAt(index));
      }
      return contrasena.toString();
   }

   /*
    * Valida los datos de entrada para el registro de un repartidor.
    */
   private String validarDatosRepartidor(String nombre, String cedula, String telefono) {
      if (!ValidacionDeDato.esTextoValido(nombre)) 
         return "El nombre del repartidor no puede estar vacío.";
      if (!ValidacionDeDato.esCedulaVenezolana(cedula) || !ValidacionDeDato.esCedulaExtranjera(cedula)) 
         return "La cédula de identidad del repartidor debe ser numérica y no estar vacía.";
      if (!ValidacionDeDato.esTelefonoValido(telefono)) 
         return "El número de teléfono del repartidor debe ser numérico y no estar vacío.";
      return null;
   }

   /*
    * Elimina un repartidor del sistema.
    * Antes de eliminar, desasigna al repartidor de cualquier pedido que tenga asignado.
    */
   public boolean eliminarRepartidor(String idRepartidor) {
      List<Pedido> pedidosRepartidor = pedidoRepo.obtenerTodos().stream()
                                                .filter(p -> p.getRepartidorAsignado() != null && p.getRepartidorAsignado().getCedulaIdentidad().equals(idRepartidor))
                                                .collect(Collectors.toList());
      for (Pedido pedido : pedidosRepartidor) {
         pedido.setRepartidorAsignado(null); // Desasignar el repartidor
         pedidoRepo.guardar(pedido); // Guardar el pedido actualizado
      }
      return repartidorRepo.eliminar(idRepartidor);
   }

   /*
    * Busca un repartidor por su cédula de identidad.
    */
   public Repartidor buscarRepartidorPorId(String idRepartidor) {
      return repartidorRepo.buscarPorId(idRepartidor);
   }

   /*
    * Intenta registrar un nuevo pedido en el sistema.
    * Realiza validaciones sobre los datos de entrada y calcula el monto total del pedido.
    */
   public String intentarRegistrarPedido(Cliente cliente, Combo combo, MetodoDePago metodoPago, Repartidor repartidor, boolean requiereCambio, String costoEntregaStr, String vueltoStr) {
      String validationError = validarDatosPedido(cliente, combo, metodoPago, requiereCambio, costoEntregaStr, vueltoStr);
      if (validationError != null) {
         return validationError;
      }

      double costoEntrega = Double.parseDouble(costoEntregaStr);
      double vuelto = requiereCambio ? Double.parseDouble(vueltoStr) : 0.0;
      String idPedido = generarIdUnicoPedido();
      Date fechaCreacion = new Date();

      Pedido nuevoPedido = new Pedido(idPedido, cliente, repartidor, cliente.getDireccion(), combo, metodoPago, requiereCambio, costoEntrega, vuelto, "Pendiente", fechaCreacion, null, null);
      pedidoRepo.guardar(nuevoPedido);
      return null;
   }

   /*
    * Valida los datos de entrada para el registro de un pedido.
    */
   private String validarDatosPedido(Cliente cliente, Combo combo, MetodoDePago metodoPago, boolean requiereCambio, String costoEntregaStr, String vueltoStr) {
      if (cliente == null) 
         return "Debe seleccionar un cliente.";
      if (combo == null) 
         return "Debe seleccionar un combo.";
      if (metodoPago == null) 
         return "Debe seleccionar un método de pago.";
      
      try {
         double costoEntrega = Double.parseDouble(costoEntregaStr);
         if (costoEntrega < 0) 
            return "El costo de entrega no puede ser negativo.";
      } catch (NumberFormatException e) {
         return "El costo de entrega debe ser un número válido.";
      }

      if (requiereCambio) {
         try {
            double vuelto = Double.parseDouble(vueltoStr);
            if (vuelto < 0) 
               return "El vuelto no puede ser negativo.";
         } catch (NumberFormatException e) {
            return "El vuelto debe ser un número válido.";
         }
      }
      return null;
   }

   /*
    * Genera un ID único para un nuevo pedido.
    * Combina la marca de tiempo actual con un número aleatorio para asegurar la unicidad.
    */
   private String generarIdUnicoPedido() {
      return "PED" + new Random().nextInt(1000);
   }

   /*
    * Cancela un pedido existente.
    * El pedido solo puede ser cancelado si su estado actual no es "Entregado" o "Cancelado".
    */
   public String cancelarPedido(String idPedido) {
      Pedido pedido = pedidoRepo.buscarPorId(idPedido);
      if (pedido == null) {
         return "Pedido no encontrado.";
      }
      if ("Entregado".equalsIgnoreCase(pedido.getEstado()) || "Cancelado".equalsIgnoreCase(pedido.getEstado())) {
         return "El pedido ya ha sido entregado o cancelado y no puede ser modificado.";
      }
      pedido.setEstado("Cancelado");
      pedidoRepo.guardar(pedido);
      return null;
   }

   /*
    * Reasigna un repartidor a un pedido existente.
    * El pedido solo puede ser reasignado si su estado actual no es "Entregado" o "Cancelado".
    */
   public String reasignarRepartidorAPedido(String idPedido, Repartidor nuevoRepartidor) {
      Pedido pedido = pedidoRepo.buscarPorId(idPedido);
      if (pedido == null) {
         return "Pedido no encontrado.";
      }
      if ("Entregado".equalsIgnoreCase(pedido.getEstado()) || "Cancelado".equalsIgnoreCase(pedido.getEstado())) {
         return "El pedido ya ha sido entregado o cancelado y no puede ser reasignado.";
      }
      if (nuevoRepartidor == null || nuevoRepartidor.getCedulaIdentidad() == null || nuevoRepartidor.getCedulaIdentidad().isEmpty()) {
         return "Debe seleccionar un repartidor válido.";
      }
      pedido.setRepartidorAsignado(nuevoRepartidor);
      pedidoRepo.guardar(pedido);
      return null;
   }

   /*
    * Busca un pedido por su ID.
    */
   public Pedido buscarPedidoPorId(String idPedido) {
      return pedidoRepo.buscarPorId(idPedido);
   }

   /*
    * Busca un historial de entrega por su ID.
    */
   public HistorialDeEntrega buscarHistorialDeEntregaPorId(String idHistorial) {
      return historialRepo.buscarPorId(idHistorial);
   }

   /*
    * Genera un modelo de tabla para mostrar los pedidos pendientes en el dashboard.
    * Incluye columnas para ID, Cliente, Tiempo de creación, Estado y una columna de acción.
    */
   public DefaultTableModel generarModeloTablaPedidosDashboard() {
      DefaultTableModel model = crearModeloTablaEditable(new Object[]{"ID", "Cliente", "Tiempo", "Estado", "Acción"}, 4);
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      pedidoRepo.obtenerTodos().stream()
                .filter(p -> "Pendiente".equalsIgnoreCase(p.getEstado()))
                .forEach(pedido -> {
                   String clienteNombre = (pedido.getCliente() != null) ? pedido.getCliente().getNombre() : "N/A";
                   String tiempoCreacion = (pedido.getFechaCreacion() != null) ? sdf.format(pedido.getFechaCreacion()) : "N/A";
                   model.addRow(new Object[]{pedido.getIdPedido(), clienteNombre, tiempoCreacion, pedido.getEstado(), "Ver Detalle"});
                });
      return model;
   }

   /*
    * Genera un modelo de tabla para mostrar la disponibilidad de los repartidores en el dashboard.
    */
   public DefaultTableModel generarModeloTablaRepartidoresDashboard() {
      DefaultTableModel model = crearModeloTablaNoEditable(new Object[]{"Repartidor", "Disponibilidad"});
      repartidorRepo.obtenerTodos().forEach(r -> 
         model.addRow(new Object[]{r.getNombre(), r.isDisponibilidad() ? "Disponible" : "No Disponible"})
      );
      return model;
   }

   /*
    * Genera un modelo de tabla para mostrar el historial de entregas en el dashboard.
    */
   public DefaultTableModel generarModeloTablaHistorialDeEntregaDashboard() {
      DefaultTableModel model = crearModeloTablaNoEditable(new Object[]{"ID Historial", "ID Pedido", "Repartidor", "Fecha", "Estado", "Direccion"});
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      historialRepo.obtenerTodos().forEach(h -> {
         String pedidoId = (h.getPedidoAsociado() != null) ? h.getPedidoAsociado().getIdPedido() : "N/A";
         String repartidorNombre = (h.getRepartidor() != null) ? h.getRepartidor().getNombre() : "N/A";
         String fechaRegistro = (h.getFechaRegistro() != null) ? sdf.format(h.getFechaRegistro()) : "N/A";
         String direccionEntrega = (h.getPedidoAsociado() != null) ? h.getPedidoAsociado().getDireccionEntrega() : "N/A";
         model.addRow(new Object[]{h.getIdHistorial(), pedidoId, repartidorNombre, fechaRegistro, h.getEstadoEntrega(), direccionEntrega});
      });
      return model;
   }

   /*
    * Genera un modelo de tabla para mostrar la lista completa de clientes.
    */
   public DefaultTableModel generarModeloTablaClientes() {
      DefaultTableModel model = crearModeloTablaEditable(new Object[]{"Nombre", "C.I.", "N° Teléfono", "Dirección", "Detalle"}, 4);
      clienteRepo.obtenerTodos().forEach(c -> 
         model.addRow(new Object[]{c.getNombre(), c.getCedulaIdentidad(), c.getNumeroTelefono(), c.getDireccion(), "Ver Detalle"})
      );
      return model;
   }

   /*
    * Genera un modelo de tabla para mostrar la lista completa de repartidores.
    */
   public DefaultTableModel generarModeloTablaRepartidores() {
      DefaultTableModel model = crearModeloTablaEditable(new Object[]{"Nombre", "C.I.", "N° Teléfono", "Disponibilidad", "Detalle"}, 4);
      repartidorRepo.obtenerTodos().forEach(r -> 
         model.addRow(new Object[]{r.getNombre(), r.getCedulaIdentidad(), r.getNumeroTelefono(), r.isDisponibilidad() ? "Disponible" : "No Disponible", "Ver Detalle"})
      );
      return model;
   }

   /*
    * Genera un modelo de tabla para mostrar la lista completa de pedidos.
    */
   public DefaultTableModel generarModeloTablaPedidos() {
      DefaultTableModel model = crearModeloTablaEditable(new Object[]{"ID", "Cliente", "Fecha", "Total", "Estado", "Repartidor", "Método de Pago", "Acción"}, 7);
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      pedidoRepo.obtenerTodos().forEach(p -> {
         String clienteNombre = (p.getCliente() != null) ? p.getCliente().getNombre() : "N/A";
         String repartidorNombre = (p.getRepartidorAsignado() != null) ? p.getRepartidorAsignado().getNombre() : "N/A";
         String fechaCreacion = (p.getFechaCreacion() != null) ? sdf.format(p.getFechaCreacion()) : "N/A";
         double total = p.getPrecioCombo() + p.getCostoEntrega();
         model.addRow(new Object[]{
            p.getIdPedido(), clienteNombre, fechaCreacion, String.format("%.2f", total), p.getEstado(), repartidorNombre, p.getMetodoPago() != null ? p.getMetodoPago().name() : "N/A", "Ver Detalle"
         });
      });
      return model;
   }

   /*
    * Genera un modelo de tabla para mostrar el historial completo de entregas.
    */
   public DefaultTableModel generarModeloTablaHistorialDeEntrega() {
      DefaultTableModel model = crearModeloTablaEditable(new Object[]{"ID Historial", "ID Pedido", "Combo", "Monto total", "Cliente", "Repartidor", "Metodo de Pago", "Estado", "Detalle"}, 8);
      historialRepo.obtenerTodos().forEach(h -> {
         String pedidoId = (h.getPedidoAsociado() != null) ? h.getPedidoAsociado().getIdPedido() : "N/A";
         String comboNombre = (h.getPedidoAsociado() != null && h.getPedidoAsociado().getCombo() != null) ? h.getPedidoAsociado().getCombo().name() : "N/A";
         double montoTotal = (h.getPedidoAsociado() != null) ? (h.getPedidoAsociado().getPrecioCombo() + h.getPedidoAsociado().getCostoEntrega()) : 0.0;
         String clienteNombre = (h.getPedidoAsociado() != null && h.getPedidoAsociado().getCliente() != null) ? h.getPedidoAsociado().getCliente().getNombre() : "N/A";
         String repartidorNombre = (h.getRepartidor() != null) ? h.getRepartidor().getNombre() : "N/A";
         String metodoPago = (h.getPedidoAsociado() != null && h.getPedidoAsociado().getMetodoPago() != null) ? h.getPedidoAsociado().getMetodoPago().name() : "N/A";
         model.addRow(new Object[]{
            h.getIdHistorial(), pedidoId, comboNombre, String.format("%.2f", montoTotal), clienteNombre, repartidorNombre, metodoPago, h.getEstadoEntrega(), "Ver Detalle"
         });
      });
      return model;
   }

   /*
    * Crea un modelo de tabla con una columna específica editable como JButton.
    */
   private DefaultTableModel crearModeloTablaEditable(Object[] columnNames, int editableColumnIndex) {
      return new DefaultTableModel(columnNames, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return column == editableColumnIndex;
         }
         @Override
         public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == editableColumnIndex ? JButton.class : Object.class;
         }
      };
   }

   /*
    * Crea un modelo de tabla donde ninguna celda es editable.
    */
   private DefaultTableModel crearModeloTablaNoEditable(Object[] columnNames) {
      return new DefaultTableModel(columnNames, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            return false;
         }
      };
   }

   /*
    * Genera un modelo de combobox con la lista de clientes.
    * Incluye un elemento nulo al inicio para la selección por defecto.
    */
   public DefaultComboBoxModel<Cliente> generarModeloComboBoxClientes() {
      DefaultComboBoxModel<Cliente> model = new DefaultComboBoxModel<>();
      model.addElement(null); // Permite una opción de "no seleccionado"
      clienteRepo.obtenerTodos().forEach(model::addElement);
      return model;
   }

   /*
    * Genera un modelo de combobox con la lista de combos disponibles.
    */
   public DefaultComboBoxModel<Combo> generarModeloComboBoxCombos() {
      DefaultComboBoxModel<Combo> model = new DefaultComboBoxModel<>();
      for (Combo combo : Combo.values()) {
         model.addElement(combo);
      }
      return model;
   }

   /*
    * Genera un modelo de combobox con la lista de métodos de pago disponibles.
    */
   public DefaultComboBoxModel<MetodoDePago> generarModeloComboBoxMetodosPago() {
      DefaultComboBoxModel<MetodoDePago> model = new DefaultComboBoxModel<>();
      for (MetodoDePago metodo : MetodoDePago.values()) {
         model.addElement(metodo);
      }
      return model;
   }

   /*
    * Genera un modelo de combobox con la lista completa de repartidores.
    * Incluye un elemento nulo al inicio para la selección por defecto.
    */
   public DefaultComboBoxModel<Repartidor> generarModeloComboBoxRepartidores() {
      DefaultComboBoxModel<Repartidor> model = new DefaultComboBoxModel<>();
      model.addElement(null); // Permite una opción de "no seleccionado"
      repartidorRepo.obtenerTodos().forEach(model::addElement);
      return model;
   }

   /*
    * Genera un modelo de combobox con la lista de repartidores activos (disponibles).
    */
   public DefaultComboBoxModel<Repartidor> generarModeloComboBoxRepartidoresActivos() {
      DefaultComboBoxModel<Repartidor> model = new DefaultComboBoxModel<>();
      repartidorRepo.obtenerTodos().stream().filter(Repartidor::isDisponibilidad).forEach(model::addElement);
      return model;
   }

   /*
    * Genera un modelo de tabla que muestra el top de repartidores basado en el número de entregas
    * y el porcentaje de entregas a tiempo (OTD%).
    */
   public DefaultTableModel generarModeloTablaTopRepartidores() {
      DefaultTableModel model = crearModeloTablaNoEditable(new Object[]{"Nombre del Repartidor", "Número de Entregas", "OTD%"});
      
      // Inicializar estadísticas para todos los repartidores
      Map<String, RepartidorStats> statsMap = inicializarEstadisticasRepartidores();
      
      // Procesar historiales de entrega para actualizar estadísticas
      actualizarEstadisticasConHistorial(statsMap);
      
      // Calcular OTD y ordenar los repartidores
      List<RepartidorStats> sortedStats = calcularOtdYOrdenar(statsMap);

      // Añadir los 10 mejores repartidores al modelo de tabla
      agregarTopRepartidoresATabla(model, sortedStats);

      return model;
   }

   /*
    * Inicializa un mapa de estadísticas para cada repartidor existente.
    */
   private Map<String, RepartidorStats> inicializarEstadisticasRepartidores() {
      Map<String, RepartidorStats> statsMap = new HashMap<>();
      repartidorRepo.obtenerTodos().forEach(r -> 
         statsMap.put(r.getCedulaIdentidad(), new RepartidorStats(r.getNombre()))
      );
      return statsMap;
   }

   /*
    * Actualiza las estadísticas de los repartidores procesando cada historial de entrega.
    */
   private void actualizarEstadisticasConHistorial(Map<String, RepartidorStats> statsMap) {
      for (HistorialDeEntrega historial : historialRepo.obtenerTodos()) {
         if (esHistorialValidoParaEstadisticas(historial)) {
            String repartidorId = historial.getRepartidor().getCedulaIdentidad();
            RepartidorStats stats = statsMap.get(repartidorId);
            if (stats != null) {
               stats.totalDeliveries++;
               if ("Entregado".equalsIgnoreCase(historial.getEstadoEntrega())) {
                  long diffInMillies = Math.abs(historial.getFechaRegistro().getTime() - historial.getPedidoAsociado().getFechaCreacion().getTime());
                  long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
                  if (diffInMinutes <= ON_TIME_THRESHOLD_MINUTES) {
                     stats.onTimeDeliveries++;
                  }
               }
            }
         }
      }
   }

   /*
    * Verifica si un historial de entrega es válido para ser incluido en las estadísticas.
    */
   private boolean esHistorialValidoParaEstadisticas(HistorialDeEntrega historial) {
      return historial.getRepartidor() != null && historial.getPedidoAsociado() != null &&
             historial.getFechaRegistro() != null && historial.getPedidoAsociado().getFechaCreacion() != null;
   }

   /*
    * Calcula el porcentaje OTD (On-Time Delivery) para cada repartidor y los ordena.
    * Los repartidores se ordenan primero por OTD (descendente) y luego por número total de entregas (descendente).
    */
   private List<RepartidorStats> calcularOtdYOrdenar(Map<String, RepartidorStats> statsMap) {
      List<RepartidorStats> sortedStats = new ArrayList<>();
      for (RepartidorStats stats : statsMap.values()) {
         if (stats.totalDeliveries > 0)
            stats.otdPercentage = (double) stats.onTimeDeliveries / stats.totalDeliveries * 100.0;
         else
            stats.otdPercentage = 0.0;
         sortedStats.add(stats);
      }
      sortedStats.sort((s1, s2) -> {
         int otdComparison = Double.compare(s2.otdPercentage, s1.otdPercentage);
         if (otdComparison == 0)
            return Integer.compare(s2.totalDeliveries, s1.totalDeliveries);
         return otdComparison;
      });
      return sortedStats;
   }

   /*
    * Agrega los primeros 10 repartidores de la lista ordenada al modelo de tabla.
    */
   private void agregarTopRepartidoresATabla(DefaultTableModel model, List<RepartidorStats> sortedStats) {
      int count = 0;
      for (RepartidorStats stats : sortedStats) {
         if (count < 10) {
            model.addRow(new Object[]{
                stats.name,
                stats.totalDeliveries,
                String.format("%.2f%%", stats.otdPercentage)
            });
            count++;
         } else {
            break;
         }
      }
   }

   /*
    * Clase interna para almacenar estadísticas de repartidores.
    * Contiene el nombre del repartidor, el total de entregas, las entregas a tiempo
    * y el porcentaje de entregas a tiempo.
    */
   private static class RepartidorStats {
      String name;
      int totalDeliveries;
      int onTimeDeliveries;
      double otdPercentage;

      /*
       * Constructor de RepartidorStats.
       */
      public RepartidorStats(String name) {
         this.name = name;
         this.totalDeliveries = 0;
         this.onTimeDeliveries = 0;
         this.otdPercentage = 0.0;
      }
   }

   /*
    * Obtiene el número de pedidos entregados en el día actual.
    */
   public int getPedidosEntregadosHoy() {
      List<HistorialDeEntrega> historiales = historialRepo.obtenerTodos();
      Date today = new Date();
      int count = 0;
      for (HistorialDeEntrega h : historiales) {
         if ("Entregado".equalsIgnoreCase(h.getEstadoEntrega()) && h.getFechaRegistro() != null) {
            if (isSameDay(h.getFechaRegistro(), today)) {
               count++;
            }
         }
      }
      return count;
   }

   /*
    * Calcula el porcentaje de entregas a tiempo (On-Time Delivery) de todos los pedidos completados.
    * Una entrega se considera a tiempo si la duración desde la creación del pedido hasta la entrega
    * es menor o igual al umbral definido (ON_TIME_THRESHOLD_MINUTES).
    */
   public double getOnTimeDeliveryPorcentaje() {
      int totalOnTimeDeliveries = 0;
      int totalCompletedDeliveries = 0;
      for (HistorialDeEntrega historial : historialRepo.obtenerTodos()) {
         if (esEntregaCompletadaYValida(historial)) {
            totalCompletedDeliveries++;
            long diffInMillies = Math.abs(historial.getFechaRegistro().getTime() - historial.getPedidoAsociado().getFechaCreacion().getTime());
            long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diffInMinutes <= ON_TIME_THRESHOLD_MINUTES)
               totalOnTimeDeliveries++;
         }
      }
      return (totalCompletedDeliveries == 0) ? 0.0 : (double) totalOnTimeDeliveries / totalCompletedDeliveries * 100.0;
   }

   /*
    * Verifica si una entrega está completada y tiene datos válidos para el cálculo de OTD.
    */
   private boolean esEntregaCompletadaYValida(HistorialDeEntrega historial) {
      return "Entregado".equalsIgnoreCase(historial.getEstadoEntrega()) && 
             historial.getPedidoAsociado() != null && 
             historial.getFechaRegistro() != null && 
             historial.getPedidoAsociado().getFechaCreacion() != null;
   }

   /*
    * Calcula el tiempo promedio de entrega para todos los pedidos completados.
    */
   public double getTiempoPromedioEntrega() {
      long totalDurationMinutes = 0;
      int completedDeliveriesCount = 0;
      for (HistorialDeEntrega historial : historialRepo.obtenerTodos()) {
         if (esEntregaCompletadaYValida(historial)) {
            long diffInMillies = Math.abs(historial.getFechaRegistro().getTime() - historial.getPedidoAsociado().getFechaCreacion().getTime());
            totalDurationMinutes += TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            completedDeliveriesCount++;
         }
      }
      return (completedDeliveriesCount == 0) ? 0.0 : (double) totalDurationMinutes / completedDeliveriesCount;
   }

   /*
    * Obtiene el número total de pedidos que se encuentran en estado "Pendiente" o "En Camino".
    */
   public int getPedidosEnCurso() {
      return (int) pedidoRepo.obtenerTodos().stream()
                             .filter(p -> "Pendiente".equalsIgnoreCase(p.getEstado()) || "En Camino".equalsIgnoreCase(p.getEstado()))
                             .count();
   }

   /*
    * Obtiene el número total de repartidores que están actualmente disponibles.
    */
   public int getRepartidoresDisponiblesActivos() {
      return (int) repartidorRepo.obtenerTodos().stream().filter(Repartidor::isDisponibilidad).count();
   }
   
   /*
    * Compara si dos fechas caen en el mismo día (ignorando la hora).
    */
   private boolean isSameDay(Date date1, Date date2) {
      if (date1 == null || date2 == null)
         return false;
      Calendar cal1 = Calendar.getInstance();
      cal1.setTime(date1);
      Calendar cal2 = Calendar.getInstance();
      cal2.setTime(date2);
      return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && 
             cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && 
             cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
   }
}

package project.controller;

import project.model.entities.Pedido;
import project.model.entities.HistorialDeEntrega;
import project.model.entities.Repartidor;
import project.model.repositories.PedidoRepository;
import project.model.repositories.HistorialEntregaRepository;
import project.model.repositories.RepartidorRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.util.Random;

/*
 * Controlador para la gestión de operaciones relacionadas con los repartidores y sus pedidos.
 * Proporciona métodos para obtener pedidos, historial de entregas, cambiar disponibilidad
 * y gestionar el estado de los pedidos (cancelar, marcar como entregado).
 */
public class RepartidorController {
   // Repositorios para interactuar con la capa de datos
   private final PedidoRepository pedidoRepo;
   private final HistorialEntregaRepository historialRepo;
   private final RepartidorRepository repartidorRepo;
   
   // Utilidades
   private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
   private final Random random = new Random();

   /*
    * Constructor de la clase RepartidorController.
    * Inicializa las instancias de los repositorios.
    */
   public RepartidorController() {
      this.pedidoRepo = new PedidoRepository();
      this.historialRepo = new HistorialEntregaRepository();
      this.repartidorRepo = new RepartidorRepository();
   }

   /*
    * Genera un ID numérico aleatorio de 5 dígitos.
    */
   private String generateNumericId() {
      int min = 10000;
      int max = 99999;
      return String.valueOf(random.nextInt(max - min + 1) + min);
   }

   /*
    * Obtiene una lista de pedidos pendientes o en camino asignados a un repartidor específico.
    * Realiza validaciones para asegurar que el objeto repartidor y su cédula no sean nulos.
    * Retorna una lista vacía si el repartidor es nulo o no tiene cédula.
    */
   public List<Pedido> obtenerPedidosPendientesParaRepartidor(Repartidor repartidor) {
      if (!isValidRepartidor(repartidor, "obtenerPedidosPendientesParaRepartidor")) {
         return new ArrayList<>();
      }

      List<Pedido> todosLosPedidos = pedidoRepo.obtenerTodos();
      List<Pedido> pedidosFiltrados = new ArrayList<>();
      for (Pedido pedido : todosLosPedidos) {
         if (isPedidoAsignadoYActivo(pedido, repartidor.getCedulaIdentidad())) {
            pedidosFiltrados.add(pedido);
         }
      }
      return pedidosFiltrados;
   }
    
   /*
    * Verifica si un objeto Repartidor es válido (no nulo y con cédula de identidad).
    */
   private boolean isValidRepartidor(Repartidor repartidor, String methodName) {
      if (repartidor == null || repartidor.getCedulaIdentidad() == null) {
         System.err.println("ERROR: El objeto repartidor o su cédula de identidad es nulo en " + methodName + ".");
         return false;
      }
      return true;
   }

   /*
    * Verifica si un pedido está asignado al repartidor dado y si su estado es "Pendiente" o "En Camino".
    */
   private boolean isPedidoAsignadoYActivo(Pedido pedido, String cedulaRepartidor) {
      return pedido.getRepartidorAsignado() != null &&
             pedido.getRepartidorAsignado().getCedulaIdentidad() != null &&
             pedido.getRepartidorAsignado().getCedulaIdentidad().equals(cedulaRepartidor) &&
             ("Pendiente".equalsIgnoreCase(pedido.getEstado()) || "En Camino".equalsIgnoreCase(pedido.getEstado()));
   }

   /*
    * Obtiene una lista del historial de entregas que han sido marcadas como "Entregado"
    * para un repartidor específico.
    * Realiza validaciones para asegurar que el objeto repartidor y su cédula no sean nulos.
    * Retorna una lista vacía si el repartidor es nulo o no tiene cédula.
    */
   public List<HistorialDeEntrega> obtenerHistorialDeEntregasEntregadasParaRepartidor(Repartidor repartidor) {
      if (!isValidRepartidor(repartidor, "obtenerHistorialDeEntregasEntregadasParaRepartidor")) {
         return new ArrayList<>();
      }

      List<HistorialDeEntrega> todosLosHistoriales = historialRepo.obtenerTodos();
      List<HistorialDeEntrega> historialesEntregados = new ArrayList<>();
      for (HistorialDeEntrega historial : todosLosHistoriales) {
         if (isHistorialDeRepartidorEntregado(historial, repartidor.getCedulaIdentidad())) {
            historialesEntregados.add(historial);
         }
      }
      return historialesEntregados;
   }

   /*
    * Verifica si un historial de entrega pertenece al repartidor dado y si su estado es "Entregado".
    */
   private boolean isHistorialDeRepartidorEntregado(HistorialDeEntrega historial, String cedulaRepartidor) {
      return historial.getRepartidor() != null &&
             historial.getRepartidor().getCedulaIdentidad() != null &&
             historial.getRepartidor().getCedulaIdentidad().equals(cedulaRepartidor) &&
             "Entregado".equalsIgnoreCase(historial.getEstadoEntrega());
   }

   /*
    * Cambia el estado de disponibilidad de un repartidor.
    * Busca el repartidor en la base de datos y actualiza su estado de disponibilidad.
    * Retorna el estado actual del repartidor si no se pudo actualizar o si el repartidor es nulo.
    */
   public boolean cambiarEstadoDisponibilidad(Repartidor repartidor) {
      if (!isValidRepartidor(repartidor, "cambiarEstadoDisponibilidad")) {
         return false;
      }
      
      Repartidor repEnBD = repartidorRepo.buscarPorId(repartidor.getCedulaIdentidad());
      if (repEnBD != null) {
         boolean nuevoEstado = !repEnBD.isDisponibilidad();
         repartidorRepo.actualizarDisponibilidad(repEnBD.getCedulaIdentidad(), nuevoEstado);
         // Actualiza el estado en los objetos en memoria
         repEnBD.setDisponibilidad(nuevoEstado);
         repartidor.setDisponibilidad(nuevoEstado);
         return nuevoEstado;
      }
      System.err.println("ERROR: Repartidor no encontrado en la base de datos para actualizar disponibilidad: " + repartidor.getCedulaIdentidad());
      return repartidor.isDisponibilidad(); // Retorna el estado actual si no se encontró o actualizó
   }

   /*
    * Obtiene un DefaultTableModel con los datos de los pedidos asignados al repartidor.
    */
   public DefaultTableModel obtenerModeloPedidosAsignados(Repartidor repartidor) {
      String[] columnNames = {"ID Pedido", "Combo", "Cliente", "Dirección", "Estado", "Detalle"};
      DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
         @Override
         public boolean isCellEditable(int row, int column) {
            // Solo la columna "Detalle" es editable (índice 5)
            return column == 5;
         }
         @Override
         public Class<?> getColumnClass(int columnIndex) {
            // La columna "Detalle" debe ser de tipo Object para el renderizado del botón
            if (columnIndex == 5) 
               return Object.class;
            return super.getColumnClass(columnIndex);
         }
      };
      
      List<Pedido> pedidos = obtenerPedidosPendientesParaRepartidor(repartidor); 
      for (Pedido pedido : pedidos) {
         String nombreCliente = getNombreCliente(pedido);
         String comboNombre = getNombreCombo(pedido);
         model.addRow(new Object[]{
            pedido.getIdPedido(), comboNombre, nombreCliente, pedido.getDireccionEntrega(), pedido.getEstado(), "Ver Detalle"
         });
      }
      return model;
   }

   /*
    * Obtiene el nombre del cliente de un pedido, o "N/A" si el cliente es nulo.
    */
   private String getNombreCliente(Pedido pedido) {
      return (pedido.getCliente() != null) ? pedido.getCliente().getNombre() : "N/A";
   }

   /*
    * Obtiene el nombre del combo de un pedido, o "N/A" si el combo es nulo.
    */
   private String getNombreCombo(Pedido pedido) {
      return (pedido.getCombo() != null) ? pedido.getCombo().toString() : "N/A";
   }

   /*
    * Obtiene un DefaultTableModel con los datos del historial de entregas del repartidor.
    */
   public DefaultTableModel obtenerModeloHistorialDeEntregas(Repartidor repartidor) {
      String[] columnNames = {"ID Historial", "ID Pedido", "Repartidor", "Estado Entrega", "Fecha Registro", "Ubicación"};
      DefaultTableModel model = new DefaultTableModel(columnNames, 0);
      
      List<HistorialDeEntrega> historiales = obtenerHistorialDeEntregasEntregadasParaRepartidor(repartidor);
      for (HistorialDeEntrega historial : historiales) {
         String idPedido = getIdPedidoAsociado(historial);
         String fechaRegistroStr = getFechaRegistroHistorial(historial);
         String nombreRepartidor = getNombreRepartidorHistorial(historial);
         
         model.addRow(new Object[]{
            historial.getIdHistorial(), idPedido, nombreRepartidor, historial.getEstadoEntrega(), fechaRegistroStr, historial.getUbicacionEntrega()
         });
      }
      return model;
   }

   /*
    * Obtiene el ID del pedido asociado a un historial de entrega, o "N/A" si el pedido es nulo.
    */
   private String getIdPedidoAsociado(HistorialDeEntrega historial) {
      return (historial.getPedidoAsociado() != null) ? historial.getPedidoAsociado().getIdPedido() : "N/A";
   }

   /*
    * Obtiene la fecha de registro de un historial de entrega formateada, o "N/A" si la fecha es nula.
    */
   private String getFechaRegistroHistorial(HistorialDeEntrega historial) {
      return (historial.getFechaRegistro() != null) ? dateFormat.format(historial.getFechaRegistro()) : "N/A";
   }

   /*
    * Obtiene el nombre del repartidor de un historial de entrega, o "N/A" si el repartidor es nulo.
    */
   private String getNombreRepartidorHistorial(HistorialDeEntrega historial) {
      return (historial.getRepartidor() != null) ? historial.getRepartidor().getNombre() : "N/A";
   }

   /*
    * Obtiene los detalles completos de un pedido a partir de su ID.
    */
   public Pedido obtenerDetallesPedido(String idPedido) {
      return pedidoRepo.buscarPorId(idPedido);
   }

   /*
    * Cancela un pedido y registra un historial de entrega con estado "Cancelado".
    */
   public boolean cancelarPedido(String idPedido) {
      Pedido pedido = pedidoRepo.buscarPorId(idPedido);
      if (pedido != null) {
         pedido.setEstado("Cancelado");
         pedido.setFechaEntrega(new Date()); // Establece la fecha de entrega como la fecha de cancelación
         
         HistorialDeEntrega historial = new HistorialDeEntrega(
            "HIST" + generateNumericId(), 
            new Date(), 
            "Cancelado", 
            pedido.getDireccionEntrega(), 
            pedido.getRepartidorAsignado(), 
            pedido
         );
         historialRepo.guardar(historial);
         return pedidoRepo.actualizarPedido(pedido);
      }
      System.err.println("ERROR: No se pudo cancelar el pedido. Pedido con ID " + idPedido + " no encontrado.");
      return false;
   }

   /*
    * Marca un pedido como "Entregado" y registra un historial de entrega con estado "Entregado".
    */
   public boolean marcarPedidoComoEntregado(String idPedido) {
      Pedido pedido = pedidoRepo.buscarPorId(idPedido);
      if (pedido != null) {
         pedido.setEstado("Entregado");
         pedido.setFechaEntrega(new Date()); // Establece la fecha de entrega actual
         
         HistorialDeEntrega historial = new HistorialDeEntrega(
            "HIST" + generateNumericId(), 
            new Date(), 
            "Entregado", 
            pedido.getDireccionEntrega(), 
            pedido.getRepartidorAsignado(), 
            pedido
         );
         historialRepo.guardar(historial);
         return pedidoRepo.actualizarPedido(pedido);
      }
      System.err.println("ERROR: No se pudo marcar el pedido como entregado. Pedido con ID " + idPedido + " no encontrado.");
      return false;
   }
}

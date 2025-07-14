
package project.model.entities;

import project.model.enums.Combo;
import project.model.enums.MetodoDePago;
import java.util.Date;
import java.util.List;
import java.util.ArrayList; // Importar ArrayList para inicializar historialEntregas

/*
 * Representa un pedido realizado por un cliente en el sistema.
 * Contiene toda la información relevante de un pedido, incluyendo el cliente,
 * el repartidor asignado, los detalles del combo, método de pago, costos, estado,
 * fechas y un historial de entregas.
 */
public class Pedido {
   private String idPedido;
   private Cliente cliente;
   private Repartidor repartidorAsignado;
   private String direccionEntrega;
   private Combo combo;
   private double precioCombo;
   private MetodoDePago metodoPago;
   private boolean requiereCambio;
   private double costoEntrega;
   private double vuelto;
   private String estado;
   private Date fechaCreacion;
   private Date fechaEntrega;
   private double montoTotal;
   // transient para indicar que esta lista no debe ser serializada si se usa un ORM o persistencia similar
   private transient List<HistorialDeEntrega> historialEntregas;
   
   /*
    * Constructor para crear una nueva instancia de Pedido.
    */
   public Pedido(String idPedido, Cliente cliente, Repartidor repartidorAsignado, String direccionEntrega, Combo combo, MetodoDePago metodoPago, boolean requiereCambio, double costoEntrega, double vuelto, String estado, Date fechaCreacion, Date fechaEntrega, List<HistorialDeEntrega> historialEntregas) {
      this.idPedido = idPedido;
      this.cliente = cliente;
      this.repartidorAsignado = repartidorAsignado;
      this.direccionEntrega = direccionEntrega;
      this.combo = combo;
      this.precioCombo = combo.getPrecio(); // El precio del combo se obtiene directamente del enum
      this.metodoPago = metodoPago;
      this.requiereCambio = requiereCambio;
      this.costoEntrega = costoEntrega;
      this.vuelto = vuelto;
      this.estado = estado;
      this.fechaCreacion = fechaCreacion;
      this.fechaEntrega = fechaEntrega;
      // Inicializa la lista de historial de entregas si es nula
      this.historialEntregas = (historialEntregas != null) ? historialEntregas : new ArrayList<>();
      this.montoTotal = this.precioCombo + this.costoEntrega; // Calcula el monto total al crear el pedido
   }
   
   /*
    * Obtiene el identificador único del pedido.
    */
   public String getIdPedido() {
      return idPedido;
   }

   /*
    * Establece el identificador único del pedido.
    */
   public void setIdPedido(String idPedido) {
      this.idPedido = idPedido;
   }

   /*
    * Obtiene el cliente asociado a este pedido.
    */
   public Cliente getCliente() {
      return cliente;
   }

   /*
    * Establece el cliente asociado a este pedido.
    */
   public void setCliente(Cliente cliente) {
      this.cliente = cliente;
   }

   /*
    * Obtiene el repartidor asignado a este pedido.
    */
   public Repartidor getRepartidorAsignado() {
      return repartidorAsignado;
   }

   /*
    * Establece el repartidor asignado a este pedido.
    */
   public void setRepartidorAsignado(Repartidor repartidorAsignado) {
      this.repartidorAsignado = repartidorAsignado;
   }

   /*
    * Obtiene la dirección de entrega del pedido.
    */
   public String getDireccionEntrega() {
      return direccionEntrega;
   }

   /*
    * Establece la dirección de entrega del pedido.
    */
   public void setDireccionEntrega(String direccionEntrega) {
      this.direccionEntrega = direccionEntrega;
   }

   /*
    * Obtiene el combo de productos seleccionado para el pedido.
    */
   public Combo getCombo() {
      return combo;
   }

   /*
    * Establece el combo de productos para el pedido.
    */
   public void setCombo(Combo combo) {
      this.combo = combo;
   }

   /*
    * Obtiene el precio del combo seleccionado.
    */
   public double getPrecioCombo() {
      return precioCombo;
   }

   /*
    * Establece el precio del combo.
    */
   public void setPrecioCombo(double precioCombo) {
      this.precioCombo = precioCombo;
   }
   
   /*
    * Obtiene el método de pago elegido para el pedido.
    */
   public MetodoDePago getMetodoPago() {
      return metodoPago;
   }

   /*
    * Establece el método de pago para el pedido.
    */
   public void setMetodoPago(MetodoDePago metodoPago) {
      this.metodoPago = metodoPago;
   }

   /*
    * Verifica si el cliente requiere cambio.
    */
   public boolean isRequiereCambio() {
      return requiereCambio;
   }

   /*
    * Establece si el cliente requiere cambio.
    */
   public void setRequiereCambio(boolean requiereCambio) {
      this.requiereCambio = requiereCambio;
   }

   /*
    * Obtiene el costo de entrega del pedido.
    */
   public double getCostoEntrega() {
      return costoEntrega;
   }

   /*
    * Establece el costo de entrega del pedido.
    */
   public void setCostoEntrega(double costoEntrega) {
      this.costoEntrega = costoEntrega;
   }

   /*
    * Obtiene el monto del vuelto a entregar.
    */
   public double getVuelto() {
      return vuelto;
   }

   /*
    * Establece el monto del vuelto a entregar.
    */
   public void setVuelto(double vuelto) {
      this.vuelto = vuelto;
   }

   /*
    * Obtiene el estado actual del pedido.
    */
   public String getEstado() {
      return estado;
   }

   /*
    * Establece el estado actual del pedido.
    */
   public void setEstado(String estado) {
      this.estado = estado;
   }

   /*
    * Obtiene la fecha y hora de creación del pedido.
    */
   public Date getFechaCreacion() {
      return fechaCreacion;
   }

   /*
    * Establece la fecha y hora de creación del pedido.
    */
   public void setFechaCreacion(Date fechaCreacion) {
      this.fechaCreacion = fechaCreacion;
   }

   /*
    * Obtiene la lista de historiales de entrega asociados a este pedido.
    */
   public List<HistorialDeEntrega> getHistorialEntregas() {
      return historialEntregas;
   }

   /*
    * Obtiene la fecha y hora de entrega del pedido.
    */
   public Date getFechaEntrega() {
      return fechaEntrega;
   }

   /*
    * Establece la fecha y hora de entrega del pedido.
    */
   public void setFechaEntrega(Date fechaEntrega) {
      this.fechaEntrega = fechaEntrega;
   }
   
   /*
    * Establece la lista de historiales de entrega para este pedido.
    *
    * @param historialEntregas La nueva lista de historiales de entrega.
    */
   public void setHistorialEntregas(List<HistorialDeEntrega> historialEntregas) {
      this.historialEntregas = historialEntregas;
   }

   /*
    * Obtiene el monto total del pedido (precio del combo + costo de entrega).
    */
   public double getMontoTotal() {
      return montoTotal;
   }

   /*
    * Establece el monto total del pedido.
    * Este método podría usarse si el monto total necesita ser ajustado manualmente
    * después de la creación inicial.
    */
   public void setMontoTotal(double montoTotal) {
      this.montoTotal = montoTotal;
   }
   
   /*
    * Actualiza el estado del pedido a un nuevo estado.
    */
   public void actualizarEstado(String nuevoEstado) {
      this.estado = nuevoEstado;
   }

   /*
    * Asigna un repartidor a este pedido.
    */
   public void asignarRepartidor(Repartidor repartidorAsignado) {
      this.repartidorAsignado = repartidorAsignado;
   }

   /*
    * Agrega un historial de entrega a la lista de historiales de este pedido.
    */
   public void agregarHistorialEntrega(HistorialDeEntrega historial) {
      this.historialEntregas.add(historial);
   }

   /*
    * Genera un resumen detallado del pedido en formato de cadena de texto.
    */
   public String obtenerResumenPedido() {
      return """
           === Resumen del Pedido ===
           ID: """ + idPedido + "\n" +
             "Cliente: " + cliente.getNombre() + "\n" +
             "Combo: " + combo + "\n" +
             "Dirección: " + direccionEntrega + "\n" +
             "Método de pago: " + metodoPago + "\n" +
             "¿Requiere cambio?: " + (requiereCambio ? "Sí" : "No") + "\n" +
             "Monto total: $" + getMontoTotal() + "\n" +
             "Costo delivery: $" + costoEntrega + "\n" +
             "Estado actual: " + estado + "\n";
   }

   /*
    * Proporciona una representación en cadena de texto del objeto Pedido.
    * Útil para propósitos de depuración o visualización rápida.
    */
   @Override
   public String toString() {
      return "Pedido #" + idPedido + " - Cliente: " + cliente.getNombre() + " - Combo: " + combo + " - Estado: " + estado + " - Monto total: $" + getMontoTotal();
   }
}

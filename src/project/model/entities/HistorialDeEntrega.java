
package project.model.entities;

import java.util.Date;

/*
 * Representa el historial de una entrega específica en el sistema.
 * Contiene detalles como el ID del historial, el pedido asociado, el repartidor,
 * la fecha de registro, el estado de la entrega y la ubicación.
 */
public class HistorialDeEntrega {
   private String idHistorial;
   private Pedido pedidoAsociado;
   private Repartidor repartidor;
   private Date fechaRegistro;
   private String estadoEntrega;
   private String ubicacionEntrega;
    
   /*
    * Constructor para crear una nueva instancia de HistorialDeEntrega.
    */
   public HistorialDeEntrega(String idHistorial, Date fechaRegistro, String estadoEntrega, String ubicacionEntrega, Repartidor repartidor, Pedido pedidoAsociado) {
      this.idHistorial = idHistorial;
      this.fechaRegistro = fechaRegistro;
      this.estadoEntrega = estadoEntrega;
      this.ubicacionEntrega = ubicacionEntrega;
      this.repartidor = repartidor;
      this.pedidoAsociado = pedidoAsociado;
   }
   
   /*
    * Obtiene el identificador único del historial de entrega.
    */
   public String getIdHistorial() {
      return idHistorial;
   }

   /*
    * Establece el identificador único del historial de entrega.
    */
   public void setIdHistorial(String idHistorial) {
      this.idHistorial = idHistorial;
   }

   /*
    * Obtiene el pedido asociado a este historial de entrega.
    */
   public Pedido getPedidoAsociado() {
      return pedidoAsociado;
   }

   /*
    * Establece el pedido asociado a este historial de entrega.
    */
   public void setPedidoAsociado(Pedido pedidoAsociado) {
      this.pedidoAsociado = pedidoAsociado;
   }

   /*
    * Obtiene el repartidor asignado a esta entrega.
    */
   public Repartidor getRepartidor() {
      return repartidor;
   }

   /*
    * Establece el repartidor asignado a esta entrega.
    */
   public void setRepartidor(Repartidor repartidor) {
      this.repartidor = repartidor;
   }

   /*
    * Obtiene la fecha y hora de registro de este historial.
    */
   public Date getFechaRegistro() {
      return fechaRegistro;
   }

   /*
    * Establece la fecha y hora de registro de este historial.
    */
   public void setFechaRegistro(Date fechaRegistro) {
      this.fechaRegistro = fechaRegistro;
   }

   /*
    * Obtiene el estado actual de la entrega.
    */
   public String getEstadoEntrega() {
      return estadoEntrega;
   }

   /*
    * Establece el estado actual de la entrega.
    */
   public void setEstadoEntrega(String estadoEntrega) {
      this.estadoEntrega = estadoEntrega;
   }

   /*
    * Obtiene la ubicación de la entrega.
    */
   public String getUbicacionEntrega() {
      return ubicacionEntrega;
   }

   /*
    * Establece la ubicación de la entrega.
    */
   public void setUbicacionEntrega(String ubicacionEntrega) {
      this.ubicacionEntrega = ubicacionEntrega;
   }
}

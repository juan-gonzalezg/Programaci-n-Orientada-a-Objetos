
package project.model.entities;

import java.util.ArrayList;
import java.util.List;

/*
 * La clase `BaseDeDatos` actúa como un contenedor central para todas las listas
 * de entidades principales del sistema, como usuarios, repartidores, pedidos, clientes
 * e historial de entregas. Facilita la gestión y el acceso a los datos en memoria.
 */
public class BaseDeDatos {
   /*
    * Lista de objetos `Usuario` registrados en el sistema.
    */
   private List<Usuario> usuarios;
   /*
    * Lista de objetos `Repartidor` registrados en el sistema.
    */
   private List<Repartidor> repartidor;
   /*
    * Lista de objetos `Pedido` actuales en el sistema.
    */
   private List<Pedido> pedido;
   /*
    * Lista de objetos `Cliente` registrados en el sistema.
    */
   private List<Cliente> cliente;
   /*
    * Lista de objetos `HistorialDeEntrega` que registran entregas pasadas.
    */
   private List<HistorialDeEntrega> historial;
   
   /*
    * Constructor de la clase `BaseDeDatos`.
    * Inicializa todas las listas de entidades como `ArrayList` vacíos
    * para asegurar que no sean nulas al inicio.
    */
   public BaseDeDatos() {
      this.usuarios = new ArrayList<>();
      this.repartidor = new ArrayList<>();
      this.pedido = new ArrayList<>();
      this.cliente = new ArrayList<>();
      this.historial = new ArrayList<>();
   }
   
   /*
    * Obtiene la lista de usuarios.
    */
   public List<Usuario> getUsuarios() {
      return usuarios;
   }

   /*
    * Establece la lista de usuarios.
    */
   public void setUsuarios(List<Usuario> usuarios) {
      this.usuarios = usuarios;
   }

   /*
    * Obtiene la lista de repartidores.
    */
   public List<Repartidor> getRepartidor() {
      return repartidor;
   }

   /*
    * Establece la lista de repartidores.
    */
   public void setRepartidor(List<Repartidor> repartidor) {
      this.repartidor = repartidor;
   }
   
   /*
    * Obtiene la lista de pedidos.
    */
   public List<Pedido> getPedido() {
      return pedido;
   }

   /*
    * Establece la lista de pedidos.
    */
   public void setPedido(List<Pedido> pedido) {
      this.pedido = pedido;
   }

   /*
    * Obtiene la lista de clientes.
    */
   public List<Cliente> getCliente() {
      return cliente;
   }

   /*
    * Establece la lista de clientes.
    */
   public void setCliente(List<Cliente> cliente) {
      this.cliente = cliente;
   }

   /*
    * Obtiene la lista del historial de entregas.
    */
   public List<HistorialDeEntrega> getHistorial() {
      return historial;
   }

   /*
    * Establece la lista del historial de entregas.
    */
   public void setHistorial(List<HistorialDeEntrega> historial) {
      this.historial = historial;
   }
}

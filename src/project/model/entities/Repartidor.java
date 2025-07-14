package project.model.entities;

import project.model.base.Persona;

/*
 * La clase `Repartidor` extiende de `Persona` y representa a un repartidor en el sistema.
 * Añade la propiedad de disponibilidad, una contraseña y métodos específicos para la gestión de repartidores,
 * como la actualización de datos y la consulta de historial/pedidos.
 */
public class Repartidor extends Persona{
   /*
    * Indica la disponibilidad del repartidor (true si está disponible, false si está ocupado).
    */
   private boolean disponibilidad;

   /*
    * Contraseña alfanumérica del repartidor, generada por el sistema.
    */
   private String contrasena;

   /*
    * Constructor de la clase `Repartidor`.
    * Inicializa un nuevo repartidor con sus datos personales, su estado de disponibilidad y una contraseña.
    */
   public Repartidor(String nombre, String cedulaIdentidad, String numeroTelefono, boolean disponibilidad, String contrasena) {
      super(nombre, numeroTelefono, cedulaIdentidad);
      this.disponibilidad = disponibilidad;
      this.contrasena = contrasena;
   }

   /*
    * Obtiene el estado de disponibilidad del repartidor.
    */
   public boolean isDisponibilidad() {
      return disponibilidad;
   }

   /*
    * Establece el estado de disponibilidad del repartidor.
    */
   public void setDisponibilidad(boolean disponibilidad) {
      this.disponibilidad = disponibilidad;
   }

   /*
    * Obtiene la contraseña del repartidor.
    */
   public String getContrasena() {
      return contrasena;
   }

   /*
    * Establece la contraseña del repartidor.
    */
   public void setContrasena(String contrasena) {
      this.contrasena = contrasena;
   }
   
   /*
    * Sobrescribe el método `obtenerInformacion` de la clase `Persona` para
    * retornar una cadena con la información específica del repartidor, incluyendo su disponibilidad.
    */
   @Override
   public String obtenerInformacion() {
      return "Repartidor: " + getNombre() + "\n" + "Teléfono: " + getNumeroTelefono() + "\n" + "Cédula: " + getCedulaIdentidad() + "\n" + "Disponibilidad: " + (disponibilidad ? "Disponible" : "Ocupado") + "\n" + "Contraseña: " + getContrasena();
   }

   /*
    * Simula la consulta del historial personal de entregas del repartidor.
    * Actualmente solo imprime un mensaje en la consola.
    */
   public void consultarHistorialPersonal() {
      System.out.println("Consultando historial de entregas de " + getNombre());
   }

   /*
    * Simula la consulta de los pedidos asignados al repartidor.
    * Actualmente solo imprime un mensaje en la consola.
    */
   public void consultarPedidosAsignados() {
      System.out.println("Consultando pedidos asignados a " + getNombre());
   }
   
   /*
    * Sobrescribe el método `toString()` para proporcionar una representación de cadena
    * legible del objeto `Repartidor`, mostrando su nombre y disponibilidad.
    */
   @Override
   public String toString() {
      return getNombre() + " - " + (disponibilidad ? "Disponible" : "Ocupado");
   }
}

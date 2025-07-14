
package project.model.entities;

import project.model.base.Persona;

/*
 * Representa a un cliente en el sistema.
 * Extiende la clase abstracta Persona para heredar propiedades básicas de una persona
 * y añade la propiedad específica de 'direccion'.
 */
public class Cliente extends Persona{
   private String direccion;

   /*
    * Constructor para crear una nueva instancia de Cliente.
    */
   public Cliente(String nombre, String cedulaIdentidad, String numeroTelefono, String direccion) {
      super(nombre, numeroTelefono, cedulaIdentidad); // Llama al constructor de la clase base Persona
      this.direccion = direccion;
   }
    
   /*
    * Obtiene la dirección del cliente.
    */
   public String getDireccion() {
      return direccion;
   }

   /*
    * Establece la dirección del cliente.
    */
   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   /*
    * Proporciona una representación detallada de la información del cliente.
    * Sobrescribe el método abstracto de la clase Persona.
    */
   @Override
   public String obtenerInformacion() {
      return "Cliente: " + getNombre() + "\n" +
             "Teléfono: " + getNumeroTelefono() + "\n" +
             "Cédula: " + getCedulaIdentidad() + "\n" +
             "Dirección: " + direccion;
   }
   
   /*
    * Proporciona una representación en cadena de texto del objeto Cliente.
    * Útil para mostrar el cliente en interfaces de usuario como ComboBoxes.
    */
   @Override
   public String toString() {
      return getNombre() + " - " + cedulaIdentidad;
   }
}

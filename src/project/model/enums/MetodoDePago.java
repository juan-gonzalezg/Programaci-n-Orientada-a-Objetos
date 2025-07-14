
package project.model.enums;

/*
 * Enumeración que define los diferentes métodos de pago aceptados en el sistema.
 * Cada método de pago tiene un nombre descriptivo asociado.
 */
public enum MetodoDePago {
   // Opción por defecto para selección en interfaces de usuario.
   SELECCIONAR("-Seleccionar-"),
   // Método de pago en efectivo, con indicación de moneda.
   EFECTIVO("Efectivo $"),
   // Método de pago a través de Pago Móvil.
   PAGO_MOVIL("Pago Móvil"),
   // Método de pago a través de transferencia bancaria.
   TRANSFERENCIA("Transferencia bancaria");

   // Campo para almacenar el nombre descriptivo del método de pago.
   private final String nombre;

   /*
    * Constructor para inicializar un elemento de la enumeración MetodoDePago.
    */
   MetodoDePago(String nombre) {
      this.nombre = nombre;
   }

   /*
    * Sobrescribe el método toString() para proporcionar una representación legible
    * del método de pago, mostrando su nombre.
    */
   @Override
   public String toString() {
      return nombre;
   }
}

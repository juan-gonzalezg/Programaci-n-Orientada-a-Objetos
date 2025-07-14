
package project.model.enums;

/*
 * Enumeración que representa los diferentes tipos de combos de comida disponibles
 * en el sistema, junto con sus nombres descriptivos y precios asociados.
 */
public enum Combo {
   // Opción por defecto para selección en interfaces de usuario, sin precio asociado.
   SELECCIONAR("-Seleccionar-",0),
   // Combo "Combo para 4" con su precio.
   PARA4("Combo para 4", 10),
   // Combo "Combo para 2" con su precio.
   PARA2("Combo para 2", 5);

   // Constructor privado por defecto, utilizado implícitamente si no se proporcionan valores.
   private Combo() {
      this.nombre = null;
      this.precio = 0;
   }

   // Campo para almacenar el nombre descriptivo del combo.
   private final String nombre;
   // Campo para almacenar el precio del combo.
   private final double precio;

   /*
    * Constructor para inicializar un elemento de la enumeración Combo.
    */
   Combo(String nombre, double precio) {
      this.nombre = nombre;
      this.precio = precio;
   }

   /*
    * Obtiene el nombre descriptivo del combo.
    */
   public String getNombre() {
      return nombre;
   }

   /*
    * Obtiene el precio del combo.
    */
   public double getPrecio() {
      return precio;
   }

   /*
    * Sobrescribe el método toString() para proporcionar una representación legible
    * del combo, mostrando su nombre.
    */
   @Override
   public String toString() {
      return nombre;
   }
}

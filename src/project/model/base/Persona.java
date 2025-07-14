
package project.model.base;

/*
 * La clase abstracta `Persona` sirve como base para todas las entidades que representan
 * a una persona en el sistema. Define atributos comunes como nombre, número de teléfono
 * y cédula de identidad, así como un método abstracto para obtener información detallada.
 */
public abstract class Persona {
   /*
    * El nombre completo de la persona.
    */
   protected String nombre;
   /*
    * El número de teléfono de la persona.
    */
   protected String numeroTelefono;
   /*
    * La cédula de identidad de la persona.
    */
   protected String cedulaIdentidad;

   /*
    * Constructor de la clase `Persona`.
    */
   public Persona(String nombre, String numeroTelefono, String cedulaIdentidad) {
      this.nombre = nombre;
      this.numeroTelefono = numeroTelefono;
      this.cedulaIdentidad = cedulaIdentidad;
   }

   /*
    * Obtiene el nombre de la persona.
    */
   public String getNombre() {
      return nombre;
   }

   /*
    * Establece el nombre de la persona.
    */
   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   /*
    * Obtiene el número de teléfono de la persona.
    */
   public String getNumeroTelefono() {
      return numeroTelefono;
   }

   /*
    * Establece el número de teléfono de la persona.
    */
   public void setNumeroTelefono(String numeroTelefono) {
      this.numeroTelefono = numeroTelefono;
   }

   /*
    * Obtiene la cédula de identidad de la persona.
    */
   public String getCedulaIdentidad() {
      return cedulaIdentidad;
   }

   /*
    * Establece la cédula de identidad de la persona.
    */
   public void setCedulaIdentidad(String cedulaIdentidad) {
      this.cedulaIdentidad = cedulaIdentidad;
   }
   
   /*
    * Método abstracto que debe ser implementado por las subclases para retornar
    * una cadena con la información detallada de la persona.
    */
   public abstract String obtenerInformacion();
}

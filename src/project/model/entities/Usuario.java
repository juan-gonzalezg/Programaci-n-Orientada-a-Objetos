
package project.model.entities;

/*
 * La clase `Usuario` representa una entidad de usuario en el sistema.
 * Contiene la cédula de identidad, la contraseña y el rol asociado al usuario.
 */
public class Usuario {
   /*
    * La cédula de identidad del usuario, utilizada como identificador único.
    */
   private String cedula;
   /*
    * La contraseña del usuario.
    */
   private String contrasena;
   /*
    * El rol del usuario en el sistema (ej. "administrador", "repartidor").
    */
   private String rol;

   /*
    * Obtiene la cédula de identidad del usuario.
    */
   public String getCedula() {
      return cedula;
   }

   /*
    * Establece la cédula de identidad del usuario.
    */
   public void setCedula(String cedula) {
      this.cedula = cedula;
   }

   /*
    * Obtiene la contraseña del usuario.
    */
   public String getContrasena() {
      return contrasena;
   }

   /*
    * Establece la contraseña del usuario.
    */
   public void setContrasena(String contrasena) {
      this.contrasena = contrasena;
   }

   /*
    * Obtiene el rol del usuario.
    */
   public String getRol() {
      return rol;
   }

   /*
    * Establece el rol del usuario.
    */
   public void setRol(String rol) {
      this.rol = rol;
   }
}

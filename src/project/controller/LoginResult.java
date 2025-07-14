
package project.controller;

import project.model.entities.Repartidor;

/*
 * La clase `LoginResult` encapsula el resultado de una operación de inicio de sesión.
 * Contiene información sobre si el login fue exitoso, un mensaje descriptivo,
 * el rol del usuario autenticado y, opcionalmente, el objeto `Repartidor`
 * si el usuario autenticado tiene ese rol.
 */
public class LoginResult {
   /*
    * Indica si la operación de login fue exitosa.
    */
   private final boolean exito;
   /*
    * Mensaje descriptivo del resultado del login (éxito o error).
    */
   private final String mensaje;
   /*
    * Rol del usuario autenticado (ej. "administrador", "repartidor", "desconocido").
    */
   private final String rol;
   /*
    * Objeto `Repartidor` asociado al usuario, si el rol es "repartidor".
    * Es `null` para otros roles o si no se encontró el perfil del repartidor.
    */
   private final Repartidor repartidor;

   /*
    * Constructor de la clase `LoginResult`.
    */
   public LoginResult(boolean exito, String mensaje, String rol, Repartidor repartidor) {
      this.exito = exito;
      this.mensaje = mensaje;
      this.rol = rol;
      this.repartidor = repartidor;
   }

   /*
    * Verifica si la operación de login fue exitosa.
    */
   public boolean isExito() {
      return exito;
   }

   /*
    * Obtiene el mensaje descriptivo del resultado del login.
    */
   public String getMensaje() {
      return mensaje;
   }

   /*
    * Obtiene el rol del usuario autenticado.
    */
   public String getRol() {
      return rol;
   }

   /*
    * Obtiene el objeto `Repartidor` asociado al usuario.
    */
   public Repartidor getRepartidor() {
      return repartidor;
   }
}

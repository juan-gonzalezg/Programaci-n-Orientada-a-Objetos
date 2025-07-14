
package project.controller;

import project.data.GestorJSON;
import project.model.entities.BaseDeDatos;
import project.model.entities.Repartidor;
import project.model.entities.Usuario;
import java.util.List;

/*
 * La clase `LoginController` maneja la lógica de negocio relacionada con el inicio de sesión de usuarios.
 * Se encarga de validar credenciales, obtener roles de usuario y procesar el flujo de login
 * interactuando con la capa de datos (`GestorJSON`) para acceder a la `BaseDeDatos`.
 */
public class LoginController {
   /*
    * Instancia de la base de datos que contiene la información de usuarios y repartidores.
    */
   private final BaseDeDatos baseDeDatos;

   /*
    * Constructor de la clase `LoginController`.
    * Inicializa el controlador cargando la base de datos desde el archivo JSON.
    */
   public LoginController() {
      baseDeDatos = GestorJSON.leerBaseDatos();
      System.out.println("DEBUG: LoginController inicializado. BaseDeDatos cargada.");
   }
    
   /*
    * Valida las credenciales de un usuario (cédula y contraseña).
    * Busca el usuario por cédula y compara la contraseña proporcionada con la almacenada.
    */
   public boolean validarCredenciales(String cedula, String contrasena) {
      System.out.println("DEBUG: Validando credenciales para Cédula: '" + cedula + "' y Contraseña: '" + contrasena + "'");
      Usuario usuario = buscarUsuarioPorCedula(cedula);
      if (usuario != null) {
         System.out.println("DEBUG: Usuario encontrado: Cédula='" + usuario.getCedula() + "', Contraseña Almacenada='" + usuario.getContrasena() + "'");
         boolean passwordMatch = usuario.getContrasena().equals(contrasena);
         System.out.println("DEBUG: Coincidencia de contraseña: " + passwordMatch);
         return passwordMatch;
      } else {
         System.out.println("DEBUG: Usuario no encontrado para cédula: " + cedula);
         return false;
      }
   }

   /*
    * Obtiene el rol de un usuario dado su cédula.
    */
   public String obtenerRol(String cedula) {
      Usuario usuario = buscarUsuarioPorCedula(cedula);
      return (usuario != null) ? usuario.getRol() : "desconocido";
   }

   /*
    * Obtiene el nombre de un repartidor dado su cédula.
    */
   public String obtenerNombreRepartidor(String cedula) {
      Repartidor r = buscarRepartidorPorCedula(cedula);
      return (r != null) ? r.getNombre() : "Desconocido";
   }

   /*
    * Procesa un intento de inicio de sesión, validando las credenciales y determinando
    * el resultado del login, incluyendo el rol del usuario y, si aplica, el objeto `Repartidor`.
    * Este método ha sido modularizado para mejorar la legibilidad y la separación de responsabilidades.
    */
   public LoginResult procesarLogin(String cedula, String contrasena) {
      String cedulaLimpia = cedula.trim();
      String contrasenaLimpia = contrasena; 
      System.out.println("DEBUG: Procesando Login para Cédula (limpia): '" + cedulaLimpia + "' y Contraseña (limpia): '" + contrasenaLimpia + "'");
      
      // Valida el usuario y la contraseña.
      Usuario usuario = validarUsuario(cedulaLimpia, contrasenaLimpia);
      
      // Si la validación del usuario falla, retorna un resultado de login fallido.
      if (usuario == null) {
         return new LoginResult(false, "Cédula o contraseña incorrecta.", null, null);
      }
      
      // Si el usuario es válido, crea y retorna el resultado del login basado en su rol.
      return crearResultadoLogin(usuario);
   }

   /*
    * Valida las credenciales de un usuario buscando por cédula y comparando la contraseña.
    */
   private Usuario validarUsuario(String cedula, String contrasena) {
      Usuario usuario = buscarUsuarioPorCedula(cedula);
      if (usuario == null) {
         System.out.println("DEBUG: Usuario NO encontrado después de buscar por cédula.");
         return null;
      }
      if (!usuario.getContrasena().equals(contrasena)) {
         System.out.println("DEBUG: Contraseña NO coincide. Almacenada: '" + usuario.getContrasena() + "', Ingresada: '" + contrasena + "'");
         return null;
      }
      System.out.println("DEBUG: Credenciales correctas. Rol: " + usuario.getRol());
      return usuario;
   }

   /*
    * Crea un objeto `LoginResult` basado en el usuario autenticado.
    * Si el usuario es un repartidor, busca y asocia el perfil del repartidor.
    */
   private LoginResult crearResultadoLogin(Usuario usuario) {
      String rol = usuario.getRol();
      if ("repartidor".equalsIgnoreCase(rol)) {
         Repartidor rep = buscarRepartidorPorCedula(usuario.getCedula());
         if (rep == null) {
            System.err.println("ERROR: Usuario es repartidor pero NO se encontró el perfil de repartidor para cédula: " + usuario.getCedula() + ". Asegúrese de que el repartidor exista en la base de datos de repartidores.");
            return new LoginResult(false, "Error de configuración: Perfil de repartidor no encontrado.", rol, null);
         }
         return new LoginResult(true, "Acceso concedido.", rol, rep);
      }
      return new LoginResult(true, "Acceso concedido.", rol, null);
   }
    
   /*
    * Busca un objeto `Usuario` en la base de datos por su cédula de identidad.
    */
   private Usuario buscarUsuarioPorCedula(String cedula) {
      List<Usuario> usuarios = baseDeDatos.getUsuarios();
      if (usuarios == null) {
         System.err.println("Advertencia: La lista de usuarios en la BaseDeDatos es nula al buscar por cédula.");
         return null;
      }
      for (Usuario u : usuarios) {
         System.out.println("DEBUG: Comparando cédula buscada '" + cedula + "' con usuario en lista '" + u.getCedula() + "'");
         if (u.getCedula() != null && u.getCedula().equals(cedula)) // Added null check for u.getCedula()
            return u;
      }
      return null;
   }
    
   /*
    * Busca un objeto `Repartidor` en la base de datos por su cédula de identidad.
    */
   private Repartidor buscarRepartidorPorCedula(String cedula) {
      List<Repartidor> repartidores = baseDeDatos.getRepartidor();
      if (repartidores == null) {
         System.err.println("Advertencia: La lista de repartidores en la BaseDeDatos es nula al buscar por cédula.");
         return null;
      }
      for (Repartidor r : repartidores) {
         System.out.println("DEBUG: Comparando cédula buscada '" + cedula + "' con repartidor en lista '" + r.getCedulaIdentidad() + "'");
         if (r.getCedulaIdentidad() != null && r.getCedulaIdentidad().equals(cedula)) { // Added null check for r.getCedulaIdentidad()
            return r;
         }
      }
      return null;
   }
}

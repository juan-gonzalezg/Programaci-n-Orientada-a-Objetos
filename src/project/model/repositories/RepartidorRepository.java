package project.model.repositories;

import project.data.GestorJSON;
import project.model.base.Repository;
import project.model.entities.BaseDeDatos;
import project.model.entities.Repartidor;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase RepartidorRepository que implementa la interfaz Repository para la entidad Repartidor.
 * Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los objetos Repartidor
 * persistiendo los datos a través de GestorJSON.
 */
public class RepartidorRepository implements Repository<Repartidor>{

   /*
    * Guarda un repartidor en la base de datos. Si el repartidor ya existe (basado en la cédula de identidad),
    * lo actualiza; de lo contrario, añade un nuevo repartidor.
    */
   @Override
   public void guardar(Repartidor repartidor) {
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      List<Repartidor> repartidores = bd.getRepartidor();
      boolean encontrado = false;

      // Asegurarse de que el repartidor que se intenta guardar no tenga una cédula nula
      // Esto es una validación adicional para evitar que se guarden repartidores inválidos
      if (repartidor == null || repartidor.getCedulaIdentidad() == null || repartidor.getCedulaIdentidad().trim().isEmpty()) {
          System.err.println("ERROR: No se puede guardar un repartidor con cédula de identidad nula o vacía.");
          return; // Sale del método si el repartidor es inválido
      }

      for (int i = 0; i < repartidores.size(); i++) {
         Repartidor rActual = repartidores.get(i);
         // Añadir una comprobación de nulidad para rActual y su cédula de identidad
         // antes de intentar llamar a .equals()
         if (rActual != null && rActual.getCedulaIdentidad() != null && rActual.getCedulaIdentidad().equals(repartidor.getCedulaIdentidad())) {
            repartidores.set(i, repartidor);
            encontrado = true;
            break;
         }
      }
      if (!encontrado)
         repartidores.add(repartidor);
      bd.setRepartidor(repartidores);
      GestorJSON.guardarBaseDatos(bd);
   }
   
   /*
    * Obtiene todos los repartidores de la base de datos.
    */
   @Override
   public List<Repartidor> obtenerTodos() {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Retorna una nueva ArrayList que contiene todos los repartidores de la base de datos.
      return new ArrayList<>(bd.getRepartidor());
   }

   /*
    * Busca un repartidor por su cédula de identidad.
    * Se añade una comprobación de nulidad para evitar NullPointerException
    * si getCedulaIdentidad() devuelve null.
    */
   @Override
   public Repartidor buscarPorId(String cedula) {
      // Obtiene todos los repartidores y usa un stream para filtrar por cédula de identidad.
      // Se añade r != null y r.getCedulaIdentidad() != null para manejar posibles valores nulos.
      return obtenerTodos().stream()
                           .filter(r -> r != null && r.getCedulaIdentidad() != null && r.getCedulaIdentidad().equals(cedula))
                           .findFirst()
                           .orElse(null);
   }

   /*
    * Elimina un repartidor de la base de datos por su cédula de identidad.
    */
   @Override
   public void eliminarPorId(String cedula) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de repartidores de la base de datos.
      List<Repartidor> repartidores = bd.getRepartidor();
      // Elimina el repartidor de la lista si su cédula de identidad coincide.
      repartidores.removeIf(repartidorActual -> repartidorActual.getCedulaIdentidad().equals(cedula));
      // Establece la lista de repartidores actualizada en la base de datos.
      bd.setRepartidor(repartidores);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }
   
   /*
    * Actualiza la disponibilidad de un repartidor específico.
    */
   public boolean actualizarDisponibilidad(String cedula, boolean nuevoEstado) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de repartidores de la base de datos.
      List<Repartidor> repartidores = bd.getRepartidor();
      // Itera sobre los repartidores para encontrar el que coincide con la cédula.
      for (Repartidor r : repartidores) {
         if (r.getCedulaIdentidad().equals(cedula)) {
            // Actualiza el estado de disponibilidad.
            r.setDisponibilidad(nuevoEstado);
            // Establece la lista de repartidores actualizada en la base de datos.
            bd.setRepartidor(repartidores);
            // Guarda la base de datos actualizada en el archivo JSON.
            GestorJSON.guardarBaseDatos(bd);
            return true; // Retorna true si la actualización fue exitosa.
         }
      }
      return false; // Retorna false si el repartidor no fue encontrado.
   }
   
   /*
    * Elimina un repartidor de la base de datos por su ID.
    */
   public boolean eliminar(String idRepartidor) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de repartidores de la base de datos.
      List<Repartidor> repartidores = bd.getRepartidor();
      // Elimina el repartidor de la lista si su cédula de identidad coincide.
      boolean eliminado = repartidores.removeIf(repartidor -> repartidor != null && repartidor.getCedulaIdentidad() != null && repartidor.getCedulaIdentidad().equals(idRepartidor));
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
      return eliminado;
   }
}

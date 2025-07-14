
package project.model.repositories;

import project.data.GestorJSON;
import project.model.base.Repository;
import project.model.entities.BaseDeDatos;
import project.model.entities.HistorialDeEntrega;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase HistorialEntregaRepository que implementa la interfaz Repository para la entidad HistorialDeEntrega.
 * Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los objetos HistorialDeEntrega
 * persistiendo los datos a través de GestorJSON.
 */
public class HistorialEntregaRepository implements Repository<HistorialDeEntrega> {

   /*
    * Guarda un historial de entrega en la base de datos. Si el historial ya existe (basado en el ID),
    * lo actualiza; de lo contrario, añade un nuevo historial.
    */
   @Override
   public void guardar(HistorialDeEntrega historial) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de historiales de entrega de la base de datos.
      List<HistorialDeEntrega> historiales = bd.getHistorial();
      boolean encontrado = false;

      // Itera sobre la lista de historiales para verificar si el historial ya existe.
      for (int i = 0; i < historiales.size(); i++) {
         // Comprueba si el ID del historial actual coincide con el ID del historial a guardar.
         if (historiales.get(i).getIdHistorial().equals(historial.getIdHistorial())) {
            // Si se encuentra, actualiza el historial existente.
            historiales.set(i, historial);
            encontrado = true;
            break; // Sale del bucle una vez que se encuentra y actualiza el historial.
         }
      }

      // Si el historial no fue encontrado, lo añade a la lista.
      if (!encontrado) {
         historiales.add(historial);
      }
      // Establece la lista de historiales actualizada en la base de datos.
      bd.setHistorial(historiales);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }

   /*
    * Obtiene todos los historiales de entrega de la base de datos.
    */
   @Override
   public List<HistorialDeEntrega> obtenerTodos() {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Retorna una nueva ArrayList que contiene todos los historiales de entrega de la base de datos.
      return new ArrayList<>(bd.getHistorial());
   }

   /*
    * Busca un historial de entrega por su ID.
    */
   @Override
   public HistorialDeEntrega buscarPorId(String id) {
      // Obtiene todos los historiales y usa un stream para filtrar por ID.
      return obtenerTodos().stream().filter(h -> h.getIdHistorial().equals(id)).findFirst().orElse(null);
   }

   /*
    * Elimina un historial de entrega de la base de datos por su ID.
    */
   @Override
   public void eliminarPorId(String id) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de historiales de entrega de la base de datos.
      List<HistorialDeEntrega> historiales = bd.getHistorial();
      // Elimina el historial de la lista si su ID coincide.
      historiales.removeIf(h -> h.getIdHistorial().equals(id));
      // Establece la lista de historiales actualizada en la base de datos.
      bd.setHistorial(historiales);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }
}

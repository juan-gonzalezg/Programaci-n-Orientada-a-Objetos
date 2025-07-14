
package project.model.repositories;

import project.data.GestorJSON;
import project.model.base.Repository;
import project.model.entities.BaseDeDatos;
import project.model.entities.Pedido;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase PedidoRepository que implementa la interfaz Repository para la entidad Pedido.
 * Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los objetos Pedido
 * persistiendo los datos a través de GestorJSON.
 */
public class PedidoRepository implements Repository<Pedido> {

   /*
    * Guarda un pedido en la base de datos. Si el pedido ya existe (basado en el ID del pedido),
    * lo actualiza; de lo contrario, añade un nuevo pedido.
    */
   @Override
   public void guardar(Pedido pedido) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de pedidos de la base de datos.
      List<Pedido> pedidos = bd.getPedido();
      boolean encontrado = false;

      // Itera sobre la lista de pedidos para verificar si el pedido ya existe.
      for (int i = 0; i < pedidos.size(); i++) {
         // Comprueba si el ID del pedido actual coincide con el ID del pedido a guardar.
         if (pedidos.get(i).getIdPedido().equals(pedido.getIdPedido())) {
            // Si se encuentra, actualiza el pedido existente.
            pedidos.set(i, pedido);
            encontrado = true;
            break; // Sale del bucle una vez que se encuentra y actualiza el pedido.
         }
      }
      // Si el pedido no fue encontrado, lo añade a la lista.
      if (!encontrado)
         pedidos.add(pedido);
      // Establece la lista de pedidos actualizada en la base de datos.
      bd.setPedido(pedidos);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }

   /*
    * Actualiza un pedido existente en la base de datos.
    */
   public boolean actualizarPedido(Pedido pedido) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de pedidos de la base de datos.
      List<Pedido> pedidos = bd.getPedido();
      boolean encontrado = false;

      // Itera sobre la lista de pedidos para verificar si el pedido ya existe.
      for (int i = 0; i < pedidos.size(); i++) {
         // Comprueba si el ID del pedido actual coincide con el ID del pedido a actualizar.
         if (pedidos.get(i).getIdPedido().equals(pedido.getIdPedido())) {
            // Si se encuentra, actualiza el pedido existente.
            pedidos.set(i, pedido);
            encontrado = true;
            break; // Sale del bucle una vez que se encuentra y actualiza el pedido.
         }
      }
      // Si el pedido no fue encontrado, lo añade a la lista (comportamiento de "upsert").
      if (!encontrado) {
         pedidos.add(pedido);
      }
      try {
         // Establece la lista de pedidos actualizada en la base de datos.
         bd.setPedido(pedidos);
         // Guarda la base de datos actualizada en el archivo JSON.
         GestorJSON.guardarBaseDatos(bd);
         return true; // Retorna true si la operación fue exitosa.
      } catch (Exception e) {
         // Captura cualquier excepción durante el guardado y la imprime en la consola de errores.
         System.err.println("Error al guardar/actualizar pedido: " + e.getMessage());
         return false; // Retorna false si hubo un error.
      }
   }

   /*
    * Obtiene todos los pedidos de la base de datos.
    */
   @Override
   public List<Pedido> obtenerTodos() {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Retorna una nueva ArrayList que contiene todos los pedidos de la base de datos.
      return new ArrayList<>(bd.getPedido());
   }
   
   /*
    * Busca un pedido por su ID.
    */
   @Override
   public Pedido buscarPorId(String id) {
      // Itera sobre todos los pedidos para encontrar uno con el ID coincidente.
      for (Pedido pedidoActual : obtenerTodos()) {
         if (pedidoActual.getIdPedido().equals(id)) {
            return pedidoActual; // Retorna el pedido si se encuentra.
         }
      }
      return null; // Retorna null si no se encuentra ningún pedido con el ID.
   }

   /*
    * Elimina un pedido de la base de datos por su ID.
    */
   @Override
   public void eliminarPorId(String id) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de pedidos de la base de datos.
      List<Pedido> pedidos = bd.getPedido();
      // Elimina el pedido de la lista si su ID coincide.
      pedidos.removeIf(pedidoActual -> pedidoActual.getIdPedido().equals(id));
      // Establece la lista de pedidos actualizada en la base de datos.
      bd.setPedido(pedidos);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }
   
   /*
    * Elimina un pedido de la base de datos por su ID.
    */
   public boolean eliminar(String idPedido) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de pedidos de la base de datos.
      List<Pedido> pedidos = bd.getPedido();
      // Intenta eliminar el pedido de la lista si su ID coincide.
      boolean removed = pedidos.removeIf(p -> p.getIdPedido().equals(idPedido));
      // Si el pedido fue eliminado, guarda la base de datos actualizada.
      if (removed)
         GestorJSON.guardarBaseDatos(bd);
      return removed; // Retorna true si fue eliminado, false si no se encontró.
   }
}

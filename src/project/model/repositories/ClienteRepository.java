
package project.model.repositories;

import project.data.GestorJSON;
import project.model.base.Repository;
import project.model.entities.BaseDeDatos;
import project.model.entities.Cliente;
import java.util.ArrayList;
import java.util.List;

/*
 * Clase ClienteRepository que implementa la interfaz Repository para la entidad Cliente.
 * Gestiona las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para los objetos Cliente
 * persistiendo los datos a través de GestorJSON.
 */
public class ClienteRepository implements Repository<Cliente> {

   /*
    * Constructor predeterminado de ClienteRepository.
    */
   public ClienteRepository() {
   }

   /*
    * Guarda un cliente en la base de datos. Si el cliente ya existe (basado en la cédula de identidad),
    * lo actualiza; de lo contrario, añade un nuevo cliente.
    */
   @Override
   public void guardar(Cliente cliente) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de clientes de la base de datos.
      List<Cliente> clientes = bd.getCliente();
      boolean encontrado = false;

      // Itera sobre la lista de clientes para verificar si el cliente ya existe.
      for (int i = 0; i < clientes.size(); i++) {
         // Comprueba si el cliente actual no es nulo y si su cédula de identidad coincide.
         if (clientes.get(i) != null && clientes.get(i).getCedulaIdentidad() != null &&
            clientes.get(i).getCedulaIdentidad().equals(cliente.getCedulaIdentidad())) {
            // Si se encuentra, actualiza el cliente existente.
            clientes.set(i, cliente);
            encontrado = true;
            break; // Sale del bucle una vez que se encuentra y actualiza el cliente.
         }
      }

      // Si el cliente no fue encontrado, lo añade a la lista.
      if (!encontrado)
         clientes.add(cliente);

      // Establece la lista de clientes actualizada en la base de datos.
      bd.setCliente(clientes);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }

   /*
    * Obtiene todos los clientes de la base de datos.
    */
   @Override
   public List<Cliente> obtenerTodos() {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Retorna una nueva ArrayList que contiene todos los clientes de la base de datos.
      return new ArrayList<>(bd.getCliente());
   }

   /*
    * Busca un cliente por su cédula de identidad.
    */
   @Override
   public Cliente buscarPorId(String cedula) {
      // Obtiene todos los clientes y usa un stream para filtrar por cédula de identidad.
      return obtenerTodos().stream()
         .filter(c -> c != null && c.getCedulaIdentidad() != null && c.getCedulaIdentidad().equals(cedula))
         .findFirst() // Encuentra el primer cliente que coincida.
         .orElse(null); // Retorna null si no se encuentra ningún cliente.
   }

   /*
    * Elimina un cliente de la base de datos por su cédula de identidad.
    */
   @Override
   public void eliminarPorId(String cedula) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de clientes de la base de datos.
      List<Cliente> clientes = bd.getCliente();

      // Elimina el cliente de la lista si su cédula de identidad coincide.
      clientes.removeIf(clienteActual -> clienteActual != null && clienteActual.getCedulaIdentidad() != null && clienteActual.getCedulaIdentidad().equals(cedula));

      // Establece la lista de clientes actualizada en la base de datos.
      bd.setCliente(clientes);
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd);
   }
    
   /*
    * Elimina un cliente de la base de datos por su ID de cliente.
    */
   public boolean eliminar(String idCliente) {
      // Lee la base de datos actual desde el archivo JSON.
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      // Obtiene la lista de clientes de la base de datos.
      List<Cliente> clientes = bd.getCliente();
      // Elimina el cliente de la lista si su cédula de identidad coincide.
      boolean eliminado = clientes.removeIf(cliente -> cliente.getCedulaIdentidad().equals(idCliente));
      // Guarda la base de datos actualizada en el archivo JSON.
      GestorJSON.guardarBaseDatos(bd); // ¡Esta línea es la clave!
      return eliminado;
   }
}

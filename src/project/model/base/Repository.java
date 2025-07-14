
package project.model.base;

import java.util.List;

/*
 * Interfaz genérica para definir las operaciones básicas de un repositorio.
 * Un repositorio es un patrón de diseño que aísla la lógica de acceso a datos
 * del resto de la aplicación.
 */
public interface Repository<T> {

   /*
    * Guarda una entidad en el repositorio. Si la entidad ya existe, la actualiza;
    * de lo contrario, la añade.
    */
   void guardar(T entidad);

   /*
    * Obtiene todas las entidades del repositorio.
    *
    * @return Una lista de todas las entidades del tipo T.
    */
   List<T> obtenerTodos();

   /*
    * Busca una entidad en el repositorio por su identificador único.
    */
   T buscarPorId(String id);

   /*
    * Elimina una entidad del repositorio por su identificador único.
    */
   void eliminarPorId(String id);
}

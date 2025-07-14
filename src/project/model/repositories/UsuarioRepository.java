
package project.model.repositories;

import project.data.GestorJSON;
import project.model.base.Repository;
import project.model.entities.BaseDeDatos;
import project.model.entities.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements Repository<Usuario> {
   @Override
   public void guardar(Usuario usuario) {
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      List<Usuario> usuarios = bd.getUsuarios();
      boolean encontrado = false;
      for (int i = 0; i < usuarios.size(); i++) {
         if (usuarios.get(i).getCedula().equals(usuario.getCedula())) {
            usuarios.set(i, usuario);
            encontrado = true;
            break;
         }
      }
      if (!encontrado)
         usuarios.add(usuario);
      bd.setUsuarios(usuarios);
      GestorJSON.guardarBaseDatos(bd);
   }
   
   @Override
   public List<Usuario> obtenerTodos() {
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      return new ArrayList<>(bd.getUsuarios());
   }
   
   @Override
   public Usuario buscarPorId(String cedula) {
      return obtenerTodos().stream().filter(u -> u.getCedula().equals(cedula)).findFirst().orElse(null);
   }

   @Override
   public void eliminarPorId(String cedula) {
      BaseDeDatos bd = GestorJSON.leerBaseDatos();
      List<Usuario> usuarios = bd.getUsuarios();
      usuarios.removeIf(usuarioActual -> usuarioActual.getCedula().equals(cedula));
      bd.setUsuarios(usuarios);
      GestorJSON.guardarBaseDatos(bd);
   }

   public Usuario autenticar(String cedula, String contrasena) {
      return obtenerTodos().stream().filter(u -> u.getCedula().equals(cedula) && u.getContrasena().equals(contrasena)).findFirst().orElse(null);
   }
}

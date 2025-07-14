
package project.data;

import project.model.entities.BaseDeDatos;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * La clase `GestorJSON` es responsable de la lectura y escritura de la base de datos
 * de la aplicación desde y hacia un archivo JSON. Utiliza la librería Gson para la
 * serialización y deserialización de objetos Java a/desde JSON, incluyendo un adaptador
 * personalizado para el manejo de fechas.
 */
public class GestorJSON {
   /*
    * Ruta del archivo JSON donde se almacena la base de datos.
    */
   private static final String FILE_PATH = "src\\project\\data\\BaseDeDatos.json";
   /*
    * Formato de fecha utilizado para la serialización y deserialización de objetos `Date`.
    */
   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
   /*
    * Instancia de Gson configurada para pretty printing y manejo personalizado de fechas.
    */
   private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> {
      // Serializador para objetos Date: convierte Date a String en el formato DATE_FORMAT.
      return src == null ? null : new JsonPrimitive(DATE_FORMAT.format(src));
   }).registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
      // Deserializador para objetos Date: convierte String a Date en el formato DATE_FORMAT.
      if (json == null || json.isJsonNull())
         return null;
      String dateString = json.getAsString();
      try {
         return DATE_FORMAT.parse(dateString);
      } catch (ParseException e) {
         // Manejo de errores si el formato de fecha es incorrecto.
         System.err.println("DEBUG: Falló la conversión de String a Date para fecha: '" + dateString + "'. Formato esperado: 'dd/MM/yyyy HH:mm'. Error: " + e.getMessage());
         throw new JsonParseException("Formato de fecha inesperado para: " + dateString, e);
      }
   }).create();

   /*
    * Lee la base de datos desde el archivo JSON especificado por `FILE_PATH`.
    * Si el archivo no existe, está vacío o malformado, retorna una nueva instancia de `BaseDeDatos`.
    */
   public static BaseDeDatos leerBaseDatos() {
      try (Reader reader = new FileReader(FILE_PATH)) {
         System.out.println("DEBUG: Intentando leer BaseDeDatos desde: " + FILE_PATH);
         BaseDeDatos bd = gson.fromJson(reader, BaseDeDatos.class);
         if (bd != null) {
            System.out.println("DEBUG: BaseDeDatos leída correctamente.");
            List<?> usuarios = bd.getUsuarios();
            if (usuarios != null) {
               System.out.println("DEBUG: Número de usuarios encontrados: " + usuarios.size());
               usuarios.forEach(u -> System.out.println("DEBUG: Usuario en BD: " + (u != null ? u.toString() : "null"))); 
            } else
               System.out.println("DEBUG: Lista de usuarios en BaseDeDatos es NULL.");
         } else
            System.out.println("DEBUG: gson.fromJson devolvió NULL. Archivo podría estar vacío o malformado.");
         return (bd != null) ? bd : new BaseDeDatos();
      } catch (Exception e) {
         System.err.println("ERROR: No se pudo leer la BaseDeDatos desde JSON: " + e.getMessage());
         return new BaseDeDatos(); // Retorna una base de datos vacía en caso de error
      }
   }

   /*
    * Guarda el objeto `BaseDeDatos` proporcionado en el archivo JSON especificado por `FILE_PATH`.
    * El JSON se guarda con formato "pretty printing" para facilitar la lectura.
    */
   public static void guardarBaseDatos(BaseDeDatos bd) {
      try (Writer writer = new FileWriter(FILE_PATH)) {
         gson.toJson(bd, writer);
         System.out.println("DEBUG: BaseDeDatos guardada correctamente en: " + FILE_PATH);
      } catch (Exception e) {
         System.err.println("ERROR: No se pudo guardar la BaseDeDatos en JSON: " + e.getMessage());
      }
   }
}

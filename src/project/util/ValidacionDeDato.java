
package project.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/*
 * Clase de utilidad para la validación de diferentes tipos de datos.
 * Contiene métodos estáticos para validar formatos de cédulas, RIF, teléfonos, textos,
 * números (double y enteros) y fechas.
 */
public class ValidacionDeDato {

   /*
    * Valida si una cadena de texto tiene el formato de una cédula venezolana.
    * Una cédula venezolana válida debe contener entre 7 y 8 dígitos numéricos.
    */
   public static boolean esCedulaVenezolana(String cedula) {
      // Utiliza una expresión regular para verificar que la cadena contiene solo dígitos
      // y tiene una longitud de entre 7 y 8 caracteres.
      return cedula.matches("^\\d{7,8}$");
   }

   /*
    * Valida si una cadena de texto tiene el formato de un RIF venezolano.
    * Un RIF válido debe comenzar con una de las letras V, E, J, G, seguida de 9 dígitos.
    */
   public static boolean esRIF(String rif) {
      // Utiliza una expresión regular para verificar el formato del RIF.
      // ^[VEJG]{1} asegura que el primer carácter sea V, E, J o G.
      // \\d{9}$ asegura que le sigan exactamente 9 dígitos.
      return rif.matches("^[VEJG]{1}\\d{9}$");
   }

   /*
    * Valida si una cadena de texto tiene el formato de una cédula extranjera genérica.
    * Una cédula extranjera válida debe ser alfanumérica y tener entre 5 y 15 caracteres.
    */
   public static boolean esCedulaExtranjera(String doc) {
      // Utiliza una expresión regular para verificar que la cadena contiene caracteres alfanuméricos
      // y tiene una longitud de entre 5 y 15 caracteres.
      return doc.matches("^[A-Za-z0-9]{5,15}$"); // El rango de longitud es ajustable según requisitos.
   }

   /*
    * Valida si una cadena de texto tiene el formato de un número telefónico venezolano válido.
    * Un número telefónico venezolano válido debe comenzar con un prefijo de operadora (0412, 0414, 0424, 0416, 0426)
    * seguido de 7 dígitos.
    */
   public static boolean esTelefonoValido(String telefono) {
      // Utiliza una expresión regular para verificar el formato del número telefónico.
      // ^(0412|0414|0424|0416|0426) asegura uno de los prefijos válidos.
      // \\d{7}$ asegura que le sigan exactamente 7 dígitos.
      return telefono.matches("^(0412|0414|0424|0416|0426)\\d{7}$");
   }

   /*
    * Valida si una cadena de texto contiene solo letras (mayúsculas, minúsculas, incluyendo Ñ y acentos) y espacios.
    * Útil para validar campos como nombres, apellidos, etc.
    */
   public static boolean esTextoValido(String texto) {
      // Utiliza una expresión regular para verificar que la cadena contiene solo letras (incluyendo acentos y Ñ)
      // y espacios.
      return texto.matches("^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$");
   }

   /*
    * Valida si una cadena de texto representa un número decimal (double) válido.
    * Acepta números enteros o números con un punto decimal seguido de dígitos.
    */
   public static boolean esDoubleValido(String input) {
      // Utiliza una expresión regular para verificar que la cadena es un número.
      // ^[0-9]+ asegura uno o más dígitos al inicio.
      // (\\.[0-9]+)?$ permite opcionalmente un punto seguido de uno o más dígitos.
      return input.matches("^[0-9]+(\\.[0-9]+)?$");
   }

   /*
    * Valida si una cadena de texto representa un número entero válido.
    */
   public static boolean esEnteroValido(String input) {
      // Utiliza una expresión regular para verificar que la cadena contiene solo dígitos.
      // ^\\d+$ asegura uno o más dígitos desde el inicio hasta el final de la cadena.
      return input.matches("^\\d+$");
   }

   /*
    * Valida si una cadena de texto tiene el formato de fecha "dd/MM/yyyy" y si es una fecha válida.
    * Utiliza SimpleDateFormat con flexibilidad estricta para asegurar la validez de la fecha.
    */
   public static boolean esFechaFormatoValido(String fechaString) {
      // Primero, valida el formato básico de la cadena con una expresión regular.
      if (!fechaString.matches("^\\d{2}\\/\\d{2}\\/\\d{4}$"))
         return false; // Si el formato no coincide, retorna false inmediatamente.

      // Crea un objeto SimpleDateFormat para el formato "dd/MM/yyyy".
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      // Establece la flexibilidad en false para que el análisis sea estricto (ej. 30/02/2023 será inválido).
      sdf.setLenient(false);
      try {
         // Intenta analizar la cadena de fecha. Si es exitoso, la fecha es válida.
         sdf.parse(fechaString);
         return true;
      } catch (ParseException e) {
         // Si ocurre una ParseException, la cadena no es una fecha válida para el formato.
         return false;
      }
   }
}

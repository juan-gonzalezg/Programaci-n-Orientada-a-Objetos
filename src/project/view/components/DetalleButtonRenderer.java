
package project.view.components;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * DetalleButtonRenderer es un renderizador de celdas de tabla para botones.
 * Permite que un JButton sea mostrado dentro de una celda de JTable,
 * aplicando estilos visuales basados en si la celda está seleccionada o no.
 */
public class DetalleButtonRenderer extends JButton implements TableCellRenderer {

   /*
    * Constructor para DetalleButtonRenderer.
    * Establece el texto del botón y lo hace opaco.
    */
   public DetalleButtonRenderer(String text) {
      setText(text);
      setOpaque(true); // Hace que el botón sea opaco para que el fondo se muestre correctamente.
   }

   /*
    * Retorna el componente usado para dibujar la celda.
    * Este método es invocado por JTable para obtener el componente que renderizará la celda.
    */
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      // Si la celda está seleccionada, aplica los colores de selección de la tabla.
      if (isSelected) {
         setForeground(table.getSelectionForeground());
         setBackground(table.getSelectionBackground());
      } else {
         // Si la celda no está seleccionada, aplica los colores predeterminados.
         setForeground(table.getForeground());
         setBackground(javax.swing.UIManager.getColor("Button.background")); // Color de fondo predeterminado del botón del UIManager.
      }
      return this; // Retorna la instancia actual del botón para que JTable la dibuje.
   }
}

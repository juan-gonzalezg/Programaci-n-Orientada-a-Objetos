
package project.view.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/*
 * DetalleButtonEditor es un editor de celdas de tabla para botones.
 * Permite que un JButton sea interactivo dentro de una celda de JTable,
 * capturando clics y notificando al editor de la tabla.
 */
public class DetalleButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
   // El botón que se mostrará y editará en la celda de la tabla.
   protected JButton button; 
   // La etiqueta o texto que se mostrará en el botón.
   private String label;     
   // Almacena los datos de la fila actual para posible uso (aunque no se usa directamente en este código).
   private Object[] rowData; 

   /*
    * Constructor para DetalleButtonEditor.
    * Inicializa el botón, lo hace opaco y añade un ActionListener para manejar los clics.
    */
   public DetalleButtonEditor(JCheckBox checkBox) {
      button = new JButton();
      button.setOpaque(true); // Hace que el botón sea opaco para que el fondo se muestre correctamente.
      button.addActionListener(this); // Añade esta clase como oyente de acciones para el botón.
   }

   /*
    * Retorna el componente que se utilizará para editar la celda.
    * Este método es invocado por JTable cuando una celda entra en modo de edición.
    */
   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      // Establece la etiqueta del botón con el valor de la celda, si no es nulo.
      this.label = (value == null) ? "" : value.toString();
      button.setText(label);

      // Almacena los datos de la fila actual.
      rowData = new Object[table.getColumnCount()];
      for (int i = 0; i < table.getColumnCount(); i++) {
         rowData[i] = table.getValueAt(row, i);
      }

      // Aplica los colores de selección o predeterminados al botón.
      if (isSelected) {
         button.setForeground(table.getSelectionForeground());
         button.setBackground(table.getSelectionBackground());
      } else {
         button.setForeground(table.getForeground());
         button.setBackground(javax.swing.UIManager.getColor("Button.background"));
      }
      return button; // Retorna el botón para que JTable lo muestre como editor.
   }

   /*
    * Retorna el valor de la celda que se está editando.
    * Este método es invocado por JTable cuando la edición de la celda finaliza.
    */
   @Override
   public Object getCellEditorValue() {
      return label; // Retorna la etiqueta del botón como el valor editado.
   }

   /*
    * Maneja los eventos de acción (clics) del botón.
    * Cuando el botón es clickeado, notifica a los oyentes que la edición ha terminado.
    */
   @Override
   public void actionPerformed(ActionEvent e) {
      // Notifica a los oyentes que la edición ha terminado.
      fireEditingStopped();
   }
}

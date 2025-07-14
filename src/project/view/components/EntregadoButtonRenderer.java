
package project.view.components;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class EntregadoButtonRenderer extends JButton implements TableCellRenderer {

   public EntregadoButtonRenderer() {
      setOpaque(true);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
      setText((value == null) ? "Entregado" : value.toString());

      if (isSelected) {
         setForeground(table.getSelectionForeground());
         setBackground(table.getSelectionBackground());
      } else {
         setForeground(table.getForeground());
         setBackground(javax.swing.UIManager.getColor("Button.background"));
      }
      return this;
   }
}

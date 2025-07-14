
package project.view.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import project.controller.RepartidorController;
import project.model.entities.Repartidor;
import project.view.RepartidorDashboardJFrame;
import javax.swing.SwingUtilities;

public class EntregadoButtonEditor extends DefaultCellEditor {
   protected JButton button;
   private String label;
   private boolean isPushed;
   private JTable table;
   private int currentRow;
   private RepartidorController repartidorController;
   private Repartidor repartidorActual;

   public EntregadoButtonEditor(JCheckBox checkBox, RepartidorController controller, Repartidor repartidorActual) {
      super(checkBox);
      this.repartidorController = controller;
      this.repartidorActual = repartidorActual;
      button = new JButton();
      button.setOpaque(true);

      button.addActionListener((ActionEvent e) -> {
         fireEditingStopped();

         String idPedido = (String) table.getModel().getValueAt(currentRow, 0);

         int confirm = JOptionPane.showConfirmDialog(button, "¿Confirmas la entrega del pedido ID: " + idPedido + "?", "Confirmar Entrega", JOptionPane.YES_NO_OPTION);
         if (confirm == JOptionPane.YES_OPTION) {
            boolean entregado = repartidorController.marcarPedidoComoEntregado(idPedido);
            if (entregado) {
               JOptionPane.showMessageDialog(button, "Pedido " + idPedido + " marcado como entregado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                 
               RepartidorDashboardJFrame parentFrame = (RepartidorDashboardJFrame) SwingUtilities.getWindowAncestor(table);
               if (parentFrame != null) {
                  parentFrame.actualizarTablaPedidos();
               }
            } else {
               JOptionPane.showMessageDialog(button, "Error al marcar como entregado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
         }
      });
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.table = table;
      this.currentRow = row;
      label = (value == null) ? "Entregado" : value.toString();
      button.setText(label);
      
      if (isSelected) {
         button.setForeground(table.getSelectionForeground());
         button.setBackground(table.getSelectionBackground());
      } else {
         button.setForeground(table.getForeground());
         button.setBackground(javax.swing.UIManager.getColor("Button.background"));
      }
      isPushed = true;
      return button;
   }
   
   @Override
   public Object getCellEditorValue() {
      if (isPushed) {
      }
      isPushed = false;
      return label;
   }

   @Override
   public boolean isCellEditable(EventObject anEvent) {
      return true;
   }

   @Override
   public boolean stopCellEditing() {
      isPushed = false;
      return super.stopCellEditing();
   }
}

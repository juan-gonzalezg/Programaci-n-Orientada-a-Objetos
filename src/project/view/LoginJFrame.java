
package project.view;

import project.controller.LoginController;
import project.controller.LoginResult;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

/*
 * La clase `LoginJFrame` representa la interfaz gráfica de usuario para la pantalla de inicio de sesión.
 * Permite a los usuarios ingresar sus credenciales (cédula y contraseña) y procesa el intento de login
 * a través de un `LoginController`. También gestiona la visualización de mensajes temporizados
 * y la navegación a diferentes paneles de control según el rol del usuario.
 */
public class LoginJFrame extends javax.swing.JFrame {
   /**
    * Instancia del controlador de login para manejar la lógica de autenticación.
    */
   private final LoginController loginController = new LoginController();
   
   /*
    * Constructor de la clase `LoginJFrame`.
    * Inicializa los componentes de la interfaz, configura los placeholders para los campos de texto
    * y establece el icono de la ventana.
    */
   public LoginJFrame() {
      initComponents();
      // Configura el texto de marcador de posición para el campo de usuario.
      configurarPlaceholder(Usuario, "Cédula de identidad");
      // Configura el texto de marcador de posición para el campo de contraseña.
      configurarPlaceholderPassword(Password, "Contraseña");
      try {
         // Intenta cargar y establecer el icono de la aplicación.
         setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Logo.png")));
      } catch (Exception e) {
         // Captura y registra cualquier error que ocurra durante la carga del icono.
         System.err.println("Error al cargar el icono: " + e.getMessage());
      }
   }

   /*
    * Configura un placeholder (texto de marcador de posición) para un campo de texto `JTextField`.
    * El texto del placeholder se muestra en gris cuando el campo está vacío y pierde el foco,
    * y desaparece cuando el campo gana el foco.
    */
   private void configurarPlaceholder(JTextField field, String placeholder) {
      field.setText(placeholder);
      field.setForeground(Color.GRAY);
      field.addFocusListener(new java.awt.event.FocusAdapter() {
         @Override
         public void focusGained(java.awt.event.FocusEvent evt) {
            // Cuando el campo gana el foco, si su texto es el placeholder, se borra y se cambia el color.
            if (field.getText().equals(placeholder)) {
               field.setText("");
               field.setForeground(Color.BLACK);
            }
         }

         @Override
         public void focusLost(java.awt.event.FocusEvent evt) {
            // Cuando el campo pierde el foco, si está vacío, se restaura el placeholder y el color.
            if (field.getText().isEmpty()) {
               field.setText(placeholder);
               field.setForeground(Color.GRAY);
            }
         }
      });
   }

   /*
    * Configura un placeholder (texto de marcador de posición) para un campo de contraseña `JPasswordField`.
    * El texto del placeholder se muestra en gris y sin caracteres ocultos cuando el campo está vacío y pierde el foco.
    * Cuando el campo gana el foco, el placeholder desaparece y los caracteres se ocultan con '*'.
    */
   private void configurarPlaceholderPassword(JPasswordField field, String placeholder) {
      field.setText(placeholder);
      field.setForeground(Color.GRAY);
      field.setEchoChar((char) 0); // Muestra el texto del placeholder sin ocultar caracteres

      field.addFocusListener(new java.awt.event.FocusAdapter() {
         @Override
         public void focusGained(java.awt.event.FocusEvent evt) {
            // Cuando el campo gana el foco, si su texto es el placeholder, se borra,
            // se cambia el color y se activa el ocultamiento de caracteres.
            if (new String(field.getPassword()).equals(placeholder)) {
               field.setText("");
               field.setForeground(Color.BLACK);
               field.setEchoChar('*'); // Oculta los caracteres ingresados
            }
         }
  
         @Override
         public void focusLost(java.awt.event.FocusEvent evt) {
            // Cuando el campo pierde el foco, si está vacío, se restaura el placeholder,
            // el color y se desactiva el ocultamiento de caracteres.
            if (field.getPassword().length == 0) {
               field.setText(placeholder);
               field.setForeground(Color.GRAY);
               field.setEchoChar((char) 0); // Muestra el texto del placeholder
            }
         }
      });
   }

   /*
    * Muestra un mensaje de error temporizado en un cuadro de diálogo `JOptionPane`.
    * El cuadro de diálogo se cierra automáticamente después de una duración especificada.
    */
   public void mostrarMensajeErrorTemporizado(Component parent, String mensaje, String titulo, int duracionMillis) {
      final JOptionPane optionPane = new JOptionPane(mensaje, JOptionPane.ERROR_MESSAGE);
      final JDialog dialog = optionPane.createDialog(parent, titulo);
      Timer timer = new Timer(duracionMillis, (ActionEvent e) -> {
         dialog.dispose(); // Cierra el diálogo al finalizar el temporizador
      });
      timer.setRepeats(false); // El temporizador se ejecuta solo una vez
      timer.start(); // Inicia el temporizador
      dialog.setVisible(true); // Hace visible el diálogo
      timer.stop(); // Detiene el temporizador (ya se habrá ejecutado si el diálogo se cerró manualmente antes)
   }

   /*
    * Muestra un mensaje informativo temporizado en un cuadro de diálogo `JOptionPane`.
    * El cuadro de diálogo se cierra automáticamente después de una duración especificada.
    */
   public void mostrarMensajeTemporizado(Component parent, String mensaje, String titulo, int duracionMillis) {
      final JOptionPane optionPane = new JOptionPane(mensaje, JOptionPane.PLAIN_MESSAGE);
      final JDialog dialog = optionPane.createDialog(parent, titulo);
      Timer timer = new Timer(duracionMillis, (ActionEvent e) -> {
         dialog.dispose(); // Cierra el diálogo al finalizar el temporizador
      });
      timer.setRepeats(false); // El temporizador se ejecuta solo una vez
      timer.start(); // Inicia el temporizador
      dialog.setVisible(true); // Hace visible el diálogo
      timer.stop(); // Detiene el temporizador
   }
   
   /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jPanel1 = new javax.swing.JPanel();
      Password = new javax.swing.JPasswordField();
      Usuario = new javax.swing.JTextField();
      jLabel1 = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      Ingresar = new javax.swing.JButton();
      jScrollPane8 = new javax.swing.JScrollPane();
      jTextArea1 = new javax.swing.JTextArea();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      jPanel1.setBackground(new java.awt.Color(255, 255, 255));

      Password.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      Password.setForeground(new java.awt.Color(0, 0, 0));
      Password.setText("jPasswordField1");

      Usuario.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
      Usuario.setForeground(new java.awt.Color(0, 0, 0));
      Usuario.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            UsuarioActionPerformed(evt);
         }
      });

      jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
      jLabel1.setForeground(new java.awt.Color(0, 0, 0));
      jLabel1.setText("Cedula de Identidad");

      jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
      jLabel2.setForeground(new java.awt.Color(0, 0, 0));
      jLabel2.setText("Contraseña");

      jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
      jLabel3.setForeground(new java.awt.Color(0, 0, 0));
      jLabel3.setText("Iniciar Sección");

      Ingresar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
      Ingresar.setForeground(new java.awt.Color(0, 0, 0));
      Ingresar.setText("Ingresar");
      Ingresar.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            IngresarActionPerformed(evt);
         }
      });

      jScrollPane8.setBorder(null);
      jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      jScrollPane8.setToolTipText("");
      jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      jScrollPane8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

      jTextArea1.setEditable(false);
      jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
      jTextArea1.setColumns(7);
      jTextArea1.setFont(new java.awt.Font("Segoe UI", 1, 125)); // NOI18N
      jTextArea1.setRows(5);
      jTextArea1.setText("Gj");
      jScrollPane8.setViewportView(jTextArea1);

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addGap(72, 72, 72)
                  .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(Ingresar))
                     .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Usuario)
                        .addComponent(Password)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))))
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addGap(85, 85, 85)
                  .addComponent(jLabel3))
               .addGroup(jPanel1Layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(79, Short.MAX_VALUE))
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addComponent(jLabel1)
            .addGap(28, 28, 28)
            .addComponent(Usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(39, 39, 39)
            .addComponent(jLabel2)
            .addGap(26, 26, 26)
            .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(43, 43, 43)
            .addComponent(Ingresar)
            .addGap(36, 36, 36))
      );

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
      );

      pack();
      setLocationRelativeTo(null);
   }// </editor-fold>//GEN-END:initComponents

   private void IngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IngresarActionPerformed
      String cedula = Usuario.getText().trim();
      String contrasena = new String(Password.getPassword());
      LoginResult resultado = loginController.procesarLogin(cedula, contrasena);
      if (!resultado.isExito()) {
         mostrarMensajeErrorTemporizado(this, resultado.getMensaje(), "Error de login", 3000);
         return;
      }
      mostrarMensajeTemporizado(this, resultado.getMensaje(), "Login correcto", 2000);
      switch (resultado.getRol().toLowerCase()) {
         case "administrador" -> new AdministradorDashboardJFrame().setVisible(true);
         case "repartidor" -> new RepartidorDashboardJFrame(resultado.getRepartidor()).setVisible(true);
         default -> mostrarMensajeErrorTemporizado(this, "Rol desconocido", "Error", 3000);
      }

      this.dispose();
   }//GEN-LAST:event_IngresarActionPerformed

   private void UsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsuarioActionPerformed
      // TODO add your handling code here:
   }//GEN-LAST:event_UsuarioActionPerformed

   /**
    * @param args the command line arguments
    */
   public static void main(String args[]) {
      /* Set the Nimbus look and feel */
      //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
      /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
      * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
      if ("Nimbus".equals(info.getName())) {
      javax.swing.UIManager.setLookAndFeel(info.getClassName());
      break;
      }
      }
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(LoginJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      //</editor-fold>

      //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(() -> {
         new LoginJFrame().setVisible(true);
      });
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton Ingresar;
   private javax.swing.JPasswordField Password;
   private javax.swing.JTextField Usuario;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JScrollPane jScrollPane8;
   private javax.swing.JTextArea jTextArea1;
   // End of variables declaration//GEN-END:variables
}

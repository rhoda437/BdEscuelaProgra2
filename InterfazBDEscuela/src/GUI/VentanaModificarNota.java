package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

/**
 * Esta clase permite la creación de una ventana que permite modificar una nota
 * ya ingresada.
 */
public class VentanaModificarNota extends JFrame {

    private JLabel porcentaje, nota, descripcion, desc;
    private JPanel panel, panelc, panels;
    private JButton b1, b2;
    private JTextField ingresar_porcentaje, ingresar_nota, ingresar_descripcion;
    private VentanaNotas anterior;
    private String[] datos;

    /**
     * Constructor.
     * @param anterior la ventana de notas.
     * @param datos los datos de la nota, separados por string.
     */
    public VentanaModificarNota(VentanaNotas anterior, String[] datos) {
        this.datos = datos;
        this.anterior = anterior;
        this.setTitle("Modificar Nota");
        desc = new JLabel("Cambie los campos para modificar la nota.");
        b1 = new JButton("Modificar");
        b2 = new JButton("Cancelar");
        panel = new JPanel(new BorderLayout());
        panelc = new JPanel(new GridLayout(3, 2));
        panels = new JPanel(new GridLayout(1, 2));

        nota = new JLabel("Nota");
        ingresar_nota = new JTextField();
        ingresar_nota.setText(datos[2]);

        porcentaje = new JLabel("Porcentaje");
        ingresar_porcentaje = new JTextField();
        ingresar_porcentaje.setText(datos[3]);

        descripcion = new JLabel("Descripcion");
        ingresar_descripcion = new JTextField();
        ingresar_descripcion.setText(datos[4]);

        //agreguemos las cosas.
        panel.add("Center", panelc);
        panel.add("South", panels);
        panel.add("North", desc);
        panels.add(b1);
        panels.add(b2);
        panelc.add(nota);
        panelc.add(ingresar_nota);
        panelc.add(porcentaje);
        panelc.add(ingresar_porcentaje);
        panelc.add(descripcion);
        panelc.add(ingresar_descripcion);
        add(panel);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                ModificarNota();
                obtener_anterior().displaynotas();
                //obtener_anterior().llenarListaNotas();
                CloseFrame();

            }
        }); //botón para confirmar la modificación de la nota.

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CloseFrame();
            }
        }); //botón para arrepentirse de lo que hiciste.
    }

    /**
     * Permite el actualizar la ventana de las notas, retornando la ventana
     * anterior para usar métodos.
     *
     * @return VentanaNotas
     */
    private VentanaNotas obtener_anterior() {
        return anterior;
    } //Actualiza.

    /**
     * Cierra la ventana, sin necesidad de un setVisible(false)
     */
    private void CloseFrame() {
        super.dispose();
    }//permite que la ventana se cierre, luego de haber modificado la nota.

    private void ModificarNota() {
        try {
            Connection conn;

            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Proyecto1", "root", "");

            //esto es para poder limitar el número que puedes colocar en las notas
            //y en el porcentaje.
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();

            System.out.println("Modificando nota:");

            //query, ejecuta la modificación, cambiando la base de datos.
            int rs = stmt.executeUpdate("UPDATE NOTA SET NOTA='" + ingresar_nota.getText() + "',PORCENTAJE='" + ingresar_porcentaje.getText()
                    + "',DESCRIPCION='" + ingresar_descripcion.getText() + "' "
                    + "WHERE ID_NOTA='" + datos[0] + "';");

            double checknota = Double.parseDouble(ingresar_nota.getText());
            double checkporc = Double.parseDouble(ingresar_porcentaje.getText());

            if (checknota < 1 || checknota > 7 || checkporc < 1 || checkporc > 100) {
                conn.rollback(); //deshace la nota.
                conn.commit(); //actualiza la base de datos.
                System.out.println("Ingresaste valores de nota o de porcentaje no validos!");
            } else {
                System.out.println("Modificaste una nota.");
            }

            conn.commit();
            stmt.close();

        } catch (Exception ex) {
            System.out.println("Error:" + ex);
        }   //en caso de no haberse agregado la nota, la consola mostrará porqué no se pudo.
    }
}

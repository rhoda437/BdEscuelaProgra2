package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import com.mysql.jdbc.Connection;

/**
 * Crea la ventana donde se muestran las notas. Separadas por asignatura.
 *
 */
public class VentanaNotas extends JFrame {

    private JList lista;
    private JButton b1, b2, b3;
    private JPanel panel, panel2;
    private JTextField tf;
    private JComboBox seleccion;
    private VentanaModificarNota vmn;
    private VentanaAgregarNota van;

    /**
     * Constructor.
     */
    public VentanaNotas() {
        this.setTitle("Ventana Alumnos");
        b1 = new JButton("Agregar");
        b2 = new JButton("Modificar");
        b3 = new JButton("Eliminar");
        lista = new JList();

        seleccion = combobox();

        panel = new JPanel(new BorderLayout());
        panel2 = new JPanel(new GridLayout(1, 3));

        panel.add("North", seleccion); //un titulo
        panel.add("Center", lista);  //la tabla
        panel.add("South", panel2); //botonera

        //botones agregados
        panel2.add(b1);
        panel2.add(b2);
        panel2.add(b3);

        add(panel);
        //llenarListaNotas();
        displaynotas();

        //Añadir evento al hacer click en el boton b1 (Agregar), abre ventana nueva
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                van = new VentanaAgregarNota(este());
                van.setVisible(true);
                van.setSize(400, 200);
                van.setLocationRelativeTo(null);
            }
        }); //si le das click a "agregar", saldrá una ventana para agregar una nota.

        //Añadir evento al hacer click en el boton b2 (Modificar), abrir ventana nueva
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                if (((String) lista.getSelectedValue()) != null) {
                    String[] datos = ((String) lista.getSelectedValue()).split(" - ");
                    vmn = new VentanaModificarNota(este(), datos);
                    vmn.setVisible(true);
                    vmn.setSize(400, 200);
                    vmn.setLocationRelativeTo(null);
                } else {
                    JOptionPane.showMessageDialog(null, "No seleccionaste nada.", "Deténgase", JOptionPane.INFORMATION_MESSAGE);
                    //este else te da un mensaje, que no seleccionaste nada para ser modificado.
                }

            }
        });

        //Añadir evento al hacer click en el boton b3 (Eliminar)
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                eliminar_nota();
                //llenarListaNotas(); //un refresh
                displaynotas();
                System.out.println("nota eliminada");

            } //esto elimina una nota, incluso de la base de datos!
        });

    }

    private VentanaNotas este() {
        return this;
    }
    

    //este método comentado lo había usado antes para desplegar las notas.
    //pensé que podría ser importante mostrarlo, en vez de borrarlo todo.
    //esto no era compatible con el ComboBox, que pensándolo bien...
    //por qué la ventana de promedios tiene una, pero el de las notas no?
    /*void llenarListaNotas(){
      
        DefaultListModel listModel = new DefaultListModel();
        try {
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Proyecto1","root","");
            
            System.out.println("llenarListaAlumnos funciona");
            
            Statement stmt = conn.createStatement() ; //permite querys.
            
            //la siguiente linea es lo que permite llenar el frame con la base de datos.
            ResultSet rs = stmt.executeQuery("select NOMBRE_ALUMNO,NOMBRE_CURSO,NOTA,PORCENTAJE,DESCRIPCION,ID_NOTA \n" +            
                    "from ALUMNO A,CURSA CU,CURSO C,NOTA N\n" +
                    "where A.N_MATRICULA=CU.N_MATRICULA AND CU.ID_CURSO=C.ID_CURSO AND C.ID_CURSO=N.ID_CURSO AND A.N_MATRICULA=N.N_MATRICULA ");
            
            //bueno, no realmente. Porque necesitas esta línea para desplegarlos.
            while (rs.next()){
                listModel.addElement(rs.getString(1)+" - "+rs.getString(2)+" - "+rs.getString(3)+" - "
                        +rs.getString(4)+" - "+rs.getString(5)+" - "+rs.getString(6));
                //6 getStrings, ya que, la tabla posee 6 atributos en total (columnas)
                //los elementos, en orden, son: nombre, asignatura, nota, porcentaje, descripcion, ID nota.
            }
            
            stmt.close() ;

            lista.setModel(listModel);
        } catch (Exception ex) {
            System.out.println("Error:" + ex);
        }//esto tambien te notifica que hubo un error, pero a diferencia del anterior,
         //hace un sout notificando el error que hubo
    
    }*/
    /**
     * Método para eliminar una nota. No necesita una ventana porque no estás
     * agregando ni modificando parámetros.
     */
    private void eliminar_nota() {

        if (((String) lista.getSelectedValue()) != null) { //en caso que seleccionste un string...

            String[] arrayCliente = ((String) lista.getSelectedValue()).split(" - ");

            try {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Proyecto1", "root", "");

                Statement stmt = conn.createStatement();

                int rs = stmt.executeUpdate("DELETE FROM NOTA WHERE ID_NOTA='" + arrayCliente[0] + "';");
                //query: Elimina la nota, en base a la ID de la nota.

                stmt.close();

            } catch (Exception ex) {
                System.out.println("Error" + ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No seleccionaste nada", "Deténgase", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * Combobox para separar las notas por asignatura
     *
     * @return combo , un combobox especial para aquello
     */
    private JComboBox combobox() {

        JComboBox combo;
        tf = new JTextField(20); //campo de texto para mostrar el texto en combobox.
        combo = new JComboBox(); //EL combobox.
        combo.addItem("Ningún Ramo"); //no seleccionaste ningún ramo.
        combo.addItem("Progra 2");
        combo.addItem("Discretas");
        combo.addItem("Ingles");

        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                tf.setText(combo.getSelectedItem().toString());
                //si no me equivoco, esto determina que seleccionaste una opcion.

                String s = combo.getSelectedItem().toString();
                //comodidad.

                System.out.println("Seleccionaste " + s);

                displaynotas();
            }
        });
        return combo;
    }

    public void displaynotas() {

        int n;

        String s = seleccion.getSelectedItem().toString();

        switch (s) {
            case "Progra 2":
                n = 1;
                break;
            case "Discretas":
                n = 2;
                break;
            case "Ingles":
                n = 3;
                break;
            default:
                n = 0;
                break;
        }

        DefaultListModel listModel = new DefaultListModel();
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Proyecto1", "root", "");
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT `ID_NOTA`, `N_MATRICULA`, `NOTA`, `PORCENTAJE`, `DESCRIPCION`, FECHA\n"
                    + "FROM NOTA\n"
                    + "WHERE ID_CURSO = '" + n + "'\n"
                    + "ORDER BY FECHA, N_MATRICULA");

            //ah! el ID_CURSO define qué curso está siendo mostrado.
            //gracias a la variable n, puedo determinar que curso se mostrará.
            while (rs.next()) {
                listModel.addElement(rs.getString(1) + " - " + rs.getString(2) + " - " + rs.getString(3) + " - " + rs.getString(4)
                        + " - " + rs.getString(5) + " - " + rs.getString(6));

            } // En orden: ID_NOTA, N_MATRICULA, NOTA, PORCENTAJE, DESCRIPCION, FECHA
            stmt.close();

            lista.setModel(listModel);
        } catch (Exception ex) {
            System.out.println("Error:" + ex);
        }

    }
}

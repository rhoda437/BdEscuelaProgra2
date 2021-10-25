package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;
import com.mysql.jdbc.Connection;

/**
 * Esta clase permite la creación de una ventana que muestra los promedios
 * Separados por asignatura.
 */
public class VentanaPromedios extends JFrame {

    private JList lista;
    private JPanel panel;
    private JComboBox seleccion;
    private JTextField tf;

    /**
     * Constructor
     */
    public VentanaPromedios() {

        this.setTitle("Ventana Promedios");
        lista = new JList();
        seleccion = combobox();

        //En los anteriores habian 2 paneles.
        //Acá no, porque no necesitamos botonera.
        panel = new JPanel(new BorderLayout());

        //agregamos cosas.
        panel.add("North", seleccion);
        panel.add("Center", lista);

        add(panel);

    }

    //metodo para mostrar los promedios en el panel.
    public void displaypromedios() {

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

            //llena el frame con los promedios!
            //saldrán números tales como 0.5 porque asumo que los promedios no están listos.
            //si tuviesemos notas suficientes para hacer un 100%, calcularía un promedio bien.
            ResultSet rs = stmt.executeQuery("SELECT NOTA.N_MATRICULA, NOTA.ID_CURSO,"
                    + " TRUNCATE( Sum(NOTA.nota*NOTA.porcentaje/100),1) AS PROMS \n"
                    + "from NOTA\n"
                    + "WHERE ID_CURSO= '" + n + "'\n"
                    + "GROUP BY NOTA.N_MATRICULA, NOTA.ID_CURSO");

            //ah! el ID_CURSO define qué curso está siendo mostrado.
            //gracias a la variable n, puedo determinar que curso se mostrará.
            while (rs.next()) {
                listModel.addElement(rs.getString(1) + " - " + rs.getString(2) + " - " + rs.getString(3));

            } // En orden: N_MATRICULA, ID_CURSO, PROMS
            stmt.close();

            lista.setModel(listModel);
        } catch (Exception ex) {
            System.out.println("Error:" + ex);
        }

    }

    /**
     * Crea un combobox especial para la interfaz.
     *
     * @return combo , un Combobox que permite separar las asignaturas.
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

                displaypromedios();

                //por qué un else, si no hay otras opciones?
            }
        });
        return combo;
    }
}

package GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/**
 * Esta clase permite la creación de una ventana que permite agregar una nota.
 */
public class VentanaAgregarNota extends JFrame {

    private JLabel id_curso, nmatricula, nota, relleno, porcentaje, desc, fecha;
    private JPanel panel, panelc, panels;
    private JButton b1, b2;
    private JTextField ingresar_id_curso, ingresar_nmatricula, ingresar_nota,
            ingresar_porcentaje, ingresar_descripcion, ingresar_fecha;
    private VentanaNotas anterior;

    /**
     * Constructor.
     *
     * @param anterior Llama a la ventana anterior para poder ejecutar metodos.
     */
    public VentanaAgregarNota(VentanaNotas anterior) {
        this.anterior = anterior;
        this.setTitle("Agregar Nota");
        relleno = new JLabel("Rellene los campos para insertar una nueva nota");
        b1 = new JButton("Insertar");
        b2 = new JButton("Cancelar");
        panel = new JPanel(new BorderLayout());
        panelc = new JPanel(new GridLayout(3, 2)); //panel con los campos.
        panels = new JPanel(new GridLayout(1, 2)); //botonera

        //"numero matricula"
        nmatricula = new JLabel("Matricula");
        ingresar_nmatricula = new JTextField();

        //"id curso"
        id_curso = new JLabel("ID Curso");
        ingresar_id_curso = new JTextField();

        //"fecha"
        fecha = new JLabel("Fecha");
        ingresar_fecha = new JTextField();

        //"nota"
        nota = new JLabel("Nota");
        ingresar_nota = new JTextField();

        //"porcentaje"
        porcentaje = new JLabel("Porcentaje");
        ingresar_porcentaje = new JTextField();

        //"descripcion"
        desc = new JLabel("Descripcion");
        ingresar_descripcion = new JTextField();

        //permite la ubicacion de los paneles.
        panel.add("Center", panelc);
        panel.add("South", panels);
        panel.add("North", relleno);
        panels.add(b1);
        panels.add(b2);

        //agrega "Numero matricula"
        panelc.add(nmatricula);
        panelc.add(ingresar_nmatricula);

        //agrega "id curso"
        panelc.add(id_curso);
        panelc.add(ingresar_id_curso);

        //agrega "fecha"
        panelc.add(fecha);
        panelc.add(ingresar_fecha);

        //agrega "nota"
        panelc.add(nota);
        panelc.add(ingresar_nota);

        //agrega "porcentaje"
        panelc.add(porcentaje);
        panelc.add(ingresar_porcentaje);

        //agrega "descripcion"
        panelc.add(desc);
        panelc.add(ingresar_descripcion);

        //agrega al panel
        add(panel);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                InsertarNota();
                obtener_anterior().displaynotas();
                //obtener_anterior().llenarListaNotas();
                CloseFrame();

            }
        }); //botón para confirmar el agregar una nota.

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                CloseFrame();
            }
        }); //el botón para retractarse de lo que querías hacer.
    }

    /**
     * Permite el actualizar la ventana de las notas, retornando la ventana
     * anterior para usar métodos.
     *
     * @return VentanaNotas
     */
    private VentanaNotas obtener_anterior() {
        return anterior;
    }

    /**
     * Cierra la ventana, sin necesidad de un setVisible(false)
     */
    private void CloseFrame() {
        super.dispose();
    }

    /**
     * Permite insertar la nota, mediante un query.
     */
    private void InsertarNota() {

        //esto se ejecuta al hacerle click al botón que agrega la nota.
        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Proyecto1", "root", "");

            //se necesita esto para poder limitar el número que puedes colocar en las notas
            //y en el porcentaje.
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();

            System.out.println("Agregando nota:");

            //Query de SQL para insertar una nota.
            int rs = stmt.executeUpdate("INSERT INTO NOTA (N_MATRICULA, ID_CURSO, FECHA, NOTA, PORCENTAJE, DESCRIPCION)"
                    + "VALUES ('" + ingresar_nmatricula.getText() + "','" + ingresar_id_curso.getText() + "','"
                    + ingresar_fecha.getText() + "','" + ingresar_nota.getText() + "','" + ingresar_porcentaje.getText() + "','"
                    + ingresar_descripcion.getText() + "');");

            double checknota = Double.parseDouble(ingresar_nota.getText());
            double checkporc = Double.parseDouble(ingresar_porcentaje.getText());

            if (checknota < 1 || checknota > 7 || checkporc < 1 || checkporc > 100) {
                conn.rollback(); //deshace la nota.
                conn.commit(); //actualiza la base de datos.
                System.out.println("Ingresaste valores de nota o de porcentaje no validos!");
            } else {
                System.out.println("Nota agregada.");
            }

            //por qué anoté solamente casos para las notas y el porcentaje?
            //porque para los otros casos no es necesario.
            //La fecha siempre será formato Año-Mes-Dia
            //Sin aceptar fechas como el 31 de febrero.
            //No puedo colocar un N_matricula negativo o mayor a de los que tengo.
            //El catch tirará el error de que esto no es válido.
            //y la nota no se registrará
            //lo mismo con ID curso.
            conn.commit(); //permite la ejecución del query, por haber quitado el autoCommit
            stmt.close(); //terminamos con las ejeciciones.

        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        } //en caso de no haberse agregado la nota, la consola mostrará porqué no se pudo.
    }

}

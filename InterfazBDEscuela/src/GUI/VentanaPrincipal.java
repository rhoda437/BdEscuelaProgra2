package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Es la ventana que permite la selección entre ver las notas, o los promedios,
 * dependiendo de la asignatura.
 */
public class VentanaPrincipal extends JFrame {

    //declaraciones
    private JButton b1, b2;
    private JPanel panel;
    private VentanaNotas va;
    private VentanaPromedios vp;

    /**
     * Constructor.
     */
    public VentanaPrincipal() {

        this.setTitle("Ventana Principal"); //titulo
        b1 = new JButton("Alumnos y sus notas"); //seccion 1
        b2 = new JButton("Promedios"); //seccion 2
        panel = new JPanel();
        panel.setLayout(new GridLayout());//con esto, los botones se expanden.

        //agregando botones.
        panel.add(b1);
        panel.add(b2);

        add(panel);

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                va = new VentanaNotas();
                va.setVisible(true);
                va.setSize(600, 400);
                va.setLocationRelativeTo(null);
            }
        }); // si presionas el botón izquierdo, verás una nueva ventana
        //con los alumnos, y sus notas.

        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                vp = new VentanaPromedios();
                vp.setVisible(true);
                vp.setSize(600, 400);
                vp.setLocationRelativeTo(null);

            }
        }); //si presionas el botón derecho, verás una nueva ventana
        //con los promedios.
    }
}

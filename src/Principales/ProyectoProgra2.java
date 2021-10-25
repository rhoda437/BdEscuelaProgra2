package Principales;

import GUI.VentanaPrincipal;
import javax.swing.JFrame;

/**
 * Main.
 */
public class ProyectoProgra2 {

    public static void main(String[] args) {
        VentanaPrincipal vp = new VentanaPrincipal(); //crea ventana
        vp.setVisible(true); //visibilidad
        vp.setSize(800, 600); //tamaño
        vp.setLocationRelativeTo(null); //centrado
        vp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //permite cerrar.
    }
}

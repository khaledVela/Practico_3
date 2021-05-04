import Visuales.Visual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.swing.*;

public class Ejecutador2 {
    private final static Logger logger = (Logger) LogManager.getRootLogger();
    public static void main(String[] args) {
        int x= Integer.parseInt(JOptionPane.showInputDialog("Ingresa el ancho de la imagen"));
        int y= Integer.parseInt(JOptionPane.showInputDialog("Ingresa el alto de la imagen"));
        Visual visu = new Visual("Persona_2",1100,x,y);
        visu.setVisible(true);
        logger.debug("Corro el programa 2");
    }
}

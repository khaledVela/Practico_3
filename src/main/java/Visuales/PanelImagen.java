package Visuales;

import Imagenes.Imagen;
import Imagenes.Pintar;
import Red.Enviar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class PanelImagen extends JPanel implements PropertyChangeListener, MouseListener {
    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private static final long serialVersionUID = 1L;
    private Imagen imagen;
    private int x, y;
    private Enviar enviar;

    public PanelImagen(Imagen img,int x,int y) {
        imagen = img;
        this.x=x;
        this.y=y;
        addMouseListener(this);
    }
    public void setEnviar(Enviar enviar) {
        this.enviar = enviar;
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            imagen.dibujar(g);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        logger.debug("puntos " + e.getX() + "-" + e.getY());
        if(enviar != null){
            enviar.setPosX(x);
            enviar.setPosY(y);
            logger.debug(enviar.getPosX()+" x");
            logger.debug(enviar.getPosY()+" y");
            imagen.cambioOk();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
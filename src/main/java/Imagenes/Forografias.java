package Imagenes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Forografias extends Transformacion {
    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private BufferedImage foto = null;
    private File imagen;

    public Forografias(Imagen foto) {
        imagenBase = foto;
    }

    public File rutas() {
        JFileChooser selector = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("jpg , png , gif", "jpg", "png", "gif");
        selector.setDialogTitle("Escoge una imagen...");
        selector.setFileFilter(filtro);
        int estado = selector.showOpenDialog(null);
        File archivoelegido = selector.getSelectedFile();
        String ruta = archivoelegido.getPath();
        if (archivoelegido.exists())
            logger.debug("bien");
        else
            logger.debug("no bien");
        if (estado == JFileChooser.APPROVE_OPTION) ;
        {
            imagen = new File(ruta);
        }
        return imagen;
    }

    @Override
    public void hacer() {
        int ancho;
        int color;
        try {
            foto = ImageIO.read(rutas());
            ancho = foto.getWidth();
            if ((foto.getWidth() == imagenBase.getAncho()) && (foto.getHeight() == imagenBase.getAlto())) {
                for (int i = 0; i < foto.getHeight(); i++) {
                    for (int j = 0; j < ancho; j++) {
                        color = foto.getRGB(j, i);

                        imagenBase.setPixel(color, j, i);
                    }
                }
            }
            else{
JOptionPane.showMessageDialog(null, "La imagen ingresada no es la misma al tamaño colocado ");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        imagenBase.cambioOk();
    }
}

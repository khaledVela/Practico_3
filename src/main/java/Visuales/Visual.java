package Visuales;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

import Imagenes.Forografias;
import Imagenes.Imagen;
import Red.Enviar;
import Red.Recibir;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Visual extends JFrame {
    private static final long serialVersionUID = 1L;
    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private Imagen modelo;
    private Enviar conexion;
    private int x, y;
    private PanelImagen panel;

    public Visual(String a, int c, int x, int y) {
        super(a);
        setLocation(c, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.x = x;
        this.y = y;
        init();
    }

    public void init() {
        logger.debug("Creo menu");
        JMenuBar menuBar = new JMenuBar();
        JMenu mnuImagen = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Cargar imagen");
        JMenuItem item2 = new JMenuItem("Servidor");
        JMenuItem item3 = new JMenuItem("Cliente");
        modelo = new Imagen(x, y);
        panel = new PanelImagen(modelo,x,y);
        modelo.addObserver(panel);
        añadirObservador();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Forografias fo = new Forografias(modelo);
                fo.hacer();
            }
        });
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                servid();
            }
        });
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client();
            }
        });
        mnuImagen.add(item);
        mnuImagen.add(item2);
        mnuImagen.add(item3);
        menuBar.add(mnuImagen);
        this.setJMenuBar(menuBar);
        this.pack();
    }

    public void client() {
        String ip = JOptionPane.showInputDialog("Coloque la ip");
        int puerto = Integer.parseInt(JOptionPane.showInputDialog(null, "Coloque el puerto"));
        int leerPuerto = leerPuerto(puerto);
        if (leerPuerto == 0) {
            JOptionPane.showMessageDialog(this, "El puerto debe ser un entero mas de 1024");
            return;
        }
        String direccion = leerIP(ip);
        logger.debug("Cliente valida ok");
        if (direccion.equals("ERROR")) {
            JOptionPane.showMessageDialog(this, "La ip deben ser 4 numeros enteros menores a 255");
            return;
        }

        try {
            conexion = new Enviar(ip, leerPuerto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor con la ip " + ip + " con el puerto " + leerPuerto);
            return;
        }

        Recibir recibir = null;
        try {
            recibir = new Recibir(conexion.getSck(), modelo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al crear el objeto para recibir, trate de nuevo");
            return;
        }

        try {
            conexion = new Enviar(recibir.getClt());
            panel.setEnviar(conexion);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Hubo un error al crear el objeto para enviar, trate de nuevo");
            return;
        }

        Thread recibiendo = new Thread(recibir);
        recibiendo.start();
        añadirObservador();
    }


    private void servid() {
        int puerto = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el puerto"));
        if (leerPuerto(puerto) != 0) {
            int port = puerto;
            try {
                if (port <= 0|| port > 65000) {
                    throw new Exception("Debe colocar un entero");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Debe colocar un número entero positivo mayor a 1024, intente de nuevo por favor");
                return;
            }
            logger.debug("Server valida ok");
            Recibir recibir;
            try {
                recibir = new Recibir(port, modelo);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Hubo un error al crear/esperar la conexión, trate con otro puerto");
                return;
            }

            try {
                conexion = new Enviar(recibir.getClt());
                panel.setEnviar(conexion);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Hubo un error al crear el objeto para enviar, trate de nuevo");
                return;
            }

            Thread recibiendo = new Thread(recibir);
            recibiendo.start();
            añadirObservador();
        }
    }

    private String leerIP(String ip) {
        String[] numeros = ip.split("\\.");
        StringBuilder respuesta = new StringBuilder();
        if (numeros.length != 4)
            return "ERROR";
        for (String unNumero : numeros) {
            int n = Integer.parseInt(unNumero);
            try {
                if (n < 0 || n > 255)
                    return "ERROR";
            } catch (Exception e) {
                return "ERROR";
            }

            respuesta.append("." + n);
        }
        return respuesta.substring(1);
    }

    private int leerPuerto(int puerto) {
        int n = puerto;
        try {
            if (n < 1 || n > 65535)
                return 0;
        } catch (Exception e) {
            return 0;
        }
        return n;
    }

    private void añadirObservador() {
        modelo.addObserver(conexion);
    }


}

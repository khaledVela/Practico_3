package Red;

import Imagenes.Imagen;
import Imagenes.Pintar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Recibir implements Runnable {
    private Imagen imagen;
    private Socket clt;
    private BufferedReader in;
    private final static Logger logger = (Logger) LogManager.getRootLogger();

    public Recibir(int port, Imagen imagen) throws IOException {
        ServerSocket srv = new ServerSocket(port);
        clt = srv.accept();
        in = new BufferedReader(new InputStreamReader(clt.getInputStream()));
        this.imagen = imagen;
    }

    public Recibir(Socket sck, Imagen imagen) throws IOException {
        logger.debug("Cliente on");
        clt = sck;
        in = new BufferedReader(new InputStreamReader(clt.getInputStream()));
        this.imagen = imagen;
    }

    public Socket getClt() {
        return clt;
    }

    @Override
    public void run() {
        try {
            Pintar pinta;
            while (!clt.isClosed()) {
                String coordenadas = in.readLine();
                String[] posiciones = coordenadas.split("\\|");
                int x = Integer.parseInt(posiciones[0]);
                int y = Integer.parseInt(posiciones[1]);
                pinta = new Pintar(imagen, x, y);
                pinta.hacer();
            }
        } catch (IOException e) {
        }
    }
}


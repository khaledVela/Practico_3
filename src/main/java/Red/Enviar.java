package Red;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class Enviar implements PropertyChangeListener {
    private Socket sck;
    private PrintWriter out;
    private int posX;
    private int posY;
    private boolean check1;
    private boolean check2;
    private final static Logger logger = (Logger) LogManager.getRootLogger();

    public Enviar(String ip, int puerto) throws IOException {
        sck = new Socket(ip, puerto);
        out = new PrintWriter(new java.io.OutputStreamWriter(sck.getOutputStream()));
    }

    public Enviar(Socket sck) throws IOException {
        logger.debug("Servidor on");
        this.sck = sck;
        out = new PrintWriter(new java.io.OutputStreamWriter(sck.getOutputStream()));
    }

    public void setPosX(int posX) {
        this.posX = posX;
        check1 = true;
    }

    public void setPosY(int posY) {
        this.posY = posY;
        check2 = true;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (check1 && check2) {
            out.println(posX + "|" + posY);
            out.flush();
            check1 = false;
            check2 = false;
        }
    }

    public Socket getSck() {
        return sck;
    }

}

package visualizer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
 
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
 
    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }
 
    public void run() {
        running = true;
 
        while (running) 
        {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try 
            {
                socket.receive(packet);
            } catch (IOException ex)
            {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Visualizer.getData(packet.getData());
            
            for (int i = 0; i < buf.length; i++)
            {
                buf[i] = 0;
            }

        }
        socket.close();
    }
    
    public void stopServer()
    {
        running = false;
    }
}

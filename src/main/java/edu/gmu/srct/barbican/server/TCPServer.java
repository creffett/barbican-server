package edu.gmu.srct.barbican.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mgauto on 10/25/14.
 */
public class TCPServer{
    private Server server;
    private final short PORT = 1337;
    private boolean running = true;

    public TCPServer(Server server) {
        this.server = server;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while(running) {
            Socket socket = serverSocket.accept();
            new Thread(new TCPServerWorker(server, socket));
        }
    }
}
class TCPServerWorker implements Runnable {
    private Server server;
    private Socket socket;

    TCPServerWorker(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}

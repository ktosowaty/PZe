package tytan.serwer;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    static int threadsNumber;
    private static int portNumber;
    private static Server server;
    private ExecutorService clientsThreads;
    private ServerSocket ServerSocket;
    private ServerSocketFactory ServerSocketfactory;

    private Server() throws IOException {

        portNumber = 8000;
        threadsNumber = 30;
        clientsThreads = Executors.newFixedThreadPool(threadsNumber);
        ServerSocketfactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
        ServerSocket = (ServerSocket) ServerSocketfactory.createServerSocket(portNumber);
    }

    public static Server getInstance() throws IOException {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void runServer() throws Exception {
        while (true) {
            LOGGER.info("Waiting for user connection...");
            Socket incommingConnection = (Socket) ServerSocket.accept();
            try {
                clientsThreads.submit(new ConnectionHandler(incommingConnection));
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.info(ex.getMessage());
            }
            LOGGER.info("Current users number " + ConnectionHandler.getUsersCount().get());
        }
    }
}

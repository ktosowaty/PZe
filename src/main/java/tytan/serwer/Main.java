package tytan.serwer;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = Server.getInstance();
            server.runServer();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

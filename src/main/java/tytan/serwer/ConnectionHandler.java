package tytan.serwer;

import tytan.serwer.beans.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static Map<String, ObjectOutputStream> usersOutputMap = Collections
            .synchronizedMap(new HashMap<String, ObjectOutputStream>());
    private static AtomicInteger usersCount = new AtomicInteger();
    private ObjectInputStream inputObject;
    private ObjectOutputStream outputObject;
    private Socket clientSocket;
    private String userNick;

    public ConnectionHandler(Socket socket) throws Exception {
        this.clientSocket = socket;
        if (ConnectionHandler.getUsersCount().get() > Server.threadsNumber - 1) {
            socket.close();
            throw new Exception("Server reached maximum users number");
        }

        inputObject = new ObjectInputStream(socket.getInputStream());
        outputObject = new ObjectOutputStream(socket.getOutputStream());
        usersCount.set(usersCount.get() + 1);
    }

    public static AtomicInteger getUsersCount() {
        return usersCount;
    }

    public void run() {
        try {
            setConnectionParameters();
            messageListenerAndSender();

        } catch (Exception ex) {
            removeUserInfo(getUserNick());
        } finally {
            closeClientStreams();
            removeUserInfo(getUserNick());
            usersCount.set(usersCount.get() - 1);
        }
        LOGGER.info(getUserNick() + " has left server, current users count " + usersCount.get());
    }

    private void setConnectionParameters() throws ClassNotFoundException, IOException {

        boolean isRegistered = true;
        LOGGER.info("Setting connection parametrs");
        do {

            Message registrationMessage = (Message) inputObject.readObject();
            LOGGER.info("Registered new user " + registrationMessage.getNickFrom());
            setUserNick(registrationMessage.getNickFrom());

            isRegistered = loginUser(registrationMessage.getNickFrom(), outputObject);
            if (isRegistered == false)
                outputObject.writeObject(new Message("empty", "empty", "wrongNick"));

            outputObject.flush();

        } while (isRegistered == false);

    }

    private void messageListenerAndSender() throws ClassNotFoundException, IOException {
        LOGGER.info("Waiting for client data...");
        do {
            Message clientMessageObject = (Message) inputObject.readObject();
            LOGGER.info("Received new message from " +clientMessageObject.getNickFrom());
            String receiptNick = clientMessageObject.getNickTo();
            ObjectOutputStream recipientObjectOutputStream = usersOutputMap.get(receiptNick);
            try {
                if (receiptNick.equals("broadcast") || clientMessageObject.getNickFrom().equals("broadcast")) {
                    for (Map.Entry<String, ObjectOutputStream> userStream : usersOutputMap.entrySet()) {
                        if (!userStream.getKey().equals(clientMessageObject.getNickFrom())) {
                            userStream.getValue().writeObject(clientMessageObject);
                        }
                    }
                } else if (recipientObjectOutputStream != null) {
                    recipientObjectOutputStream.writeObject(clientMessageObject);
                    recipientObjectOutputStream.flush();
                }
            } catch (IOException e) {
                LOGGER.warning("Exception occured");
            }
        } while (true);
    }

    public boolean loginUser(String userNick, ObjectOutputStream outputObject) throws IOException {

        if (usersOutputMap.containsKey(userNick)) {
            LOGGER.info("Wrong username");
            return false;
        } else {
            LOGGER.info("Sending info about available users");
            for (Map.Entry<String, ObjectOutputStream> entry : usersOutputMap.entrySet()) {

                String nick = entry.getKey();

                outputObject.writeObject(new Message("addNewUser", nick, "empty"));
                outputObject.flush();
            }

            usersOutputMap.put(userNick, outputObject);
            LOGGER.info("Sending new user to others");
            for (Map.Entry<String, ObjectOutputStream> entry : usersOutputMap.entrySet())
                if (!entry.getKey().equals(userNick)) {
                    entry.getValue().writeObject(new Message("addNewUser", userNick, "empty"));
                    entry.getValue().flush();
                }
        }
        return true;
    }

    private void closeClientStreams() {
        try {
            inputObject.close();
            outputObject.close();
            clientSocket.close();
        } catch (Exception ex) {
            LOGGER.warning("Error occured");
        }
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public void removeUserInfo(String userNick) {
        for (Map.Entry<String, ObjectOutputStream> entry : usersOutputMap.entrySet())
            if (!entry.getKey().equals(userNick)) {
                try {
                    entry.getValue().writeObject(new Message(entry.getKey(), userNick, "removeUserFromList"));
                    entry.getValue().flush();
                } catch (IOException e) {
                    LOGGER.warning("Error occured");
                }
            }
        usersOutputMap.remove(userNick);
    }

    public void remove(String removeUserNick) {
        usersOutputMap.remove(removeUserNick);
    }

}

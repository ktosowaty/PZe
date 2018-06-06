package tytan.data.rdp.specification.web_socket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
//TODO NEEDS REFACTOR
@ClientEndpoint
public class ClientsListEndpoint {
    Session userSession = null;
    private MessageHandler messageHandler;

    public ClientsListEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider
                    .getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("onOpen");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("onClose");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("onMessage");
        if (this.messageHandler != null)
            this.messageHandler.handleMessage(message);
    }

    public void sendMessage(String message) {
//        String bytesString = message.array();
        this.userSession.getAsyncRemote().sendText(message);
    }


    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }
}

package tytan.data.rdp.specification.web_socket;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.json.JSONObject;
import tytan.config.Config;
import tytan.data.rdp.entity.Client;
import tytan.data.rdp.mapper.ClientsListResponseMapper;

import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ConnectAndGetListOfClientsSpecification implements WebSocketSpecification {
    @Override
    public Observable<String> getResults() {
        return Observable.create(observableEmitter -> {
            String url = Config.getInstance().getApiUrl() + "/operator";
            ConnectAndGetListOfClientsSpecification.ClientsListEndpoint clientsListEndpoint = null;
            try {
                clientsListEndpoint = new ConnectAndGetListOfClientsSpecification.ClientsListEndpoint(new URI(url));
                clientsListEndpoint.addMessageHandler(new MessageHandler() {
                    @Override
                    public void handleMessage(String message) {
                        if (!observableEmitter.isDisposed()) {
                            observableEmitter.onNext(message);
                        }
                    }
                });
            } catch (URISyntaxException e) {
                observableEmitter.onError(e);
            }
        });
    }

    @ClientEndpoint
    public static class ClientsListEndpoint {
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

        public void addMessageHandler(MessageHandler msgHandler) {
            this.messageHandler = msgHandler;
        }
    }
}

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
//TODO NEEDS REFACTOR
public class ConnectAndGetListOfClientsSpecification implements WebSocketSpecification {
    @Override
    public Observable<String> getResults() {
        return Observable.create(observableEmitter -> {
            String url = Config.getInstance().getApiUrl() + "/operator";
            ClientsListEndpoint clientsListEndpoint = null;
            try {
                clientsListEndpoint = new ClientsListEndpoint(new URI(url));
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


}

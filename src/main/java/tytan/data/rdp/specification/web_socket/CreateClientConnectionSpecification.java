package tytan.data.rdp.specification.web_socket;

import io.reactivex.Observable;
import org.json.JSONObject;
import tytan.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
//TODO NEEDS REFACTOR
public class CreateClientConnectionSpecification implements WebSocketSpecification {
    @Override
    public Observable<String> getResults() {
        return Observable.create(observableEmitter -> {
            String url = Config.getInstance().getApiUrl() + "/clients";
            ClientsListEndpoint clientsListEndpoint = new ClientsListEndpoint(new URI(url));
            clientsListEndpoint.addMessageHandler(new MessageHandler() {
                @Override
                public void handleMessage(String message) {
                    JSONObject messageJO = new JSONObject(message);
                    String clientId = messageJO.getString("clientId");
                    observableEmitter.onNext(clientId);
                }
            });
        });
    }


}

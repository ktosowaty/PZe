package tytan.data.rdp.specification.web_socket;

import io.reactivex.Completable;
import io.reactivex.Observable;
import tytan.config.Config;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
//TODO NEEDS REFACTOR
public class StreamVideoSpecification implements StreamWebSocketSpecification {

    private ByteBuffer byteBuffer;
    private String clientId;
    ClientsListEndpoint clientsListEndpoint;

    public StreamVideoSpecification(String clientId) {
        this.clientId = clientId;
        String url = Config.getInstance().getApiUrl() + "/clients/list/" + clientId;
        try {
            clientsListEndpoint = new ClientsListEndpoint(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Observable<String> getResults() {
        return Observable.create(observableEmitter -> {


        });
    }

    @Override
    public Completable establishConnection() {
        return null;
    }

    @Override
    public void sendMessage(ByteBuffer byteBuffer) {
        clientsListEndpoint.sendMessage(byteBuffer);
        System.out.println("Sending bytes to server.");
    }
}

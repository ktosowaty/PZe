package tytan.data.rdp.repository.web_socket;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import org.json.JSONObject;
import tytan.data.rdp.entity.Client;
import tytan.data.rdp.mapper.ClientsListResponseMapper;
import tytan.data.rdp.repository.Repository;
import tytan.data.rdp.specification.Specification;
import tytan.data.rdp.specification.web_socket.ConnectAndGetListOfClientsSpecification;
import tytan.data.rdp.specification.web_socket.CreateClientConnectionSpecification;
import tytan.data.rdp.specification.web_socket.WebSocketSpecification;

import java.util.ArrayList;
//TODO NEEDS REFACTOR
public class ClientWebSocketRepository implements Repository<Client> {

    @Override
    public Observable<ArrayList<Client>> streamQuery(Specification specification) {
        if (specification instanceof ConnectAndGetListOfClientsSpecification) {
            return ((ConnectAndGetListOfClientsSpecification) specification).getResults().map(response -> {
                ClientsListResponseMapper mapper = new ClientsListResponseMapper();
                return mapper.mapResponseToClientsList(new JSONObject(response));
            });
        } else if (specification instanceof CreateClientConnectionSpecification) {
            return ((CreateClientConnectionSpecification) specification).getResults().map(response -> new ArrayList<>());
        } else {
            return Observable.fromArray();
        }
    }
}

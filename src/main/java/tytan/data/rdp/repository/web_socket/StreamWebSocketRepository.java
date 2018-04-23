package tytan.data.rdp.repository.web_socket;

import io.reactivex.Observable;
import tytan.data.rdp.repository.Repository;
import tytan.data.rdp.specification.Specification;
import tytan.data.rdp.specification.web_socket.StreamVideoSpecification;

import java.util.ArrayList;
//TODO NEEDS REFACTOR
public class StreamWebSocketRepository implements Repository<String> {
    @Override
    public Observable<ArrayList<String>> streamQuery(Specification specification) {
        return ((StreamVideoSpecification) specification).getResults().map(response -> {
            ArrayList<String> strings = new ArrayList<>();
            strings.add(response);
            return strings;
        });
    }
}

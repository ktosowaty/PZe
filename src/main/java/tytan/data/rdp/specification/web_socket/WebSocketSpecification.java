package tytan.data.rdp.specification.web_socket;

import io.reactivex.Observable;
import tytan.data.rdp.specification.Specification;

import java.util.ArrayList;

public interface WebSocketSpecification extends Specification {

    Observable<String> getResults();

}

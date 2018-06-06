package tytan.data.rdp.specification.web_socket;

import io.reactivex.Completable;
import io.reactivex.Observable;
import tytan.data.rdp.specification.Specification;

import java.nio.ByteBuffer;
//TODO NEEDS REFACTOR
public interface StreamWebSocketSpecification extends Specification {


    Completable establishConnection();
    void sendMessage(String stream);

}

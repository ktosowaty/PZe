package tytan.data.rdp.repository;


import io.reactivex.Observable;
import tytan.data.rdp.specification.Specification;

import java.util.ArrayList;

public interface Repository<T> {

    Observable<ArrayList<T>> streamQuery(Specification specification);

}

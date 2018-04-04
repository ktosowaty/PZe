package tytan.client.test;

import tytan.client.controller.AbstractController;
import tytan.client.model.AbstractModel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

public class MockController extends AbstractController {

    public List<String> propertyList;

    public MockController() {
        propertyList = new ArrayList<>();
    }

    @Override
    public void addModel(String name, AbstractModel model) {
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        propertyList.add(evt.getPropertyName());

    }

}

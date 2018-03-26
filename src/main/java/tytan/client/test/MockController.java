package tytan.client.test;

import java.beans.PropertyChangeEvent;

import client.controller.AbstractController;
import client.model.AbstractModel;

public class MockController extends AbstractController {

	@Override
	public void addModel(String name, AbstractModel model) {
		model.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getPropertyName());

	}

}

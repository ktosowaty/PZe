package tytan.client.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import client.model.AbstractModel;

public abstract class AbstractController implements PropertyChangeListener {

	public abstract void addModel(String name, AbstractModel model);
	
	@Override
	public abstract void propertyChange(PropertyChangeEvent arg0);

}

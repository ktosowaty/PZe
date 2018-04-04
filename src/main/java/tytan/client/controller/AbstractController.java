package tytan.client.controller;

import tytan.client.model.AbstractModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class AbstractController implements PropertyChangeListener {

    public abstract void addModel(String name, AbstractModel model);

    @Override
    public abstract void propertyChange(PropertyChangeEvent arg0);

}

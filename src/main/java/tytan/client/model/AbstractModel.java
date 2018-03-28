package tytan.client.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {

	private PropertyChangeSupport propertyChangeSupport;

	public AbstractModel() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	public void firePropertyChange(String notificationName, Object oldValue, Object newValue){
		propertyChangeSupport.firePropertyChange(notificationName, oldValue, newValue);
	}
}

package tytan.data.rdp.entity;

import javafx.beans.property.*;
import javafx.scene.image.Image;

public class Client {
    private StringProperty name;
    private IntegerProperty id;
    private LongProperty connectedFrom;


    public Client(String name, int id, long connectedFrom) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty(id);
        this.connectedFrom = new SimpleLongProperty(connectedFrom);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public long getConnectedFrom() {
        return connectedFrom.get();
    }

    public LongProperty connectedFromProperty() {
        return connectedFrom;
    }

    public void setConnectedFrom(long connectedFrom) {
        this.connectedFrom.set(connectedFrom);
    }
}

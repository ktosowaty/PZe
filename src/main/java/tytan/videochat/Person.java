package tytan.videochat;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Person {
    private StringProperty name;
    private ObjectProperty<Image> icon;

    public Person(String name) {
        this.name = new SimpleStringProperty(name);
        this.icon = new SimpleObjectProperty<>(new Image(String.valueOf(getClass().getResource("/images/user_40.png"))));
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

    public Image getIcon() {
        return icon.get();
    }

    public void setIcon(Image icon) {
        this.icon.set(icon);
    }

    public ObjectProperty<Image> iconProperty() {
        return icon;
    }
}

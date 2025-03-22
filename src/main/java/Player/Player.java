package Player;

import Element.ActiveElement.Desert;
import Element.Element;

public class Player {
    private Element position;
    private final String name;

    Player(String name) {
        this.name = name;
        this.position = null;
    }

    public Element getPosition() {
        return this.position;
    }

    public void setPosition(Element position) throws IllegalArgumentException {
        if(position == null) throw new IllegalArgumentException();
        if(position instanceof Desert) throw new IllegalArgumentException();
        else this.position = position;
    }

    public String getName() {
        return this.name;
    }
}
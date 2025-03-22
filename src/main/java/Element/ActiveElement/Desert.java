package Element.ActiveElement;

import Element.Element;
import Element.Pipe;

import java.util.*;

public class Desert implements ActiveElement {
    private int water;
    private final String name;

    public Desert() {
        this.name = "Desert-" + UUID.randomUUID();
    }

    public int getWater() {
        return water;
    }

    @Override
    public void flow(Pipe source) throws IllegalArgumentException {
        if(source == null) throw new IllegalArgumentException();
        if(source.isFull()) {
            water++;
            source.empty();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Element> getNeighbors() {
        return new LinkedList<>();
    }

    @Override
    public void connectOutput(Pipe output) {
        return;
    }

    @Override
    public void connectInput(Pipe input) {
        return;
    }

    @Override
    public void disconnectInput(Pipe input) {
        return;
    }

    @Override
    public void disconnectOutput(Pipe output) {
        return;
    }
}

package Element;

import Element.ActiveElement.ActiveElement;
import Element.ActiveElement.Desert;

import java.util.*;

public class Pipe implements Element {
    private boolean isPunctured;
    private boolean isFull;
    private ActiveElement input;
    private ActiveElement output;
    private final Desert desert;
    private final String name;

    public Pipe(Desert desert) {
        isPunctured = false;
        isFull = false;
        this.desert = desert;
        name = "Pipe-" + UUID.randomUUID();
    }

    public void puncture() {
        this.isPunctured = true;
        isFull = false;
    }

    public void fix() {
        this.isPunctured = false;
    }

    public boolean isFull() {
        return isFull;
    }

    public void empty() {
        isFull = false;
    }

    public void flow(ActiveElement source) {
        if(source != null) {
            isFull = true;
        }
        if(isPunctured) {
            desert.flow(this);
        }
        output.flow(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Element> getNeighbors() {
        List<Element> neighbors = new LinkedList<>();
        neighbors.add(input);
        neighbors.add(output);
        return neighbors;
    }

    public void connectInput(ActiveElement input) {
        this.input = input;
    }

    public void connectOutput(ActiveElement output) {
        this.output = output;
    }

    public boolean isPunctured() {
        return isPunctured;
    }

    public ActiveElement getInput() {
        return input;
    }

    public ActiveElement getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pipe pipe) {
            return this.name.equals(pipe.getName());
        }else {
            return false;
        }
    }
}


package Element;

import Element.ActiveElement.ActiveElement;
import Element.ActiveElement.Desert;

import java.util.*;

public class Pipe implements Element {
    // Boolean indicating if the pipe is punctured or not
    private boolean isPunctured;

    // Boolean indicating if the pipe carries water or not
    private boolean isFull;

    // Input and output of the pipe
    private ActiveElement input;
    private ActiveElement output;

    // Desert to which water leaks when pipe is punctured
    private final Desert desert;

    // Name of the pipe
    private final String name;

    public Pipe(Desert desert) {
        isPunctured = false;
        isFull = false;
        this.desert = desert;
        name = "Pipe-" + UUID.randomUUID();
    }

    // TODO Check if this is necessary
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pipe pipe) {
            return this.name.equals(pipe.getName());
        }else {
            return false;
        }
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

    // Puncture and empty pipe
    public void puncture() {
        this.isPunctured = true;
        empty();
    }

    // Fix pipe
    public void fix() {
        this.isPunctured = false;
    }

    // Return if the pipe carries water or not
    public boolean isFull() {
        return isFull;
    }

    // Empty pipe
    public void empty() {
        isFull = false;
    }

    // Flow water
    // Call this with null source if there is no water flowing into this pipe
    public void flow(ActiveElement source) {
        if(source != null) {
            // If source is not null, fill the pipe
            isFull = true;
        }
        if(isPunctured) {
            // If the pipe is punctured, water leaks to the desert and the pipe becomes empty
            desert.flow(this);
            empty();
        }

        output.flow(this);
    }

    // Set the input of the pipe
    public void connectInput(ActiveElement input) {
        this.input = input;
    }

    // Set the output of the pipe
    public void connectOutput(ActiveElement output) {
        this.output = output;
    }

    // Returns if the pipe is punctured or not
    public boolean isPunctured() {
        return isPunctured;
    }

    // Return the input element
    public ActiveElement getInput() {
        return input;
    }

    // Return the output element
    public ActiveElement getOutput() {
        return output;
    }
}


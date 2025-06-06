package Element.ActiveElement;

import Element.Element;
import Element.Pipe;

import java.util.*;

public class Pump implements ActiveElement{
    private boolean isBroken;
    private Pipe input;
    private Pipe output;
    private final int tankCapacity = 10;
    private int tank = 0;
    private final String name;
    private final Set<Pipe> neighbors;
    private final int connectionCapacity;

    public Pump() {
        connectionCapacity = 4;
        isBroken = false;
        this.name = "Pump-" + UUID.randomUUID();
        neighbors = new HashSet<>();
    }

    @Override
    public void flow(Pipe source) throws IllegalArgumentException {
        if(source == null) throw new IllegalArgumentException("The source is null");
        if(!input.equals(source)) {
            return;
        }
        if(source.isFull()) {
            source.empty();
            if(isBroken) {
                // If source is full and pump is broken, water flows into tank
                addToTank();
                output.flow(null);
            }else {
                // If source is full and pump is not broken, water flows into next pipe
                output.flow(this);
            }
        }else {
            if(isTankEmpty() || !isBroken) {
                // If source is empty and either tank is also empty or is broken,
                // water does not flow into next pipe
                output.flow(null);
            }else {
                // If source is empty, tank is not empty and not broken,
                // water in the tank flows into next pipe
                removeFromTank();
                output.flow(this);
            }
        }
    }

    @Override
    public List<Element> getNeighbors() {
        return new LinkedList<>(neighbors);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void disconnectInput(Pipe input) {
        if(input.equals(this.input)) return;
        neighbors.remove(input);
    }

    @Override
    public void disconnectOutput(Pipe output) {
        if(output.equals(this.output)) return;
        neighbors.remove(output);
    }

    public void breakPump() {
        isBroken = true;
    }

    public void fix() {
        isBroken = false;
    }

    private boolean isTankEmpty() {
        return tank == 0;
    }

    private void addToTank() {
        if(tank < tankCapacity) tank++;
    }

    private void removeFromTank() {
        if(tank > 0) tank--;
    }

    public void connectInput(Pipe pipe) {
        if(neighbors.size() < connectionCapacity) {
            neighbors.add(pipe);
            input = pipe;
        }
    }

    public void connectOutput(Pipe pipe) {
        if(neighbors.size() < connectionCapacity) {
            neighbors.add(pipe);
            output = pipe;
        }
    }

    public boolean hasConnectionCapacity() {
        return neighbors.size() < connectionCapacity;
    }

    public void setInput(Pipe pipe) {
        if(!neighbors.contains(pipe)) throw new IllegalArgumentException();
        input = pipe;
    }

    public void setOutput(Pipe pipe) {
        if(!neighbors.contains(pipe)) throw new IllegalArgumentException();
        output = pipe;
    }

    public Pipe getInput() {
        return input;
    }

    public Pipe getOutput() {
        return output;
    }

    public boolean isBroken() {
        return isBroken;
    }
}

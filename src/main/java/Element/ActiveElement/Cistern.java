package Element.ActiveElement;

import Element.Element;
import Element.Pipe;
import jdk.jshell.spi.ExecutionControl;

import java.util.*;

public class Cistern implements ActiveElement{
    private int water;
    private final Set<Pipe> inputs;
    private final Queue<Pipe> pipes;
    private final Queue<Pump> pumps;
    private final String name;

    public Cistern() {
        this.inputs = new HashSet<>();
        this.pipes = new LinkedList<>();
        this.pumps = new LinkedList<>();
        this.name = "Cistern-" + UUID.randomUUID();
    }

    @Override
    public void flow(Pipe source) throws IllegalArgumentException {
        if(source == null) throw new IllegalArgumentException();
        if(!inputs.contains(source)) throw new IllegalArgumentException();

        if(source.isFull()) {
            water++;
            source.empty();
        }
    }

    @Override
    public void connectInput(Pipe pipe) {
        inputs.add(pipe);
    }

    @Override
    public void connectOutput(Pipe pipe) {
        return;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Element> getNeighbors() {
        return new LinkedList<>(inputs);
    }

    @Override
    public void disconnectInput(Pipe input) {
        if(inputs.size() > 1)
            inputs.remove(input);
    }

    @Override
    public void disconnectOutput(Pipe output) {
        return;
    }

    public int getWater() {
        return water;
    }

    public void manufacturePipe(Desert desert) {
        pipes.add(new Pipe(desert));
    }

    public void manufacturePump() {
        pumps.add(new Pump());
    }

    public boolean hasPipe() {
        return !pipes.isEmpty();
    }

    public boolean hasPump() {
        return !pumps.isEmpty();
    }

    public Pipe getPipe() {
        return pipes.poll();
    }

    public Pump getPump() {
        return pumps.poll();
    }
}

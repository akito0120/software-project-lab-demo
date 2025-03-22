package Element.ActiveElement;

import Element.Element;
import Element.Pipe;

import java.util.*;

public class Spring implements ActiveElement{
    private Set<Pipe> outputs;
    private final String name;

    public Spring() {
        outputs = new HashSet<>();
        name = "Spring-" + UUID.randomUUID();
    }

    @Override
    public void flow(Pipe source) {
        for(var output: outputs) {
            output.flow(this);
        }
    }

    @Override
    public void connectOutput(Pipe pipe) {
        outputs.add(pipe);
    }

    @Override
    public void connectInput(Pipe input) {
        return;
    }

    @Override
    public void disconnectOutput(Pipe input) {
        if(outputs.size() > 1)
            outputs.remove(input);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Element> getNeighbors() {
        return new LinkedList<>(outputs);
    }

    @Override
    public void disconnectInput(Pipe input) {
        return;
    }
}

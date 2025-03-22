package Player;

import Element.ActiveElement.Pump;
import Element.Pipe;

import java.util.LinkedList;
import java.util.List;

public class Plumber extends Player{
    LinkedList<Pipe> pipes;
    LinkedList<Pump> pumps;

    public Plumber(String name) {
        super(name);
        pipes = new LinkedList<>();
        pumps = new LinkedList<>();
    }

    public void getPipe(Pipe pipe) {
        pipes.add(pipe);
    }

    public void getPump(Pump pump) {
        pumps.add(pump);
    }

    public Pipe usePipe() {
        return pipes.poll();
    }

    public Pump usePump() {
        return pumps.poll();
    }
}

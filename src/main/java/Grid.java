import Element.ActiveElement.Cistern;
import Element.ActiveElement.Desert;
import Element.ActiveElement.Pump;
import Element.ActiveElement.Spring;
import Element.Pipe;

import java.util.LinkedList;
import java.util.List;

public class Grid {
    private final Spring spring;
    private final Cistern cistern;
    private final Desert desert;
    private final List<Pump> pumps;

    Grid() {
        this.spring = new Spring();
        this.cistern = new Cistern();
        this.desert = new Desert();
        this.pumps = new LinkedList<>();

        Pipe pipe = new Pipe(desert);
        spring.connectOutput(pipe);
        cistern.connectInput(pipe);
        pipe.connectOutput(cistern);
        pipe.connectInput(spring);
    }

    public void calculateWaterFlow() {
        spring.flow(null);
    }

    public int getPlumberScore() {
        return cistern.getWater();
    }

    public int getSaboteurScore() {
        return desert.getWater();
    }

    public Spring getSpring() {
        return spring;
    }

    public Cistern getCistern() {
        return cistern;
    }

    public Desert getDesert() {
        return desert;
    }

    public List<Pump> getPumps() {
        return pumps;
    }

    public void addPump(Pump pump) {
        pumps.add(pump);
    }
}
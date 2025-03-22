import Element.ActiveElement.ActiveElement;
import Element.ActiveElement.Cistern;
import Element.ActiveElement.Pump;
import Element.ActiveElement.Spring;
import Element.Element;
import Player.Player;
import Player.Plumber;
import Player.Saboteur;
import Element.Pipe;

import java.util.*;

public class Game {
    private final Grid grid;
    List<Plumber> plumbers;
    List<Saboteur> saboteurs;

    Game() {
        this.grid = new Grid();
        this.plumbers = new LinkedList<>();
        this.saboteurs = new LinkedList<>();
    }

    public void addPlayer(Plumber plumber) {
        plumbers.add(plumber);
    }

    public void addPlayer(Saboteur saboteur) {
        saboteurs.add(saboteur);
    }

    public void start() {
        if(plumbers.size() < 2 || saboteurs.size() < 2) throw new IllegalStateException();

        Queue<Plumber> plumberPriorityQueue = new LinkedList<>();
        Queue<Saboteur> saboteurQueue = new LinkedList<>();

        for(var plumber: plumbers) {
            plumberPriorityQueue.offer(plumber);

            // Plumbers are initially on the cistern
            plumber.setPosition(grid.getCistern());
        }
        for(var saboteur: saboteurs) {
            saboteurQueue.offer(saboteur);

            // Saboteurs are initially on the spring
            saboteur.setPosition(grid.getSpring());
        }

        int turns = Math.max(plumbers.size(), saboteurs.size()) * 3;
        for(int i = 0; i < turns; i++) {
            // Plumber's turn
            Plumber plumber = plumberPriorityQueue.poll();
            turn(plumber);
            plumberPriorityQueue.offer(plumber);

            // Saboteur's turn
            Saboteur saboteur = saboteurQueue.poll();
            turn(saboteur);
            saboteurQueue.offer(saboteur);
        }

        end();
    }

    public void end() {
        int plumberScore = grid.getPlumberScore();
        int saboteurScore = grid.getSaboteurScore();

        System.out.println("Plumbers' Score: " + plumberScore);
        System.out.println("Saboteurs' Score: " + saboteurScore);

        if(plumberScore > saboteurScore) {
            System.out.println("Winner: Plumbers");
        }else if(plumberScore == saboteurScore) {
            System.out.println("Draw");
        }else {
            System.out.println("Winner: Saboteurs");
        }
    }

    // Called when the turn starts
    private void postTurnProcess() {
        grid.calculateWaterFlow();
        System.out.println("Plumbers' Score: " + grid.getPlumberScore());
        System.out.println("Saboteur's Score: " + grid.getSaboteurScore());
    }

    // Called when the turn ends
    private void preTurnProcess() {
        Cistern cistern = grid.getCistern();
        cistern.manufacturePipe(grid.getDesert());
        cistern.manufacturePump();

        if(!grid.getPumps().isEmpty()) {
            Random random = new Random();
            int num = random.nextInt(100);
            List<Pump> pumps = grid.getPumps();
            if(num < 30) {
                int index = random.nextInt(pumps.size());
                Pump pump = pumps.get(index);
                if(!pump.isBroken()) {
                    pump.breakPump();
                    System.out.println(pumps.get(index).getName() + " was broken");
                }
            }
        }
    }

    private void printSeparator() {
        System.out.println("====================================================");
    }

    public void turn(Plumber plumber) {
        preTurnProcess();

        Scanner scanner = new Scanner(System.in);

        mainLoop:
        while(true) {
            printSeparator();
            System.out.println(plumber.getName() + "'s turn");
            System.out.println("Enter a command");
            String command = scanner.nextLine();

            switch (command) {
                case "end":
                    break mainLoop;
                case "move":
                    move(plumber);
                    break;
                case "fix":
                    fix(plumber);
                    break;
                case "get":
                    get(plumber);
                    break;
                case "insert pipe":
                    insertPipe(plumber);
                    break;
                case "insert pump":
                    insertPump(plumber);
                    break;
                case "change direction":
                    changeDirection(plumber);
                    break;
                case "remove":
                    remove(plumber);
                    break;
                case "position":
                    position(plumber);
                    break;
            }
            printSeparator();
        }

        postTurnProcess();
    }

    public void turn(Saboteur saboteur) {
        preTurnProcess();

        Scanner scanner = new Scanner(System.in);

        mainLoop:
        while(true) {
            printSeparator();
            System.out.println(saboteur.getName() + "'s turn");
            System.out.println("Enter a command");
            String command = scanner.nextLine();

            switch (command) {
                case "end":
                    break mainLoop;
                case "move":
                    move(saboteur);
                    break;
                case "puncture":
                    puncture(saboteur);
                    break;
                case "change direction":
                    changeDirection(saboteur);
                    break;
                case "position":
                    position(saboteur);
                    break;
            }
            printSeparator();
        }

        postTurnProcess();
    }

    // Move
    private void move(Player player) {
        Scanner scanner = new Scanner(System.in);
        Element position = player.getPosition();
        List<Element> neighbors = position.getNeighbors();

        // List the neighbors to which the player can move
        for(int i = 0; i < neighbors.size(); i++) {
            System.out.println("    [" + i + "]" + neighbors.get(i).getName());
        }

        // Read user input
        int index = scanner.nextInt();
        scanner.nextLine();

        if(0 <= index && index < neighbors.size()) {
            Element target = neighbors.get(index);

            if (target instanceof Pipe pipe) {
                // If the target is Pipe, check if nobody is standing on it
                if(isOccupied(pipe)) return;
            }

            player.setPosition(target);
            System.out.println(player.getName() + " moved to " + target.getName());
        }
    }

    // Return if there is a player standing on given pipe
    private boolean isOccupied(Pipe pipe) {
        for (var p : plumbers) {
            if (p.getPosition().equals(pipe)) {
                System.out.println(p.getName() + " is already standing on" + pipe.getName());
                return true;
            }
        }
        for (var s : saboteurs) {
            if (s.getPosition().equals(pipe)) {
                System.out.println(s.getName() + " is already standing on" + pipe.getName());
                return true;
            }
        }
        return false;
    }

    // Fix pipe or pump
    private void fix(Plumber plumber) {
        Element position = plumber.getPosition();
        if(position instanceof Pipe pipe) {
            if(pipe.isPunctured()) {
                // If the target is a pipe and is punctured, fix it
                pipe.fix();
                System.out.println(plumber.getName() + " fixed " + pipe.getName());
            }else {
                // If the target is a pipe and is not punctured, do nothing
                System.out.println(pipe.getName() + " is not punctured");
            }
        }else if(position instanceof Pump pump) {
            if(pump.isBroken()) {
                // If target is a pump and is broken, fix it
                pump.fix();
                System.out.println(plumber.getName() + " fixed " + pump.getName());
            }else {
                // If target is a pump and is not broken, do nothing
                System.out.println(pump.getName() + " is not broken");
            }
        }else {
            // If target is not a fixable element, do nothing
            System.out.println(plumber.getName() + " is standing on neither a pipe nor a pump");
        }
    }

    // Get pipe or pump from cistern
    private void get(Plumber plumber) {
        Scanner scanner = new Scanner(System.in);
        Element position = plumber.getPosition();
        if(position instanceof Cistern cistern) {
            System.out.println("    [0]Pipe");
            System.out.println("    [1]Pump");

            int index = scanner.nextInt();
            scanner.nextLine();

            if(index == 0) {
                if(cistern.hasPipe()) {
                    Pipe pipe = cistern.getPipe();
                    plumber.getPipe(pipe);
                    System.out.println(plumber.getName() + " got " + pipe.getName());
                }else {
                    System.out.println("Cistern has no available pipe");
                }
            }else if(index == 1) {
                if(cistern.hasPump()) {
                    Pump pump = cistern.getPump();
                    plumber.getPump(pump);
                    System.out.println(plumber.getName() + " got " + pump.getName());
                }else {
                    System.out.println("Cistern has no available pump");
                }
            }
        }else {
            System.out.println(plumber.getName() + " is not standing on a cistern");
        }
    }

    private void insertPipe(Plumber plumber) {
        Scanner scanner = new Scanner(System.in);
        Element position = plumber.getPosition();
        if(position instanceof Spring spring) {
            Pipe pipe = plumber.usePipe();
            if(pipe == null) {
                System.out.println(plumber.getName() + " doesn't have a pipe");
            }else {
                // When the player is standing on a spring,
                // the other end can be either the cistern or a pump
                System.out.println("    [0]" + grid.getCistern().getName());
                List<Pump> pumps = grid.getPumps();
                for(int i = 0; i < pumps.size(); i++) {
                    System.out.println("    [" + (i + 1) + "]" + pumps.get(i).getName());
                }

                int index = scanner.nextInt();
                scanner.nextLine();

                if(index == 0) {
                    // Spring -> Pipe -> Cistern
                    Cistern cistern = grid.getCistern();

                    spring.connectOutput(pipe);
                    pipe.connectInput(spring);

                    cistern.connectInput(pipe);
                    pipe.connectOutput(cistern);

                    System.out.println(plumber.getName() + " connected " + spring.getName() + " to " + cistern.getName());
                }else if(1 <= index && index <= pumps.size()){
                    // Spring -> Pipe -> Pump
                    Pump pump = pumps.get(index - 1);
                    if(pump.hasConnectionCapacity()) {
                        spring.connectOutput(pipe);
                        pipe.connectInput(spring);

                        pump.connectInput(pipe);
                        pipe.connectOutput(pump);

                        System.out.println(plumber.getName() + " connected " + spring.getName() + " to " + pump.getName());
                    }else {
                        System.out.println(pump.getName() + "has no more connection capacity");
                    }
                }
            }
        }else if(position instanceof Pump sourcePump) {
            if(sourcePump.hasConnectionCapacity()) {
                Pipe pipe = plumber.usePipe();
                if(pipe == null) {
                    System.out.println(plumber.getName() + "doesn't have a pipe");
                }else {
                    // When the player is standing on a pump,
                    // the other end can be cistern, spring or another pump
                    System.out.println("    [0]" + grid.getCistern().getName());
                    System.out.println("    [1]" + grid.getSpring().getName());
                    List<Pump> pumps = grid.getPumps();
                    for(int i = 0; i < pumps.size(); i++) {
                        System.out.println("    [" + (i + 2) + "]" + pumps.get(i).getName());
                    }

                    int index = scanner.nextInt();
                    scanner.nextLine();

                    if(index == 0) {
                        // Pump -> Pipe -> Cistern
                        Cistern cistern = grid.getCistern();

                        sourcePump.connectOutput(pipe);
                        pipe.connectInput(sourcePump);

                        cistern.connectInput(pipe);
                        pipe.connectOutput(cistern);

                        System.out.println(plumber.getName() + " connected " + sourcePump.getName() + " to " + cistern.getName());
                    }else if(index == 1) {
                        // Spring -> Pipe -> Pump
                        Spring spring = grid.getSpring();

                        spring.connectOutput(pipe);
                        pipe.connectInput(spring);
                        sourcePump.connectInput(pipe);
                        pipe.connectOutput(sourcePump);
                    } else if(2 <= index && index <= pumps.size() + 1){
                        // Pump -> Pipe -> Pump
                        Pump pump = pumps.get(index - 2);

                        sourcePump.connectOutput(pipe);
                        pipe.connectInput(sourcePump);
                        pump.connectInput(pipe);
                        pipe.connectOutput(pump);

                        System.out.println(plumber.getName() + " connected " + sourcePump.getName() + " to " + pump.getName());
                    }
                }
            }else {
                System.out.println(sourcePump.getName() + "has no more connection capacity");
            }
        }else {
            System.out.println(plumber.getName() + " is not standing on an active element");
        }
    }

    private void insertPump(Plumber plumber) {
        Element position = plumber.getPosition();
        if(position instanceof Pipe pipe) {
            // Pump can only be inserted in the middle of a pipe

            Pump pump = plumber.usePump();
            if(pump == null) {
                System.out.println(plumber.getName() + " doesn't have a pump");
                return;
            }

            // Before: Input -> OldPipe -> Output
            // After: Input -> OldPipe -> Pump -> NewPipe -> Output

            // Output
            ActiveElement output = pipe.getOutput();

            // Set the output of the existing pipe to the pump
            pipe.connectOutput(pump);
            pump.connectInput(pipe);

            // Create new pipe and connect the pump with original output of the existing pipe
            Pipe newPipe = new Pipe(grid.getDesert());
            pump.connectOutput(newPipe);
            newPipe.connectInput(pump);
            output.connectInput(newPipe);
            newPipe.connectOutput(output);

            // Set the position of the plumber to the pump
            plumber.setPosition(pump);

            // Add pump to the grid
            grid.addPump(pump);

            System.out.println(plumber.getName() + " inserted " + pump.getName() + " on " + pipe.getName());
        }else {
            System.out.println(plumber.getName() + " is not standing on a pipe");
        }
    }

    private void changeDirection(Player player) {
        Scanner scanner = new Scanner(System.in);
        Element position = player.getPosition();

        if(position instanceof Pump pump) {
            List<Element> neighbors = pump.getNeighbors();

            for(int i = 0; i < neighbors.size(); i++) {
                System.out.println("    [" + i + "]" + neighbors.get(i).getName());
            }

            System.out.println("Select input");
            int inputIndex = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Select output");
            int outputIndex = scanner.nextInt();
            scanner.nextLine();

            if(inputIndex == outputIndex) System.out.println("Input and output cannot be the same");

            Pipe input = (Pipe)neighbors.get(inputIndex);
            Pipe output = (Pipe)neighbors.get(outputIndex);

            pump.setInput(input);
            pump.setOutput(output);

            input.connectOutput(pump);
            output.connectInput(pump);

            System.out.println(player.getName() + " set the direction of " + pump.getName());
            System.out.println("New input: " + input.getName());
            System.out.println("New output: " + output.getName());
        }else {
            System.out.println(player.getName() + " is not standing on a pump");
        }
    }

    private void puncture(Saboteur saboteur) {
        Element position = saboteur.getPosition();
        if (position instanceof Pipe pipe) {
            if (pipe.isPunctured()) {
                System.out.println(pipe.getName() + " is already punctured");
            } else {
                pipe.puncture();
                System.out.println(saboteur.getName() + " punctured " + pipe.getName());
            }
        } else {
            System.out.println(saboteur.getName() + " is not standing on a pipe");
        }
    }

    private void remove(Plumber plumber) {
        Scanner scanner = new Scanner(System.in);
        Element position = plumber.getPosition();
        if(position instanceof ActiveElement elm) {
            List<Element> neighbors = elm.getNeighbors();
            for(int i = 0; i < neighbors.size(); i++) {
                System.out.println("    [" + i + "]" + neighbors.get(i).getName());
            }

            int index = scanner.nextInt();
            scanner.nextLine();

            if(0 <= index && index < neighbors.size()) {
                Pipe pipe = (Pipe)neighbors.get(index);

                ActiveElement input = pipe.getInput();
                ActiveElement output = pipe.getOutput();

                if(input instanceof Pump pump) {
                    if(pump.getOutput().equals(pipe)) {
                        System.out.println(pipe.getName() + " is the output of " + pump.getName());
                        return;
                    }
                }else {
                    if(input.getNeighbors().size() <= 1) {
                        System.out.println("Active element must have at least 1 neighbor");
                        return;
                    }
                }

                if(output instanceof Pump pump) {
                    if(pump.getInput().equals(pipe)) {
                        System.out.println(pipe.getName() + " is the input of " + pump.getName());
                        return;
                    }
                }else {
                    if(output.getNeighbors().size() <= 1) {
                        System.out.println("Active element must have at least 1 neighbor");
                        return;
                    }
                }

                input.disconnectInput(pipe);
                output.disconnectOutput(pipe);
                pipe.connectInput(null);
                pipe.connectOutput(null);

                System.out.println(plumber.getName() + " removed " + pipe.getName());
            }
        }else {
            System.out.println(plumber.getName() + " is not standing on an active element");
        }
    }

    private void position(Player player) {
        System.out.println(player.getPosition().getName());
    }
}

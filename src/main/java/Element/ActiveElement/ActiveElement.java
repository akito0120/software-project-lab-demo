package Element.ActiveElement;

import Element.Element;
import Element.Pipe;

public interface ActiveElement extends Element {
    void flow(Pipe source);
    void connectInput(Pipe input);
    void connectOutput(Pipe output);
    void disconnectInput(Pipe input);
    void disconnectOutput(Pipe output);
}

package Element;

import java.util.List;

// Common interface for all elements
public interface Element {
    // Return the name of the element
    String getName();

    // Return all the neighbors of the element
    List<Element> getNeighbors();
}
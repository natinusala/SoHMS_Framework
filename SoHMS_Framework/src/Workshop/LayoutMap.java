package Workshop;

import Crosscutting.AutomatIterator;
import Crosscutting.State;
import Crosscutting.TerminalSequence;

import java.util.HashMap;

public interface LayoutMap {
    HashMap<String, State> getEdges();

    TerminalSequence[] getSequences_NoLoop(String initialState, String objectiveState);

    void addEdge(String origin, String dst, String tLabel, double tWeight);

    AutomatIterator automatIterator(String stateName);
}

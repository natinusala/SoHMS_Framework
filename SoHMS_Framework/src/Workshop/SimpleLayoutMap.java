package Workshop;

import Crosscutting.*;

import java.util.ArrayList;
import java.util.HashMap;

/*
LayoutMap implementation with one SOURCE, one SINK and three machines
 */
@SuppressWarnings("unused")
public class SimpleLayoutMap implements LayoutMap {

    private static final String[] NODES = {
            "SOURCE",
            "SINK",
            "M1",
            "M2",
            "M3"
    };

    @Override
    public HashMap<String, State> getEdges() {
        HashMap<String, State> edges = new HashMap<>();

        int i = 0;
        for (String node1 : NODES)
        {
            for (String node2 : NODES)
            {
                if (node1.equals(node2))
                    continue;

                Transition transition = new Transition(String.valueOf(i), 1.0);
                Arc arc = new Arc(transition, node2);

                ArrayList<Arc> arcs = new ArrayList<>();
                arcs.add(arc);

                State state = new State(arcs);
                state.isTerminal = true;

                edges.put(node1, state);

                i++;
            }
        }

        return edges;
    }

    @Override
    public TerminalSequence[] getSequences_NoLoop(String initialState, String objectiveState) {
        TerminalSequence seq = new TerminalSequence();
        seq.initialState = initialState;
        seq.finalState = objectiveState;
        seq.sequence= new ArrayList<>();
        return new TerminalSequence[] {seq};
    }

    @Override
    public void addEdge(String origin, String dst, String tLabel, double tWeight) {
        //Unused
    }

    @Override
    public AutomatIterator automatIterator(String stateName) {
        return new AutomatIterator(this, stateName);
    }
}

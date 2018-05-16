package Crosscutting;

import java.util.Comparator;

/**
 * Class used to Sort the Alternative Paths 
 * @author Francisco
 *
 */
public class PathArcComparator implements Comparator<PathArc> {
	    @Override
	    public int compare(PathArc o1, PathArc o2) {
	        return o1.getCost().compareTo(o2.getCost());
	    }
}

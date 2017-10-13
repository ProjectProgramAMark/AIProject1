import java.util.Comparator;

public class HeuristicComparator implements Comparator<QueueNode> {
    @Override
    public int compare(QueueNode o1, QueueNode o2) {
        if(o1.getF() < o2.getF()) {
            return -1;
        } else if(o1.getF() > o2.getF()) {
            return 1;
        } else {
            return 0;
        }
    }
}

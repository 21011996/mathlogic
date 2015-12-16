import java.util.LinkedList;
import java.util.List;

public abstract class BinOperation implements Expression {
    protected Expression left;
    protected Expression right;

    public BinOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public abstract List<Pair> firstFreeEntry(String x);
    public abstract String toStringWVar(Term term, String var);

    protected List<Pair> pathToFirstFreeEntryImpl(String x, String operationType) {
        List<Pair> pathFromCurPos;

        pathFromCurPos = left.firstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(operationType, "left"));
            return pathFromCurPos;
        }

        pathFromCurPos = right.firstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(operationType, "right"));
            return pathFromCurPos;
        }

        return null;
    }

    protected String toStringWithReplacedVarImpl(Term term, String var, String operationSign) {
        return "(" + left.toStringWVar(term, var) + operationSign
                + right.toStringWVar(term, var) + ")";
    }
}

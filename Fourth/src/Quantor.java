import java.util.LinkedList;
import java.util.List;

public abstract class Quantor implements Expression {
    protected String var = "#";
    protected Expression expression;

    public Quantor(Expression expression) {
        this.expression = expression;
    }

    public String getVar() {
        return var;
    }

    public Expression getExpression() {
        return expression;
    }

    public abstract List<Pair> firstFreeEntry(String x);
    public abstract String toStringWVar(Term term, String var);

    protected List<Pair> pathToFirstFreeEntryImpl(String x, String operationType) {
        List<Pair> pathFromCurPos = expression.firstFreeEntry(x);

        if (!var.equals(x) && pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(operationType, var));
            return pathFromCurPos;
        }

        return null;
    }

    protected String toStringWithReplacedVarImpl(Term term, String var, String operationSign) {
        if (this.var.equals(var)) {
            return toString();
        } else {
            return operationSign + this.var + expression.toStringWVar(term, var);
        }
    }

    public boolean canReplace(Term term, String var) {
        return this.var.equals(var) || !(term.firstFreeEntry(this.var) != null && expression.firstFreeEntry(var) != null) && expression.canReplace(term, var);

    }

}

import java.util.List;
import java.util.Map;

/**
 * Created by Ilya on 06.10.2015.
 */

public abstract class TermNode extends Term {

    private Term left;
    private Term right;

    public TermNode(Term left, Term right) {
        this.left = left;
        this.right = right;
    }

    abstract String getOperator();

    public Term getLeft() {
        return left;
    }

    public Term getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "(" + left + getOperator() + right + ")";
    }

    @Override
    public boolean compWithPatt(Term pattern, Map<String, Term> patternValues) {
        if (this == pattern) {
            return true;
        }
        if (getClass() == pattern.getClass()) {
            TermNode termNodePattern = (TermNode) pattern;
            return getOperator().equals(termNodePattern.getOperator())
                    && left.compWithPatt(termNodePattern.left, patternValues)
                    && right.compWithPatt(termNodePattern.right, patternValues);
        }
        return super.compWithPatt(pattern, patternValues);
    }

    @Override
    public boolean compWithExp(Term term) {
        if (this == term) {
            return true;
        }
        if (getClass() == term.getClass()) {
            TermNode binExpression = (TermNode) term;
            return getOperator().equals(binExpression.getOperator())
                    && left.compWithExp(binExpression.left)
                    && right.compWithExp(binExpression.right);
        }
        return false;
    }

    @Override
    public boolean compile(Map<String, Boolean> values) {
        return evaluate(left.compile(values), right.compile(values));
    }

    protected abstract boolean evaluate(boolean left, boolean right);

    @Override
    public void renew(Map<String, Term> expForNamedAnyExpression) {
        if (left.getClass() == OtherTerms.class) {
            OtherTerms named = (OtherTerms) left;
            left = expForNamedAnyExpression.get(named.name);
        } else {
            left.renew(expForNamedAnyExpression);
        }
        if (right.getClass() == OtherTerms.class) {
            OtherTerms named = (OtherTerms) right;
            right = expForNamedAnyExpression.get(named.name);
        } else {
            right.renew(expForNamedAnyExpression);
        }
    }

    @Override
    public void addSteps(Map<String, Boolean> varValues, List<Term> steps) {
        left.addSteps(varValues, steps);
        right.addSteps(varValues, steps);
        Term.addSteps(getSolution(left.compile(varValues), right.compile(varValues)),
                left, right, steps);
    }

    protected abstract String[] getSolution(boolean left, boolean right);
}

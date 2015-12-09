import java.util.List;
import java.util.Map;

public class Constant extends Term {

    public final String name;

    public Constant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Constant constant = (Constant) o;

        return name.equals(constant.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public boolean compWithPatt(Term pattern, Map<String, Term> patternValues) {
        if (pattern.getClass() == getClass()) {
            return compWithExp(pattern);
        }
        return super.compWithPatt(pattern, patternValues);
    }

    @Override
    public boolean compWithExp(Term term) {
        if (term.getClass() != getClass()) {
            return false;
        }
        Constant constant = (Constant) term;
        return name.equals(constant.getName());
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        return values.get(name);
    }

    @Override
    public void renew(Map<String, Term> expForNamedAnyExpression) {
    }

    @Override
    public void addSteps(Map<String, Boolean> varValues, List<Term> steps) {
        steps.add(evaluate(varValues) ?
                this :
                new Not(this)
        );
    }
}

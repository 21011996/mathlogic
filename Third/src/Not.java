import java.util.List;
import java.util.Map;

public class Not extends Term {

    final static String[] TRUE = new String[]{
            "A->!A->A",
            "!A->A",
            "!A->!A->!A",
            "(!A->!A->!A)->(!A->(!A->!A)->!A)->!A->!A",
            "(!A->(!A->!A)->!A)->!A->!A",
            "!A->(!A->!A)->!A",
            "!A->!A",
            "(!A->A)->(!A->!A)->!!A",
            "(!A->!A)->!!A",
            "!!A"
    };

    final static String[] FALSE = new String[]{
    };

    public Term term;

    public Not(Term term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "!" + term;
    }

    @Override
    public boolean compWithPatt(Term pattern, Map<String, Term> patternValues) {
        if (pattern.getClass() == getClass()) {
            Not notPattern = (Not) pattern;
            return term.compWithPatt(notPattern.term, patternValues);
        }
        return super.compWithPatt(pattern, patternValues);
    }

    @Override
    public boolean compWithExp(Term term) {
        return term.getClass() == Not.class && this.term.compWithExp(((Not) term).term);
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        return !term.evaluate(values);
    }

    @Override
    public void renew(Map<String, Term> expForNamedAnyExpression) {
        if (term.getClass() == OtherTerms.class) {
            OtherTerms named = (OtherTerms) term;
            term = expForNamedAnyExpression.get(named.name);
        } else {
            term.renew(expForNamedAnyExpression);
        }
    }

    @Override
    public void addSteps(Map<String, Boolean> varValues, List<Term> steps) {
        term.addSteps(varValues, steps);
        Term.addSteps(term.evaluate(varValues) ? TRUE : FALSE, term, null, steps);
    }
}

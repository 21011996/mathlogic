import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya on 06.10.2015.
 */

public abstract class Term {

    public boolean compWithPatt(Term pattern, Map<String, Term> patternValues) {
        if (pattern.getClass() == OtherTerms.class) {
            OtherTerms namedExpression = (OtherTerms) pattern;
            Term term = patternValues.get(namedExpression.getName());
            if (term != null) {
                return compWithExp(term);
            } else {
                patternValues.put(namedExpression.getName(), this);
                return true;
            }
        }
        return false;
    }

    public abstract boolean compWithExp(Term term);

    public abstract boolean compile(Map<String, Boolean> values);

    public abstract void renew(Map<String, Term> expForNamedAnyExpression);

    public abstract void addSteps(Map<String, Boolean> varValues, List<Term> steps);

    protected static void addSteps(String[] solution, Term A, Term B, List<Term> steps) {
        PattParser parser = new PattParser();
        for (String str : solution) {
            Term exp = parser.parseExpression(str);
            Map<String, Term> map = new HashMap<String, Term>();
            map.put("A", A);
            map.put("B", B);
            exp.renew(map);
            if (exp.getClass() == OtherTerms.class) {
                exp = map.get(((OtherTerms) exp).name);
            }
            steps.add(exp);
        }
    }
}

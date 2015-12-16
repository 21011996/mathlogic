import java.util.LinkedList;
import java.util.List;

public class Predicate implements Expression {
    private String value;
    private List<Term> subTerms;
    public Predicate(String value) {
        this.value = value;
    }

    public Predicate(String value, List<Term> subTerms) {
        this.value = value;
        this.subTerms = subTerms;
    }

    public String getValue() {
        return value;
    }

    public List<Term> getSubTerms() {
        return subTerms;
    }

    public String toString() {
        if (subTerms == null || subTerms.size() == 0) {
            return value;
        } else {
            StringBuilder res = new StringBuilder();
            res.append(value).append("(");
            res.append(subTerms.get(0).toString());
            for (int i = 1; i < subTerms.size(); i++) {
                res.append(",");
                res.append(subTerms.get(i).toString());
            }
            res.append(")");
            return res.toString();
        }
    }

    public List<Pair> firstFreeEntry(String x) {
        if (subTerms == null) {
            return null;
        }

        for (int i = 0; i < subTerms.size(); i++) {
            List<Pair> pathFromCurPos = subTerms.get(i).firstFreeEntry(x);
            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.PREDICATE, new Pair(value, i)));
                return pathFromCurPos;
            }
        }

        return null;
    }

    public String toStringWVar(Term term, String var) {
        if (subTerms == null || subTerms.size() == 0) {
            return value;
        } else {
            StringBuilder res = new StringBuilder();
            res.append(value).append("(");
            res.append(subTerms.get(0).toStringWVar(term, var));
            for (int i = 1; i < subTerms.size(); i++) {
                res.append(",");
                res.append(subTerms.get(i).toStringWVar(term, var));
            }
            res.append(")");
            return res.toString();
        }
    }

    public boolean canReplace(Term term, String var) {
        return true;
    }
}

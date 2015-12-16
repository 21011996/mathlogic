import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BinOperations {

    public static class And extends BinOperation {
        public And(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public String toString() {
            return "(" + left.toString() + "&" + right.toString() + ")";
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.CONJUCNTION);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "&");
        }

        public boolean canReplace(Term term, String var) {
            return (left.canReplace(term, var) && right.canReplace(term, var));
        }
    }

    public static And makeAnd(Expression a, Expression b) {
        return new And(a,b);
    }

    public static class Or extends BinOperation {

        public Or(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public String toString() {
            return "(" + left.toString() + "|" + right.toString() + ")";
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.DISJUCNTION);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "|");
        }

        public boolean canReplace(Term term, String var) {
            return (left.canReplace(term, var) && right.canReplace(term, var));
        }
    }

    public static Or makeOr(Expression a, Expression b) {
        return new Or(a,b);
    }

    public static class Equality extends BinOperation {

        public Equality(Term left, Term right) {
            super(left, right);
        }

        public String toString() {
            return "(" + left.toString() + "=" + right.toString() + ")";
        }

        public Expression getLeft() {
            return left;
        }

        public Expression getRight() {
            return right;
        }

        public List<Pair> firstFreeEntry(String x) {
            List<Pair> pathFromCurPos;

            pathFromCurPos = left.firstFreeEntry(x);
            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.EQUALITY, "left"));
                return pathFromCurPos;
            }

            pathFromCurPos = right.firstFreeEntry(x);
            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.EQUALITY, "right"));
                return pathFromCurPos;
            }

            return null;
        }

        public String toStringWVar(Term term, String var) {
            return "(" + left.toStringWVar(term, var) + "="
                    + right.toStringWVar(term, var) + ")";
        }

        public boolean canReplace(Term term, String var) {
            return (left.canReplace(term, var) && right.canReplace(term, var));
        }
    }

    public static Equality createEquality(Term a, Term b){
        return new Equality(a,b);
    }

    public static class Implication extends BinOperation {
        public Implication(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public String toString() {
            return "(" + left.toString() + "->" + right.toString() + ")";
        }

        public Expression getLeft() {
            return left;
        }

        public Expression getRight() {
            return right;
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.IMPLICATION);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "->");
        }

        public boolean canReplace(Term term, String var) {
            return (left.canReplace(term, var) && right.canReplace(term, var));
        }
    }

    public static Implication createImplication(Expression a, Expression b){
        return new Implication(a,b);
    }

    public static class Negation extends UnOperation {
        public Negation(Expression expression) {
            super(expression);
        }

        @Override
        public String toString() {
            return ("!" + expression.toString());
        }

        public List<Pair> firstFreeEntry(String x) {
            List<Pair> resultPath = new ArrayList<Pair>();
            List<Pair> pathFromCurPos = expression.firstFreeEntry(x);

            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.NEGATION, null));
                return pathFromCurPos;
            }

            return null;
        }

        public String toStringWVar(Term term, String var) {
            return "!" + expression.toStringWVar(term, var);
        }

        public boolean canReplace(Term term, String var) {
            return expression.canReplace(term, var);
        }
    }

    public static Negation createNegation(Expression a){
        return new Negation(a);
    }
}

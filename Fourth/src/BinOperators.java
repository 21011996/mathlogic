import java.util.LinkedList;
import java.util.List;

public class BinOperators {

    public static class Brackets extends UnOperator {

        public Brackets(Term term) {
            super(term);
        }

        public String toString() {
            return "(" + term.toString() + ")";
        }

        public List<Pair> firstFreeEntry(String x) {
            List<Pair> pathFromCurPos = term.firstFreeEntry(x);
            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.BRACKETS, null));
                return pathFromCurPos;
            } else {
                return null;
            }
        }

        public String toStringWVar(Term term, String var) {
            return "(" + this.term.toStringWVar(term, var) + ")";
        }

        public boolean canReplace(Term term, String var) {
            return this.term.canReplace(term, var);
        }
    }

    public static Brackets createBrackets(Term a){
        return  new Brackets(a);
    }

    public static class Incr extends UnOperator {

        public Incr(Term term) {
            super(term);
        }

        public String toString() {
            if (term instanceof BinOperator) {
                return ("(" + term.toString() + ")\'");
            }
            return (term.toString() + "\'");
        }

        public List<Pair> firstFreeEntry(String x) {
            List<Pair> pathFromCurPos = term.firstFreeEntry(x);

            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.INCREMENT, null));
                return pathFromCurPos;
            }

            return null;
        }

        public String toStringWVar(Term term, String var) {
            if (this.term instanceof BinOperator) {
                return ("(" + this.term.toStringWVar(term, var) + ")\'");
            }

            if (term instanceof BinOperator) {
                if (this.term instanceof Terms.Variable && this.term.toString().equals(var)) {
                    return ("(" + this.term.toStringWVar(term, var) + ")\'");
                }
            }
            return (this.term.toStringWVar(term, var) + "\'");
        }

        public boolean canReplace(Term term, String var) {
            return this.term.canReplace(term, var);
        }
    }

    public static Incr createIncr(Term a){
        return new Incr(a);
    }

    public static class Mul extends BinOperator {

        public Mul(Term left, Term right) {
            super(left, right);
        }

        public String toString() {
            String res = "";
            if (left instanceof Plus) {
                res += "(" + left.toString() + ")";
            } else {
                res += left.toString();
            }
            res += "*";
            if (right instanceof Plus) {
                res += "(" + right.toString() + ")";
            } else {
                res += right.toString();
            }
            return res;
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.MULTIPLY);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "*");
        }

        public boolean canReplace(Term term, String var) {
            return (left.canReplace(term, var) && right.canReplace(term, var));
        }
    }

    public static Mul createMul(Term a, Term b){
        return new Mul(a,b);
    }

    public static class Plus extends BinOperator {

        public Plus(Term left, Term right) {
            super(left, right);
        }

        public String toString() {
            return left.toString() + "+" + right.toString();
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.PLUS);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "+");
        }

        public boolean canReplace(Term term, String var) {
            return (left.canReplace(term, var) && right.canReplace(term, var));
        }
    }

    public static Plus createPlus(Term a, Term b){
        return new Plus(a,b);
    }
}

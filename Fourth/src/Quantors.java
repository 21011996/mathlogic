import java.util.List;

public class Quantors {

    public static class Exist extends Quantor {
        public Exist(Expression expression) {
            super(expression);
        }

        public Exist(Expression expression, String connectingVaraible) {
            super(expression);
            var = connectingVaraible;
        }

        @Override
        public String toString() {
            return "?" + var + expression.toString();
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.EXIST);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "?");
        }
    }

    public static Exist createExist(Expression a, String b){
        return new Exist(a,b);
    }

    public static class Forall extends Quantor {
        public Forall(Expression expression) {
            super(expression);
        }

        public Forall(Expression expression, String connectingVar) {
            super(expression);
            var = connectingVar;
        }

        @Override
        public String toString() {
            return "@" + var + expression.toString();
        }

        public List<Pair> firstFreeEntry(String x) {
            return pathToFirstFreeEntryImpl(x, Bank.FORALL);
        }

        public String toStringWVar(Term term, String var) {
            return toStringWithReplacedVarImpl(term, var, "@");
        }
    }

    public static Forall createForall(Expression a, String b){
        return new Forall(a,b);
    }
}

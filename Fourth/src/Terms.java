import java.util.LinkedList;
import java.util.List;

public class Terms {

    public static class TermWithArgs extends Term {
        private String value;
        private List<Term> subTerms;

        public TermWithArgs(String value, List<Term> subTerms) {
            this.value = value;
            this.subTerms = subTerms;
        }

        public String getValue() {
            return value;
        }

        public List<Term> getSubTerms() {
            return subTerms;
        }

        @Override
        public String toString() {
            if (subTerms.size() == 0) {
                System.err.println("ERROR. No subterms in TermWithArgs.toString() =(");
            }

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

        public List<Pair> firstFreeEntry(String x) {
            for (int i = 0; i < subTerms.size(); i++) {
                List<Pair> pathFromCurPos = subTerms.get(i).firstFreeEntry(x);
                if (pathFromCurPos != null) {
                    ((LinkedList) pathFromCurPos).addFirst(new Pair(Bank.TERMWITHARGS, new Pair(value, i)));
                    return pathFromCurPos;
                }
            }

            return null;
        }

        public String toStringWVar(Term term, String var) {
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

        public boolean canReplace(Term term, String var) {
            return true;
        }
    }

    public static TermWithArgs createTermWithArgs(String a, List<Term> b){
        return new TermWithArgs(a,b);
    }

    public static class Variable extends Term {
        private String value;

        public Variable(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public List<Pair> firstFreeEntry(String x) {
            if (value.equals(x)) {
                List<Pair> resultPath = new LinkedList<>();
                resultPath.add(new Pair(Bank.VARIABLE, null));
                return resultPath;
            }

            return null;
        }

        public String toStringWVar(Term term, String var) {
            if (this.value.equals(var)) {
                return term.toString();
            } else {
                return value;
            }
        }

        public boolean canReplace(Term term, String var) {
            return true;
        }
    }

    public static Variable createVariable(String a){
        return new Variable(a);
    }

    public static class Zero extends Term {
        private static final String value = "0";

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public List<Pair> firstFreeEntry(String x) {
            return null;
        }

        public String toStringWVar(Term term, String var) {
            return value;
        }

        public boolean canReplace(Term term, String var) {
            return true;
        }
    }

    public static Zero createZero(){
        return new Zero();
    }


}

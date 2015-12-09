import java.util.Map;

public class Not implements Term {
    private Term expr;

    public Not(Term expr) {
        this.expr = expr;
    }

    public Term getExpr() {
        return expr;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> var) {
        return !expr.evaluate(var);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Not) {
            Not c = (Not) o;
            return (expr.equals(c.expr));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "(" + "!" + expr.toString() + ")";
    }
}

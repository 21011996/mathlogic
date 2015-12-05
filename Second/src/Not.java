import java.util.Map;

/**
 * Created by Ilya on 05.12.2015.
 */

public class Not implements Expression {
    private Expression expr;

    public Not(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
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

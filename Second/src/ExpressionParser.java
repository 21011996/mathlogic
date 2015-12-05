
/**
 * Created by Ilya on 05.12.2015.
 */

public class ExpressionParser {
    private String expr;

    public ExpressionParser(String expr) {
        this.expr = expr.replaceAll("->", ">");
    }

    private Expression parse(int begin, int end) {

        int balance = 0;
        for (int i = begin; i < end; ++i) {
            if (expr.charAt(i) == '(') {
                balance++;
            }
            if (expr.charAt(i) == ')') {
                balance--;
            }
            if (expr.charAt(i) == '>' && balance == 0) {
                return new LinkBetween(parse(begin, i), parse(i + 1, end));
            }
        }

        balance = 0;
        for (int i = begin; i < end; ++i) {
            if (expr.charAt(i) == '(') {
                balance++;
            }
            if (expr.charAt(i) == ')') {
                balance--;
            }
            if (expr.charAt(i) == '|' && balance == 0) {
                return new Or(parse(begin, i), parse(i + 1, end));
            }
        }

        balance = 0;
        for (int i = begin; i < end; ++i) {
            if (expr.charAt(i) == '(') {
                balance++;
            }
            if (expr.charAt(i) == ')') {
                balance--;
            }
            if (expr.charAt(i) == '&' && balance == 0) {
                return new And(parse(begin, i), parse(i + 1, end));
            }
        }

        if (expr.charAt(begin) == '!') {
            return new Not(parse(begin + 1, end));
        }
        if (Character.isAlphabetic(expr.charAt(begin))){
            String s = "";
            int i = begin;
            while(i < end && (Character.isAlphabetic(expr.charAt(i)) || Character.isDigit(expr.charAt(i)))){
                s += expr.charAt(i);
                ++i;
            }
            return new Constant(s);
        }
        if (expr.charAt(begin) == '('){
            return parse(begin + 1, end - 1);
        }

        return null;
    }

    public Expression parse() {
        return parse(0, expr.length());
    }
}
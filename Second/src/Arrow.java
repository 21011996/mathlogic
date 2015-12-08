
/**
 * Created by Ilya on 05.12.2015.
 */
//forgot how implication was called in English XD
public class Arrow extends BinOperation {

    public Arrow(Term left, Term right) {
        super(left, right);
    }

    @Override
    protected boolean calculate(boolean leftVal, boolean rightVal) {
        return !(leftVal && !rightVal);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Arrow) {
            Arrow c = (Arrow) o;
            return (left.equals(c.left) && right.equals(c.right));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "(" + left.toString() + "->" + right.toString() + ")";
    }
}

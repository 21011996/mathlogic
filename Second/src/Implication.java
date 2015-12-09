
public class Implication extends BinOperation {

    public Implication(Term left, Term right) {
        super(left, right);
    }

    @Override
    protected boolean calculate(boolean leftVal, boolean rightVal) {
        return !(leftVal && !rightVal);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Implication) {
            Implication c = (Implication) o;
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

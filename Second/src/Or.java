
public class Or extends BinOperation {

    public Or(Term left, Term right) {
        super(left, right);
    }

    @Override
    protected boolean calculate(boolean leftVal, boolean rightVal) {
        return (leftVal || rightVal);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Or) {
            Or c = (Or) o;
            return (left.equals(c.left) && right.equals(c.right));
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return "(" + left.toString() + "|" +  right.toString() + ")";
    }
}


/**
 * Created by Ilya on 05.12.2015.
 */

public class LinkBetween extends BaseOperation {

    public LinkBetween(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected boolean apply(boolean leftVal, boolean rightVal) {
        return !(leftVal && !rightVal);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkBetween) {
            LinkBetween c = (LinkBetween) o;
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

import java.util.Map;

public abstract class BinOperation implements Term {

    protected Term left;
    protected Term right;

    public BinOperation(Term left, Term right) {
        this.left = left;
        this.right = right;
    }

    public Term getLeft(){
        return left;
    }

    public Term getRight(){
        return right;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> var) {
        return calculate(left.evaluate(var), right.evaluate(var));
    }

    protected abstract boolean calculate(boolean leftVal, boolean rightVal);

}

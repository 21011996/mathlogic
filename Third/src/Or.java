
public class Or extends TermNode {

    final static String[] TRUE_TRUE = TermBank.or_TRUE_TRUE;
    final static String[] TRUE_FALSE = TermBank.or_TRUE_FALSE;
    final static String[] FALSE_TRUE = TermBank.or_FALSE_TRUE;
    final static String[] FALSE_FALSE = TermBank.or_FALSE_TRUE;

    public Or(Term left, Term right) {
        super(left, right);
    }

    @Override
    String getOperator() {
        return "|";
    }

    @Override
    protected boolean evaluate(boolean left, boolean right) {
        return left || right;
    }

    @Override
    protected String[] getSolution(boolean left, boolean right) {
        if (left) {
            if (right) {
                return TRUE_TRUE;
            } else {
                return TRUE_FALSE;
            }
        } else {
            if (right) {
                return FALSE_TRUE;
            } else {
                return FALSE_FALSE;
            }
        }
    }

}

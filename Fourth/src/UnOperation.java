public abstract class UnOperation implements Expression {
    protected Expression expression;

    public UnOperation(Expression expression) {
        this.expression = expression;
    }
}

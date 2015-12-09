
public class Expression {

    long hash;
    int count;
    String s;
    Expression left;
    Expression right;

    Expression() {
        left = null;
        right = null;
    }

    Expression(String s, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.s = s;
        count = 1;
        int leftCount, rightCount = 0;
        if (left != null) {
            leftCount = left.count;
            count += leftCount;
        }
        if (left != null) {
            rightCount = right.count;
            count += rightCount;
        }
        hash = 0;
        if (left != null) hash += left.hash;
        hash *= ExpressionToHash.expressionHash.getHash(1);
        hash += s.charAt(0);
        if (right != null) {
            hash *= ExpressionToHash.expressionHash.getHash(rightCount);
            hash += right.hash;
        }

    }
}

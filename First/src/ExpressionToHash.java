
public class ExpressionToHash {
    public static class ExpressionHash {

        long hashSumm[];
        int max = 8000;

        ExpressionHash() {
            hashSumm = new long[max * max];
            hashSumm[0] = 1;
            hashSumm[1] = 31;
            for (int i = 2; i < max * max; i++)
                hashSumm[i] = hashSumm[i - 1] * hashSumm[1];
        }
        long getHash(int i) {
            return hashSumm[i];
        }
    }
    static ExpressionHash expressionHash = new ExpressionHash();
    ExpressionToHash() {
       expressionHash = new ExpressionHash();
    }
}

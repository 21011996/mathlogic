
/**
 * Created by Ilya on 01.12.2015.
 */

public class HelpUtils {
    public static class ExpressionHash {
        long hashSumm[];
        private final static int N = 8000;
        ExpressionHash() {
            hashSumm = new long[N * N];
            hashSumm[0] = 1;
            hashSumm[1] = 31;
            for (int i = 2; i < N * N; i++)
                hashSumm[i] = hashSumm[i - 1] * hashSumm[1];
        }
        long getHash(int i) {
            return hashSumm[i];
        }
    }
    static ExpressionHash expressionHash = new ExpressionHash();
    HelpUtils() {
       expressionHash = new ExpressionHash();
    }
}

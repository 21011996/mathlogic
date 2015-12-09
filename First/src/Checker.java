import java.io.*;
import java.util.StringTokenizer;

public class Checker {
    private String fileName = "input";
    int max = 8000;
    int n, currentChar;
    String s;

    public static void main(String[] args) {
        new Checker().run();
    }

    public void mainForTest(String s) {
        fileName = s;
        this.run();
    }

    Expression expressions[] = new Expression[max];
    AxiomChecker axiomChecker = new AxiomChecker();
    boolean isProved[] = new boolean[max];

    void solve() throws IOException {
        InputStream is = new FileInputStream(fileName + ".in");
        FastScanner in = new FastScanner(is);
        PrintWriter out = new PrintWriter(new File(fileName + ".out"));

        int counter = 1;
        while (in.hasNext()) {
            s = in.next();
            s = s.replace(" ", "");
            n = s.length();
            currentChar = 0;
            if (n == 0) break;
            out.print("(" + counter + ") " + s);
            try {
                Expression expr = parseExpression();
                expressions[counter - 1] = expr;
                int axiomNumber  = axiomChecker.isAxiom(expr);
                if (axiomNumber != -1) {
                    out.println(" (Сх. акс. " + axiomNumber + ")");
                    isProved[counter - 1] = true;
                } else {
                    Pair mp = isMP(counter - 1);
                    if (mp.first != -1) {
                        out.println(" (M.P. " + (mp.first + 1) + ", " + (mp.second + 1) + ")");
                        isProved[counter - 1] = true;
                    } else {
                        out.println(" (Не доказано)");
                    }
                }

            } catch (Exception e) {
                out.println(e.getMessage() + " in " + s);
            }
            counter++;
        }

        out.close();
    }

    private class Pair {
        int first, second;
        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    boolean compareExpressions(Expression a, Expression b) {
        return !((a.left != null && b.left == null) || (a.left == null && b.left != null)) && !((a.right != null && b.right == null) || (a.right == null && b.right != null)) && a.s.equals(b.s) && !(a.left != null && b.left != null && !compareExpressions(a.left, b.left)) && !(a.right != null && b.right != null && !compareExpressions(a.right, b.right));
    }

    boolean isEqual(Expression a, Expression b) {
        return a.hash == b.hash && compareExpressions(a, b);
    }

    Expression parseExpression() throws Exception {
        Expression expr1 = parseOr();
        if (currentChar < n && s.charAt(currentChar) == '-' && s.charAt(++currentChar) == '>') {
            currentChar++;
            Expression expr2 = parseExpression();
            return new Expression("->", expr1, expr2);
        }
        return expr1;
    }

    Expression parseOr() throws Exception {
        Expression expr = parseAnd();
        while (currentChar < n && s.charAt(currentChar) == '|') {
            currentChar++;
            Expression expr2 = parseAnd();
            expr = new Expression("|", expr, expr2);
        }
        return expr;
    }

    Expression parseAnd() throws Exception {
        Expression expr = parseNot();
        while (currentChar < n && s.charAt(currentChar) == '&') {
            currentChar++;
            Expression expr2 = parseNot();
            expr = new Expression("&", expr, expr2);
        }
        return expr;
    }

    Expression parseNot() throws Exception {
        char c = s.charAt(currentChar);
        if (c >= 'A' && c <= 'Z') {
            String name = "";
            name += c;
            currentChar++;
            if (currentChar < n && Character.isDigit(s.charAt(currentChar))) {
                name += s.charAt(currentChar++);
            }
            return new Expression(name, null, null);
        } else if (c == '!') {
            currentChar++;
            Expression expr = parseNot();
            return new Expression("!", null, expr);
        } else if (c == '(') {
            currentChar++;
            Expression expr = parseExpression();
            if (currentChar >= n || s.charAt(currentChar++) != ')') {
                throw new Exception(") doesn't exist");
            }
            return expr;
        }
        throw new Exception("incorrect formula");
    }

    Pair isMP(int id) {
        for (int i = id - 1; i >= 0; i--) {
            if (!isProved[i]) continue;
            Expression AB = expressions[i];
            if (AB != null && AB.s.equals("->") && AB.right != null && expressions[id] != null && isEqual(AB.right, expressions[id])) {
                for (int j = 0; j < id; j++) {
                    if (!isProved[j]) continue;
                    Expression A = expressions[j];
                    if (A != null && AB.left != null && isEqual(A, AB.left)) {
                        return new Pair(j, i);
                    }
                }
            }
        }
        return new Pair(-1, -1);
    }

    public void run() {
        try {
            solve();
        } catch (IOException ignored) {
        }
    }

    public static class FastScanner {

        private StringTokenizer tokenizer;

        public FastScanner(InputStream is) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 1024);
                byte[] buf = new byte[1024];
                while (true) {
                    int read = is.read(buf);
                    if (read == -1)
                        break;
                    bos.write(buf, 0, read);
                }
                tokenizer = new StringTokenizer(new String(bos.toByteArray()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean hasNext() {
            return tokenizer.hasMoreTokens();
        }

        public int nextInt() {
            return Integer.parseInt(tokenizer.nextToken());
        }

        public long nextLong() {
            return Long.parseLong(tokenizer.nextToken());
        }

        public double nextDouble() {
            return Double.parseDouble(tokenizer.nextToken());
        }

        public String next() {
            return tokenizer.nextToken();
        }

    }


}
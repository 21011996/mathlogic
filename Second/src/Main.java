import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Ilya on 05.12.2015.
 */

public class Main {

    private String fileNameInput = "input";
    private String fileNameOutput = "output";
    FastScanner in;
    PrintWriter out;

    public static void main(String[] arg) {
        new Main().run();
    }

    public void mainForTest(String s){
        fileNameInput = s;
        fileNameOutput = s;
        this.run();
    }

    void solve() throws IOException {
        ArrayList<Expression> expressions = new ArrayList<Expression>();
        String s = in.readLine();
        ExpressionParser expressionParser;
        String[] input = s.split(",");
        ArrayList<Expression> assumptions = new ArrayList<Expression>();
        for (int i = 0; i < input.length - 1; i++) {
            expressionParser = new ExpressionParser(input[i]);
            assumptions.add(expressionParser.parse());
        }
        String[] toDo = input[input.length - 1].split("\\|-");
        Expression alpha = (new ExpressionParser(toDo[0])).parse();
        ArrayList<Expression> answer = new ArrayList<Expression>();
        if (in.hasNext())
            s = in.readLine();

        while (in.hasNext()) {
            expressionParser = new ExpressionParser(s);
            expressions.add(expressionParser.parse());
            s = in.readLine();
        }
        boolean flag = true;
        int i = 0;
        for (Expression expr : expressions) {
            i++;
            flag = false;
            if (CheckAxiom.ifAxiom(expr) != -1) {
                answer.add(expr);
                answer.add(new LinkBetween(expr, new LinkBetween(alpha, expr)));
                answer.add(new LinkBetween(alpha, expr));
                flag = true;
                continue;
            }
            for (Expression g : assumptions) {
                if (g.equals(expr)) {
                    answer.add(expr);
                    answer.add(new LinkBetween(expr, new LinkBetween(alpha, expr)));
                    answer.add(new LinkBetween(alpha, expr));
                    flag = true;

                }
                if (flag) break;
            }
            if (alpha.equals(expr)) {
                answer.add(new LinkBetween(alpha, new LinkBetween(alpha, alpha)));
                answer.add(new LinkBetween(new LinkBetween(alpha, new LinkBetween(alpha, alpha)),
                        new LinkBetween(new LinkBetween(alpha, new LinkBetween(
                                new LinkBetween(alpha, alpha), alpha)), new LinkBetween(alpha, alpha))
                ));
                answer.add(new LinkBetween(new LinkBetween(alpha, new LinkBetween(new LinkBetween(alpha, alpha), alpha)),
                        new LinkBetween(alpha, alpha)));
                answer.add(new LinkBetween(alpha, new LinkBetween(new LinkBetween(alpha, alpha), alpha)));
                answer.add(new LinkBetween(alpha, alpha));
                flag = true;
                continue;
            }

            for (int j = 0; j < i - 1; j++) {
                Expression mp = expressions.get(j);
                if (mp instanceof LinkBetween) {
                    LinkBetween impl = (LinkBetween) mp;
                    if (impl.getRight().equals(expr)) {
                        for (int k = 0; k < i - 1; k++) {
                            Expression mp2 = expressions.get(k);
                            if (mp2.equals(impl.getLeft())) {
                                answer.add(new LinkBetween(new LinkBetween(alpha, mp2), new LinkBetween(new
                                        LinkBetween(alpha, new LinkBetween(mp2, expr)), new LinkBetween(alpha, expr))));
                                answer.add(new LinkBetween(new LinkBetween(alpha, new LinkBetween(mp2, expr)),
                                        new LinkBetween(alpha, expr)));
                                answer.add(new LinkBetween(alpha, expr));
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag)
                        break;
                }
            }
            if (!flag) {
                break;
            }
        }
        if (flag) {
            for (Expression expr : answer) {
                out.println(expr.toString());
            }
        } else {
            for (Expression expr : answer) {
                out.println(expr.toString());
            }
            out.println("Error");
        }
    }

    public void run() {
        try {
            InputStream is = new FileInputStream(fileNameInput + ".in");
            in = new FastScanner(is);
            out = new PrintWriter(new File(fileNameOutput + ".out"));
            solve();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
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

        public String readLine() {
            return tokenizer.nextToken();
        }

    }
}

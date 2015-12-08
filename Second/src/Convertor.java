import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Ilya on 05.12.2015.
 */

public class Convertor {

    private String fileNameInput = "input";
    private String fileNameOutput = "output";
    FastScanner in;
    PrintWriter out;

    public static void main(String[] arg) {
        new Convertor().run();
    }

    public void mainForTest(String s){
        fileNameInput = s;
        fileNameOutput = s;
        this.run();
    }

    void solve() throws IOException {
        ArrayList<Term> terms = new ArrayList<Term>();
        String s = in.readLine();
        TermsParser termsParser;
        String[] input = s.split(",");
        ArrayList<Term> assumptions = new ArrayList<Term>();
        for (int i = 0; i < input.length - 1; i++) {
            termsParser = new TermsParser(input[i]);
            assumptions.add(termsParser.parse());
        }
        String[] toDo = input[input.length - 1].split("\\|-");
        Term alpha = (new TermsParser(toDo[0])).parse();
        ArrayList<Term> answer = new ArrayList<Term>();
        if (in.hasNext())
            s = in.readLine();

        while (in.hasNext()) {
            termsParser = new TermsParser(s);
            terms.add(termsParser.parse());
            s = in.readLine();
        }
        boolean flag = true;
        int i = 0;
        for (Term expr : terms) {
            i++;
            flag = false;
            if (CheckAxiom.ifAxiom(expr) != -1) {
                answer.add(expr);
                answer.add(new Arrow(expr, new Arrow(alpha, expr)));
                answer.add(new Arrow(alpha, expr));
                flag = true;
                continue;
            }
            for (Term g : assumptions) {
                if (g.equals(expr)) {
                    answer.add(expr);
                    answer.add(new Arrow(expr, new Arrow(alpha, expr)));
                    answer.add(new Arrow(alpha, expr));
                    flag = true;

                }
                if (flag) break;
            }
            if (alpha.equals(expr)) {
                answer.add(new Arrow(alpha, new Arrow(alpha, alpha)));
                answer.add(new Arrow(new Arrow(alpha, new Arrow(alpha, alpha)),
                        new Arrow(new Arrow(alpha, new Arrow(
                                new Arrow(alpha, alpha), alpha)), new Arrow(alpha, alpha))
                ));
                answer.add(new Arrow(new Arrow(alpha, new Arrow(new Arrow(alpha, alpha), alpha)),
                        new Arrow(alpha, alpha)));
                answer.add(new Arrow(alpha, new Arrow(new Arrow(alpha, alpha), alpha)));
                answer.add(new Arrow(alpha, alpha));
                flag = true;
                continue;
            }

            for (int j = 0; j < i - 1; j++) {
                Term mp = terms.get(j);
                if (mp instanceof Arrow) {
                    Arrow impl = (Arrow) mp;
                    if (impl.getRight().equals(expr)) {
                        for (int k = 0; k < i - 1; k++) {
                            Term mp2 = terms.get(k);
                            if (mp2.equals(impl.getLeft())) {
                                answer.add(new Arrow(new Arrow(alpha, mp2), new Arrow(new
                                        Arrow(alpha, new Arrow(mp2, expr)), new Arrow(alpha, expr))));
                                answer.add(new Arrow(new Arrow(alpha, new Arrow(mp2, expr)),
                                        new Arrow(alpha, expr)));
                                answer.add(new Arrow(alpha, expr));
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
            for (Term expr : answer) {
                out.println(expr.toString());
            }
        } else {
            for (Term expr : answer) {
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

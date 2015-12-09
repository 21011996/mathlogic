import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentBuilder {

    private String inFileName = "input";
    private String outFileName = "output";
    PrintWriter out;

    public static void main(String[] args) {
        new ArgumentBuilder().run();
    }

    public void mainForTest(String s) {
        inFileName = s;
        outFileName = s;
        this.run();
    }

    void solve() throws IOException {
        Scanner in = new Scanner(new File(inFileName + ".in"));
        out = new PrintWriter(new File(outFileName + ".out"));
        ArgumentBuilder argumentBuilder = new ArgumentBuilder();
        argumentBuilder.checkArgument(in, out);
        out.println();
        out.close();
    }

    private boolean next(boolean[] cur) {
        for (int i = cur.length - 1; i >= 0; i--) {
            if (!cur[i]) {
                cur[i] = true;
                for (int j = i + 1; j < cur.length; j++) {
                    cur[j] = false;
                }
                return true;
            }
        }
        return false;
    }

    private String checkArgument(Scanner in, PrintWriter out) throws IOException {
        ParserForExp parser = new ParserForExp();
        String curLine = in.next();
        Term exp = parser.parseExpression(curLine);
        List<String> varNames = parser.getAllVariables(curLine);
        boolean[] cur = new boolean[varNames.size()];
        Map<String, Boolean> failValues = null;
        do {
            Map<String, Boolean> values = new HashMap<String, Boolean>();
            for (int i = 0; i < varNames.size(); i++) {
                values.put(varNames.get(i), cur[i]);
            }
            if (!exp.evaluate(values)) {
                failValues = values;
                break;
            }

        } while (next(cur));
        if (failValues != null) {
            StringBuilder verdict = new StringBuilder("Высказывание ложно при ");
            for (int i = 0; i < varNames.size(); i++) {
                String res;
                if (failValues.get(varNames.get(i)))
                    res = "И";
                else
                    res = "Л";
                verdict.append(varNames.get(i)).append("=").append(res);
                if (i != varNames.size() - 1) {
                    verdict.append(',');
                }
            }
            out.println(verdict.toString());
            return verdict.toString();
        }
        HashMap<String, Boolean> varValues = new HashMap<String, Boolean>();
        for (String name : varNames) {
            varValues.put(name, false);
        }
        VariousUtils.CheckedWithAssumptions proof = TermBank.getList(0, exp, varNames, varValues);
        for (Term step : proof.steps) {
            out.println(step.toString());
            if (step.compWithExp(exp)) {
                break;
            }
        }
        return "proofed";
    }

    public void run() {
        try {
            solve();
        } catch (IOException ignored) {
        }
    }

}

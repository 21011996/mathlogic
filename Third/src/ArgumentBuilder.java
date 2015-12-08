import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya on 06.10.2015.
 */

public class ArgumentBuilder {

    private List<Term> lemma_4(Term alpha, Term beta) {
        VariousUtils variousUtils = new VariousUtils();
        Term NB = new Not(beta);
        Term NA = new Not(alpha);
        Term AB = new TermBank(alpha, beta);
        Term ANB = new TermBank(alpha, NB);
        Term ANB_NA = new TermBank(ANB, NA);
        Term NB_ANB = new TermBank(NB, ANB);

        List<Term> steps = Lists.newArrayList(
                new TermBank(AB, ANB_NA),
                AB,
                ANB_NA,
                NB_ANB,
                NB,
                ANB,
                NA
        );

        return variousUtils.totalyDeduct(
                Lists.newArrayList(AB, new Not(beta)),
                new Not(alpha),
                steps);
    }

    private List<Term> lemma_5(Term alpha) {
        Term NA = new Not(alpha);
        Term NNA = new Not(NA);
        Term AorNA = new Or(alpha, NA);
        Term A_AorNA = new TermBank(alpha, AorNA);
        Term N_AorNA = new Not(AorNA);
        Term NN_AorNA = new Not(N_AorNA);
        Term NA_AorNA = new TermBank(NA, AorNA);
        Term N_AorNA__NA = new TermBank(N_AorNA, NA);
        Term N_AorNA__NNA = new TermBank(N_AorNA, NNA);
        Term N_AorNA__NNA______NN_AorNA = new TermBank(N_AorNA__NNA, NN_AorNA);

        List<Term> result = new ArrayList<Term>();

        result.add(A_AorNA);
        result.addAll(lemma_4(alpha, AorNA));
        result.add(N_AorNA__NA);

        result.add(NA_AorNA);
        result.addAll(lemma_4(NA, AorNA));
        result.add(N_AorNA__NNA);

        result.add(new TermBank(N_AorNA__NA, N_AorNA__NNA______NN_AorNA));
        result.add(N_AorNA__NNA______NN_AorNA);
        result.add(NN_AorNA);
        result.add(new TermBank(NN_AorNA, AorNA));
        result.add(AorNA);
        return result;
    }

    private VariousUtils.CheckedWithAssumptions lemma_6(VariousUtils.CheckedWithAssumptions ifP,
                                                        VariousUtils.CheckedWithAssumptions ifNP) {
        List<Term> steps = new ArrayList<Term>();
        VariousUtils variousUtils = new VariousUtils();
        VariousUtils.CheckedWithAssumptions fromPtoA = variousUtils.useDeductionConvertion(ifP);
        variousUtils = new VariousUtils();
        VariousUtils.CheckedWithAssumptions fromNPtoA = variousUtils.useDeductionConvertion(ifNP);

        Term P = ifP.alphaAssum;
        Term A = ifP.toBeProofed;

        Term NP = new Not(P);
        Term NP_A = new TermBank(NP, A);
        Term PorNP = new Or(P, NP);
        Term PorNP_A = new TermBank(PorNP, A);
        Term NP_A____PorNP_A = new TermBank(NP_A, PorNP_A);

        steps.addAll(fromPtoA.steps);
        steps.addAll(fromNPtoA.steps);
        steps.addAll(lemma_5(P));
        steps.add(new TermBank(new TermBank(P, A), NP_A____PorNP_A));
        steps.add(NP_A____PorNP_A);
        steps.add(PorNP_A);
        steps.add(A);

        List<Term> assumptions = new ArrayList<Term>();
        for (int i = 0; i < ifP.assumptions.size() - 1; i++) {
            assumptions.add(ifP.assumptions.get(i));
        }
        Term alpha = null;
        if (ifP.assumptions.size() >= 1) {
            alpha = ifP.assumptions.get(ifP.assumptions.size() - 1);
        }
        return new VariousUtils.CheckedWithAssumptions(assumptions, alpha, A, steps);
    }

    private VariousUtils.CheckedWithAssumptions getList(int varIndex, Term exp, List<String> varNames, Map<String, Boolean> varValues) {
        if (varIndex < varNames.size()) {
            varValues.put(varNames.get(varIndex), false);
            VariousUtils.CheckedWithAssumptions proofF = getList(varIndex + 1, exp, varNames, varValues);
            varValues.put(varNames.get(varIndex), true);
            VariousUtils.CheckedWithAssumptions proofT = getList(varIndex + 1, exp, varNames, varValues);
            return lemma_6(proofT, proofF);
        } else {
            List<Term> assums = new ArrayList<Term>();
            for (int i = 0; i < varNames.size() - 1; i++) {
                String name = varNames.get(i);
                if (varValues.get(name)) {
                    assums.add(new Constant(name));
                } else {
                    assums.add(new Not(new Constant(name)));
                }
            }
            Term alphaAsum;
            String name = varNames.get(varValues.size() - 1);
            if (varValues.get(name)) {
                alphaAsum = new Constant(name);
            } else {
                alphaAsum = new Not(new Constant(name));
            }
            List<Term> steps = new ArrayList<Term>();
            addSteps(exp, varValues, steps);
            return new VariousUtils.CheckedWithAssumptions(assums, alphaAsum, exp, steps);
        }
    }

    private void addSteps(Term exp, Map<String, Boolean> varValues, List<Term> steps) {
        exp.addSteps(varValues, steps);
    }

    private String checkArgum(Scanner in, PrintWriter out) throws IOException {
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
            if (!exp.compile(values)) {
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
        VariousUtils.CheckedWithAssumptions proof = getList(0, exp, varNames, varValues);
        for (Term step : proof.steps) {
            out.println(step.toString());
            if (step.compWithExp(exp)) {
                break;
            }
        }
        return "proofed";
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
        argumentBuilder.checkArgum(in, out);
        out.println();
        out.close();
    }

    public void run() {
        try {
            solve();
        } catch (IOException ignored) {
        }
    }

}

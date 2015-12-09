import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TermBank extends TermNode {

    public TermBank(Term left, Term right) {
        super(left, right);
    }

    @Override
    String getOperator() {
        return "->";
    }

    @Override
    protected boolean evaluate(boolean left, boolean right) {
        return !left || right;
    }

    final static String[] rawAxioms = {
            "A->(B->A)",
            "(A->B)->(A->B->C)->(A->C)",
            "A->B->A&B",
            "A&B->A",
            "A&B->B",
            "A->A|B",
            "B->A|B",
            "(A->C)->(B->C)->(A|B->C)",
            "(A->B)->(A->!B)->!A",
            "!!A->A"
    };

    final static String[] TRUE_TRUE = {
            "B->A->B",
            "A->B"
    };

    final static String[] TRUE_FALSE = {
            "(A->B)->(A->B)->A->B",
            "((A->B)->(A->B)->A->B)->((A->B)->((A->B)->A->B)->A->B)->(A->B)->A->B",
            "((A->B)->((A->B)->A->B)->A->B)->(A->B)->A->B",
            "(A->B)->((A->B)->A->B)->A->B",
            "(A->B)->A->B",
            "A->(A->B)->A",
            "(A->B)->A",
            "((A->B)->A)->((A->B)->A->B)->(A->B)->B",
            "((A->B)->A->B)->(A->B)->B",
            "(A->B)->B",
            "B->!A|B",
            "(B->!A|B)->(A->B)->B->!A|B",
            "(A->B)->B->!A|B",
            "((A->B)->B)->((A->B)->B->!A|B)->(A->B)->!A|B",
            "((A->B)->B->!A|B)->(A->B)->!A|B",
            "(A->B)->!A|B",
            "(!A->A)->(!A->!A)->!!A",
            "A->!A->A",
            "!A->A",
            "(!A->!A)->!!A",
            "!A->!A->!A",
            "(!A->!A->!A)->(!A->(!A->!A)->!A)->!A->!A",
            "(!A->(!A->!A)->!A)->!A->!A",
            "!A->(!A->!A)->!A",
            "!A->!A",
            "!!A",
            "!A->!A->!A",
            "(!A->!A->!A)->(!A->(!A->!A)->!A)->!A->!A",
            "(!A->(!A->!A)->!A)->!A->!A",
            "!A->(!A->!A)->!A",
            "!A->!A",
            "(!!A&!B->!A)->(!!A&!B->!!A)->!(!!A&!B)",
            "((!!A&!B->!A)->(!!A&!B->!!A)->!(!!A&!B))->!A->(!!A&!B->!A)->(!!A&!B->!!A)->!(!!A&!B)",
            "!A->(!!A&!B->!A)->(!!A&!B->!!A)->!(!!A&!B)",
            "!A->!!A&!B->!A",
            "(!A->!!A&!B->!A)->!A->!A->!!A&!B->!A",
            "!A->!A->!!A&!B->!A",
            "(!A->!A)->(!A->!A->!!A&!B->!A)->!A->!!A&!B->!A",
            "(!A->!A->!!A&!B->!A)->!A->!!A&!B->!A",
            "!A->!!A&!B->!A",
            "(!A->!!A&!B->!A)->(!A->(!!A&!B->!A)->(!!A&!B->!!A)->!(!!A&!B))->!A->(!!A&!B->!!A)->!(!!A&!B)",
            "(!A->(!!A&!B->!A)->(!!A&!B->!!A)->!(!!A&!B))->!A->(!!A&!B->!!A)->!(!!A&!B)",
            "!A->(!!A&!B->!!A)->!(!!A&!B)",
            "!!A&!B->!!A",
            "(!!A&!B->!!A)->!A->!!A&!B->!!A",
            "!A->!!A&!B->!!A",
            "(!A->!!A&!B->!!A)->(!A->(!!A&!B->!!A)->!(!!A&!B))->!A->!(!!A&!B)",
            "(!A->(!!A&!B->!!A)->!(!!A&!B))->!A->!(!!A&!B)",
            "!A->!(!!A&!B)",
            "B->B->B",
            "(B->B->B)->(B->(B->B)->B)->B->B",
            "(B->(B->B)->B)->B->B",
            "B->(B->B)->B",
            "B->B",
            "(!!A&!B->B)->(!!A&!B->!B)->!(!!A&!B)",
            "((!!A&!B->B)->(!!A&!B->!B)->!(!!A&!B))->B->(!!A&!B->B)->(!!A&!B->!B)->!(!!A&!B)",
            "B->(!!A&!B->B)->(!!A&!B->!B)->!(!!A&!B)",
            "B->!!A&!B->B",
            "(B->!!A&!B->B)->B->B->!!A&!B->B",
            "B->B->!!A&!B->B",
            "(B->B)->(B->B->!!A&!B->B)->B->!!A&!B->B",
            "(B->B->!!A&!B->B)->B->!!A&!B->B",
            "B->!!A&!B->B",
            "(B->!!A&!B->B)->(B->(!!A&!B->B)->(!!A&!B->!B)->!(!!A&!B))->B->(!!A&!B->!B)->!(!!A&!B)",
            "(B->(!!A&!B->B)->(!!A&!B->!B)->!(!!A&!B))->B->(!!A&!B->!B)->!(!!A&!B)",
            "B->(!!A&!B->!B)->!(!!A&!B)",
            "!!A&!B->!B",
            "(!!A&!B->!B)->B->!!A&!B->!B",
            "B->!!A&!B->!B",
            "(B->!!A&!B->!B)->(B->(!!A&!B->!B)->!(!!A&!B))->B->!(!!A&!B)",
            "(B->(!!A&!B->!B)->!(!!A&!B))->B->!(!!A&!B)",
            "B->!(!!A&!B)",
            "(!A->!(!!A&!B))->(B->!(!!A&!B))->!A|B->!(!!A&!B)",
            "(B->!(!!A&!B))->!A|B->!(!!A&!B)",
            "!A|B->!(!!A&!B)",
            "!!A->!B->!!A&!B",
            "!B->!!A&!B",
            "!!A&!B",
            "!!A&!B->!A|B->!!A&!B",
            "!A|B->!!A&!B",
            "(!A|B->!!A&!B)->(!A|B->!(!!A&!B))->!(!A|B)",
            "(!A|B->!(!!A&!B))->!(!A|B)",
            "!(!A|B)",
            "!(!A|B)->(A->B)->!(!A|B)",
            "(A->B)->!(!A|B)",
            "((A->B)->!A|B)->((A->B)->!(!A|B))->!(A->B)",
            "((A->B)->!(!A|B))->!(A->B)",
            "!(A->B)"
    };

    final static String[] FALSE_TRUE = {
            "B->A->B",
            "A->B"
    };

    final static String[] FALSE_FALSE = {
            "(!B->A)->(!B->!A)->!!B",
            "((!B->A)->(!B->!A)->!!B)->A->(!B->A)->(!B->!A)->!!B",
            "A->(!B->A)->(!B->!A)->!!B",
            "!A->!B->!A",
            "(!A->!B->!A)->A->!A->!B->!A",
            "A->!A->!B->!A",
            "A->!B->A",
            "(A->!B->A)->A->A->!B->A",
            "A->A->!B->A",
            "A->A->A",
            "(A->A->A)->(A->(A->A)->A)->A->A",
            "(A->(A->A)->A)->A->A",
            "A->(A->A)->A",
            "A->A",
            "!A->A->!A",
            "A->!A",
            "(A->A)->(A->A->!B->A)->A->!B->A",
            "(A->A->!B->A)->A->!B->A",
            "A->!B->A",
            "(A->!A)->(A->!A->!B->!A)->A->!B->!A",
            "(A->!A->!B->!A)->A->!B->!A",
            "A->!B->!A",
            "(A->!B->A)->(A->(!B->A)->(!B->!A)->!!B)->A->(!B->!A)->!!B",
            "(A->(!B->A)->(!B->!A)->!!B)->A->(!B->!A)->!!B",
            "A->(!B->!A)->!!B",
            "(A->!B->!A)->(A->(!B->!A)->!!B)->A->!!B",
            "(A->(!B->!A)->!!B)->A->!!B",
            "A->!!B",
            "!!B->B",
            "(!!B->B)->A->!!B->B",
            "A->!!B->B",
            "(A->!!B)->(A->!!B->B)->A->B",
            "(A->!!B->B)->A->B",
            "A->B"
    };

    final static String[] and_TRUE_TRUE = {
            "A->B->A&B",
            "B->A&B",
            "A&B"
    };
    final static String[] and_TRUE_FALSE = {
            "!B->A&B->!B",
            "A&B->!B",
            "A&B->B",
            "(A&B->B)->(A&B->!B)->!(A&B)",
            "(A&B->!B)->!(A&B)",
            "!(A&B)"
    };
    final static String[] and_FALSE_TRUE = {
            "!A->A&B->!A",
            "A&B->!A",
            "A&B->A",
            "(A&B->A)->(A&B->!A)->!(A&B)",
            "(A&B->!A)->!(A&B)",
            "!(A&B)"
    };
    final static String[] and_FALSE_FALSE = {
            "!B->A&B->!B",
            "A&B->!B",
            "A&B->B",
            "(A&B->B)->(A&B->!B)->!(A&B)",
            "(A&B->!B)->!(A&B)",
            "!(A&B)"
    };

    final static String[] or_TRUE_TRUE = {
            "A->A|B",
            "A|B"
    };
    final static String[] or_TRUE_FALSE = {
            "A->A|B",
            "A|B"
    };
    final static String[] or_FALSE_TRUE = {
            "B->A|B",
            "A|B"
    };
    final static String[] or_FALSE_FALSE = {
            "A->A->A",
            "(A->A->A)->(A->(A->A)->A)->A->A",
            "(A->(A->A)->A)->A->A",
            "A->(A->A)->A",
            "A->A",
            "(!A&!B->A)->(!A&!B->!A)->!(!A&!B)",
            "((!A&!B->A)->(!A&!B->!A)->!(!A&!B))->A->(!A&!B->A)->(!A&!B->!A)->!(!A&!B)",
            "A->(!A&!B->A)->(!A&!B->!A)->!(!A&!B)",
            "A->!A&!B->A",
            "(A->!A&!B->A)->A->A->!A&!B->A",
            "A->A->!A&!B->A",
            "(A->A)->(A->A->!A&!B->A)->A->!A&!B->A",
            "(A->A->!A&!B->A)->A->!A&!B->A",
            "A->!A&!B->A",
            "(A->!A&!B->A)->(A->(!A&!B->A)->(!A&!B->!A)->!(!A&!B))->A->(!A&!B->!A)->!(!A&!B)",
            "(A->(!A&!B->A)->(!A&!B->!A)->!(!A&!B))->A->(!A&!B->!A)->!(!A&!B)",
            "A->(!A&!B->!A)->!(!A&!B)",
            "!A&!B->!A",
            "(!A&!B->!A)->A->!A&!B->!A",
            "A->!A&!B->!A",
            "(A->!A&!B->!A)->(A->(!A&!B->!A)->!(!A&!B))->A->!(!A&!B)",
            "(A->(!A&!B->!A)->!(!A&!B))->A->!(!A&!B)",
            "A->!(!A&!B)",
            "B->B->B",
            "(B->B->B)->(B->(B->B)->B)->B->B",
            "(B->(B->B)->B)->B->B",
            "B->(B->B)->B",
            "B->B",
            "(!A&!B->B)->(!A&!B->!B)->!(!A&!B)",
            "((!A&!B->B)->(!A&!B->!B)->!(!A&!B))->B->(!A&!B->B)->(!A&!B->!B)->!(!A&!B)",
            "B->(!A&!B->B)->(!A&!B->!B)->!(!A&!B)",
            "B->!A&!B->B",
            "(B->!A&!B->B)->B->B->!A&!B->B",
            "B->B->!A&!B->B",
            "(B->B)->(B->B->!A&!B->B)->B->!A&!B->B",
            "(B->B->!A&!B->B)->B->!A&!B->B",
            "B->!A&!B->B",
            "(B->!A&!B->B)->(B->(!A&!B->B)->(!A&!B->!B)->!(!A&!B))->B->(!A&!B->!B)->!(!A&!B)",
            "(B->(!A&!B->B)->(!A&!B->!B)->!(!A&!B))->B->(!A&!B->!B)->!(!A&!B)",
            "B->(!A&!B->!B)->!(!A&!B)",
            "!A&!B->!B",
            "(!A&!B->!B)->B->!A&!B->!B",
            "B->!A&!B->!B",
            "(B->!A&!B->!B)->(B->(!A&!B->!B)->!(!A&!B))->B->!(!A&!B)",
            "(B->(!A&!B->!B)->!(!A&!B))->B->!(!A&!B)",
            "B->!(!A&!B)",
            "(A->!(!A&!B))->(B->!(!A&!B))->A|B->!(!A&!B)",
            "(B->!(!A&!B))->A|B->!(!A&!B)",
            "A|B->!(!A&!B)",
            "!A->!B->!A&!B",
            "!B->!A&!B",
            "!A&!B",
            "!A&!B->A|B->!A&!B",
            "A|B->!A&!B",
            "(A|B->!A&!B)->(A|B->!(!A&!B))->!(A|B)",
            "(A|B->!(!A&!B))->!(A|B)",
            "!(A|B)"
    };

    public static List<Term> lemma_4(Term alpha, Term beta) {
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

    public static List<Term> lemma_5(Term alpha) {
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

    public static VariousUtils.CheckedWithAssumptions lemma_6(VariousUtils.CheckedWithAssumptions ifP,
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

    public static VariousUtils.CheckedWithAssumptions getList(int varIndex, Term exp, List<String> varNames, Map<String, Boolean> varValues) {
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

    public static void addSteps(Term exp, Map<String, Boolean> varValues, List<Term> steps) {
        exp.addSteps(varValues, steps);
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VariousUtils {

    private static final String[] unparsedAxioms = TermBank.rawAxioms;

    private final List<Term> axioms;
    private final List<Term> checkedTerms;
    private final List<Term> assumptions;

    public VariousUtils() {
        this.axioms = new ArrayList<Term>(unparsedAxioms.length);
        this.checkedTerms = new ArrayList<Term>();
        this.assumptions = new ArrayList<Term>();
        PattParser parser = new PattParser();
        for (String axiom : unparsedAxioms) {
            axioms.add(parser.parseExpression(axiom));
        }
    }

    private boolean isCorespondsToAxiom(Term term) {
        for (Term axiom : axioms) {
            if (term.compWithPatt(axiom, new HashMap<String, Term>())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsIn(List<Term> terms, Term term) {
        for (Term exp : terms) {
            if (term.compWithExp(exp)) {
                return true;
            }
        }
        return false;
    }

    private boolean wasChecked(Term term) {
        return containsIn(checkedTerms, term);
    }

    private boolean isCheckedByModusPonons(Term term) {
        for (Term checked : checkedTerms) {
            if (checked.getClass() == TermBank.class) {
                TermBank proofedTermBank = (TermBank) checked;
                if (term.compWithExp(proofedTermBank.getRight())
                        && wasChecked(proofedTermBank.getLeft())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class CheckedWithAssumptions {
        public List<Term> assumptions;
        public Term alphaAssum;
        public Term toBeProofed;
        public List<Term> steps;

        public CheckedWithAssumptions(List<Term> assumptions, Term alphaAssum, Term toBeProofed, List<Term> steps) {
            this.assumptions = assumptions;
            this.alphaAssum = alphaAssum;
            this.toBeProofed = toBeProofed;
            this.steps = steps;
        }
    }

    public List<Term> totalyDeduct(List<Term> assumptions, Term toBeProofed, List<Term> steps) {
        CheckedWithAssumptions proof = new CheckedWithAssumptions(
                assumptions.subList(0, assumptions.size() - 1), assumptions.get(assumptions.size() - 1), toBeProofed, steps);
        while (proof.alphaAssum != null) {
            proof = new VariousUtils().useDeductionConvertion(proof);
        }
        return proof.steps;
    }

    public CheckedWithAssumptions useDeductionConvertion(CheckedWithAssumptions proof) {
        List<Term> resAssumptions = new ArrayList<Term>();
        Term resAlpha = null;
        Term resToBeProofed = null;
        List<Term> resSteps = new ArrayList<Term>();
        for (Term ass : proof.assumptions) {
            assumptions.add(ass);
        }
        StringBuilder firstLine = new StringBuilder();
        for (int i = 0; i < assumptions.size() - 1; i++) {
            resAssumptions.add(assumptions.get(i));
        }
        if (assumptions.size() >= 1) {
            resAlpha = assumptions.get(assumptions.size() - 1);
        }
        resToBeProofed = new TermBank(proof.alphaAssum, proof.toBeProofed);
        int firstIncorrectLine = -1;
        int lineNumber = 0;
        for (Term expI : proof.steps) {
            if (firstIncorrectLine == -1) {
                checkedTerms.add(expI);
                if (isCorespondsToAxiom(expI) || containsIn(assumptions, expI)) {
                    Term alphaConsSigma = new TermBank(proof.alphaAssum, expI);
                    resSteps.add(expI);
                    resSteps.add(new TermBank(expI, alphaConsSigma));
                    resSteps.add(alphaConsSigma);
                } else if (expI.compWithExp(proof.alphaAssum)) {
                    Term AA = new TermBank(proof.alphaAssum, proof.alphaAssum);
                    Term A_AA = new TermBank(proof.alphaAssum, AA);
                    Term AA_A = new TermBank(AA, proof.alphaAssum);
                    Term A__AA_A = new TermBank(proof.alphaAssum, AA_A);
                    Term lemma1 = A_AA;
                    Term lemma2 = new TermBank(A_AA, new TermBank(A__AA_A, AA));
                    Term lemma3 = new TermBank(A__AA_A, AA);
                    Term lemma4 = A__AA_A;
                    Term lemma5 = AA;
                    resSteps.add(lemma1);
                    resSteps.add(lemma2);
                    resSteps.add(lemma3);
                    resSteps.add(lemma4);
                    resSteps.add(lemma5);
                } else {
                    Term expK = null;
                    Term expJ = null;
                    for (Term proofed : checkedTerms) {
                        if (proofed.getClass() == TermBank.class) {
                            TermBank proofedTermBank = (TermBank) proofed;
                            if (expI.compWithExp(proofedTermBank.getRight())
                                    && wasChecked(proofedTermBank.getLeft())) {
                                expK = proofedTermBank;
                                expJ = proofedTermBank.getLeft();
                                break;
                            }
                        }
                    }
                    if (expK == null || expJ == null) {
                        firstIncorrectLine = lineNumber;
                    } else {
                        Term res2 = new TermBank(new TermBank(proof.alphaAssum, new TermBank(expJ, expI)),
                                new TermBank(proof.alphaAssum, expI));
                        Term res1 = new TermBank(new TermBank(proof.alphaAssum, expJ), res2);
                        resSteps.add(res1);
                        resSteps.add(res2);
                        resSteps.add(new TermBank(proof.alphaAssum, expI));
                    }
                }
            }
            lineNumber++;
        }
        if (firstIncorrectLine != -1) {
            return null;
        } else {
            return new CheckedWithAssumptions(resAssumptions, resAlpha, resToBeProofed, resSteps);
        }
    }

    public List<String> useDeductionConvertion(List<String> source) {
        ParserForExp parser = new ParserForExp();
        String[] lineParts = source.get(0).split("(,|\\|-)");
        List<Term> exprAssums = new ArrayList<Term>();
        for (int i = 0; i < lineParts.length - 2; i++) {
            exprAssums.add(parser.parseExpression(lineParts[i]));
        }
        Term alphaAssum = parser.parseExpression(lineParts[lineParts.length - 2]);
        Term toBeProofed = parser.parseExpression(lineParts[lineParts.length - 1]);
        List<Term> steps = new ArrayList<Term>();
        for (int i = 1; i < source.size(); i++) {
            steps.add(parser.parseExpression(source.get(i)));
        }
        CheckedWithAssumptions newProof = useDeductionConvertion(new CheckedWithAssumptions(exprAssums, alphaAssum, toBeProofed, steps));
        List<String> result = new ArrayList<String>();
        StringBuilder firstLine = new StringBuilder();
        for (Term ass : newProof.assumptions) {
            firstLine.append(ass + ",");
        }
        if (newProof.alphaAssum != null) {
            firstLine.append(newProof.alphaAssum + "|-" + newProof.toBeProofed);
            result.add(firstLine.toString());
        }
        for (Term step : newProof.steps) {
            result.add(step.toString());
        }
        result.add("");
        return result;
    }


}

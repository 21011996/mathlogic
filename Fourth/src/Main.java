import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    FastScanner in;
    PrintWriter out;
    static String inputFile = "bigbadtest.in";
    static String outputFile = "bigbadtest.out";
    List<Expression> inputProof = new ArrayList<>();
    HashMap<String, ArrayList<Integer>> inputHash = new HashMap<>();
    List<String> outputProof = new ArrayList<>();
    List<String> forallProof = new ArrayList<>();
    List<String> existProof = new ArrayList<>();
    String[] conditions;
    Expression alphaExp;
    String alpha;
    String beta;
    Bank bank;

    private static final String RES_PATH = "";
    private FastScanner inProof;

    private void loadForallProof() throws MyException {
        for (int i = 0; i < 83; i++) {
            forallProof.add(Bank.forall_bank[i]);
        }
    }

    private void loadExistProof() throws MyException {
        for (int i = 0; i < 107; i++) {
            existProof.add(Bank.exists_bank[i]);
        }
    }

    private void initConditions(String headString) throws MyException {
        String[] y = headString.split("\\|-");
        String onlyFirstPart = y[0];
        String[] x = onlyFirstPart.split(",");
        conditions = new String[x.length - 1];
        for (int i = 0; i < (x.length - 1); i++) {
            conditions[i] = parse(x[i]);
        }


        alphaExp = (new ProofParser()).parse(x[x.length - 1]);
        alpha = parse(x[x.length - 1]);
        beta = parse(y[1]);
    }

    private boolean isCondition(String curLine) {
        for (String condition : conditions) {
            if (curLine.equals(condition)) {
                return true;
            }
        }
        return false;
    }

    private void solve() throws IOException, MyException {
        StringBuilder incorrectInputMsg = new StringBuilder();
        bank = new Bank();
        bank.init();
        loadForallProof();
        loadExistProof();
        String inputString;

        if ((inputString = in.nextLine()) != null) {
            initConditions(inputString);
        }

        while ((inputString = in.nextLine()) != null) {
            inputProof.add((new ProofParser()).parse(inputString));
        }

        for (int i = 0; i<inputProof.size(); i++) {
            String s = inputProof.get(i).toString();
            if (inputHash.containsKey(s)) {
                ArrayList<Integer> temp = inputHash.get(s);
                temp.add(i);
                inputHash.put(s,temp);
            } else {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(i);
                inputHash.put(s,temp);
            }
        }

        StringBuilder headString = new StringBuilder();
        if (conditions.length > 0) {
            headString.append(conditions[0]);
        }
        for (int i = 1; i < conditions.length; i++) {
            headString.append(",").append(conditions[i]);
        }
        headString.append("|-(").append(alpha).append("->").append(beta).append(")");
        Expression curExp;
        String curLine;
        String tmp;

        for (int i = 0; i < inputProof.size(); i++) {
            System.out.println("Current line: " + i);
            curExp = inputProof.get(i);
            curLine = curExp.toString();

            if ((AxiomChecker.doesMatchAxioms(curExp)) || ((conditions.length > 0)
                    && isCondition(curLine))) {
                tmp = curLine + "->" + alpha + "->" + curLine;
                outputProof.add(tmp);
                tmp = curLine;
                outputProof.add(tmp);
                tmp = alpha + "->" + curLine;
                outputProof.add(tmp);

                continue;
            }
            if (AxiomChecker.getFlag()) {
                incorrectInputMsg.append(Bank.INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": ").append(AxiomChecker.getErrorMsg());
                break;
            }

            if (curLine.equals(alpha)) {
                tmp = alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + ")";
                outputProof.add(tmp);
                tmp = alpha + "->(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                tmp = "(" + alpha + "->(" + alpha + "->" + alpha + "))->" +
                        "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                        "(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                tmp = "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                        "(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                tmp = "(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                continue;
            }

            String lineJ = isModusPonensOfTwoLines(curLine, i);
            if (lineJ != null) {
                tmp = "(" + alpha + "->" + lineJ + ")->" +
                        "(" + alpha + "->" + lineJ + "->" + curLine + ")->" +
                        "(" + alpha + "->" + curLine + ")";
                outputProof.add(tmp);
                tmp = "(" + alpha + "->" + lineJ + "->" + curLine + ")->" +
                        "(" + alpha + "->" + curLine + ")";
                outputProof.add(tmp);
                tmp = "(" + alpha + "->" + curLine + ")";
                outputProof.add(tmp);
                continue;
            }

            lineJ = isApplicationOfForallRule(curExp);
            if (lineJ == null) {
                String left = ((BinOperations.Implication) curExp).getLeft().toString();
                String right = ((Quantors.Forall) ((BinOperations.Implication) curExp).getRight()).getExpression().toString();
                String var = ((Quantors.Forall) ((BinOperations.Implication) curExp).getRight()).getVar();
                insertQuantifierProof(forallProof, left, right, var);
                continue;
            } else if (!lineJ.equals("NOT OK")) {
                incorrectInputMsg.append(Bank.INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": ").append(lineJ);
                break;
            }

            lineJ = isApplicationOfExistRule(curExp);
            if (lineJ == null) {
                String left = ((Quantors.Exist) ((BinOperations.Implication) curExp).getLeft()).getExpression().toString();
                String right = ((BinOperations.Implication) curExp).getRight().toString();
                String var = ((Quantors.Exist) ((BinOperations.Implication) curExp).getLeft()).getVar();
                insertQuantifierProof(existProof, left, right, var);
                continue;
            } else if (!lineJ.equals("NOT OK")) {
                incorrectInputMsg.append(Bank.INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": ").append(lineJ);
                break;
            }

            incorrectInputMsg.append(Bank.INCORRECT_INPUT);
            incorrectInputMsg.append(i + 1);
            break;
        }

        if (incorrectInputMsg.length() > 0) {
            out.println(incorrectInputMsg);
        } else {
            out.println(headString);
            for (String anOutputProof : outputProof) {
                out.println(parse(anOutputProof));
            }
        }
    }

    private String isModusPonensOfTwoLines(String curLine, int k) {
        String lineJ;
        String requiredString;
        for (int i = 0; i < k; i++) {
            lineJ = inputProof.get(i).toString();
            requiredString = "(" + lineJ + "->" + curLine + ")";

            if (inputHash.containsKey(requiredString)) {
                ArrayList<Integer> temp = inputHash.get(requiredString);
                for (int j : temp) {
                    if (j < k) {
                        return lineJ;
                    }
                }
            }
            /*for (int j = 0; j < k; j++) {
                if (requiredString.equals(inputProof.get(j).toString())) {
                    return lineJ;
                }
            }*/
        }

        return null;
    }

    private static final String A = "A77";
    private static final String B = "B77";
    private static final String C = "C77";
    private static final String VAR = "v77";

    private void insertQuantifierProof(List<String> proof, String left, String right, String var) {
        String s;
        for (String aProof : proof) {
            s = aProof;
            s = s.replace(A, alpha);
            s = s.replace(B, left);
            s = s.replace(C, right);
            s = s.replace(VAR, var);
            outputProof.add(s);
        }
    }

    private String isApplicationOfForallRule(Expression curExp) throws MyException {
        String var;
        if (curExp instanceof BinOperations.Implication) {
            Expression right = ((BinOperations.Implication) curExp).getRight();
            if (right instanceof Quantors.Forall) {
                var = ((Quantors.Forall) right).getVar();
            } else {
                return "NOT OK";
            }
        } else {
            return "NOT OK";
        }

        String curLine = curExp.toString();
        String requiredString;
        Expression expJ;

        for (Expression anInputProof : inputProof) {
            expJ = anInputProof;
            if (expJ instanceof BinOperations.Implication) {
                requiredString = parse("(" + ((BinOperations.Implication) expJ).getLeft().toString() + "->@" +
                        var + ((BinOperations.Implication) expJ).getRight().toString() + ")");
                if (requiredString.equals(curLine)) {
                    return isVarFreeInExpression(((BinOperations.Implication) curExp).getLeft(), var);
                }
            }
        }
        return "NOT OK";
    }

    private String isApplicationOfExistRule(Expression curExp) throws MyException {
        String var;
        if (curExp instanceof BinOperations.Implication) {
            Expression left = ((BinOperations.Implication) curExp).getLeft();
            if (left instanceof Quantors.Exist) {
                var = ((Quantors.Exist) left).getVar();
            } else {
                return "NOT OK";
            }
        } else {
            return "NOT OK";
        }

        String curLine = curExp.toString();
        String requiredString;
        Expression expJ;

        for (Expression anInputProof : inputProof) {
            expJ = anInputProof;
            if (expJ instanceof BinOperations.Implication) {
                requiredString = parse("(?" + var + ((BinOperations.Implication) expJ).getLeft().toString() +
                        "->" + ((BinOperations.Implication) expJ).getRight().toString() + ")");
                if (requiredString.equals(curLine)) {
                    return isVarFreeInExpression(((BinOperations.Implication) curExp).getRight(), var);
                }
            }
        }
        return "NOT OK";
    }

    private String isVarFreeInExpression(Expression exp, String var) {
        if (exp.firstFreeEntry(var) != null) {
            return ("используется правило с квантором по" +
                    " переменной " + var + " входящей" +
                    " свободно в допущение " + exp.toString());
        }


        if (alphaExp.firstFreeEntry(var) != null) {
            return ("используется правило с квантором по" +
                    " переменной " + var + " входящей" +
                    " свободно в допущение " + alpha);
        }

        return null;
    }

    private String parse(String curLine) throws MyException {
        return (new ProofParser()).parse(curLine).toString();
    }

    public void run() {
        try {
            in = new FastScanner(new File(inputFile));
            out = new PrintWriter(new File(outputFile));

            solve();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException me) {
            System.out.println(me.getMsg());
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AxiomChecker {
    private static Pattern[] statementsAxioms = {
            Pattern.compile(
                    "^[\\(]{1}(.+)->[\\(]{1}(.+)->\\1[\\)]{2}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)->(.+)[\\)]{1}->[\\(]{2}\\1->" +
                            "[\\(]{1}\\2->(.+)[\\)]{2}->[\\(]{1}\\1->\\3[\\)]{3}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)->[\\(]{1}(.+)->[\\(]{1}\\1&\\2[\\)]{3}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)&(.+)[\\)]{1}->\\1[\\)]{1}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)&(.+)[\\)]{1}->\\2[\\)]{1}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)->[\\(]{1}\\1\\|(.+)[\\)]{2}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)->[\\(]{1}(.+)\\|\\1[\\)]{2}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)->(.+)[\\)]{1}->[\\(]{2}(.+)" +
                            "->\\2[\\)]{1}->[\\(]{2}\\1\\|\\3[\\)]{1}->\\2[\\)]{3}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)->(.+)[\\)]{1}->[\\(]{2}\\1->!\\2[\\)]{1}->!\\1[\\)]{2}$"),
            Pattern.compile("^[\\(]{1}!!(.+)->\\1[\\)]{1}$")
    };


    private static Pattern[] arithmeticAxioms = {
            Pattern.compile(
                    "^[\\(]{2}(.+)=(.+)[\\)]{1}->[\\(]{1}\\1'=\\2'[\\)]{2}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)=(.+)[\\)]{1}->[\\(]{2}\\1=(.+)[\\)]{1}->[\\(]{1}" +
                            "\\2=\\3[\\)]{3}$"),
            Pattern.compile(
                    "^[\\(]{2}(.+)'=(.+)'[\\)]{1}->[\\(]{1}\\1=\\2[\\)]{2}$"),
            Pattern.compile(
                    "^![\\(]{1}(.+)'=0[\\)]{1}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)\\+(.+)'=[\\(]{1}\\1\\+\\2[\\)]{1}'[\\)]{1}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)\\+0=\\1[\\)]{1}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)\\*0=0[\\)]{1}$"),
            Pattern.compile(
                    "^[\\(]{1}(.+)\\*(.+)'=\\1\\*\\2\\+\\1[\\)]{1}$")


    };


    private static boolean flag = false;
    private static String errorMsg = "";


    public static boolean doesMatchAxioms(Expression exp) {

        flag = false;
        errorMsg = "";

        String expAsString = exp.toString();
        Matcher matcher;
        for (Pattern statementsAxiom : statementsAxioms) {
            matcher = statementsAxiom.matcher(expAsString);
            if (matcher.matches()) {
                return true;
            }
        }

        return doesMatchForallAxiom(exp) || !flag && doesMatchExistAxiom(exp) || doesMatchArithmeticsAxioms(exp);


    }

    private static boolean doesMatchForallAxiom(Expression exp) {
        if (exp instanceof BinOperations.Implication) {
            if (((BinOperations.Implication) exp).left instanceof Quantors.Forall) {
                String var = (((Quantors.Forall) ((BinOperations.Implication) exp).left).var);
                Expression phi = (((Quantors.Forall) ((BinOperations.Implication) exp).left).expression);
                Expression newPhi = ((BinOperations.Implication) exp).right;
                return areTwoExpEqualAfterSubst(phi, newPhi, var);
            }
        }
        return false;
    }

    private static boolean doesMatchExistAxiom(Expression exp) {
        if (exp instanceof BinOperations.Implication) {
            if (((BinOperations.Implication) exp).right instanceof Quantors.Exist) {
                String var = (((Quantors.Exist) ((BinOperations.Implication) exp).right).var);
                Expression phi = (((Quantors.Exist) ((BinOperations.Implication) exp).right).expression);
                Expression newPhi = ((BinOperations.Implication) exp).left;
                return areTwoExpEqualAfterSubst(phi, newPhi, var);
            }
        }
        return false;
    }

    private static boolean doesMatchArithmeticsAxioms(Expression exp) {
        Matcher matcher;
        String expAsString = exp.toString();
        Expression tmp;

        Expression left;
        Expression right;
        if (exp instanceof BinOperations.Implication) {
            if (((BinOperations.Implication) exp).left instanceof BinOperations.Equality &&
                    ((BinOperations.Implication) exp).right instanceof BinOperations.Equality) {

                matcher = arithmeticAxioms[0].matcher(expAsString);
                if (matcher.matches()) {
                    return true;
                }

                left = ((BinOperations.Implication) exp).left;
                right = ((BinOperations.Implication) exp).right;
                if (((BinOperations.Equality) right).left instanceof BinOperators.Incr &&
                        ((BinOperations.Equality) right).right instanceof BinOperators.Incr) {
                    if (((BinOperations.Equality) left).left.toString().equals(
                            ((BinOperators.Incr) ((BinOperations.Equality) right).left).term.toString())
                            &&
                            ((BinOperations.Equality) left).right.toString().equals(
                                    ((BinOperators.Incr) ((BinOperations.Equality) right).right).term.toString())) {
                        return true;
                    }
                }
            }

            if (((BinOperations.Implication) exp).left instanceof BinOperations.Equality &&
                    ((BinOperations.Implication) exp).right instanceof BinOperations.Implication) {
                tmp = ((BinOperations.Implication) exp).right;
                if (((BinOperations.Implication) tmp).left instanceof BinOperations.Equality &&
                        ((BinOperations.Implication) tmp).right instanceof BinOperations.Equality) {
                    matcher = arithmeticAxioms[1].matcher(expAsString);
                    if (matcher.matches()) {
                        return true;
                    }
                }

            }

            if (((BinOperations.Implication) exp).left instanceof BinOperations.Equality &&
                    ((BinOperations.Implication) exp).right instanceof BinOperations.Equality) {
                matcher = arithmeticAxioms[2].matcher(expAsString);
                if (matcher.matches()) {
                    return true;
                }

                left = ((BinOperations.Implication) exp).left;
                right = ((BinOperations.Implication) exp).right;
                if (((BinOperations.Equality) left).left instanceof BinOperators.Incr &&
                        ((BinOperations.Equality) left).right instanceof BinOperators.Incr) {
                    if (((BinOperations.Equality) right).left.toString().equals(
                            ((BinOperators.Incr) ((BinOperations.Equality) left).left).term.toString())
                            &&
                            ((BinOperations.Equality) right).right.toString().equals(
                                    ((BinOperators.Incr) ((BinOperations.Equality) left).right).term.toString())) {
                        return true;
                    }
                }

            }
        }

        if (exp instanceof BinOperations.Negation &&
                ((BinOperations.Negation) exp).expression instanceof BinOperations.Equality) {

            matcher = arithmeticAxioms[3].matcher(expAsString);
            if (matcher.matches()) {
                return true;
            }
        }

        if (exp instanceof BinOperations.Equality) {
            for (int i = 4; i < 8; i++) {
                matcher = arithmeticAxioms[i].matcher(expAsString);
                if (matcher.matches()) {
                    return true;
                }
            }


            if (((BinOperations.Equality) exp).left instanceof BinOperators.Plus &&
                    ((BinOperations.Equality) exp).right instanceof BinOperators.Incr) {
                left = ((BinOperations.Equality) exp).left;
                right = ((BinOperations.Equality) exp).right;
                if (((BinOperators.Incr) right).term instanceof BinOperators.Plus &&
                        ((BinOperators.Plus) left).right instanceof BinOperators.Incr) {
                    // if (a1 == a2 && b1 == b2)
                    if (((BinOperators.Plus) left).left.toString().equals(
                            ((BinOperators.Plus) ((BinOperators.Incr) right).term).left.toString())
                            &&
                            ((BinOperators.Incr) ((BinOperators.Plus) left).right).term.toString().equals(
                                    ((BinOperators.Plus) ((BinOperators.Incr) right).term).right.toString())) {
                        return true;
                    }
                }
            }

            if (((BinOperations.Equality) exp).left instanceof BinOperators.Mul &&
                    ((BinOperations.Equality) exp).right instanceof BinOperators.Plus) {

                left = ((BinOperations.Equality) exp).left;
                right = ((BinOperations.Equality) exp).right;
                if (((BinOperators.Mul) left).right instanceof BinOperators.Incr &&
                        ((BinOperators.Plus) right).left instanceof BinOperators.Mul) {
                    if (((BinOperators.Mul) left).left.toString().equals(
                            ((BinOperators.Plus) right).right.toString())
                            &&
                            ((BinOperators.Mul) left).left.toString().equals(
                                    ((BinOperators.Mul) ((BinOperators.Plus) right).left).left.toString())
                            &&
                            ((BinOperators.Incr) ((BinOperators.Mul) left).right).term.toString().equals(
                                    ((BinOperators.Mul) ((BinOperators.Plus) right).left).right.toString())) {

                        return true;
                    }
                }
            }
        }

        Expression phi;
        Expression phiWithZero;
        Expression phiWithInc;
        String x;
        if (exp instanceof BinOperations.Implication) {
            phi = ((BinOperations.Implication) exp).right;
            if (((BinOperations.Implication) exp).left instanceof BinOperations.And) {
                tmp = ((BinOperations.And) (((BinOperations.Implication) exp).left)).right;
                if (tmp instanceof Quantors.Forall) {
                    x = ((Quantors.Forall) tmp).getVar();
                    phiWithZero = ((BinOperations.And) (((BinOperations.Implication) exp).left)).left;
                    String tmpString = phi.toStringWVar(Terms.createZero(), x);
                    if (tmpString.equals(phiWithZero.toString())) {
                        tmp = ((Quantors.Forall) tmp).getExpression();
                        if (tmp instanceof BinOperations.Implication) {
                            phiWithInc = ((BinOperations.Implication) tmp).getRight();
                            tmpString = phi.toStringWVar(BinOperators.createIncr(Terms.createVariable(x)), x);
                            if (tmpString.equals(phiWithInc.toString())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean areTwoExpEqualAfterSubst(Expression phi, Expression newPhi, String var) {
        List<Pair> pathToFirstFreeEntry = phi.firstFreeEntry(var);
        if (pathToFirstFreeEntry == null) {
            return (newPhi.toString().equals(phi.toString()));
        }
        Term replacingTerm = getReplacingTerm(pathToFirstFreeEntry, newPhi);
        if (replacingTerm != null) {
            String expWithReplacedVar = phi.toStringWVar(replacingTerm, var);
            if (expWithReplacedVar.equals(newPhi.toString())) {
                if (phi.canReplace(replacingTerm, var)) {
                    return true;
                } else {
                    flag = true;
                    errorMsg = ("терм " + replacingTerm.toString() +
                            " не свободен для подстановки в формулу " +
                            phi.toString() + " вместо переменной " + var);
                }
            }
        }
        return false;
    }

    private static Term getReplacingTerm(List<Pair> path, Expression newPhi) {
        Expression exp = newPhi;
        for (Pair expPair : path) {
            switch (expPair.expressionType) {
                case Bank.IMPLICATION:
                    if (exp instanceof BinOperations.Implication) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((BinOperations.Implication) exp).left;
                        } else {
                            exp = ((BinOperations.Implication) exp).right;
                        }
                        continue;
                    } else {
                        return null;
                    }
                case Bank.CONJUCNTION:
                    if (exp instanceof BinOperations.And) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((BinOperations.And) exp).left;
                        } else {
                            exp = ((BinOperations.And) exp).right;
                        }
                        continue;
                    } else {
                        return null;
                    }
                case Bank.DISJUCNTION:
                    if (exp instanceof BinOperations.Or) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((BinOperations.Or) exp).left;
                        } else {
                            exp = ((BinOperations.Or) exp).right;
                        }
                        continue;
                    } else {
                        return null;
                    }
                case Bank.EQUALITY:
                    if (exp instanceof BinOperations.Equality) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((BinOperations.Equality) exp).left;
                        } else {
                            exp = ((BinOperations.Equality) exp).right;
                        }
                        continue;
                    } else {
                        return null;
                    }
                case Bank.NEGATION:
                    if (exp instanceof BinOperations.Negation) {
                        exp = ((BinOperations.Negation) exp).expression;
                        continue;
                    } else {
                        return null;
                    }
                case Bank.FORALL:
                    if (exp instanceof Quantors.Forall &&
                            ((Quantors.Forall) exp).var.equals(expPair.additionalInfo)) {

                        exp = ((Quantors.Forall) exp).expression;
                        continue;
                    } else {
                        return null;
                    }
                case Bank.EXIST:
                    if (exp instanceof Quantors.Exist &&
                            ((Quantors.Exist) exp).var.equals(expPair.additionalInfo)) {

                        exp = ((Quantors.Exist) exp).expression;
                        continue;
                    } else {
                        return null;
                    }
                case Bank.PREDICATE:
                    if (exp instanceof Predicate) {
                        Pair pair = (Pair) expPair.additionalInfo;
                        List<Term> subTerms = ((Predicate) exp).getSubTerms();

                        if (pair.expressionType.equals(((Predicate) exp).getValue()) &&
                                subTerms.size() > ((Integer) pair.additionalInfo)) {

                            exp = subTerms.get(((Integer) pair.additionalInfo));
                            continue;
                        }
                    }
                    return null;
                case Bank.PLUS:
                    if (exp instanceof BinOperators.Plus) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((BinOperators.Plus) exp).left;
                        } else {
                            exp = ((BinOperators.Plus) exp).right;
                        }
                        continue;
                    } else {
                        return null;
                    }
                case Bank.MULTIPLY:
                    if (exp instanceof BinOperators.Mul) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((BinOperators.Mul) exp).left;
                        } else {
                            exp = ((BinOperators.Mul) exp).right;
                        }
                        continue;
                    } else {
                        return null;
                    }
                case Bank.INCREMENT:
                    if (exp instanceof BinOperators.Incr) {
                        exp = ((BinOperators.Incr) exp).term;
                        continue;
                    } else {
                        return null;
                    }
                case Bank.TERMWITHARGS:
                    if (exp instanceof Terms.TermWithArgs) {

                        Pair pair = (Pair) expPair.additionalInfo;
                        String value = ((Terms.TermWithArgs) exp).getValue();
                        List<Term> subTerms = ((Terms.TermWithArgs) exp).getSubTerms();
                        if (value.equals(pair.expressionType) &&
                                subTerms.size() > ((Integer) pair.additionalInfo)) {
                            exp = subTerms.get(((Integer) pair.additionalInfo));
                            continue;
                        }
                    } else {
                        return null;
                    }
                case Bank.BRACKETS:
                    if (exp instanceof BinOperators.Brackets) {
                        exp = ((BinOperators.Brackets) exp).term;
                        continue;
                    } else {
                        return null;
                    }
                case Bank.VARIABLE:
                    if (exp instanceof Term) {
                        return (Term) exp;
                    } else {
                        return null;
                    }
            }
        }
        return null;
    }

    public static boolean getFlag() {
        return flag;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }
}

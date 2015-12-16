import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ProofParser {

    private Stack<Expression> implicationList;

    public ProofParser() {
        implicationList = new Stack<Expression>();
    }

    public Expression parse(String s) throws MyException {
        if (s.isEmpty()) throw new MyException("String to parse is empty");
        Res<Expression> res = implication(s);
        if (!res.rest.isEmpty()) {
            throw new MyException("Error in \'" + res.rest + "\'");
        }
        implicationList.clear();
        return res.exp;
    }

    private Res implication(String s) throws MyException {
        Res<Expression> current = conjunction(s);
        int localCounter = 0;

        if (current.exp instanceof Term) {
            return current;
        }

        while (current.rest.length() > 1) {
            char sign1 = current.rest.charAt(0);
            char sign2 = current.rest.charAt(1);
            if (sign1 != '-' || sign2 != '>') {
                break;
            }
            implicationList.add(current.exp);
            localCounter++;

            String next = current.rest.substring(2);
            current = conjunction(next);
            if (current.exp instanceof Term) {
                throw new MyException("Implication had a bad argument in \'" + next + "\'");
            }
        }

        if (!implicationList.isEmpty() && (localCounter > 0)) {
            implicationList.add(current.exp);
            Expression exp = implicationList.pop();
            for (int i = localCounter - 1; i > -1; i--) {
                exp = BinOperations.createImplication(implicationList.pop(), exp);
            }

            return new Res<Expression>(exp, current.rest);
        }

        return new Res<Expression>(current.exp, current.rest);
    }

    private Res conjunction(String s) throws MyException {
        Res<Expression> current = disjunction(s);
        Expression exp = current.exp;

        if (exp instanceof Term) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '&') {
                break;
            }

            String next = current.rest.substring(1);
            current = disjunction(next);
            if (!(current.exp instanceof Term)) {
                exp = BinOperations.makeAnd(exp, current.exp);
            } else {
                throw new MyException("Conjunction had a bad argument\n");
            }
        }

        return new Res<Expression>(exp, current.rest);
    }

    private Res disjunction(String s) throws MyException {
        Res<Expression> current = unaryOperations(s);
        Expression exp = current.exp;

        if (exp instanceof Term) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '|') {
                break;
            }

            String next = current.rest.substring(1);
            current = unaryOperations(next);
            if (!(current.exp instanceof Term)) {
                exp = BinOperations.makeOr(exp, current.exp);
            } else {
                throw new MyException("Disjunction had a bad argument\n");
            }
        }

        return new Res(exp, current.rest);
    }

    private Res unaryOperations(String s) throws MyException {
        String next;
        Res<Expression> current;
        if (s.length() > 0 && s.charAt(0) == '!') {
            next = s.substring(1);
            current = unaryOperations(next);
            if (current.exp instanceof Term) {
                throw new MyException("Negation had a bad argument\n");
            } else {
                return new Res<Expression>(BinOperations.createNegation(current.exp), current.rest);
            }
        }

        if (s.length() > 1 && (s.charAt(0) == '@' || s.charAt(0) == '?') &&
                Character.isLetter(s.charAt(1)) && Character.isLowerCase(s.charAt(1))) {
            StringBuilder var = new StringBuilder();
            var.append(s.charAt(1));
            int i = 2;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                var.append(s.charAt(i));
                i++;
            }

            next = s.substring(var.length() + 1);
            current = unaryOperations(next);
            if (current.exp instanceof Term) {
                throw new MyException("Quantor \'" + s.charAt(0) + "\' had a bad argument\n");
            } else {
                if (s.charAt(0) == '@') {
                    return new Res<Expression>(Quantors.createForall(current.exp, var.toString()), current.rest);
                } else {
                    return new Res<Expression>(Quantors.createExist(current.exp, var.toString()), current.rest);
                }
            }
        }

        return equality(s);
    }

    private Res equality(String s) throws MyException {
        Res<Expression> current = plus(s);
        Expression exp = current.exp;

        if (!(exp instanceof Term)) {
            return current;
        }

        if (current.rest.length() > 0 && (current.rest.charAt(0) == '=')) {
            String next = current.rest.substring(1);
            current = plus(next);
            if (current.exp instanceof Term) {
                exp = BinOperations.createEquality((Term) exp,  (Term) current.exp);
            } else {
                throw new MyException("Equality had a bad argument\n");
            }
        }

        return new Res(exp, current.rest);
    }

    private Res plus(String s) throws MyException {
        Res current = multiply(s);
        Expression exp = (Expression) current.exp;
        if (!(exp instanceof Term)) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '+') {
                break;
            }

            String next = current.rest.substring(1);
            current = multiply(next);
            if (current.exp instanceof Term) {
                exp = BinOperators.createPlus((Term) exp,  (Term) current.exp);
            } else {
                throw new MyException("Plus had a bad argument\n");
            }
        }

        return new Res(exp, current.rest);
    }

    private Res multiply(String s) throws MyException {
        Res current = increment(s);
        Expression exp = (Expression) current.exp;
        if (!(exp instanceof Term)) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '*') {
                break;
            }

            String next = current.rest.substring(1);
            current = increment(next);
            if (current.exp instanceof Term) {
                exp = BinOperators.createMul((Term) exp, (Term) current.exp);
            } else {
                throw new MyException("Multiply had a bad argument\n");
            }

        }

        return new Res(exp, current.rest);
    }

    private Res increment(String s) throws MyException {
        Res current = brackets(s);
        Expression exp = (Expression) current.exp;
        if (current.exp instanceof Term) {
            while (current.rest.length() > 0 && current.rest.charAt(0) == '\'') {
                exp = BinOperators.createIncr((Term) exp);
                current.rest = current.rest.substring(1);
            }
        }

        return new Res(exp, current.rest);
    }

    private Res brackets(String s) throws MyException {
        if (s.length() == 0) {
            throw new MyException("Error in \"" + s + "\"");
        }

        char ch = s.charAt(0);
        if (ch == '(') {
            Res r = implication(s.substring(1));
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new MyException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }

            if (r.exp instanceof Term) {
                if (r.rest.length() > 0 && r.rest.charAt(0) == '\'') {
                    r.exp = BinOperators.createIncr((Term) r.exp);
                    r.rest = r.rest.substring(1);
                }
            }

            return r;
        }

        return predicate(s);
    }

    private Res predicate(String s) throws MyException {
        StringBuilder res = new StringBuilder();
        int i = 0;
        if (s.length() > 0 && (Character.isLetter(s.charAt(0)) &&
                               Character.isUpperCase(s.charAt(0))) ) {
            res.append(s.charAt(0));
            i++;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                res.append(s.charAt(i));
                i++;
            }
        }

        if (res.length() == 0) {
            Res<Term> result = terms(s);
            return result;
        }

        s = s.substring(res.length());

        Res<List<Term>> result;
        if (s.length() > 0 && s.charAt(0) == '(') {
            result = comma(s.substring(1));
            if (result.rest.length() > 0 && result.rest.charAt(0) == ')') {
                return new Res<Predicate>(new Predicate(res.toString(), result.exp), result.rest.substring(1));
            } else {
                throw new MyException("Error in \"" + s + "\"");
            }
        } else {
            return new Res<Predicate>(new Predicate(res.toString()), s);
        }
    }

    private Res<Term> bracketsInTerms(String s) throws MyException {
        if (s.length() == 0) {
            throw new MyException("Error in \"" + s + "\"");
        }

        char ch = s.charAt(0);
        if (ch == '(') {
            Res<Term> r = plus(s.substring(1));
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new MyException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }
            return new Res<Term>(BinOperators.createBrackets(r.exp), r.rest);
        }
        return terms(s);
    }

    private Res<Term> terms(String s) throws MyException {
        StringBuilder res = new StringBuilder();
        int i = 0;

        if (s.length() > 0 && s.charAt(0) == '0') {
            return new Res<Term>(Terms.createZero(), s.substring(1));
        }

        if (s.length() > 0 && (Character.isLetter(s.charAt(0)) &&
            Character.isLowerCase(s.charAt(0))) ) {

            res.append(s.charAt(0));
            i++;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                res.append(s.charAt(i));
                i++;
            }
        }

        if (res.length() == 0) {
            throw new MyException("Error in \"" + s + "\". Most probably some term is missed.");
        }

        s = s.substring(res.length());

        Res<List<Term>> result;
        if (s.length() > 0 && s.charAt(0) == '(') {
            result = comma(s.substring(1));
            if (result.rest.length() > 0 && result.rest.charAt(0) == ')') {
                return new Res<Term>(Terms.createTermWithArgs(res.toString(), result.exp), result.rest.substring(1));
            } else {
                throw new MyException("Error in \"" + s + "\"");
            }
        } else {
            return new Res<Term>(Terms.createVariable(res.toString()), s);
        }
    }

    private Res<List<Term>> comma(String s) throws MyException {
        Res<Term> current = plus(s);
        Term exp = current.exp;

        List<Term> terms = new ArrayList();
        terms.add(exp);

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != ',') {
                break;
            }

            String next = current.rest.substring(1);
            current = plus(next);

            terms.add(current.exp);
        }

        return new Res(terms, current.rest);
    }
}

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserForExp {

    public List<String> getAllVariables(String line) {
        Set<String> matches = new HashSet<String>();
        String regexp = "[A-Z][0-9]*";
        Matcher m = Pattern.compile("(?=(" + regexp + "))").matcher(line);
        while (m.find()) {
            matches.add(m.group(1));
        }

        return new ArrayList<>(matches);
    }

    private static class StringWithBrackets {
        private final String string;
        private final int[] indexOfPairBracket;

        private StringWithBrackets(String string) {
            this.string = string;
            this.indexOfPairBracket = new int[string.length()];
            Stack<Integer> stack = new Stack<Integer>();
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == '(') {
                    stack.push(i);
                } else if (string.charAt(i) == ')') {
                    if (stack.empty()) {
                        throw new IllegalArgumentException("Paired bracket was not found at " + i);
                    }
                    //Preconditions.checkArgument(!stack.empty(), "Paired bracket was not found at " + i);
                    int openIndex = stack.pop();
                    indexOfPairBracket[openIndex] = i;
                    indexOfPairBracket[i] = openIndex;
                }
            }
            if (!stack.empty()) {
                throw new IllegalArgumentException("Bracket balance is broken");
            }
            //Preconditions.checkArgument(stack.empty(), "Bracket balance is broken");
        }

        public char charAt(int index) {
            return string.charAt(index);
        }

        public int getClosingBracket(int openIndex) {
            if (string.charAt(openIndex) != '(') {
                throw new IllegalArgumentException("Not open bracket");
            }
            //Preconditions.checkArgument(string.charAt(openIndex) == '(', "Not open bracket");
            return indexOfPairBracket[openIndex];
        }

        public int getOpeningBracket(int closeIndex) {
            if (string.charAt(closeIndex) != ')') {
                throw new IllegalArgumentException("Not close bracket");
            }
            //Preconditions.checkArgument(string.charAt(closeIndex) == ')', "Not close bracket");
            return indexOfPairBracket[closeIndex];
        }
    }

    public Term parseExpression(String source) {
        return parseExpression(new StringWithBrackets(source), 0, source.length());
    }

    private Term parseExpression(StringWithBrackets source, int from, int to) {
        for (int i = from; i < to; i++) {
            if (source.charAt(i) == '(') {
                i = source.getClosingBracket(i);
                continue;
            }
            if (source.charAt(i) == '-') {
                if (source.charAt(i + 1) != '>') {
                    throw new IllegalArgumentException("Incomplete symbol '->'");
                }
                //Preconditions.checkArgument(source.charAt(i + 1) == '>', "Incomplete symbol '->'");
                return new Sequence(parseDisjunction(source, from, i), parseExpression(source, i + 2, to));
            }
        }
        return parseDisjunction(source, from, to);
    }

    private Term parseDisjunction(StringWithBrackets source, int from, int to) {
        for (int i = to - 1; i >= from; i--) {
            if (source.charAt(i) == ')') {
                i = source.getOpeningBracket(i);
                continue;
            }
            if (source.charAt(i) == '|') {
                return new Or(parseDisjunction(source, from, i), parseConjunction(source, i + 1, to));
            }
        }
        return parseConjunction(source, from, to);
    }

    private Term parseConjunction(StringWithBrackets source, int from, int to) {
        for (int i = to - 1; i >= from; i--) {
            if (source.charAt(i) == ')') {
                i = source.getOpeningBracket(i);
                continue;
            }
            if (source.charAt(i) == '&') {
                return new And(parseConjunction(source, from, i), parseNegation(source, i + 1, to));
            }
        }
        return parseNegation(source, from, to);
    }

    private Term parseNegation(StringWithBrackets source, int from, int to) {
        if (source.charAt(from) == '!') {
            return new Not(parseNegation(source, from + 1, to));
        } else if (source.charAt(from) == '(') {
            if (source.getClosingBracket(from) != to - 1) {
                throw new IllegalArgumentException();
            }
            //Preconditions.checkState(source.getClosingBracket(from) == to - 1);
            return parseExpression(source, from + 1, to - 1);
        } else {
            return createNamed(source.string.substring(from, to));
        }
    }

    protected Term createNamed(String name) {
        if (!name.matches("[A-Z][0-9]*")) {
            throw new IllegalArgumentException("Incorrect name : " + name);
        }
        //Preconditions.checkState(name.matches("[A-Z][0-9]*"), "Incorrect name : " + name);
        return new Constant(name);
    }

}
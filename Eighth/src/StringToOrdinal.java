
public class StringToOrdinal {
    public static String input;
    public static Character currentChar;
    public static StringBuilder tempNumber;

    public static boolean flag = false;
    private static int currentIndex;

    public static Ordinal parse(String s) throws Exception {
        input = s;
        Ordinal v = new Operators.Number(0);
        currentIndex = 0;
        if (!input.isEmpty()) {
            getChar();
            v = expression(false);
        }
        if (currentIndex - 1 != input.length())
            throw new Exception("parse fail");

        return v;
    }

    public static Ordinal primary(boolean get) throws Exception {
        if (get) {
            getChar();
        }
        if (Character.isDigit(currentChar)) {
            int tmp1;
            if (tempNumber.toString().equals(String.valueOf(Integer.MIN_VALUE).substring(1))) {
                flag = true;
            }
            try {
                if (flag) {
                    tempNumber = new StringBuilder();
                    tempNumber.append(Integer.MIN_VALUE);
                }
                tmp1 = Integer.parseInt(tempNumber.toString());
            } catch (Exception e) {
                throw new Exception("parse fail");
            }

            Ordinal tmp = new Operators.Number(tmp1);
            getChar();
            if (currentIndex <= input.length() && (currentChar == 'w' || Character.isDigit(currentChar)))
                throw new Exception("parse fail");
            return tmp;
        } else if (currentChar == '-') {
            flag = false;
            Ordinal tmp = primary(true);
            if (!flag)
                return new Operators.Subtract(new Operators.Number(0), tmp);
            else {
                flag = false;
                return tmp;
            }
        } else if (currentChar == '(') {
            Ordinal tmp = expression(true);
            if (currentChar != ')') {
                throw new Exception("parse fail");
            }
            getChar();
            return tmp;
        } else if (currentChar == ')') {
            throw new Exception("parse fail");
        } else if (currentChar == 'w') {
            Ordinal tmp = new Operators.W();
            getChar();
            if (currentIndex <= input.length() && (Character.isDigit(currentChar) || currentChar == 'w'))
                throw new Exception("parse fail");
            return tmp;
        } else
            throw new Exception("parse fail");

    }

    public static void getChar() throws Exception {
        if (currentIndex > input.length()) {
            throw new Exception("parse fail");
        }
        if (currentIndex >= input.length()) {
            currentIndex++;
            return;
        }
        while (currentIndex < input.length() && Character.isWhitespace(input.charAt(currentIndex))) {
            currentIndex++;
        }
        tempNumber = new StringBuilder();
        boolean flag = false;
        while (currentIndex < input.length() && (Character.isDigit(input.charAt(currentIndex)))) {
            currentChar = input.charAt(currentIndex);
            tempNumber.append(String.valueOf(input.charAt(currentIndex)));
            currentIndex++;
            flag = true;
        }
        if (flag) return;
        if (currentIndex >= input.length()) return;
        if (!(Character.isDigit(input.charAt(currentIndex)))) {
            char tmp = input.charAt(currentIndex);
            currentIndex++;
            currentChar = tmp;
        }
    }



    public static Ordinal subTerminal(boolean get) throws Exception {
        Ordinal left = primary(get);
        while (currentIndex < input.length()) {
            switch (currentChar) {
                case '^': {
                    Ordinal tmp = primary(true);
                    left = new Operators.Power(left, tmp);
                    break;
                }

                default:
                    return left;
            }

        }

        return left;
    }

    public static Ordinal terminal(boolean get) throws Exception {
        Ordinal left = subTerminal(get);

        while (currentIndex < input.length()) {
            switch (currentChar) {
                case '*': {
                    Ordinal tmp = subTerminal(true);
                    left = new Operators.Multiply(left, tmp);
                    break;
                }

                default:
                    return left;
            }

        }

        return left;
    }

    public static Ordinal expression(boolean get) throws Exception {
        Ordinal left = terminal(get);
        while (currentIndex < input.length()) {
            switch (currentChar) {
                case '+': {
                    left = new Operators.Add(left, terminal(true));
                    break;
                }
                case '-': {
                    left = new Operators.Subtract(left, terminal(true));
                    break;
                }
                default:
                    return left;
            }
        }

        return left;
    }

}

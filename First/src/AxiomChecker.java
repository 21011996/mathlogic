
public class AxiomChecker {

    boolean isEqual(Expression a, Expression b) {
        return a.hash == b.hash && compareExpressions(a, b);
    }

    boolean compareExpressions(Expression a, Expression b) {
        return !((a.left != null && b.left == null) || (a.left == null && b.left != null)) && !((a.right != null && b.right == null) || (a.right == null && b.right != null)) && a.s.equals(b.s) && !(a.left != null && b.left != null && !compareExpressions(a.left, b.left)) && !(a.right != null && b.right != null && !compareExpressions(a.right, b.right));
    }

    int isAxiom(Expression v) {
        if (isFirst(v)) return 1;
        else if (isSecond(v)) return 2;
        else if (isThird(v)) return 3;
        else if (isFourth(v)) return 4;
        else if (isFifth(v)) return 5;
        else if (isSixth(v)) return 6;
        else if (isSeventh(v)) return 7;
        else if (isEighth(v)) return 8;
        else if (isNinth(v)) return 9;
        else if (isTenth(v)) return 10;
        return -1;
    }

    boolean isFirst(Expression v) {
        return v != null && v.s.equals("->") && v.right != null && v.right.s.equals("->") && (v.left != null && v.right.right != null && isEqual(v.left, v.right.right));
    }

    boolean isSecond(Expression v) {
        if (v != null && v.s.equals("->") && v.left != null && v.left.s.equals("->") && v.right != null && v.right.s.equals("->")) {
            if (v.right.left != null && v.right.left.s.equals("->") && v.right.right != null && v.right.right.s.equals("->")) {
                if (v.right.left.right != null && v.right.left.right.s.equals("->")) {
                    if (v.left.left != null && v.left.right != null && v.right.left.left != null && v.right.left.right.left != null && v.right.left.right.right != null &&
                            v.right.right.left != null && v.right.right.right != null) {
                        return ((isEqual(v.left.left, v.right.left.left) && isEqual(v.right.right.left, v.left.left)) &&
                                (isEqual(v.left.right, v.right.left.right.left)) &&
                                (isEqual(v.right.right.right, v.right.left.right.right)));
                    }
                }
            }
        }
        return false;
    }

    boolean isThird(Expression v) {
        if (v != null && v.s.equals("->") && v.right != null && v.right.s.equals("->") && v.right.right != null && v.right.right.s.equals("&")) {
            if (v.left != null && v.right.left != null && v.right.right.left != null && v.right.right.right != null) {
                return (isEqual(v.left, v.right.right.left) && isEqual(v.right.left, v.right.right.right));
            }
        }
        return false;
    }

    boolean isFourth(Expression v) {
        if (v != null && v.s.equals("->") && v.left != null && v.left.s.equals("&")) {
            if (v.left.left != null && v.left.right != null && v.right != null) {
                return isEqual(v.left.left, v.right);
            }
        }
        return false;
    }

    boolean isFifth(Expression v) {
        if (v != null && v.s.equals("->") && v.left != null && v.left.s.equals("&")) {
            if (v.left.left != null && v.left.right != null && v.right != null) {
                return isEqual(v.left.right, v.right);
            }
        }
        return false;
    }

    boolean isSixth(Expression v) {
        if (v != null && v.s.equals("->") && v.right != null && v.right.s.equals("|")) {
            if (v.left != null && v.right.left != null && v.right.right != null) {
                return isEqual(v.left, v.right.left);
            }
        }
        return false;
    }

    boolean isSeventh(Expression v) {
        if (v != null && v.s.equals("->") && v.right != null && v.right.s.equals("|")) {
            if (v.left != null && v.right.left != null && v.right.right != null) {
                return isEqual(v.left, v.right.right);
            }
        }
        return false;
    }

    boolean isEighth(Expression v) {
        if (v != null && v.s.equals("->") && v.left != null && v.left.s.equals("->") && v.right != null && v.right.s.equals("->")) {
            if (v.right.left != null && v.right.left.s.equals("->") && v.right.right != null && v.right.right.s.equals("->") &&
                    v.right.right.left != null && v.right.right.left.s.equals("|")) {
                if (v.left.left != null && v.left.right != null && v.right.left.left != null && v.right.left.right != null && v.right.right.right != null &&
                        v.right.right.left.left != null && v.right.right.left.right != null) {
                    return (isEqual(v.left.left, v.right.right.left.left) &&
                            isEqual(v.right.left.left, v.right.right.left.right) &&
                            isEqual(v.left.right, v.right.left.right));
                }
            }
        }
        return false;
    }

    boolean isNinth(Expression v) {
        if (v != null && v.s.equals("->") && v.left != null && v.left.s.equals("->") && v.right != null && v.right.s.equals("->")) {
            if (v.right.left != null && v.right.left.s.equals("->") && v.right.left.right != null && v.right.left.right.s.equals("!") &&
                    v.right.right != null && v.right.right.s.equals("!")) {
                if (v.left.left != null && v.right.left.left != null && v.right.right.right != null && v.left.right != null && v.right.left.right.right != null) {
                    return (isEqual(v.left.left, v.right.left.left) && isEqual(v.left.left, v.right.right.right) &&
                            isEqual(v.left.right, v.right.left.right.right));
                }
            }
        }
        return false;
    }

    boolean isTenth(Expression v) {
        if (v != null && v.s.equals("->") && v.left != null && v.left.s.equals("!") && v.left.right != null && v.left.right.s.equals("!")) {
            if (v.right != null && v.left.right.right != null) {
                return isEqual(v.left.right.right, v.right);
            }
        }
        return false;
    }
}

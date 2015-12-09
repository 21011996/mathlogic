
public class AxiomChecker {

    public static int isAxiom(Term e){
        if (isFirst(e)) return 1;
        if (isSecond(e)) return 2;
        if (isThird(e)) return 3;
        if (isFourth(e)) return 4;
        if (isFifth(e)) return 5;
        if (isSixth(e)) return 6;
        if (isSeventh(e)) return 7;
        if (isEighth(e)) return 8;
        if (isNineth(e)) return 9;
        if (isTenth(e)) return 10;
        return -1;
    }

    private static boolean isFirst(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getRight() instanceof Implication) {
                Implication impl2 = (Implication) impl1.getRight();
                return (impl1.getLeft().equals(impl2.getRight()));
            }
        }
        return false;
    }

    private static boolean isSecond(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getLeft() instanceof Implication) {
                Implication impl2 = (Implication) impl1.getLeft();
                if (impl1.getRight() instanceof Implication) {
                    Implication impl3 = (Implication) impl1.getRight();
                    if (impl3.getLeft() instanceof Implication) {
                        Implication impl4 = (Implication) impl3.getLeft();
                        if (impl4.getRight() instanceof Implication) {
                            Implication impl5 = (Implication) impl4.getRight();
                            if (impl3.getRight() instanceof Implication) {
                                Implication impl6 = (Implication) impl3.getRight();
                                boolean fl1 = impl2.getLeft().equals(impl4.getLeft()) && impl2.getLeft().equals(impl6.getLeft());
                                boolean fl2 = impl2.getRight().equals(impl5.getLeft());
                                boolean fl3 = impl5.getRight().equals(impl6.getRight());
                                return (fl1 && fl2 && fl3);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isThird(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getRight() instanceof Implication) {
                Implication impl2 = (Implication) impl1.getRight();
                if (impl2.getRight() instanceof And) {
                    And conj1 = (And) impl2.getRight();
                    boolean fl1 = impl1.getLeft().equals(conj1.getLeft());
                    boolean fl2 = impl2.getLeft().equals(conj1.getRight());
                    return (fl1 && fl2);
                }
            }
        }
        return false;
    }

    private static boolean isFourth(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getLeft() instanceof And) {
                And conj1 = (And) impl1.getLeft();
                return (impl1.getRight().equals(conj1.getLeft()));
            }
        }
        return false;
    }

    private static boolean isFifth(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getLeft() instanceof And) {
                And conj1 = (And) impl1.getLeft();
                return (impl1.getRight().equals(conj1.getRight()));
            }
        }
        return false;
    }

    private static boolean isSixth(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getRight() instanceof Or) {
                Or disj1 = (Or) impl1.getRight();
                return (impl1.getLeft().equals(disj1.getLeft()));
            }
        }
        return false;
    }

    private static boolean isSeventh(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getRight() instanceof Or) {
                Or disj1 = (Or) impl1.getRight();
                return (impl1.getLeft().equals(disj1.getRight()));
            }
        }
        return false;
    }

    private static boolean isEighth(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getLeft() instanceof Implication) {
                Implication impl2 = (Implication) impl1.getLeft();
                if (impl1.getRight() instanceof Implication) {
                    Implication impl3 = (Implication) impl1.getRight();
                    if (impl3.getLeft() instanceof Implication) {
                        Implication impl4 = (Implication) impl3.getLeft();
                        if (impl3.getRight() instanceof Implication) {
                            Implication impl5 = (Implication) impl3.getRight();
                            if (impl5.getLeft() instanceof Or) {
                                Or disj1 = (Or) impl5.getLeft();
                                boolean fl1 = impl2.getLeft().equals(disj1.getLeft());
                                boolean fl2 = impl2.getRight().equals(impl4.getRight()) && impl2.getRight().equals(impl5.getRight());
                                boolean fl3 = impl4.getLeft().equals(disj1.getRight());
                                return (fl1 && fl2 && fl3);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isNineth(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication)e;
            if (impl1.getLeft() instanceof Implication){
                Implication impl2 = (Implication)impl1.getLeft();
                if(impl1.getRight() instanceof Implication){
                    Implication impl3 = (Implication)impl1.getRight();
                    if (impl3.getLeft() instanceof Implication){
                        Implication impl4 = (Implication)impl3.getLeft();
                        if (impl4.getRight() instanceof Not){
                            Not neg1 = (Not) impl4.getRight();
                            if (impl3.getRight() instanceof Not) {
                                Not neg2 = (Not) impl3.getRight();
                                boolean fl1 = neg2.getExpr().equals(impl2.getLeft());
                                fl1 = fl1 && neg2.getExpr().equals(impl4.getLeft());
                                boolean fl2 = impl2.getRight().equals(neg1.getExpr());
                                return (fl1 && fl2);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isTenth(Term e) {
        if (e instanceof Implication) {
            Implication impl1 = (Implication) e;
            if (impl1.getLeft() instanceof Not) {
                Not neg1 = (Not) impl1.getLeft();
                if (neg1.getExpr() instanceof Not) {
                    Not neg2 = (Not) neg1.getExpr();
                    return impl1.getRight().equals(neg2.getExpr());
                }
            }
        }
        return false;
    }

}

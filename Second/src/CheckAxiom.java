
/**
 * Created by Ilya on 05.12.2015.
 */

public class CheckAxiom {

    private static boolean check1(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getRight() instanceof LinkBetween) {
                LinkBetween impl2 = (LinkBetween) impl1.getRight();
                return (impl1.getLeft().equals(impl2.getRight()));
            }
        }
        return false;
    }

    private static boolean check2(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getLeft() instanceof LinkBetween) {
                LinkBetween impl2 = (LinkBetween) impl1.getLeft();
                if (impl1.getRight() instanceof LinkBetween) {
                    LinkBetween impl3 = (LinkBetween) impl1.getRight();
                    if (impl3.getLeft() instanceof LinkBetween) {
                        LinkBetween impl4 = (LinkBetween) impl3.getLeft();
                        if (impl4.getRight() instanceof LinkBetween) {
                            LinkBetween impl5 = (LinkBetween) impl4.getRight();
                            if (impl3.getRight() instanceof LinkBetween) {
                                LinkBetween impl6 = (LinkBetween) impl3.getRight();
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

    private static boolean check3(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getRight() instanceof LinkBetween) {
                LinkBetween impl2 = (LinkBetween) impl1.getRight();
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

    private static boolean check4(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getLeft() instanceof And) {
                And conj1 = (And) impl1.getLeft();
                return (impl1.getRight().equals(conj1.getLeft()));
            }
        }
        return false;
    }

    private static boolean check5(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getLeft() instanceof And) {
                And conj1 = (And) impl1.getLeft();
                return (impl1.getRight().equals(conj1.getRight()));
            }
        }
        return false;
    }

    private static boolean check6(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getRight() instanceof Or) {
                Or disj1 = (Or) impl1.getRight();
                return (impl1.getLeft().equals(disj1.getLeft()));
            }
        }
        return false;
    }

    private static boolean check7(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getRight() instanceof Or) {
                Or disj1 = (Or) impl1.getRight();
                return (impl1.getLeft().equals(disj1.getRight()));
            }
        }
        return false;
    }

    private static boolean check8(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
            if (impl1.getLeft() instanceof LinkBetween) {
                LinkBetween impl2 = (LinkBetween) impl1.getLeft();
                if (impl1.getRight() instanceof LinkBetween) {
                    LinkBetween impl3 = (LinkBetween) impl1.getRight();
                    if (impl3.getLeft() instanceof LinkBetween) {
                        LinkBetween impl4 = (LinkBetween) impl3.getLeft();
                        if (impl3.getRight() instanceof LinkBetween) {
                            LinkBetween impl5 = (LinkBetween) impl3.getRight();
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

    private static boolean check9(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween)e;
            if (impl1.getLeft() instanceof LinkBetween){
                LinkBetween impl2 = (LinkBetween)impl1.getLeft();
                if(impl1.getRight() instanceof LinkBetween){
                    LinkBetween impl3 = (LinkBetween)impl1.getRight();
                    if (impl3.getLeft() instanceof LinkBetween){
                        LinkBetween impl4 = (LinkBetween)impl3.getLeft();
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

    private static boolean check10(Expression e) {
        if (e instanceof LinkBetween) {
            LinkBetween impl1 = (LinkBetween) e;
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

    public static int ifAxiom(Expression e){
        if (check1(e)) return 1;
        if (check2(e)) return 2;
        if (check3(e)) return 3;
        if (check4(e)) return 4;
        if (check5(e)) return 5;
        if (check6(e)) return 6;
        if (check7(e)) return 7;
        if (check8(e)) return 8;
        if (check9(e)) return 9;
        if (check10(e)) return 10;
        return -1;
    }
}

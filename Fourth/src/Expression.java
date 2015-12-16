import java.util.List;


public interface Expression {
    @Override
    String toString();

    List<Pair> firstFreeEntry(String x);

    String toStringWVar(Term term, String var);

    boolean canReplace(Term term, String var);
}
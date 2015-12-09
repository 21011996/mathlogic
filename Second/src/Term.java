import java.util.Map;

public interface Term {

    public abstract boolean evaluate(Map<String, Boolean> variables);
}

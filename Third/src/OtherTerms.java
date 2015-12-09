import java.util.Map;

public class OtherTerms extends Constant {

    public OtherTerms(String name) {
        super(name);
    }

    @Override
    public boolean evaluate(Map<String, Boolean> values) {
        throw new UnsupportedOperationException("Patterns can not be evaluated!");
    }
}

import java.util.Map;

/**
 * Created by Ilya on 06.10.2015.
 */

public class OtherTerms extends Constant {

    public OtherTerms(String name) {
        super(name);
    }

    @Override
    public boolean compile(Map<String, Boolean> values) {
        throw new UnsupportedOperationException("Patterns can not be evaluated!");
    }
}

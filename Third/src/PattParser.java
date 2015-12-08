import com.google.common.base.Preconditions;

/**
 * Created by Ilya on 15.10.2015.
 */

public class PattParser extends ParserForExp {

    protected Term createNamed(String name) {
        Preconditions.checkState(name.matches("[A-Z][0-9]*"), "Incorrect variable name: " + name);
        return new OtherTerms(name);
    }

}

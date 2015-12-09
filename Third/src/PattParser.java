import com.google.common.base.Preconditions;

public class PattParser extends ParserForExp {

    protected Term createNamed(String name) {
        Preconditions.checkState(name.matches("[A-Z][0-9]*"), "Incorrect name: " + name);
        return new OtherTerms(name);
    }

}

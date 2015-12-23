
public class PattParser extends ParserForExp {

    protected Term createNamed(String name) {
        if (!name.matches("[A-Z][0-9]*")) {
            throw new IllegalArgumentException("Incorrect name: " + name);
        }
        //Preconditions.checkState(name.matches("[A-Z][0-9]*"), "Incorrect name: " + name);
        return new OtherTerms(name);
    }

}

import java.util.Map;

public class Constant implements Term {

    private String name;

    public Constant(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> var) {
        return var.get(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Constant) {
            Constant v = (Constant) o;
            return v.name.equals(name);
        } else {
            return false;
        }
    }

    @Override
    public String toString(){
        return name;
    }

}

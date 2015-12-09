import java.nio.file.Files;
import java.nio.file.Paths;

public class RunTests {

    public static void main(String[] args) {
        int n = 6;
        boolean justDelete = false;

        for (int i = 1; i <= n; i++) {
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\good" + i + ".out"));
            } catch (Exception e) {
            }
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\wrong" + i + ".out"));
            } catch (Exception e) {
            }
        }
        if (!justDelete) {
            for (int i = 1; i <= n; i++) {
                Checker testInstance = new Checker();
                testInstance.mainForTest("good" + i);
            }

            for (int i = 1; i <= n; i++) {
                Checker testInstance = new Checker();
                testInstance.mainForTest("wrong" + i);
            }
        }
    }
}

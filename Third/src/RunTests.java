import java.nio.file.Files;
import java.nio.file.Paths;

public class RunTests {

    public static void main(String[] args) {
        int n = 7;
        boolean justDelete = false;

        for (int i = 1; i <= n; i++) {
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\true" + i + ".out"));
            } catch (Exception e) {
            }
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\false" + i + ".out"));
            } catch (Exception e) {
            }
        }
        if (!justDelete) {
            for (int i = 1; i <= n; i++) {
                ArgumentBuilder testInstance = new ArgumentBuilder();
                testInstance.mainForTest("true" + i);
            }
            ArgumentBuilder testInstance = new ArgumentBuilder();
            testInstance.mainForTest("false" + 1);
        }
    }
}

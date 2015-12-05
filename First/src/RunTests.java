import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Ilya on 05.12.2015.
 */

public class RunTests {

    public static void main(String[] args) {
        int n = 6;
        boolean justDelete = true;

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
                Main testInstance = new Main();
                testInstance.mainForTest("good" + i);
            }

            for (int i = 1; i <= n; i++) {
                Main testInstance = new Main();
                testInstance.mainForTest("wrong" + i);
            }
        }
    }
}

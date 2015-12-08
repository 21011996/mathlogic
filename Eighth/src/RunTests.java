import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Ilya on 05.12.2015.
 */

public class RunTests {

    public static void main(String[] args) {
        int n = 7;
        boolean justDelete = false;

        for (int i = 1; i <= n; i++) {
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\different" + i + ".out"));
            } catch (Exception e) {
            }
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\equal" + i + ".out"));
            } catch (Exception e) {
            }
        }
        if (!justDelete) {
            for (int i = 1; i <= n; i++) {
                Main testInstance = new Main();
                testInstance.mainForTests("different" + i);
            }

            for (int i = 1; i <= n; i++) {
                Main testInstance = new Main();
                testInstance.mainForTests("equal" + i);
            }
        }
    }
}

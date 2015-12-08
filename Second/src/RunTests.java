import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Ilya on 05.12.2015.
 */

public class RunTests {

    public static void main(String[] args) {
        int n = 3;
        boolean justDelete = false;

        for (int i = 0; i < n; i++) {
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\contra" + i + ".out"));
            } catch (Exception e) {
            }
        }
        if (!justDelete) {
            for (int i = 0; i < n; i++) {
                Convertor testInstance = new Convertor();
                testInstance.mainForTest("contra" + i);
            }
        }
    }
}

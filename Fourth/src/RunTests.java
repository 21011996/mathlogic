import java.nio.file.Files;
import java.nio.file.Paths;

public class RunTests {

    public static void main(String[] args) {
        int n = 14;
        boolean justDelete = false;

        for (int i = 1; i <= n; i++) {
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\correct" + i + ".out"));
            } catch (Exception e) {
            }
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "\\incorrect" + i + ".out"));
            } catch (Exception e) {
            }
        }
        if (!justDelete) {
            String name = "correct";
            String name1 = "incorrect";
            for (int i = 1; i <= 14; i++) {
                Main.inputFile = name + i + ".in";
                Main.outputFile = name + i + ".out";
                run();
            }

            System.out.println();

            for (int i = 1; i <= 10; i++) {
                Main.inputFile = name1 + i + ".in";
                Main.outputFile = name1 + i + ".out";
                run();
            }
        }
    }

    private static void run() {
        new Main().run();
    }
}

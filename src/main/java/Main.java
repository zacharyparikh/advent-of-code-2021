import solutions.Day4;
import solutions.Day5;
import util.Solution;

public class Main {
    public static void main(String[] args) {
        Solution solution;
        try {
            solution = Day5.solve();
        } catch (Exception exception) {
            System.out.println("Could not solve");
            exception.printStackTrace();
            return;
        }

        System.out.println(solution);
    }
}

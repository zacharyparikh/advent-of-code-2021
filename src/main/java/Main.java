import solutions.Day4;
import solutions.Day5;
import solutions.Day6;
import solutions.Day7;
import solutions.Day8;
import util.Solution;

public class Main {

    public static void main(String[] args) {
        Solution solution;

        try {
            solution = Day8.solve();
        } catch (Exception exception) {
            System.out.println("Could not solve");
            exception.printStackTrace();
            return;
        }

        System.out.println(solution);
    }
}

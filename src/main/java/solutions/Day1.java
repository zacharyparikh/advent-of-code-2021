package solutions;

import util.Solution;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;
import static util.InputReader.readInput;

public class Day1 {

    static Integer part1() throws IOException {
        var reader = readInput("day1.txt");
        var count = 0;
        Integer last = null;
        for (String line : reader.lines().toList()) {
            var depth = parseInt(line);

            if (last != null && last < depth) {
                count += 1;
            }

            last = depth;
        }

        return count;
    }

    static Integer part2() throws IOException {
        var reader = readInput("day1.txt");
        var count = 0;
        Integer last = null;
        List<Integer> depths = reader.lines().map(Integer::parseInt).toList();

        for (int i = 0; i < depths.size() - 2; i++) {
            var depth = depths.get(i) + depths.get(i + 1) + depths.get(i + 2);

            if (last != null && last < depth) {
                count += 1;
            }

            last = depth;
        }

        return count;
    }

    static public Solution solve() throws IOException {
        return new Solution(part1().toString(), part2().toString());
    }
}

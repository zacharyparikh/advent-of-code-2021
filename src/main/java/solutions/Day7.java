package solutions;

import static util.InputReader.readInput;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import util.Solution;

public class Day7 {

    private static Integer medianPosition(List<Integer> positions) {
        List<Integer> sorted = positions.stream().sorted().toList();

        int size = sorted.size();
        if (size % 2 == 0) {
            int middle1 = sorted.get(size / 2 - 1);
            int middle2 = sorted.get(size / 2);

            return (middle1 + middle2) / 2;
        }

        return sorted.get(size / 2);
    }

    private static Integer fuelSpentToAlign(List<Integer> positions) {
        Integer median = medianPosition(positions);
        return positions.stream().map(p -> Math.abs(p - median)).reduce(0, Integer::sum);
    }

    static String part1(List<Integer> positions) {
        return fuelSpentToAlign(positions).toString();
    }

    static String part2(List<Integer> positions) {
        Integer min = positions.stream().min(Integer::compareTo).orElse(0);
        Integer max = positions.stream().max(Integer::compareTo).orElse(0);
        int fuelSpentToAlign = IntStream.range(min, max)
                .map(end ->
                        positions.stream()
                                .map(start -> {
                                    int distance = Math.abs(start - end);
                                    return (distance * (distance + 1)) / 2;
                                })
                                .reduce(0, Integer::sum)
                )
                .min()
                .orElseThrow();
        return String.valueOf(fuelSpentToAlign);
    }

    public static Solution solve() throws IOException {
        List<Integer> positions;

        try (var reader = readInput("day7.txt")) {
            positions = Arrays.stream(reader.readLine().split(","))
                    .map(Integer::parseInt)
                    .toList();
        }

        return new Solution(part1(positions), part2(positions));
    }
}

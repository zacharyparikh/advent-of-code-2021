package solutions;

import static util.InputReader.readInput;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import util.Solution;

public class Day6 {

    static class School {

        Map<Integer, Long> timerCounts;

        void cycleDays(int days) {
            if (days == 0) {
                return;
            }

            Map<Integer, Long> nextTimerCounts = new HashMap<>();

            timerCounts.forEach((time, count) -> {
                if (time == 0) {
                    nextTimerCounts.put(8, count);
                    nextTimerCounts.merge(6, count, Long::sum);
                    return;
                }

                nextTimerCounts.merge(time - 1, count, Long::sum);
            });

            this.timerCounts = nextTimerCounts;
            cycleDays(days - 1);
        }

        Long countFish() {
            return this.timerCounts.values().stream().reduce(0L, Long::sum);
        }

        public School(String fishTimers) {
            this.timerCounts = Arrays.stream(fishTimers.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.groupingBy(t -> t, Collectors.counting()));
        }
    }

    static String part1(String schoolString) {
        School school = new School(schoolString);
        school.cycleDays(80);

        return school.countFish().toString();
    }

    static String part2(String schoolString) {
        School school = new School(schoolString);
        school.cycleDays(256);

        return school.countFish().toString();
    }

    public static Solution solve() throws IOException {
        String schoolString = "";

        try (var reader = readInput("day6.txt")) {
            schoolString = reader.readLine();
        }

        return new Solution(part1(schoolString), part2(schoolString));
    }
}

package solutions;

import util.Solution;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.InputReader.readInput;

public class Day3 {

    static String mostCommonBit(List<String> lines, int position) {
        var bitCounts = lines.stream()
                .map(line -> line.charAt(position))
                .collect(Collectors.groupingBy(bit -> bit, Collectors.counting()));

        if (
                bitCounts.getOrDefault('0', 0L) >
                        bitCounts.getOrDefault('1', 0L)
        ) {
            return "0";
        } else {
            return "1";
        }
    }

    static String leastCommonBit(List<String> lines, int position) {
        var bitCounts = lines.stream()
                .map(line -> line.charAt(position))
                .collect(Collectors.groupingBy(bit -> bit, Collectors.counting()));

        if (
                bitCounts.getOrDefault('0', 0L) >
                        bitCounts.getOrDefault('1', 0L)
        ) {
            return "1";
        } else {
            return "0";
        }
    }

    static String part1() {
        var reader = readInput("day3.txt");
        List<String> lines = reader.lines().toList();
        int lineLength = lines.get(0).length();

        String gammaRate =
                IntStream.range(0, lineLength)
                        .mapToObj((i) -> mostCommonBit(lines, i))
                        .collect(Collectors.joining());

        int gammaRateNumber = Integer.parseInt(gammaRate, 2);

        String epsilonRate = IntStream.range(0, lineLength)
                .mapToObj((i) -> leastCommonBit(lines, i))
                .collect(Collectors.joining());

        int epsilonRateNumber = Integer.parseInt(epsilonRate, 2);

        return Integer.toString(gammaRateNumber * epsilonRateNumber);
    }

    static String numberWithMostCommonBits(List<String> numbers, int position) {
        if (numbers.size() == 1) {
            return numbers.get(0);
        }

        String bit = mostCommonBit(numbers, position);

        return numberWithMostCommonBits(
                numbers
                        .stream()
                        .filter(n -> Character.toString(n.charAt(position)).equals(bit))
                        .toList(),
                position + 1
        );
    }

    static String numberWithLeastCommonBits(List<String> numbers, int position) {
        if (numbers.size() == 1) {
            return numbers.get(0);
        }

        String bit = leastCommonBit(numbers, position);

        return numberWithLeastCommonBits(
                numbers
                        .stream()
                        .filter(n -> Character.toString(n.charAt(position)).equals(bit))
                        .toList(),
                position + 1
        );
    }

    static String part2() {
        var reader = readInput("day3.txt");
        List<String> lines = reader.lines().toList();

        String oxygenGeneratorRating = numberWithMostCommonBits(lines, 0);
        String co2ScrubberRating = numberWithLeastCommonBits(lines, 0);

        return Integer.toString(
                Integer.parseInt(oxygenGeneratorRating, 2) *
                        Integer.parseInt(co2ScrubberRating, 2)
        );
    }

    public static Solution solve() {

        return new Solution(part1(), part2());
    }
}

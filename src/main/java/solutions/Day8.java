package solutions;

import static util.InputReader.readInput;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import util.Solution;

public class Day8 {

    static final Map<Integer, EnumSet<Segment>> digitSegments = Map.of(
            0, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_LEFT,
                    Segment.TOP_RIGHT,
                    Segment.BOTTOM_LEFT,
                    Segment.BOTTOM_RIGHT,
                    Segment.BOTTOM),

            1, EnumSet.of(Segment.TOP_RIGHT, Segment.BOTTOM_RIGHT),

            2, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_RIGHT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_LEFT,
                    Segment.BOTTOM),

            3, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_RIGHT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_RIGHT,
                    Segment.BOTTOM),

            4, EnumSet.of(
                    Segment.TOP_LEFT,
                    Segment.TOP_RIGHT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_RIGHT),

            5, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_LEFT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_RIGHT,
                    Segment.BOTTOM),

            6, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_LEFT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_LEFT,
                    Segment.BOTTOM_RIGHT,
                    Segment.BOTTOM),

            7, EnumSet.of(Segment.TOP, Segment.TOP_RIGHT, Segment.BOTTOM_RIGHT),

            8, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_LEFT,
                    Segment.TOP_RIGHT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_LEFT,
                    Segment.BOTTOM_RIGHT,
                    Segment.BOTTOM),

            9, EnumSet.of(
                    Segment.TOP,
                    Segment.TOP_LEFT,
                    Segment.TOP_RIGHT,
                    Segment.MIDDLE,
                    Segment.BOTTOM_RIGHT,
                    Segment.BOTTOM)
    );

    static final LinkedHashSet<Integer> uniqueSegmentCounts = new LinkedHashSet<>();

    static {
        uniqueSegmentCounts.add(digitSegments.get(1).size());
        uniqueSegmentCounts.add(digitSegments.get(7).size());
        uniqueSegmentCounts.add(digitSegments.get(4).size());
        uniqueSegmentCounts.add(digitSegments.get(8).size());
    }

    static String part1(List<Display> displays) {

        Stream<String> outputs = displays.stream().flatMap(d -> d.outputs().stream());
        List<String> outputsWithUniqueSegmentCounts = outputs.filter(
                o -> uniqueSegmentCounts.contains(o.length())
        ).toList();

        return String.valueOf(outputsWithUniqueSegmentCounts.size());
    }

    static Map<Character, Segment> analyzePatterns(List<String> patterns) {
        Map<Character, Segment> map = new HashMap<>();
        Map<String, EnumSet<Segment>> known = new HashMap<>();

        Function<String, Set<Character>> getSignals = (pattern) ->
                pattern.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());

        Function<Collection<Character>, String> signalsToKey = signals ->
                signals.stream().sorted().map(String::valueOf).collect(Collectors.joining());

        uniqueSegmentCounts.forEach(segmentCount -> {
            String pattern = patterns.stream()
                    .filter(p -> p.length() == segmentCount).findAny().orElseThrow();

            Set<Character> patternSignals = getSignals.apply(pattern);

            Integer digit = digitSegments.entrySet().stream()
                    .filter(e -> e.getValue().size() == segmentCount)
                    .map(Entry::getKey)
                    .findFirst()
                    .orElseThrow();

            var patternSegments = digitSegments.get(digit);
            var patternKey = signalsToKey.apply(patternSignals);

            LinkedHashMap<String, EnumSet<Segment>> candidates = new LinkedHashMap<>();
            candidates.put(patternKey, patternSegments);

            var queue = new LinkedList<String>();
            queue.add(patternKey);

            while (!queue.isEmpty()) {
                var signalKey = queue.pop();
                var signals = getSignals.apply(signalKey);
                var segments = candidates.get(signalKey);

                known.forEach((knownSignalsKey, knownSegments) -> {

                    var knownSignalsSet = new HashSet<>(getSignals.apply(knownSignalsKey));

                    var signalsDifference = new HashSet<>(signals);
                    signalsDifference.removeAll(knownSignalsSet);

                    var segmentsDifference = segments.clone();
                    segmentsDifference.removeAll(knownSegments);

                    var signalsDifferenceKey = signalsToKey.apply(signalsDifference);

                    if (
                            !signalsDifferenceKey.isEmpty() &&
                                    !candidates.containsKey(signalsDifferenceKey)
                    ) {
                        candidates.remove(signalKey);
                        candidates.put(signalsDifferenceKey, segmentsDifference);
                        queue.push(signalsDifferenceKey);
                    }

                });
            }


            known.putAll(candidates);
        });

        System.out.println(known);

        patterns.forEach(p -> {
            int numSegments = p.length();

            Set<Character> patternSignals = p.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toSet());

            var patternKey = signalsToKey.apply(patternSignals);


        });

        return map;
    }

    static String part2(List<Display> displays) {

        /*
         *
         * edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
         * cg = 1
         * cg = {TR, BR}
         *
         * bcg = 7
         * cg = {TR, BR}
         * b = T
         *
         * cefg = 4
         * cefg = {TL, TR, M, BR}
         * cg = {TR, BR}
         * ef = {TL, M}
         * b = T
         *
         */

        displays.stream().limit(1).forEach(display -> {
            var patterns = display.patterns();
            var signals = analyzePatterns(patterns);
        });

        return "";
    }

    public static Solution solve() throws IOException {
        List<Display> displays;

        try (var reader = readInput("day8.test.txt")) {
            displays = reader.lines().map(line -> {
                String[] parts = line.split(" \\| ");
                String patternsString = parts[0];
                String outputsString = parts[1];

                return new Display(
                        Arrays.stream(patternsString.split(" ")).toList(),
                        Arrays.stream(outputsString.split(" ")).toList()
                );
            }).toList();


        }

        return new Solution(part1(displays), part2(displays));
    }

    enum Segment {
        TOP,
        TOP_LEFT,
        TOP_RIGHT,
        MIDDLE,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        BOTTOM
    }


    record Display(List<String> patterns, List<String> outputs) {

    }

}

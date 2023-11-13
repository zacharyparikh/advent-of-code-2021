package solutions;

import util.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static util.InputReader.readInput;

class Line {
    Point start;
    Point end;

    public Line(String lineString) {
        String[] points = lineString.split(" -> ");
        String[] point = points[0].split(",");
        this.start = new Point(parseInt(point[0]), parseInt(point[1]));

        point = points[1].split(",");
        this.end = new Point(parseInt(point[0]), parseInt(point[1]));
    }

    boolean isHorizontal() {
        return start.x() == end.x();
    }

    boolean isVertical() {
        return start.y() == end.y();
    }

    public boolean isStraight() {
        return this.isHorizontal() || this.isVertical();
    }

    Point getNextPoint(Point start, Point end) {
        if (start.x() == end.x() && start.y() == end.y()) {
            return end;
        }

        if (start.x() == end.x()) {
            if (start.y() < end.y()) {
                return new Point(start.x(), start.y() + 1);
            } else {
                return new Point(start.x(), start.y() - 1);
            }
        }

        if (start.y() == end.y()) {
            if (start.x() < end.x()) {
                return new Point(start.x() + 1, start.y());
            } else {
                return new Point(start.x() - 1, start.y());
            }
        }

        int nextX = start.x();
        if (start.x() < end.x()) {
            nextX += 1;
        } else {
            nextX -= 1;
        }

        int nextY = start.y();
        if (start.y() < end.y()) {
            nextY += 1;
        } else {
            nextY -= 1;
        }

        return new Point(nextX, nextY);
    }

    public List<String> getPoints() {
        ArrayList<Point> points = new ArrayList<>(List.of(start));

        Point current = start;

        while (current.x() != end.x() || current.y() != end.y()) {
            current = getNextPoint(current, end);
            points.add(current);
        }

        return points.stream().map(Point::toString).toList();
    }

    @Override
    public String toString() {
        return start.toString() + " -> " + end.toString();
    }

    record Point(int x, int y) {
        @Override
        public String toString() {
            return x + "," + y;
        }
    }
}

public class Day5 {
    static String part1(List<Line> lines) {
        var overlappingPointsCount = lines.stream()
                .filter(Line::isStraight)
                .flatMap(line -> {
                    return line.getPoints().stream();
                })
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()))
                .values()
                .stream()
                .filter(v -> v > 1).count();

        return Long.toString(overlappingPointsCount);
    }

    static String part2(List<Line> lines) {
        var overlappingPointsCount = lines.stream()
                .flatMap(line -> {
                    return line.getPoints().stream();
                })
                .collect(Collectors.groupingBy(String::toString, Collectors.counting()))
                .values()
                .stream()
                .filter(v -> v > 1).count();

        return Long.toString(overlappingPointsCount);
    }

    public static Solution solve() {
        var reader = readInput("day5.txt");
        List<Line> lines = reader.lines().filter(l -> !l.isBlank()).map(Line::new).toList();

        return new Solution(part1(lines), part2(lines));
    }
}

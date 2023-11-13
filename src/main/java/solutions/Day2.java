package solutions;

import util.Solution;

import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;
import static util.InputReader.readInput;

public class Day2 {

    static List<Command> parseCommands() {
        var reader = readInput("day2.txt");
        return reader.lines().map(line -> {
            String[] parts = line.split(" ");
            String direction = parts[0];
            Integer units = parseInt(parts[1]);
            return new Command(direction, units);
        }).toList();
    }

    static String part1(List<Command> commands) {

        record Position(int x, int y) {

        }

        Position position = commands.stream().reduce(new Position(0, 0),
                (p, c) -> switch (c.direction()) {
                    case "forward" -> new Position(p.x() + c.units(), p.y());
                    case "up" -> new Position(p.x(), p.y() - c.units());
                    case "down" -> new Position(p.x(), p.y() + c.units());
                    default -> p;
                }, (p1, p2) -> new Position(p1.x() + p2.x(), p1.y() + p2.y()));

        return String.valueOf(position.x() * position.y());
    }

    static String part2(List<Command> commands) {

        record AimedPosition(int aim, int x, int y) {

        }

        AimedPosition position = commands.stream().reduce(
                new AimedPosition(0, 0, 0),
                (p, c) -> switch (c.direction()) {
                    case "forward" -> new AimedPosition(p.aim(), p.x() + c.units(),
                            p.y() + p.aim() * c.units());
                    case "up" -> new AimedPosition(p.aim() - c.units(), p.x(), p.y());
                    case "down" -> new AimedPosition(p.aim() + c.units(), p.x(), p.y());
                    default -> p;
                },
                (p1, p2) -> new AimedPosition(p1.aim() + p2.aim(), p1.x() + p2.x(),
                        p1.y() + p2.y()));

        return String.valueOf(position.x() * position.y());
    }

    static public Solution solve() throws IOException {
        List<Command> commands = parseCommands();
        return new Solution(part1(commands), part2(commands));
    }

    record Command(String direction, Integer units) {

    }
}

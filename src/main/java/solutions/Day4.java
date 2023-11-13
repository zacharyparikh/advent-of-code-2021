package solutions;

import util.Solution;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.InputReader.readInput;

class Board {

    List<List<Number>> rows;

    int numColumns;
    Integer lastCalled = null;

    public Board(String boardString) {
        this.rows =
                Arrays.stream(boardString.split("\n"))
                        .map(row -> Arrays.stream(row.split(" "))
                                .filter(n -> !n.isBlank())
                                .map(Integer::parseInt)
                                .map(value -> new Number(value, false))
                                .toList())
                        .toList();

        this.numColumns = this.rows.get(0).size();
    }

    void call(int calledNumber) {

        this.lastCalled = calledNumber;
        this.rows = this.rows.stream()
                .map(row -> row.stream()
                        .map(number -> number.value() == calledNumber ?
                                new Number(number.value(), true) :
                                number)
                        .toList())
                .toList();
    }

    public boolean won() {

        return (
                // Check Rows
                this.rows.stream().anyMatch(row -> row.stream().allMatch(Number::called)) ||
                        // Check Columns
                        IntStream.range(0, numColumns)
                                .anyMatch(i -> rows.stream().allMatch(row -> row.get(i).called()))
        );
    }

    public Integer score() {

        Integer notCalledSum = rows.stream()
                .flatMap(row -> row.stream().filter(n -> !n.called()).map(Number::value))
                .reduce(0, Integer::sum);

        return notCalledSum * this.lastCalled;
    }

    private record Number(Integer value, boolean called) {

    }
}

public class Day4 {
    static String part1(List<Board> boards, List<Integer> numbers) {

        Board winningBoard = findWinningBoard(boards, new ArrayList<>(numbers));

        if (winningBoard != null) {
            return winningBoard.score().toString();
        }

        return "";
    }

    static String part2(List<Board> boards, List<Integer> numbers) {

        Board lastWinningBoard = findLastWinningBoard(boards, new LinkedList<>(numbers));

        if (lastWinningBoard != null) {
            return lastWinningBoard.score().toString();
        }

        return "";
    }

    static Board findWinningBoard(List<Board> boards, List<Integer> numbers) {
        for (Integer n : numbers) {
            boards.forEach(board -> board.call(n));
            Optional<Board> winningBoard = boards.stream().filter(Board::won).findAny();

            if (winningBoard.isPresent()) {
                return winningBoard.get();
            }
        }

        return null;
    }

    static Board findLastWinningBoard(List<Board> boards, LinkedList<Integer> numbers) {
        if (numbers.isEmpty() || boards.isEmpty()) {
            return null;
        }

        if (boards.size() == 1) {
            Board lastBoard = boards.get(0);

            if (lastBoard.won()) {
                return lastBoard;
            }
        }

        List<Board> losingBoards = boards.stream().filter(board -> !board.won()).toList();

        Integer number = numbers.remove(0);
        boards.forEach(board -> board.call(number));

        return findLastWinningBoard(losingBoards, numbers);
    }

    public static Solution solve() throws IOException {
        var reader = readInput("day4.txt");
        String numbersLine = reader.readLine();

        List<Integer> numbers =
                Arrays.stream(numbersLine.split(",")).map(Integer::parseInt).toList();

        String boardsInput = reader.lines().skip(1).collect(Collectors.joining("\n"));

        String[] boardStrings =
                boardsInput.split("\n\n");

        return new Solution(
                part1(Arrays.stream(boardStrings).map(Board::new).toList(), numbers),
                part2(Arrays.stream(boardStrings).map(Board::new).toList(), numbers));
    }
}

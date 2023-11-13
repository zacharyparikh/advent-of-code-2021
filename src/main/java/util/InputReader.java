package util;

import solutions.Day1;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputReader {
    static public BufferedReader readInput(String inputName) {
        var inputStream = InputReader.class.getClassLoader().getResourceAsStream("inputs/" + inputName);
        assert inputStream != null;
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}

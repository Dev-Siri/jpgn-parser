package dev.siri;

import dev.siri.parser.Parser;
import dev.siri.parser.exceptions.PgnParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("ERR: PGN File path not provided.");
            System.exit(1);
        }

        final String filePath = args[0];
        final FileHandler fileHandler = new FileHandler(filePath);

        try {
            final String fileContent = fileHandler.readFileToString();
            final Parser pgnParser = new Parser(fileContent);

            pgnParser.parse();
        } catch (FileNotFoundException _) {
            System.err.println("ERR: PGN File does not exist at " + filePath);
        } catch (IOException _) {
            System.err.println("ERR: An error occurred while reading lines from the file.");
        } catch (PgnParseException e) {
            System.err.println("ERR: " + e.getMessage());
        }
    }
}
package dev.siri.parser;

import dev.siri.parser.exceptions.PgnParseException;

import java.util.List;

public class Parser {
    private final String gamePgn;

    public Parser(String gamePgn) {
        this.gamePgn = gamePgn;
    }

    public void parse() throws PgnParseException {
        // Splits between the move list and the metadata.
        final String[] lines = gamePgn.split("\n\n");

        if (lines.length < 1) {
            throw new PgnParseException("PGN is missing one or more parts, likely the move list.");
        }

        for (final String line : lines) {
            if (line.startsWith("[")) {

            }
        }
    }
}

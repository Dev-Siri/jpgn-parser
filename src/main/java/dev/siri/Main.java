package dev.siri;

import dev.siri.models.*;
import dev.siri.parser.Parser;
import dev.siri.parser.ParserConverters;
import dev.siri.parser.exceptions.PgnParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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
            final Game game = pgnParser.parse();

            final Metadata metadata = game.getMetadata();
            final ArrayList<Move> moves = game.getMoves();

            final Optional<Integer> whiteEloOptioned = metadata.getWhiteElo();
            final String whiteElo = whiteEloOptioned.map(Object::toString).orElse("???");
            final Optional<Integer> blackEloOptioned = metadata.getBlackElo();
            final String blackElo = blackEloOptioned.map(Object::toString).orElse("???");

            System.out.println(
                    metadata.getWhitePlayer() + " (" + whiteElo + ") vs. " + metadata.getBlackPlayer() + " (" + blackElo + ")"
            );

            System.out.println(
                    "Event: " + metadata.getEvent() + "\n"
                            + "Date: " + metadata.getDate() + "\n"
                            + "Round: " + metadata.getRound() + "\n"
                            + "Site: " + metadata.getSite() + "\n"
                            + "Result: " + ParserConverters.resultToStatement(metadata.getResult())

            );

            final Optional<String> annotator = metadata.getAnnotator();
            final Optional<String> eco = metadata.getEco();
            final Optional<String> opening = metadata.getOpening();
            final Optional<Integer> plyCount = metadata.getPlyCount();

            annotator.ifPresent(s -> System.out.println("Annotator: " + s));
            opening.ifPresent(s -> System.out.println("Opening: " + s));
            eco.ifPresent(s -> System.out.println("ECO: " + s));
            plyCount.ifPresent(s -> System.out.println("Ply Count: " + s));

            final HashMap<String, String> extraHeaders = metadata.getExtraHeaders();

            if (!extraHeaders.isEmpty()) {
                System.out.println("--------Extra Headers--------");
                extraHeaders.forEach(
                        (k, v) -> System.out.println(k + ": " + v)
                );
            }

            System.out.println();

            for (final Move move : moves) {
                final Destination destination = move.getDestination();
                final Square square = destination.getSquare();
                final String displayedDestination = ParserConverters.pieceToChar(destination.getPiece());
                final String displayedSquare = ParserConverters.squareToString(square);
                final Optional<String> comment = move.getComment();

                comment.ifPresent(s -> System.out.println("* " + s));

                System.out.println(
                        "Move(" +
                                move.getMoveNumber() + ", " + displayedDestination + ", " + displayedSquare
                                + ")"
                );
            }
        } catch (FileNotFoundException _) {
            System.err.println("ERR: PGN File does not exist at " + filePath);
        } catch (IOException _) {
            System.err.println("ERR: An error occurred while reading lines from the file.");
        } catch (PgnParseException e) {
            System.err.println("ERR: " + e.getMessage());
        }
    }
}
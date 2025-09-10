package dev.siri.parser;

import dev.siri.models.Game;
import dev.siri.models.Metadata;
import dev.siri.models.Move;
import dev.siri.parser.exceptions.PgnParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Parser {
    private final String gamePgn;

    public Parser(String gamePgn) {
        this.gamePgn = gamePgn;
    }

    public Game parse() throws PgnParseException {
        // Splits between the move list and the metadata.
        final String[] lines = gamePgn.split("\n\n");

        if (lines.length < 1) {
            throw new PgnParseException("PGN is missing one or more parts, likely the move list.");
        }

        final String metadataSection = lines[0];
        final String movesSection = lines[1];

        final Metadata metadata = this.parseMetadata(metadataSection);
        final ArrayList<Move> moveList = this.parseMoveList(movesSection);

        return new Game(metadata, moveList);
    }

    private Metadata parseMetadata(String metadataContent) throws PgnParseException {
        final String[] metadataTags = metadataContent.split("\n");

        String whitePlayer = "?";
        String blackPlayer = "?";
        String date = "???.??.??";
        String round = "?";
        String site = "?";
        String event = "?";
        Metadata.GameResult result = Metadata.GameResult.ONGOING;

        Optional<Integer> whiteElo = Optional.empty();
        Optional<Integer> blackElo = Optional.empty();
        Optional<String> annotator = Optional.empty();
        Optional<String> eco = Optional.empty();
        Optional<String> opening = Optional.empty();
        Optional<Integer> plyCount = Optional.empty();

        final HashMap<String, String> extraHeaders = new HashMap<>();

        for (final String metadataTag : metadataTags) {
            if (metadataTag.charAt(0) != '[' || metadataTag.charAt(metadataTag.length() - 1) != ']') {
                throw new PgnParseException("PGN metadata not in proper format, missing either of the enclosing square brackets.");
            }

            final String tagContent = metadataTag.substring(1, metadataTag.length() - 1);
            boolean isReadingValue = false;

            if (tagContent.charAt(tagContent.length() - 1) != '"') {
                throw new PgnParseException("PGN metadata content does not contain an ending quote");
            }

            final StringBuilder metadataName = new StringBuilder();
            final StringBuilder value = new StringBuilder();

            for (int i = 0; i < tagContent.length(); i++) {
                final char tagContentCharToken = tagContent.charAt(i);

                // The white space between the header name and value, like `[White_"Player"]`
                // So it skips over this since white spaces aren't part of metadata names.
                if (!isReadingValue && tagContentCharToken == ' ') {
                    continue;
                }

                // Goes from the left to the right, toggles when the quote opens to true
                // Then toggles back to false on quote end since it expects to encounter the character twice.
                if (tagContentCharToken == '"') {
                    isReadingValue = !isReadingValue;
                    continue;
                }

                if (isReadingValue) {
                    value.append(tagContentCharToken);
                } else {
                    metadataName.append(tagContentCharToken);
                }

            }

            final String metadataNameStr = metadataName.toString();
            final String valueStr = value.toString();

            try {
                switch (metadataNameStr.toLowerCase()) {
                    case "white":
                        whitePlayer = valueStr;
                        break;
                    case "black":
                        blackPlayer = valueStr;
                        break;
                    case "whiteelo":
                        whiteElo = valueStr.equals("?") ? Optional.empty() : Optional.of(Integer.parseInt(valueStr));
                        break;
                    case "blackelo":
                        blackElo = valueStr.equals("?") ? Optional.empty() : Optional.of(Integer.parseInt(valueStr));
                        break;
                    case "event":
                        event = valueStr;
                        break;
                    case "result":
                        result = switch (valueStr) {
                            case "1-0" -> Metadata.GameResult.WHITE_WINS;
                            case "0-1" -> Metadata.GameResult.BLACK_WINS;
                            case "1/2-1/2" -> Metadata.GameResult.DRAW;
                            case "*" -> Metadata.GameResult.ONGOING;
                            default -> result;
                        };
                        break;
                    case "site":
                        site = valueStr;
                        break;
                    case "round":
                        round = valueStr;
                        break;
                    case "date":
                        date = valueStr;
                        break;
                    case "plycount":
                        plyCount = valueStr.equals("?") ? Optional.empty() : Optional.of(Integer.parseInt(valueStr));
                        break;
                    default:
                        extraHeaders.put(metadataNameStr, valueStr);
                }
            } catch (NumberFormatException e) {
                throw new PgnParseException("Non-number value is invalid for WhiteElo, BlackElo and PlyCount");
            }
        }

        final Metadata.Builder metadataBuilder = new Metadata.Builder()
                .whitePlayer(whitePlayer)
                .blackPlayer(blackPlayer)
                .date(date)
                .site(site)
                .event(event)
                .round(round)
                .result(result)
                .extraHeaders(extraHeaders);

        whiteElo.ifPresent(metadataBuilder::whiteElo);
        blackElo.ifPresent(metadataBuilder::blackElo);
        plyCount.ifPresent(metadataBuilder::plyCount);
        annotator.ifPresent(metadataBuilder::annotator);
        eco.ifPresent(metadataBuilder::eco);
        opening.ifPresent(metadataBuilder::opening);

        return metadataBuilder.build();
    }

    private ArrayList<Move> parseMoveList(String movesSection) {
        final ArrayList<Move> moves = new ArrayList<>();

        System.out.println(movesSection);

        return moves;
    }
}

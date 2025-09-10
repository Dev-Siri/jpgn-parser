package dev.siri.parser;

import dev.siri.models.*;
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
                        result = ParserConverters.stringToResult(valueStr);
                        break;
                    case "site":
                        site = valueStr;
                        break;
                    case "round":
                        round = valueStr;
                        break;
                    case "eco":
                        eco = valueStr.equals("?") ? Optional.empty() : Optional.of(valueStr);
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
        final String movesText = movesSection.trim();

        // This means that there are no moves in the PGN at all, it just has
        // a result "1-0", "0-1" right away. So just return the empty list.
        if (
                movesText.equals("1-0") ||
                        movesText.equals("0-1") ||
                        movesText.equals("1/2-1/2") ||
                        movesText.equals("*")
        ) return moves;

        final ArrayList<String> rawMoves = getStrings(movesText);

        for (int i = 0; i < rawMoves.size(); i++) {
            final int moveCount = i + 1;
            final String currentString = rawMoves.get(i);

            String whiteMove = null;
            String blackMove = null;
            Move.Nag nag = Move.Nag.UNKNOWN;

            // The position of the word in the string, like 0 for e4 in "e4 $1 e5"
            int parsingPosition = 0;
            boolean isReadingComment = false;

            final StringBuilder comment = new StringBuilder();
            final StringBuilder builtUpValue = new StringBuilder();

            for (int j = 0; j < currentString.length(); j++) {
                final char currentCharToken = currentString.charAt(j);

                if (currentCharToken == '{') {
                    isReadingComment = true;
                    continue;
                }

                if (currentCharToken == '}') {
                    isReadingComment = false;
                    continue;
                }

                if (isReadingComment) {
                    comment.append(currentCharToken);
                    continue;
                }

                if (currentCharToken == ' ') {
                    final String token = builtUpValue.toString();
                    builtUpValue.setLength(0);

                    if (!token.isEmpty()) {
                        if (parsingPosition == 0) {
                            whiteMove = token;
                        } else if (parsingPosition == 1) {
                            if (token.startsWith("$")) {
                                nag = ParserConverters.stringToNag(token);
                            } else {
                                blackMove = token;
                            }
                        } else {
                            blackMove = token;
                        }
                    }

                    parsingPosition++;
                    continue;
                }

                builtUpValue.append(currentCharToken);
            }

            // Commit any remaining token after the loop.
            if (!builtUpValue.isEmpty()) {
                final String token = builtUpValue.toString();
                if (parsingPosition == 0) {
                    whiteMove = token;
                } else if (parsingPosition == 1) {
                    if (token.startsWith("$")) {
                        nag = ParserConverters.stringToNag(token);
                    } else {
                        blackMove = token;
                    }
                } else {
                    blackMove = token;
                }
            }

            if (whiteMove == null || whiteMove.isEmpty()) {
                throw new PgnParseException("White move cannot be empty.");
            }

            final String commentStr = comment.toString();
            final String commentValue = commentStr.isEmpty() ? null : commentStr;
            final Move.Nag nagValue = nag == Move.Nag.UNKNOWN ? null : nag;

            if (!whiteMove.equals("..")) {
                final Destination.Piece pieceWhite = ParserConverters.sanToPiece(whiteMove);
                final Square squareWhite = ParserConverters.sanToSquare(whiteMove, Game.Player.WHITE);

                final Destination moveDestinationWhite = new Destination(pieceWhite, squareWhite);

                final Move moveWhite = new Move(moveDestinationWhite, commentValue, moveCount, nagValue);
                moves.add(moveWhite);
            }

            if (blackMove != null) {
                final Destination.Piece pieceBlack = ParserConverters.sanToPiece(blackMove);
                final Square squareBlack = ParserConverters.sanToSquare(blackMove, Game.Player.BLACK);

                final Destination moveDestinationBlack = new Destination(pieceBlack, squareBlack);
                final Move moveBlack = new Move(moveDestinationBlack, commentValue, moveCount, nagValue);

                moves.add(moveBlack);
            }
        }

        return moves;
    }

    private ArrayList<String> getStrings(String movesText) {
        final ArrayList<String> rawMoves = new ArrayList<>();
        // Split the moves text so each token begins with the move number (e.g., "1. e4 e5").
        // This regex uses a lookahead to keep the move number with the following text.
        final String[] tokens = movesText.split("(?=\\d+\\.)");

        for (String token : tokens) {
            final String trimmed = token.trim();

            if (trimmed.isEmpty()) continue;

            final String withoutNumber = trimmed.replaceFirst("^\\d+\\.\\s*", "");

            if (withoutNumber.equals("1-0")
                    || withoutNumber.equals("0-1")
                    || withoutNumber.equals("1/2-1/2")
                    || withoutNumber.equals("*")) {
                continue;
            }

            if (withoutNumber.length() == 1) continue;

            rawMoves.add(withoutNumber.replace("\n", " "));
        }
        return rawMoves;
    }
}

package dev.siri.parser;

import dev.siri.models.*;

public class ParserConverters {
    public static Metadata.GameResult stringToResult(String value) {
        return switch (value) {
            case "1-0" -> Metadata.GameResult.WHITE_WINS;
            case "0-1" -> Metadata.GameResult.BLACK_WINS;
            case "1/2-1/2" -> Metadata.GameResult.DRAW;
            default -> Metadata.GameResult.ONGOING;
        };
    }

    public static Move.Nag stringToNag(String value) {
        return switch (value) {
            case "$0" -> Move.Nag.NULL;                 // no annotation
            case "$1" -> Move.Nag.GOOD_MOVE;            // !
            case "$2" -> Move.Nag.MISTAKE;              // ?
            case "$3" -> Move.Nag.BRILLIANT_MOVE;       // !!
            case "$4" -> Move.Nag.BLUNDER;              // ??
            case "$5" -> Move.Nag.INTERESTING_MOVE;     // !?
            case "$6" -> Move.Nag.DUBIOUS_MOVE;         // ?!
            case "$7" -> Move.Nag.FORCED_MOVE;          // ⨀
            case "$8" -> Move.Nag.SINGULAR_MOVE;        // ⟳
            case "$9" -> Move.Nag.WORST_MOVE;           // ⨂

            // Positional/initiative-related NAGs
            case "$10" -> Move.Nag.EQUAL;                // =
            case "$11" -> Move.Nag.UNCLEAR;              // ∞
            case "$12" -> Move.Nag.WHITE_SLIGHT_ADVANTAGE; // ⩲
            case "$13" -> Move.Nag.BLACK_SLIGHT_ADVANTAGE; // ⩱
            case "$14" -> Move.Nag.WHITE_MODERATE_ADVANTAGE; // ±
            case "$15" -> Move.Nag.BLACK_MODERATE_ADVANTAGE; // ∓
            case "$16" -> Move.Nag.WHITE_DECISIVE_ADVANTAGE; // +−
            case "$17" -> Move.Nag.BLACK_DECISIVE_ADVANTAGE; // −+
            case "$18" -> Move.Nag.WHITE_CRUSHING;       // +− (stronger)
            case "$19" -> Move.Nag.BLACK_CRUSHING;       // −+

            // More symbols
            case "$20" -> Move.Nag.ZUGZWANG;             // ○
            case "$21" -> Move.Nag.DEVELOPMENT;          // ⟳
            case "$22" -> Move.Nag.INITIATIVE;           // →
            case "$23" -> Move.Nag.ATTACK;               // →
            case "$24" -> Move.Nag.COUNTERPLAY;          // ⇆
            case "$25" -> Move.Nag.TIME_PRESSURE;        // ⏱
            case "$26" -> Move.Nag.WITH_COMPENSATION;    // ⨀

            default -> Move.Nag.UNKNOWN;
        };
    }

    public static Destination.Piece sanToPiece(String san) {
        if (san.equals("O-O") || san.equals("O-O-O")) {
            return Destination.Piece.KING;
        }

        if (san.length() == 2) {
            return Destination.Piece.PAWN;
        }

        final char pieceLetter = san.charAt(0);

        return switch (pieceLetter) {
            case 'N' -> Destination.Piece.KNIGHT;
            case 'K' -> Destination.Piece.KING;
            case 'B' -> Destination.Piece.BISHOP;
            case 'R' -> Destination.Piece.ROOK;
            case 'Q' -> Destination.Piece.QUEEN;
            default -> Destination.Piece.UNKNOWN;
        };
    }

    public static Square sanToSquare(String san, Game.Player player) {
        final int castleRank = player == Game.Player.WHITE ? 1 : 8;

        if (san.equals("O-O")) {
            return new Square('g', castleRank);
        } else if (san.equals("O-O-O")) {
            return new Square('c', castleRank);
        }

        final String cleanSan = san.replace("+", "").replace("#", "");

        final int fileIndex = getFileIndex(cleanSan);
        final char file = cleanSan.charAt(fileIndex);
        final int rank = Integer.parseInt(cleanSan.charAt(fileIndex + 1) + "");

        return new Square(file, rank);
    }

    private static int getFileIndex(String san) {
        final boolean isPawn = san.length() == 2 || (Character.isLowerCase(san.charAt(0)) && san.contains("x"));
        final boolean isCapture = san.contains("x");
        final boolean isDisambiguated = !isCapture && (
                san.length() >= 4 &&
                        (Character.isLetter(san.charAt(1)) || Character.isDigit(san.charAt(1))));

        if (isCapture) {
            // Pawn capture like dxe5 → target square starts at index 2
            if (isPawn) {
                return 2;
            }
            return isDisambiguated ? 3 : 2;
        } else {
            if (isPawn) {
                return 0;
            } else if (isDisambiguated) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    public static String pieceToChar(Destination.Piece piece) {
        return switch (piece) {
            case PAWN -> "P";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case ROOK -> "R";
            case QUEEN -> "Q";
            case KING -> "K";
            case UNKNOWN -> "NULL";
        };
    }

    public static String resultToStatement(Metadata.GameResult value) {
        return switch (value) {
            case Metadata.GameResult.WHITE_WINS -> "White wins.";
            case Metadata.GameResult.BLACK_WINS -> "Black wins.";
            case Metadata.GameResult.DRAW -> "Draw.";
            case Metadata.GameResult.ONGOING -> "To be decided.";
        };
    }

    public static String squareToString(Square square) {
        return square.getFile() + "" + square.getRank();
    }
}

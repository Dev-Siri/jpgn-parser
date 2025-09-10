package dev.siri.models;

public class Destination {
    public enum Piece {
        PAWN,
        KING,
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
        UNKNOWN,
    }

    private final Piece piece;
    private final Square square;

    public Destination(Piece piece, Square square) {
        this.piece = piece;
        this.square = square;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getSquare() {
        return square;
    }
}

package dev.siri.models;

public class Move {
    public enum Piece {
        PAWN,
        KING,
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
    }

    private final Piece piece;
    private final Square square;

    public Move(Piece piece, Square square) {
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

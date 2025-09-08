package dev.siri.models;

public class Square {
    private final char file;
    private final int rank;

    public Square(char file, int rank) {
        this.file = file;
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
}

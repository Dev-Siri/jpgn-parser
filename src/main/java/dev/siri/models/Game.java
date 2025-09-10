package dev.siri.models;

import java.util.ArrayList;

public class Game {
    private final Metadata metadata;
    private final ArrayList<Move> moves;

    public Game(Metadata metadata, ArrayList<Move> moves) {
        this.metadata = metadata;
        this.moves = moves;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }
}

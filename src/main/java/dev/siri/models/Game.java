package dev.siri.models;

import java.util.List;

public class Game {
    private final Metadata metadata;
    private final List<Move> moves;

    public Game(Metadata metadata, List<Move> moves) {
        this.metadata = metadata;
        this.moves = moves;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public List<Move> getMoves() {
        return moves;
    }
}

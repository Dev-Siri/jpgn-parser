package dev.siri.models;

import java.util.Optional;

public class Move {
    public enum Nag {
        NULL,
        GOOD_MOVE,                // !
        MISTAKE,                  // ?
        BRILLIANT_MOVE,           // !!
        BLUNDER,                  // ??
        INTERESTING_MOVE,         // !?
        DUBIOUS_MOVE,             // ?!
        FORCED_MOVE,              // ⨀
        SINGULAR_MOVE,            // ⟳
        WORST_MOVE,               // ⨂
        EQUAL,                    // =
        UNCLEAR,                  // ∞
        WHITE_SLIGHT_ADVANTAGE,   // ⩲
        BLACK_SLIGHT_ADVANTAGE,   // ⩱
        WHITE_MODERATE_ADVANTAGE, // ±
        BLACK_MODERATE_ADVANTAGE, // ∓
        WHITE_DECISIVE_ADVANTAGE, // +−
        BLACK_DECISIVE_ADVANTAGE, // −+
        WHITE_CRUSHING,           // +− (stronger)
        BLACK_CRUSHING,           // −+
        ZUGZWANG,                 // ○
        DEVELOPMENT,              // ⟳
        INITIATIVE,               // →
        ATTACK,                   // →
        COUNTERPLAY,              // ⇆
        TIME_PRESSURE,            // ⏱
        WITH_COMPENSATION,        // ⨀
        UNKNOWN,
    }

    private final Destination destination;
    private final String comment;
    private final Integer moveNumber;
    private final Nag nag;

    public Move(Destination destination, String comment, int moveNumber, Nag nag) {
        this.destination = destination;
        this.comment = comment;
        this.moveNumber = moveNumber;
        this.nag = nag;
    }

    public Destination getDestination() {
        return destination;
    }

    public Integer getMoveNumber() {
        return moveNumber;
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }

    public Optional<Nag> getNag() {
        return Optional.ofNullable(nag);
    }
}

package dev.siri.parser;

import dev.siri.models.Metadata;
import dev.siri.parser.exceptions.PgnParseException;

public class Parser {
    private final String gamePgn;

    public Parser(String gamePgn) {
        this.gamePgn = gamePgn;
    }

    public void parse() throws PgnParseException {
        // Splits between the move list and the metadata.
        final String[] lines = gamePgn.split("\n\n");

        if (lines.length < 1) {
            throw new PgnParseException("PGN is missing one or more parts, likely the move list.");
        }

        final String metadataSection = lines[0];
        final String movesSection = lines[1];

        final Metadata metadata = this.parseMetadata(metadataSection);
    }

    private Metadata parseMetadata(String metadataContent) {
        System.out.println(metadataContent);
        final Metadata.Builder metadataBuilder = new Metadata.Builder("Garry", "Anatoly", "Moscow", "1984", "World ChampionShip", Metadata.GameResult.WHITE_WINS);

        return metadataBuilder.build();
    }
}

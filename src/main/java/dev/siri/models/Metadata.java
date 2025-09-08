package dev.siri.models;

import java.util.Optional;

public class Metadata {
    public enum GameResult {
        WHITE_WINS,
        BLACK_WINS,
        DRAW,
        ONGOING
    }

    private final String whitePlayer;
    private final String blackPlayer;
    private final String date;
    private final String site;
    private final String event;
    private final GameResult result;
    private final String annotator;
    private final String eco;
    private final String opening;
    private final Integer plyCount;

    public Metadata(Builder builder) {
        this.whitePlayer = builder.whitePlayer;
        this.blackPlayer = builder.blackPlayer;
        this.date = builder.date;
        this.site = builder.site;
        this.event = builder.event;
        this.result = builder.result;
        this.annotator = builder.annotator;
        this.eco = builder.eco;
        this.opening = builder.opening;
        this.plyCount = builder.plyCount;
    }

    public static class Builder {
        // required
        private final String whitePlayer;
        private final String blackPlayer;
        private final String date;
        private final String site;
        private final String event;
        private final GameResult result;

        // optional
        private String annotator;
        private String eco;
        private String opening;
        private Integer plyCount;

        public Builder(String whitePlayer, String blackPlayer, String date, String site, String event, GameResult result) {
            this.whitePlayer = whitePlayer;
            this.blackPlayer = blackPlayer;
            this.date = date;
            this.site = site;
            this.event = event;
            this.result = result;
        }

        public Builder annotator(String annotator) {
            this.annotator = annotator;
            return this;
        }

        public Builder eco(String eco) {
            this.eco = eco;
            return this;
        }

        public Builder opening(String opening) {
            this.opening = opening;
            return this;
        }

        public Builder plyCount(Integer plyCount) {
            this.plyCount = plyCount;
            return this;
        }

        public Metadata build() {
            return new Metadata(this);
        }
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public String getDate() {
        return date;
    }

    public String getSite() {
        return site;
    }

    public String getEvent() {
        return event;
    }

    public GameResult getResult() {
        return result;
    }

    public Optional<String> getAnnotator() {
        return Optional.ofNullable(annotator);
    }

    public Optional<String> getEco() {
        return Optional.ofNullable(eco);
    }

    public Optional<String> getOpening() {
        return Optional.ofNullable(opening);
    }

    public Optional<Integer> getPlyCount() {
        return Optional.ofNullable(plyCount);
    }

}

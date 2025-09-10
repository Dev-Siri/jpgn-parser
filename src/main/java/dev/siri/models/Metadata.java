package dev.siri.models;

import java.util.HashMap;
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
    private final Integer whiteElo;
    private final Integer blackElo;
    private final String round;
    private final String date;
    private final String site;
    private final String event;
    private final GameResult result;
    private final String annotator;
    private final String eco;
    private final String opening;
    private final Integer plyCount;
    private final HashMap<String, String> extraHeaders;

    public Metadata(Builder builder) {
        this.whitePlayer = builder.whitePlayer;
        this.blackPlayer = builder.blackPlayer;
        this.whiteElo = builder.whiteElo;
        this.blackElo = builder.blackElo;
        this.round = builder.round;
        this.date = builder.date;
        this.site = builder.site;
        this.event = builder.event;
        this.result = builder.result;
        this.annotator = builder.annotator;
        this.eco = builder.eco;
        this.opening = builder.opening;
        this.plyCount = builder.plyCount;
        this.extraHeaders = builder.extraHeaders;
    }

    public static class Builder {
        private String whitePlayer;
        private String blackPlayer;
        private Integer whiteElo;
        private Integer blackElo;
        private String date;
        private String round;
        private String site;
        private String event;
        private GameResult result;
        private String annotator;
        private String eco;
        private String opening;
        private Integer plyCount;
        private HashMap<String, String> extraHeaders;

        public Builder whitePlayer(String whitePlayer) {
            this.whitePlayer = whitePlayer;
            return this;
        }

        public Builder blackPlayer(String blackPlayer) {
            this.blackPlayer = blackPlayer;
            return this;
        }

        public Builder whiteElo(int whiteElo) {
            this.whiteElo = whiteElo;
            return this;
        }

        public Builder blackElo(int blackElo) {
            this.blackElo = blackElo;
            return this;
        }

        public Builder round(String round) {
            this.round = round;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder event(String event) {
            this.event = event;
            return this;
        }

        public Builder result(GameResult result) {
            this.result = result;
            return this;
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

        public Builder extraHeaders(HashMap<String, String> extraHeaders) {
            this.extraHeaders = extraHeaders;
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

    public String getRound() {
        return round;
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

    public Optional<Integer> getWhiteElo() {
        return Optional.ofNullable(whiteElo);
    }

    public Optional<Integer> getBlackElo() {
        return Optional.ofNullable(blackElo);
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

    public HashMap<String, String> getExtraHeaders() {
        return extraHeaders;
    }
}

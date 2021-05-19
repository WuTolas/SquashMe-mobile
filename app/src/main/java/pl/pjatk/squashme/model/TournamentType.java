package pl.pjatk.squashme.model;

import androidx.annotation.NonNull;

import java.util.stream.Stream;

public enum TournamentType {

    ROUND_ROBIN("Round robin"),
    PLAYOFFS_ONLY("Playoffs only");

    private final String name;

    TournamentType(String name) {
        this.name = name;
    }

    public static TournamentType getByName(String name) {
        return Stream.of(TournamentType.values()).filter(v -> v.name.equals(name)).findFirst().orElse(null);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

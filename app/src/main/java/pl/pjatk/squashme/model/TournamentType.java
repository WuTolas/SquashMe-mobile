package pl.pjatk.squashme.model;

import androidx.annotation.NonNull;

import java.util.stream.Stream;

/**
 * Enum containing possible tournament types handled currently by the application.
 */
public enum TournamentType {

    ROUND_ROBIN("Round robin");

    private final String name;

    TournamentType(String name) {
        this.name = name;
    }

    /**
     * Gets enum by string.
     *
     * @param name - string name of the enum
     * @return enum value
     */
    public static TournamentType getByName(String name) {
        return Stream.of(TournamentType.values()).filter(v -> v.name.equals(name)).findFirst().orElse(null);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

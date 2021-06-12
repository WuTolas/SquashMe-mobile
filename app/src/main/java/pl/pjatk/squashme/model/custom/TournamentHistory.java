package pl.pjatk.squashme.model.custom;

import java.io.Serializable;

public class TournamentHistory implements Serializable {

    private static final long serialVersionUID = 7951969785975411033L;

    private long tournamentId;
    private String tournamentName;

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }
}

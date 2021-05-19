package pl.pjatk.squashme.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(primaryKeys = {"player_id", "tournament_id"})
public class PlayerTournament implements Serializable {

    private static final long serialVersionUID = -8302867428899265836L;

    @ColumnInfo(name = "player_id")
    private long playerId;
    @ColumnInfo(name = "tournament_id")
    private long tournamentId;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }
}

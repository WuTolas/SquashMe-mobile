package pl.pjatk.squashme.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Match implements Serializable {

    private static final long serialVersionUID = -5707926154973431429L;

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "player1")
    private long player1Id;
    @ColumnInfo(name = "player2")
    private long player2Id;
    @ColumnInfo(name = "best_of")
    private Integer bestOf;
    @ColumnInfo(name = "two_points_advantage")
    private boolean twoPointsAdvantage;
    @ColumnInfo(name = "referee_mode")
    private Boolean refereeMode;
    @ColumnInfo(name = "finished")
    private boolean finished;
    @ColumnInfo(name = "tournament_id")
    private Long tournamentId;
    @ColumnInfo(name = "tournament_round")
    private Integer tournamentRound;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(long player1Id) {
        this.player1Id = player1Id;
    }

    public long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(long player2Id) {
        this.player2Id = player2Id;
    }

    public Integer getBestOf() {
        return bestOf;
    }

    public void setBestOf(Integer bestOf) {
        this.bestOf = bestOf;
    }

    public boolean isTwoPointsAdvantage() {
        return twoPointsAdvantage;
    }

    public void setTwoPointsAdvantage(boolean twoPointsAdvantage) {
        this.twoPointsAdvantage = twoPointsAdvantage;
    }

    public Boolean isRefereeMode() {
        return refereeMode;
    }

    public void setRefereeMode(Boolean refereeMode) {
        this.refereeMode = refereeMode;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Integer getTournamentRound() {
        return tournamentRound;
    }

    public void setTournamentRound(Integer tournamentRound) {
        this.tournamentRound = tournamentRound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id &&
                player1Id == match.player1Id &&
                player2Id == match.player2Id &&
                twoPointsAdvantage == match.twoPointsAdvantage &&
                finished == match.finished &&
                Objects.equals(bestOf, match.bestOf) &&
                Objects.equals(refereeMode, match.refereeMode) &&
                Objects.equals(tournamentId, match.tournamentId) &&
                Objects.equals(tournamentRound, match.tournamentRound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player1Id, player2Id, bestOf, twoPointsAdvantage, refereeMode, finished, tournamentId, tournamentRound);
    }
}

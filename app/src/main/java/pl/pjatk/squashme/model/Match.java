package pl.pjatk.squashme.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

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
    private Long tournament_id;
    @ColumnInfo(name = "tournament_round")
    private Integer tournament_round;

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

    public Long getTournament_id() {
        return tournament_id;
    }

    public void setTournament_id(Long tournament_id) {
        this.tournament_id = tournament_id;
    }

    public Integer getTournament_round() {
        return tournament_round;
    }

    public void setTournament_round(Integer tournament_round) {
        this.tournament_round = tournament_round;
    }
}

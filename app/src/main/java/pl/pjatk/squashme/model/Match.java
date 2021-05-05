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
    private String player1;
    @ColumnInfo(name = "player2")
    private String player2;
    @ColumnInfo(name = "best_of")
    private Integer bestOf;
    @ColumnInfo(name = "two_points_advantage")
    private boolean twoPointsAdvantage;
    @ColumnInfo(name = "referee_mode")
    private boolean refereeMode;
    @ColumnInfo(name = "finished")
    private boolean finished;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
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

    public boolean isRefereeMode() {
        return refereeMode;
    }

    public void setRefereeMode(boolean refereeMode) {
        this.refereeMode = refereeMode;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}

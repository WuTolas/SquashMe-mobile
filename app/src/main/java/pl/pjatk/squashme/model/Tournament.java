package pl.pjatk.squashme.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;

import pl.pjatk.squashme.model.converter.TournamentStatusConverter;
import pl.pjatk.squashme.model.converter.TournamentTypeConverter;

/**
 * Entity class which defines tournament structure in the database.
 */
@Entity
public class Tournament implements Serializable {

    private static final long serialVersionUID = -4140072371780494590L;

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo
    private String name;
    @ColumnInfo(name = "best_of")
    private Integer bestOf;
    @ColumnInfo(name = "two_points_advantage")
    private boolean twoPointsAdvantage;
    @ColumnInfo(name = "tournament_status")
    @TypeConverters(TournamentStatusConverter.class)
    private TournamentStatus status;
    @ColumnInfo(name = "tournament_type")
    @TypeConverters(TournamentTypeConverter.class)
    private TournamentType type;
    @ColumnInfo(name = "max_players")
    private int maxPlayers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public TournamentType getType() {
        return type;
    }

    public void setType(TournamentType type) {
        this.type = type;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}

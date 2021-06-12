package pl.pjatk.squashme.model.custom;

import java.io.Serializable;

/**
 * Custom tournament results class for SQL data result.
 */
public class TournamentResults implements Serializable {

    private static final long serialVersionUID = -8865573171543425183L;

    private long playerId;
    private String name;
    private int winning;
    private int losing;
    private int setsWinning;
    private int setsLosing;
    private int pointsWinning;
    private int pointsLosing;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWinning() {
        return winning;
    }

    public void setWinning(int winning) {
        this.winning = winning;
    }

    public int getLosing() {
        return losing;
    }

    public void setLosing(int losing) {
        this.losing = losing;
    }

    public int getSetsWinning() {
        return setsWinning;
    }

    public void setSetsWinning(int setsWinning) {
        this.setsWinning = setsWinning;
    }

    public int getSetsLosing() {
        return setsLosing;
    }

    public void setSetsLosing(int setsLosing) {
        this.setsLosing = setsLosing;
    }

    public int getPointsWinning() {
        return pointsWinning;
    }

    public void setPointsWinning(int pointsWinning) {
        this.pointsWinning = pointsWinning;
    }

    public int getPointsLosing() {
        return pointsLosing;
    }

    public void setPointsLosing(int pointsLosing) {
        this.pointsLosing = pointsLosing;
    }
}

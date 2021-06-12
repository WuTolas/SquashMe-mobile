package pl.pjatk.squashme.model.custom;

import java.io.Serializable;

public class TournamentMatchSimple implements Serializable {

    private static final long serialVersionUID = -5449914392080224255L;

    private long matchId;
    private boolean finished;
    private Boolean refereeMode;
    private Integer tournamentRound;
    private String player1;
    private String player2;
    private int sets1;
    private int sets2;

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Boolean isRefereeMode() {
        return refereeMode;
    }

    public void setRefereeMode(Boolean refereeMode) {
        this.refereeMode = refereeMode;
    }

    public Integer getTournamentRound() {
        return tournamentRound;
    }

    public void setTournamentRound(Integer tournamentRound) {
        this.tournamentRound = tournamentRound;
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

    public int getSets1() {
        return sets1;
    }

    public void setSets1(int sets1) {
        this.sets1 = sets1;
    }

    public int getSets2() {
        return sets2;
    }

    public void setSets2(int sets2) {
        this.sets2 = sets2;
    }
}

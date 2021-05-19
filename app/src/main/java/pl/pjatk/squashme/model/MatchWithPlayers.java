package pl.pjatk.squashme.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatchWithPlayers implements Serializable {

    private static final long serialVersionUID = 8940991921376445226L;

    @Embedded
    private Match match;
    @Relation(
            parentColumn = "player1",
            entityColumn = "id"
    )
    private Player player1;
    @Relation(
            parentColumn = "player2",
            entityColumn = "id"
    )
    private Player player2;
    @Relation(
            parentColumn = "id",
            entityColumn = "match_id"
    )
    private List<Result> results = new ArrayList<>();

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}

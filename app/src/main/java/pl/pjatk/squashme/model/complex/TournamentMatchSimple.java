package pl.pjatk.squashme.model.complex;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.Player;

public class TournamentMatchSimple implements Serializable {

    private static final long serialVersionUID = -5449914392080224255L;

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
}

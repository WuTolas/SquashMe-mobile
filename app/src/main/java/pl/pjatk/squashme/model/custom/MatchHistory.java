package pl.pjatk.squashme.model.custom;

public class MatchHistory {

    private long id;
    private String player1;
    private String player2;
    private int sets1;
    private int sets2;

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

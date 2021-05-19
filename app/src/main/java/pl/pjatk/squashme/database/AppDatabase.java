package pl.pjatk.squashme.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.PlayerTournamentDao;
import pl.pjatk.squashme.dao.ResultDao;
import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.dao.TournamentDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.PlayerTournament;
import pl.pjatk.squashme.model.Result;
import pl.pjatk.squashme.model.Player;
import pl.pjatk.squashme.model.Tournament;

@Database(entities = {
        Match.class,
        Player.class,
        Tournament.class,
        Result.class,
        PlayerTournament.class
}, version = 13)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "SquashMe.db";

    public abstract MatchDao getMatchDao();

    public abstract ResultDao getResultDao();

    public abstract PlayerDao getPlayerDao();

    public abstract TournamentDao getTournamentDao();

    public abstract PlayerTournamentDao getPlayerTournamentDao();
}

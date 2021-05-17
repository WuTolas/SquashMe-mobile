package pl.pjatk.squashme.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.PlayerDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.Player;

@Database(entities = {Match.class, Player.class}, version = 8)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "SquashMe.db";

    public abstract MatchDao getMatchDao();

    public abstract PlayerDao getPlayerDao();
}

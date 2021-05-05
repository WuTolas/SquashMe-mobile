package pl.pjatk.squashme.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.model.Match;

@Database(entities = {Match.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "SquashMe.db";

    public abstract MatchDao getMatchDao();
}

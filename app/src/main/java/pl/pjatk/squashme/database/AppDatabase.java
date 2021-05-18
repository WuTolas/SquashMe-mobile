package pl.pjatk.squashme.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pl.pjatk.squashme.dao.MatchDao;
import pl.pjatk.squashme.dao.ResultDao;
import pl.pjatk.squashme.model.Match;
import pl.pjatk.squashme.model.Result;

@Database(entities = {Match.class, Result.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "SquashMe.db";

    public abstract MatchDao getMatchDao();

    public abstract ResultDao getResultDao();
}

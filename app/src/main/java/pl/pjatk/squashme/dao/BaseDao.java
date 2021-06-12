package pl.pjatk.squashme.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

/**
 * Base data access object for common database operations on entities.
 *
 * @param <T> - entity class
 */
public interface BaseDao<T> {

    @Insert
    long insert(T t);

    @Delete
    void delete(T t);

    @Update
    void update(T t);
}

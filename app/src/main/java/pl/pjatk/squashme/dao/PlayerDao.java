package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;

import pl.pjatk.squashme.model.Player;

/**
 * Player data access object for operations related to Player entity and alike data.
 */
@Dao
public interface PlayerDao extends BaseDao<Player> {

    @Query("SELECT id FROM `Player` WHERE name LIKE :name")
    long getId(String name);
}

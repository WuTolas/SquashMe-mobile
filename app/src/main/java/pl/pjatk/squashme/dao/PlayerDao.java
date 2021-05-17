package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.Optional;

import pl.pjatk.squashme.model.Player;

@Dao
public interface PlayerDao extends BaseDao<Player> {

    @Query("SELECT * FROM `Player` WHERE name LIKE :name")
    Optional<Player> getPlayer(String name);
}

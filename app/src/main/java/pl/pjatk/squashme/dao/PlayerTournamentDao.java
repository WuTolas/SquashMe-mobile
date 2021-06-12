package pl.pjatk.squashme.dao;

import androidx.room.Dao;

import pl.pjatk.squashme.model.PlayerTournament;


/**
 * Player in tournament data access object for operations related to PlayerTournament entity
 * and alike data.
 */
@Dao
public interface PlayerTournamentDao extends BaseDao<PlayerTournament> {
}

package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.Optional;

import pl.pjatk.squashme.model.Tournament;

@Dao
public interface TournamentDao extends BaseDao<Tournament> {

    @Query("SELECT * FROM `Tournament` WHERE tournament_status != 'FINISHED'")
    Optional<Tournament> getCurrentTournament();
}

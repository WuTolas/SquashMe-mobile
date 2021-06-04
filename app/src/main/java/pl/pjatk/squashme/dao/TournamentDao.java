package pl.pjatk.squashme.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.Optional;

import pl.pjatk.squashme.model.Tournament;

@Dao
public interface TournamentDao extends BaseDao<Tournament> {

    @Query("SELECT * FROM `Tournament` WHERE tournament_status != 'FINISHED'")
    Optional<Tournament> getCurrentTournament();

    @Query("SELECT * FROM `Tournament` WHERE id = :id")
    Optional<Tournament> getTournament(long id);

    @Query("UPDATE `Tournament` SET tournament_status = 'FINISHED' WHERE id = :tournamentId")
    void finishTournament(long tournamentId);
}

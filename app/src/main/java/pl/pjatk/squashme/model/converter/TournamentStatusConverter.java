package pl.pjatk.squashme.model.converter;

import androidx.room.TypeConverter;

import org.apache.commons.lang3.StringUtils;

import pl.pjatk.squashme.model.TournamentStatus;

/**
 * Converter class needed for tournament status conversion between database string and enum class.
 */
public class TournamentStatusConverter {

    @TypeConverter
    public String fromTournamentStatus(TournamentStatus tournamentStatus) {
        return tournamentStatus != null ? tournamentStatus.toString() : null;
    }

    @TypeConverter
    public TournamentStatus toTournamentStatus(String tournamentStatus) {
        return StringUtils.isBlank(tournamentStatus) ? null : TournamentStatus.valueOf(tournamentStatus);
    }
}

package pl.pjatk.squashme.model.converter;

import androidx.room.TypeConverter;

import org.apache.commons.lang3.StringUtils;

import pl.pjatk.squashme.model.TournamentType;

public class TournamentTypeConverter {

    @TypeConverter
    public String fromTournamentType(TournamentType tournamentType) {
        return tournamentType != null ? tournamentType.toString() : null;
    }

    @TypeConverter
    public TournamentType toTournamentType(String tournamentType) {
        return StringUtils.isBlank(tournamentType) ? null : TournamentType.valueOf(tournamentType);
    }
}

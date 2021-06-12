package pl.pjatk.squashme.service

import pl.pjatk.squashme.dao.MatchDao
import pl.pjatk.squashme.model.Match
import pl.pjatk.squashme.model.Player
import pl.pjatk.squashme.model.custom.MatchWithPlayers
import pl.pjatk.squashme.service.implementation.MatchServiceImpl
import spock.lang.Specification
import spock.lang.Subject

class MatchServiceImplSpec extends Specification {

    MatchDao matchDao = Mock(MatchDao)

    @Subject
    MatchService sut

    Match match
    MatchWithPlayers matchWithPlayers

    def "setup"() {
        sut = new MatchServiceImpl(matchDao)

        given:
            Match match = new Match()
            match.twoPointsAdvantage = true
            match.tournamentRound = null
            match.player1Id = 1
            match.player2Id = 2
            match.finished = false
            match.bestOf = 3
            match.refereeMode = true
            match.tournamentId = null
            match.id = 0
        and:
            matchWithPlayers = new MatchWithPlayers()
            matchWithPlayers.player1 = new Player()
            matchWithPlayers.player1.id = 1L
            matchWithPlayers.player1.name = "Nameski"
            matchWithPlayers.player2 = new Player()
            matchWithPlayers.player2.id = 2L
            matchWithPlayers.player2.name = "Opponentowski"
            matchWithPlayers.match = new Match()
            matchWithPlayers.match.id = 1L
            matchWithPlayers.match.tournamentRound = 2
            matchWithPlayers.match.tournamentId = 2L
            matchWithPlayers.match.refereeMode = true
            matchWithPlayers.match.bestOf = 3
            matchWithPlayers.match.twoPointsAdvantage = false
            matchWithPlayers.results = new ArrayList<>()
    }

    def "saveMatch should insert new match without changing any fields"() {
        when:
            sut.saveMatch(match)
        then:
            1 * matchDao.insert({ Match called ->
                called == match
            })
    }

    def "getCurrentQuickMatchWithPlayers should get object from database without altering any other fields"() {
        given:
            matchDao.getCurrentQuickMatchWithPlayers() >> Optional.of(matchWithPlayers)
        when:
            Optional<MatchWithPlayers> result = sut.getCurrentQuickMatchWithPlayers()
        then:
            result.get() == matchWithPlayers
    }

    def "saveWithPlayersReturn should insert new match without altering any fields"() {
        given:
            matchDao.getCurrentQuickMatchWithPlayers() >> Optional.of(new MatchWithPlayers())
        when:
            sut.saveWithPlayersReturn(match)
        then:
            1 * matchDao.insert({ Match called ->
                called == match
            })
    }

    def "saveWithPlayersReturn should return object without altering any other fields from database"() {
        given:
            matchDao.getCurrentQuickMatchWithPlayers() >> Optional.of(matchWithPlayers)
        when:
            MatchWithPlayers result = sut.saveWithPlayersReturn(new Match())
        then:
            result == matchWithPlayers
    }

    def "saveWithPlayersReturn should throw RuntimeException when database returns empty optional"() {
        given:
            matchDao.getCurrentQuickMatchWithPlayers() >> Optional.empty()
        when:
            sut.saveWithPlayersReturn(new Match())
        then:
            thrown(RuntimeException)
    }

    def "updateMatch should update match without changing any fields"() {
        when:
            sut.updateMatch(match)
        then:
            1 * matchDao.update({ Match called ->
                called == match
            })
    }

    def "updateRefereeMode should call database update referee mode"() {
        given:
            long matchId = 1L
            boolean refereeMode = true
        when:
            sut.updateRefereeMode(matchId, refereeMode)
        then:
            1 * matchDao.updateRefereeMode(_ as Boolean, _ as Long)
    }

    def "getMatchWithResults should return object without altering any other fields from database"() {
        given:
            matchDao.getMatchWithResults(_ as Long) >> Optional.of(matchWithPlayers)
        when:
            MatchWithPlayers result = sut.getMatchWithResults(1L)
        then:
            result == matchWithPlayers
    }

    def "getMatchWithResults should return null when optional was empty"() {
        given:
            matchDao.getMatchWithResults(_ as Long) >> Optional.empty()
        when:
            MatchWithPlayers result = sut.getMatchWithResults(2L)
        then:
            result == null
    }

    def "searchMatchHistory should call search match history in database"() {
        when:
            sut.searchMatchHistory()
        then:
            1 * matchDao.searchMatchHistory()
    }
}

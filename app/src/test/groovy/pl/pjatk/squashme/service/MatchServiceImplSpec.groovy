package pl.pjatk.squashme.service

import pl.pjatk.squashme.dao.MatchDao
import pl.pjatk.squashme.model.Match
import pl.pjatk.squashme.service.implementation.MatchServiceImpl
import spock.lang.Specification
import spock.lang.Subject

class MatchServiceImplSpec extends Specification {

    MatchDao matchDao = Mock(MatchDao)

    @Subject
    MatchService sut

    def "setup"() {
        sut = new MatchServiceImpl(matchDao)
    }

    def "saveMatch should insert new match without changing any fields"() {
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

        when:
            sut.saveMatch(match)

        then:
            1 * matchDao.insert({ Match called ->
                called == match
            })
    }
}

package pl.pjatk.squashme.service.implementation

import pl.pjatk.squashme.dao.PlayerTournamentDao
import pl.pjatk.squashme.dao.TournamentDao
import pl.pjatk.squashme.model.Match
import pl.pjatk.squashme.model.PlayerTournament
import pl.pjatk.squashme.model.Tournament
import pl.pjatk.squashme.model.TournamentStatus
import pl.pjatk.squashme.service.MatchService
import pl.pjatk.squashme.service.PlayerService
import pl.pjatk.squashme.service.TournamentService
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class TournamentServiceImplSpec extends Specification {

    TournamentDao tournamentDao = Mock(TournamentDao)
    PlayerTournamentDao playerTournamentDao = Mock(PlayerTournamentDao)
    PlayerService playerService = Mock(PlayerService)
    MatchService matchService = Mock(MatchService)

    @Subject
    TournamentService sut

    def "setup"() {
        sut = new TournamentServiceImpl(tournamentDao, playerTournamentDao, playerService, matchService)
    }

    def "getCurrentTournament should call get tournament from dao"() {
        when:
            sut.getCurrentTournament()
        then:
            1 * tournamentDao.getCurrentTournament()
    }

    def "save should call insert dao method"() {
        given:
            Tournament tournament = new Tournament()
        when:
            sut.save(tournament)
        then:
            1 * tournamentDao.insert(tournament)
    }

    def "save should set id for the provided tournament"() {
        given:
            Tournament tournament = new Tournament()
        and:
            long id = 212L
            tournamentDao.insert(tournament) >> id
        when:
            Tournament result = sut.save(tournament)
        then:
            result.id == id
            tournament.id == result.id
    }

    def "searchTournamentHistory should call search tournament history from dao"() {
        when:
            sut.searchTournamentHistory()
        then:
            1 * tournamentDao.searchTournamentHistory()
    }

    def "removeTournament should call remove tournament from dao with provided tournament id"() {
        given:
            long tournamentId = 2L
        when:
            sut.removeTournament(tournamentId)
        then:
            1 * tournamentDao.deleteTournament(tournamentId)
    }

    def "searchTournamentResults should call search tournament results from dao with provided tournament id"() {
        given:
            long tournamentId = 4L
        when:
            sut.searchTournamentResults(tournamentId)
        then:
            1 * tournamentDao.searchTournamentResults(tournamentId)
    }

    def "endTournament should call finish tournament from dao with provided id"() {
        given:
            long tournamentId = 55L
        when:
            sut.endTournament(tournamentId)
        then:
            1 * tournamentDao.finishTournament(tournamentId)
    }

    def "generateRoundRobinMatches should throw RuntimeException when there is no tournament"() {
        given:
            long tournamentId = 1L
            List<String> players = new ArrayList<>()
        and:
            tournamentDao.getTournament(_ as Long) >> Optional.empty()
        when:
            sut.generateRoundRobinMatches(tournamentId, players)
        then:
            thrown(RuntimeException)
    }

    def "generateRoundRobinMatches should set tournament status to ONGOING"() {
        given:
            long tournamentId = 1L
            List<String> players = new ArrayList<>()
        and:
            tournamentDao.getTournament(_ as Long) >> Optional.of(new Tournament())
        when:
            sut.generateRoundRobinMatches(tournamentId, players)
        then:
            1 * tournamentDao.update({ Tournament t ->
                t.status == TournamentStatus.ONGOING
            })
    }

    def "generateRoundRobinMatches should save players in tournament"() {
        given:
            long tournamentId = 3L
            List<String> players = ["One", "Two", "Three", "Four", "Five"]
        and:
            Tournament tournament = new Tournament()
            tournament.id = tournamentId
            tournamentDao.getTournament(_ as Long) >> Optional.of(tournament)
        when:
            sut.generateRoundRobinMatches(tournamentId, players)
        then:
            players.size() * playerService.getIdWithSave(_ as String)
        and:
            players.size() * playerTournamentDao.insert( {PlayerTournament pt ->
                pt.tournamentId == tournamentId
            })
    }

    def "generateRoundRobinMatches should not save any match when there is only one player in tournament"() {
        given:
            long tournamentId = 55L
            List<String> players = ["SpecialAgent"]
        and:
            tournamentDao.getTournament(_ as Long) >> Optional.of(new Tournament())
        when:
            sut.generateRoundRobinMatches(tournamentId, players)
        then:
            0 * matchService.saveMatch(_ as Match)
    }

    @Unroll
    def "generateRoundRobinMatches should create appropriate number of matches based on number of players"() {
        given:
            long tournamentId = 4L
            List<String> players = plrs
        and:
            Tournament tournament = new Tournament()
            tournament.id = tournamentId
            tournament.twoPointsAdvantage = true
            tournament.bestOf = 5
            tournamentDao.getTournament(tournamentId) >> Optional.of(tournament)
        and:
            playerService.getIdWithSave(_ as String) >> 8L
        when:
            sut.generateRoundRobinMatches(tournamentId, players)
        then:
            matchesNumber * matchService.saveMatch( {Match m ->
                m.twoPointsAdvantage == tournament.twoPointsAdvantage
                m.tournamentId == tournament.id
                m.bestOf == tournament.bestOf
            })

        where:
            matchesNumber   |   plrs
            3               |   ["One", "Two", "Three"]
            6               |   ["One", "Two", "Three", "Four"]
            10              |   ["One", "Two", "Three", "Four", "Five"]
            15              |   ["One", "Two", "Three", "Four", "Five", "Six"]
    }
}

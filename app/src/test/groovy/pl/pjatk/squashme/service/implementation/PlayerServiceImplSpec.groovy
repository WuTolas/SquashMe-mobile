package pl.pjatk.squashme.service.implementation

import pl.pjatk.squashme.dao.PlayerDao
import pl.pjatk.squashme.model.Player
import pl.pjatk.squashme.service.PlayerService
import spock.lang.Specification
import spock.lang.Subject

class PlayerServiceImplSpec extends Specification {

    PlayerDao playerDao = Mock(PlayerDao)

    @Subject
    PlayerService sut

    def "setup"() {
        sut = new PlayerServiceImpl(playerDao)
    }

    def "getIdWithSave should call insert when user was not found"() {
        given:
            playerDao.getId(_ as String) >> 0L
        when:
            sut.getIdWithSave("any")
        then:
            1 * playerDao.insert(_ as Player)
    }

    def "getIdWithSave should not call insert when user was found"() {
        given:
            playerDao.getId(_ as String) >> 1L
        when:
            sut.getIdWithSave("anything")
        then:
            0 * playerDao.insert(_ as Player)
    }
}

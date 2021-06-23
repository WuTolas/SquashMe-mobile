package pl.pjatk.squashme.service.implementation

import pl.pjatk.squashme.dao.ResultDao
import pl.pjatk.squashme.model.Result
import pl.pjatk.squashme.service.ResultService
import spock.lang.Specification
import spock.lang.Subject

class ResultServiceImplSpec extends Specification {

    ResultDao resultDao = Mock(ResultDao)

    @Subject
    ResultService sut

    def "setup"() {
        sut = new ResultServiceImpl(resultDao)
    }

    def "addPoint should call insert"() {
        given:
            def result = new Result(1, 0, "L", 1, 1, 0, 1, 0)
        when:
            sut.addPoint(result)
        then:
            1 * resultDao.insert({ result })
    }

    def "revertPoint should call delete when result is not null"() {
        given:
            def result = new Result(1, 0, "L", 1, 1, 0, 1, 1)
        when:
            sut.revertPoint(result)
        then:
            1 * resultDao.delete({ result })
    }

    def "revertPoint should not call dao when result is null"() {
        when:
            sut.revertPoint(null)
        then:
            0 * resultDao.delete()
    }
}

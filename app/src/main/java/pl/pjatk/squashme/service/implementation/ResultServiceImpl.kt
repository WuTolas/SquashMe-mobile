package pl.pjatk.squashme.service.implementation

import pl.pjatk.squashme.dao.ResultDao
import pl.pjatk.squashme.model.Result
import pl.pjatk.squashme.service.ResultService
import javax.inject.Inject

class ResultServiceImpl @Inject constructor(private val resultDao: ResultDao) : ResultService {
    override fun addPoint(result: Result): Result {
        val id = resultDao.insert(result)
        result.id = id
        return result
    }

    override fun revertPoint(result: Result?) {
        result?.also {
            resultDao.delete(it)
        }
    }
}

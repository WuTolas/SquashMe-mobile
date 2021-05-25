package pl.pjatk.squashme.service

import android.util.Log
import pl.pjatk.squashme.dao.ResultDao
import pl.pjatk.squashme.model.Result
import javax.inject.Inject

class ResultServiceImpl @Inject constructor(private val resultDao: ResultDao) :  ResultService {
    override fun addPoint(result: Result) {
        resultDao.insert(result)
        Log.i("ResultService", "point added")
    }

    override fun revertPoint(result: Result?) {
        result?.also {
        resultDao.delete(it)
            Log.i("ResultService", "point reverted")
        }
    }
}

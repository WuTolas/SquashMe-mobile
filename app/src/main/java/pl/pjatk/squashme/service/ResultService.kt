package pl.pjatk.squashme.service

import pl.pjatk.squashme.model.Result

interface ResultService {
    fun addPoint(result: Result): Result
    fun revertPoint(result: Result?)
}

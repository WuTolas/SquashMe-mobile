package pl.pjatk.squashme.service

import pl.pjatk.squashme.model.Result

interface ResultService {

    /**
     * Save provided result to database
     * @param result Result
     */
    fun addPoint(result: Result): Result

    /**
     * Delete provided result from database
     * @param result Result?
     */
    fun revertPoint(result: Result?)
}

package pl.pjatk.squashme.fragment.referee

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.pjatk.squashme.model.Result

class RefereeModel(savedResults: MutableList<Result>) : ViewModel() {
    private val _results: MutableLiveData<MutableList<Result>> = MutableLiveData()
    val results: LiveData<MutableList<Result>>
        get() = _results

    init {
        _results.value = savedResults
    }

    fun addPoint(result: Result) {
        _results.value?.add(result)
        updateList()
    }

    fun revertPoint(): Result? {
        return _results.value?.removeLastOrNull()
                .also {
                    updateList()
                }
    }

    fun getLastResult(): Result? {
        return _results.value?.lastOrNull()
    }

    private fun updateList() {
        _results.value = _results.value
    }
}

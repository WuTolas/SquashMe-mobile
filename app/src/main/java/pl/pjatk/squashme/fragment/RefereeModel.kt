package pl.pjatk.squashme.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.pjatk.squashme.model.Result

class RefereeModel : ViewModel() {
    val results: MutableLiveData<MutableList<Result>> = MutableLiveData()
//    val results: LiveData<MutableList<Result>>
//        get() = _results

    init {
        results.value = mutableListOf()
    }

    fun addPoint(result: Result) {
        results.value?.add(result)
        updateList()
    }

    fun revertPoint(): Result? {
        return results.value?.removeLast().also {
            updateList()
        }
    }

//    fun finishSet() {
//        results.value?.add(result)
//
//    }

    private fun updateList() {
        results.value = results.value
    }
}

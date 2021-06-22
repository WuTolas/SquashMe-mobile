package pl.pjatk.squashme.fragment.referee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.pjatk.squashme.model.Result

/**
 * factory class to let create RefereeModel class with parameter
 */
class RefereeModelFactory(param: MutableList<Result>) : ViewModelProvider.Factory {
    private var mParam: MutableList<Result> = param

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RefereeModel(mParam) as T
    }
}

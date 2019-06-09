package by.ve.omdbapiandroid.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory<ViewModelT : ViewModel> @Inject constructor(
    private val viewModelProvider: Provider<ViewModelT>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModelProvider.get() as T
}

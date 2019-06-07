package by.ve.omdbapiandroid.view

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import by.ve.omdbapiandroid.presentation.ViewModelFactory

inline fun <reified ViewModelT : ViewModel> FragmentActivity.loadViewModel(
    crossinline factoryProvider: () -> ViewModelFactory<ViewModelT>
) = lazy {
    ViewModelProviders.of(this, factoryProvider.invoke()).get(ViewModelT::class.java)
}
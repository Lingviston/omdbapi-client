package by.ve.omdbapiandroid.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import by.ve.omdbapiandroid.databinding.LayoutFilterBinding
import by.ve.omdbapiandroid.presentation.MoviesListViewModel
import by.ve.omdbapiandroid.presentation.ViewModelFactory
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class FilterDialogFragment : DaggerDialogFragment() {

    companion object {

        const val TAG = "FilterDialogFragment"
    }

    @Inject
    lateinit var moviesListViewModelFactory: ViewModelFactory<MoviesListViewModel>

    private val moviesListViewModel by loadActivityViewModel { moviesListViewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FrameLayout(requireActivity()).apply {
            LayoutFilterBinding.inflate(inflater, this, true).also {
                it.viewModel = moviesListViewModel.filterViewModel
            }
        }
    }
}
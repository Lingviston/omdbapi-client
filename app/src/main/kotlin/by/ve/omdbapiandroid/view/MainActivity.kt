package by.ve.omdbapiandroid.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import by.ve.omdbapiandroid.R
import by.ve.omdbapiandroid.databinding.ActivityMainBinding
import by.ve.omdbapiandroid.presentation.MoviesListViewModel
import by.ve.omdbapiandroid.presentation.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var searchQueriesAdapter: SearchQueriesAdapter

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    @Inject
    lateinit var moviesListViewModelFactory: ViewModelFactory<MoviesListViewModel>

    private val moviesListViewModel by loadViewModel { moviesListViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).also {
            it.lifecycleOwner = this
            it.searchQueriesAdapter = searchQueriesAdapter
            it.moviesAdapter = moviesAdapter
            it.viewModel = moviesListViewModel
        }
        moviesListViewModel.searchesQuery.observe(this, Observer {
            searchQueriesAdapter.submitList(it)
        })
        moviesListViewModel.movies.observe(this, Observer {
            moviesAdapter.submitList(it)
        })
        moviesListViewModel.showFilterSettings.observe(this, Observer {
            with(supportFragmentManager) {
                val existingFragment = findFragmentByTag(FilterDialogFragment.TAG)
                if (existingFragment == null) {
                    FilterDialogFragment().show(this, FilterDialogFragment.TAG)
                }
            }
        })
    }
}
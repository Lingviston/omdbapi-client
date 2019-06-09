package by.ve.omdbapiandroid.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import by.ve.omdbapiandroid.presentation.viewmodel.model.FilterParams
import by.ve.omdbapiandroid.presentation.viewmodel.model.FilterParamsUpdate
import by.ve.omdbapiandroid.repositories.model.MediaContentType
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import java.util.*

private const val MINIMAL_YEAR = 1900
private const val ANY_YEAR = "Any"
private const val ANY_YEAR_INDEX = 0

class FilterViewModel {

    val years = arrayOf(ANY_YEAR) + (Calendar.getInstance()[Calendar.YEAR] downTo MINIMAL_YEAR).map { it.toString() }

    val selectedYearPosition = MutableLiveData<Int>(null)

    val movieTypeSelected = MutableLiveData<Boolean>(null)

    val seriesTypeSelected = MutableLiveData<Boolean>(null)

    val gameTypeSelected = MutableLiveData<Boolean>(null)

    val appliedFilterParams: Observable<FilterParams>

    private val applyFilterSubject = PublishSubject.create<Unit>()

    private val yearSubject = PublishSubject.create<FilterParamsUpdate.YearUpdate>()

    private val selectedTypesSubject = PublishSubject.create<FilterParamsUpdate.TypeUpdate>()

    private val restoredFilterParamsSubject = PublishSubject.create<FilterParamsUpdate.FullParamsUpdate>()

    init {

        val filterParams = Observable.merge(yearSubject, selectedTypesSubject, restoredFilterParamsSubject)
            .scan(FilterParams.EMPTY, ::reduceUpdatesToFilterParams)
            .distinctUntilChanged()
            .doOnNext {
                Log.d("SearchViewModel", "New filter params: $it")
                selectedYearPosition.value = if (it.year == null) ANY_YEAR_INDEX else years.indexOf(it.year.toString())
                movieTypeSelected.value = it.type == MediaContentType.MOVIE
                seriesTypeSelected.value = it.type == MediaContentType.SERIES
                gameTypeSelected.value = it.type == MediaContentType.GAME
            }

        appliedFilterParams = applyFilterSubject.withLatestFrom(filterParams) { _, params ->
            params
        }
    }

    fun onSelectedYearIndexChanged(yearIndex: Int) {
        val year = if (yearIndex == ANY_YEAR_INDEX) null else years[yearIndex].toInt()
        yearSubject.onNext(FilterParamsUpdate.YearUpdate(year))
    }

    fun onMovieCheckChanged(isChecked: Boolean) {
        updateSelectedType(MediaContentType.MOVIE, isChecked)
    }

    fun onSeriesCheckChanged(isChecked: Boolean) {
        updateSelectedType(MediaContentType.SERIES, isChecked)
    }

    fun onGameCheckChanged(isChecked: Boolean) {
        updateSelectedType(MediaContentType.GAME, isChecked)
    }

    fun onApplyFilterPressed() {
        applyFilterSubject.onNext(Unit)
    }

    fun onFilterParamsRestored(params: FilterParams) {
        restoredFilterParamsSubject.onNext(FilterParamsUpdate.FullParamsUpdate(params))
    }

    private fun updateSelectedType(type: MediaContentType, isSelected: Boolean) {
        selectedTypesSubject.onNext(FilterParamsUpdate.TypeUpdate(type, isSelected))
    }

    private fun reduceUpdatesToFilterParams(oldParams: FilterParams, update: FilterParamsUpdate): FilterParams {
        return when (update) {
            is FilterParamsUpdate.YearUpdate -> oldParams.copy(
                year = update.year
            )
            is FilterParamsUpdate.TypeUpdate -> oldParams.copy(
                type = chooseNewSelectedType(oldParams, update)
            )
            is FilterParamsUpdate.FullParamsUpdate -> update.params
        }
    }

    private fun chooseNewSelectedType(oldParams: FilterParams, update: FilterParamsUpdate.TypeUpdate) =
        if (update.isSelected) update.type else if (update.type == oldParams.type) null else oldParams.type
}
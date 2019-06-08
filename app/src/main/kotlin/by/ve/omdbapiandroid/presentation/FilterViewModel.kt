package by.ve.omdbapiandroid.presentation

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject
import java.util.*

private const val MINIMAL_YEAR = 1900
private const val ANY_YEAR = "Any"
private const val DEFAULT_YEAR_POSITION = 0

class FilterViewModel {

    val years = arrayOf(ANY_YEAR) + (Calendar.getInstance()[Calendar.YEAR] downTo MINIMAL_YEAR).map { it.toString() }

    val selectedYearPosition = MutableLiveData(DEFAULT_YEAR_POSITION)

    val filterParams: Observable<FilterParams>

    private val applyFilterSubject = PublishSubject.create<Unit>()

    private val yearSubject = PublishSubject.create<String>()

    init {
        filterParams = applyFilterSubject.withLatestFrom(yearSubject) { _, year ->
            FilterParams(year = year.takeIf { it != ANY_YEAR }?.toInt())
        }
    }

    fun onSelectedYearIndexChanged(yearIndex: Int) {
        selectedYearPosition.value = yearIndex
        yearSubject.onNext(years[yearIndex])
    }

    fun onApplyFilterPressed() {
        applyFilterSubject.onNext(Unit)
    }

    fun onFilterParamsRestored(params: FilterParams) {
        selectedYearPosition.value = if (params.year == null) {
            0
        } else {
            years.indexOf(params.year.toString())
        }
    }
}
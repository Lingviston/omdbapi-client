package by.ve.omdbapiandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import by.ve.omdbapiandroid.presentation.viewmodel.model.FilterParams
import by.ve.omdbapiandroid.repositories.model.MediaContentType
import io.reactivex.observers.TestObserver
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import junitparams.naming.TestCaseName
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import java.util.*

@RunWith(Enclosed::class)
class FilterViewModelTest {

    abstract class BaseTest {
        @get:Rule
        val liveDataRule = InstantTaskExecutorRule()

        protected val tested by lazy { FilterViewModel() }
    }

    class InitialStateTest : BaseTest() {

        @Test
        fun `given current year when created then years array is from 1900 till current year and includes any year option`() {
            // given
            val years = arrayOf("Any") + (Calendar.getInstance()[Calendar.YEAR] downTo 1900).map { it.toString() }

            // when
            tested

            // then
            assertArrayEquals(years, tested.years)
        }

        @Test
        fun `when created then selected year position is 0`() {
            // when
            tested

            // then
            assertEquals(0, tested.selectedYearPosition.value)
        }

        @Test
        fun `when created then movie type is not selected`() {
            // when
            tested

            // then
            assertEquals(false, tested.movieTypeSelected.value)
        }

        @Test
        fun `when created then series type is not selected`() {
            // when
            tested

            // then
            assertEquals(false, tested.seriesTypeSelected.value)
        }

        @Test
        fun `when created then game type is not selected`() {
            // when
            tested

            // then
            assertEquals(false, tested.gameTypeSelected.value)
        }

        @Test
        fun `given created when observing applied filter params then no params emitted`() {
            // given
            tested

            // when
            val observer = tested.appliedFilterParams.test()

            // then
            observer.assertEmpty()
        }
    }

    @RunWith(JUnitParamsRunner::class)
    class LiveDataUpdatesTest : BaseTest() {

        @Test
        fun `when selected year index changes then updates live data`() {
            // when
            tested.onSelectedYearIndexChanged(1)

            // then
            assertEquals(1, tested.selectedYearPosition.value)
        }

        @Test
        @Parameters(method = "paramsForTypeFilterChangeTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} when {3} becomes selected: {4} then movie selected: {5}, series selected: {6}, game selected: {7}")
        fun `given selected type filter when new type filter becomes selected then only new type filter is selected`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            mediaContentTypeThatChangedSelection: MediaContentType,
            newSelectionState: Boolean,
            expectedMovieSelection: Boolean,
            expectedSeriesSelection: Boolean,
            expectedGameSelection: Boolean
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)

            // when
            when (mediaContentTypeThatChangedSelection) {
                MediaContentType.MOVIE -> tested.onMovieCheckChanged(newSelectionState)
                MediaContentType.SERIES -> tested.onSeriesCheckChanged(newSelectionState)
                MediaContentType.GAME -> tested.onGameCheckChanged(newSelectionState)
            }

            // then
            assertEquals(expectedMovieSelection, tested.movieTypeSelected.value)
            assertEquals(expectedSeriesSelection, tested.seriesTypeSelected.value)
            assertEquals(expectedGameSelection, tested.gameTypeSelected.value)
        }

        private fun paramsForTypeFilterChangeTest() = arrayOf(
            arrayOf(false, false, false, MediaContentType.MOVIE, false, false, false, false),
            arrayOf(false, false, false, MediaContentType.MOVIE, true, true, false, false),
            arrayOf(false, false, false, MediaContentType.SERIES, false, false, false, false),
            arrayOf(false, false, false, MediaContentType.SERIES, true, false, true, false),
            arrayOf(false, false, false, MediaContentType.GAME, false, false, false, false),
            arrayOf(false, false, false, MediaContentType.GAME, true, false, false, true),
            arrayOf(true, false, false, MediaContentType.MOVIE, false, false, false, false),
            arrayOf(true, false, false, MediaContentType.MOVIE, true, true, false, false),
            arrayOf(true, false, false, MediaContentType.SERIES, false, true, false, false),
            arrayOf(true, false, false, MediaContentType.SERIES, true, false, true, false),
            arrayOf(true, false, false, MediaContentType.GAME, false, true, false, false),
            arrayOf(true, false, false, MediaContentType.GAME, true, false, false, true),
            arrayOf(false, true, false, MediaContentType.MOVIE, false, false, true, false),
            arrayOf(false, true, false, MediaContentType.MOVIE, true, true, false, false),
            arrayOf(false, true, false, MediaContentType.SERIES, false, false, false, false),
            arrayOf(false, true, false, MediaContentType.SERIES, true, false, true, false),
            arrayOf(false, true, false, MediaContentType.GAME, false, false, true, false),
            arrayOf(false, true, false, MediaContentType.GAME, true, false, false, true),
            arrayOf(false, false, true, MediaContentType.MOVIE, false, false, false, true),
            arrayOf(false, false, true, MediaContentType.MOVIE, true, true, false, false),
            arrayOf(false, false, true, MediaContentType.SERIES, false, false, false, true),
            arrayOf(false, false, true, MediaContentType.SERIES, true, false, true, false),
            arrayOf(false, false, true, MediaContentType.GAME, false, false, false, false),
            arrayOf(false, false, true, MediaContentType.GAME, true, false, false, true)
        )

        @Test
        fun `when filter params restored then selected year index updated`() {
            // when
            tested.onFilterParamsRestored(FilterParams(1902, MediaContentType.GAME))

            // then
            assertEquals(tested.years.indexOf("1902"), tested.selectedYearPosition.value)
        }

        @Test
        @Parameters(method = "paramsForFullFilterParamsRestorationTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} when {3} filter restored then movie selected: {4}, series selected: {5}, game selected: {6}")
        fun `given selected type filter when filter params restored then only new type filter is selected`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            newFilterParams: FilterParams,
            expectedMovieSelection: Boolean,
            expectedSeriesSelection: Boolean,
            expectedGameSelection: Boolean
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)

            // when
            tested.onFilterParamsRestored(newFilterParams)

            // then
            assertEquals(expectedMovieSelection, tested.movieTypeSelected.value)
            assertEquals(expectedSeriesSelection, tested.seriesTypeSelected.value)
            assertEquals(expectedGameSelection, tested.gameTypeSelected.value)
        }

        private fun paramsForFullFilterParamsRestorationTest() = arrayOf(
            arrayOf(false, false, false, FilterParams(null, null), false, false, false),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.MOVIE), true, false, false),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.SERIES), false, true, false),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.GAME), false, false, true),
            arrayOf(true, false, false, FilterParams(null, null), false, false, false),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.MOVIE), true, false, false),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.SERIES), false, true, false),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.GAME), false, false, true),
            arrayOf(false, true, false, FilterParams(null, null), false, false, false),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.MOVIE), true, false, false),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.SERIES), false, true, false),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.GAME), false, false, true),
            arrayOf(false, false, true, FilterParams(null, null), false, false, false),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.MOVIE), true, false, false),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.SERIES), false, true, false),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.GAME), false, false, true)
        )
    }

    @RunWith(JUnitParamsRunner::class)
    class ApplyFilterTest : BaseTest() {

        private lateinit var observer: TestObserver<FilterParams>

        @Before
        fun `given observing applied filter params`() {
            observer = tested.appliedFilterParams.test()
        }

        @Test
        fun `when apply filter pressed then empty params are emitted`() {
            // when
            tested.onApplyFilterPressed()

            // then
            observer.assertValue(FilterParams.EMPTY)
        }

        @Test
        fun `when year index changes to the same value then no new params are emitted`() {
            // when
            tested.onSelectedYearIndexChanged(0)

            // then
            observer.assertEmpty()
        }

        @Test
        fun `given year index changes to the same value when apply filter pressed then params with new year are emitted`() {
            // given
            tested.onSelectedYearIndexChanged(0)

            // when
            tested.onApplyFilterPressed()

            // then
            observer.assertValue {
                it.year == null
            }
        }

        @Test
        fun `when year index changes to the new value then no new params are emitted`() {
            // when
            tested.onSelectedYearIndexChanged(1)

            // then
            observer.assertEmpty()
        }

        @Test
        fun `given and year index changes to the new value when apply filter pressed then params with new year are emitted`() {
            // given
            tested.onSelectedYearIndexChanged(1)

            // when
            tested.onApplyFilterPressed()

            // then
            observer.assertValue {
                it.year == tested.years[1].toInt()
            }
        }

        @Test
        @Parameters(method = "paramsForTypeFilterChangeTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} when {3} becomes selected: {4} then filter params with type filter {5} are emitted")
        fun `given selected type filter when new type filter becomes selected then no new filter params are emitted`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            mediaContentTypeThatChangedSelection: MediaContentType,
            newSelectionState: Boolean,
            stub: MediaContentType?
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)

            // when
            when (mediaContentTypeThatChangedSelection) {
                MediaContentType.MOVIE -> tested.onMovieCheckChanged(newSelectionState)
                MediaContentType.SERIES -> tested.onSeriesCheckChanged(newSelectionState)
                MediaContentType.GAME -> tested.onGameCheckChanged(newSelectionState)
            }

            // then
            observer.assertEmpty()
        }

        @Test
        @Parameters(method = "paramsForTypeFilterChangeTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} and {3} becomes selected: {4} when apply filter pressed then filter params with type filter {5} are emitted")
        fun `given selected type filter and new type filter becomes selected when apply filter pressed then filter params with new type filter are emitted`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            mediaContentTypeThatChangedSelection: MediaContentType,
            newSelectionState: Boolean,
            expectedTypeFilter: MediaContentType?
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)
            when (mediaContentTypeThatChangedSelection) {
                MediaContentType.MOVIE -> tested.onMovieCheckChanged(newSelectionState)
                MediaContentType.SERIES -> tested.onSeriesCheckChanged(newSelectionState)
                MediaContentType.GAME -> tested.onGameCheckChanged(newSelectionState)
            }

            // when
            tested.onApplyFilterPressed()

            // then
            observer.assertValue {
                it.type == expectedTypeFilter
            }
        }

        private fun paramsForTypeFilterChangeTest() = arrayOf(
            arrayOf(false, false, false, MediaContentType.MOVIE, false, null),
            arrayOf(false, false, false, MediaContentType.MOVIE, true, MediaContentType.MOVIE),
            arrayOf(false, false, false, MediaContentType.SERIES, false, null),
            arrayOf(false, false, false, MediaContentType.SERIES, true, MediaContentType.SERIES),
            arrayOf(false, false, false, MediaContentType.GAME, false, null),
            arrayOf(false, false, false, MediaContentType.GAME, true, MediaContentType.GAME),
            arrayOf(true, false, false, MediaContentType.MOVIE, false, null),
            arrayOf(true, false, false, MediaContentType.MOVIE, true, MediaContentType.MOVIE),
            arrayOf(true, false, false, MediaContentType.SERIES, false, MediaContentType.MOVIE),
            arrayOf(true, false, false, MediaContentType.SERIES, true, MediaContentType.SERIES),
            arrayOf(true, false, false, MediaContentType.GAME, false, MediaContentType.MOVIE),
            arrayOf(true, false, false, MediaContentType.GAME, true, MediaContentType.GAME),
            arrayOf(false, true, false, MediaContentType.MOVIE, false, MediaContentType.SERIES),
            arrayOf(false, true, false, MediaContentType.MOVIE, true, MediaContentType.MOVIE),
            arrayOf(false, true, false, MediaContentType.SERIES, false, null),
            arrayOf(false, true, false, MediaContentType.SERIES, true, MediaContentType.SERIES),
            arrayOf(false, true, false, MediaContentType.GAME, false, MediaContentType.SERIES),
            arrayOf(false, true, false, MediaContentType.GAME, true, MediaContentType.GAME),
            arrayOf(false, false, true, MediaContentType.MOVIE, false, MediaContentType.GAME),
            arrayOf(false, false, true, MediaContentType.MOVIE, true, MediaContentType.MOVIE),
            arrayOf(false, false, true, MediaContentType.SERIES, false, MediaContentType.GAME),
            arrayOf(false, false, true, MediaContentType.SERIES, true, MediaContentType.SERIES),
            arrayOf(false, false, true, MediaContentType.GAME, false, null),
            arrayOf(false, false, true, MediaContentType.GAME, true, MediaContentType.GAME)
        )

        @Test
        fun `when filter params restored then no new filter params are emitted`() {
            // when
            tested.onFilterParamsRestored(FilterParams(1902, MediaContentType.GAME))

            // then
            observer.assertEmpty()
        }

        @Test
        fun `given filter params restored when apply filter pressed then filter params with new year are emitted`() {
            // given
            tested.onFilterParamsRestored(FilterParams(1902, MediaContentType.GAME))

            // when
            tested.onApplyFilterPressed()

            // then
            observer.assertValue {
                it.year == 1902
            }
        }

        @Test
        @Parameters(method = "paramsForFullFilterParamsRestorationTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} when filter params: {3} restored then no filter params are emitted")
        fun `given selected type filter when filter params restored then no filter params are emitted`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            newFilterParams: FilterParams,
            stub: MediaContentType?
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)

            // when
            tested.onFilterParamsRestored(newFilterParams)

            // then
            observer.assertEmpty()
        }

        @Test
        @Parameters(method = "paramsForFullFilterParamsRestorationTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} and filter params: {3} restored when apply filter pressed then filter params with {4} type are emitted")
        fun `given selected type filter and filter params restored when apply filter pressed then only new type filter is selected`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            newFilterParams: FilterParams,
            expectedTypeFilter: MediaContentType?
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)
            tested.onFilterParamsRestored(newFilterParams)

            // when
            tested.onApplyFilterPressed()

            // then
            observer.assertValue { it.type == expectedTypeFilter }
        }

        private fun paramsForFullFilterParamsRestorationTest() = arrayOf(
            arrayOf(false, false, false, FilterParams(null, null), null),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.MOVIE), MediaContentType.MOVIE),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.SERIES), MediaContentType.SERIES),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.GAME), MediaContentType.GAME),
            arrayOf(true, false, false, FilterParams(null, null), null),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.MOVIE), MediaContentType.MOVIE),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.SERIES), MediaContentType.SERIES),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.GAME), MediaContentType.GAME),
            arrayOf(false, true, false, FilterParams(null, null), null),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.MOVIE), MediaContentType.MOVIE),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.SERIES), MediaContentType.SERIES),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.GAME), MediaContentType.GAME),
            arrayOf(false, false, true, FilterParams(null, null), null),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.MOVIE), MediaContentType.MOVIE),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.SERIES), MediaContentType.SERIES),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.GAME), MediaContentType.GAME)
        )
    }

    @RunWith(JUnitParamsRunner::class)
    class OnClearedTest : BaseTest() {

        @Test
        fun `given cleared when selected year index changes then does not update livedata`() {
            // given
            tested.onCleared()

            // when
            tested.onSelectedYearIndexChanged(1)

            // then
            assertEquals(0, tested.selectedYearPosition.value)
        }

        @Test
        @Parameters(method = "paramsForTypeFilterChangeTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} and cleared when {3} becomes selected: {4} then movie selected: {5}, series selected: {6}, game selected: {7}")
        fun `given selected type filter and cleared when new type filter becomes selected then old type filter stays selected`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            mediaContentTypeThatChangedSelection: MediaContentType,
            newSelectionState: Boolean,
            expectedMovieSelection: Boolean,
            expectedSeriesSelection: Boolean,
            expectedGameSelection: Boolean
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)
            tested.onCleared()

            // when
            when (mediaContentTypeThatChangedSelection) {
                MediaContentType.MOVIE -> tested.onMovieCheckChanged(newSelectionState)
                MediaContentType.SERIES -> tested.onSeriesCheckChanged(newSelectionState)
                MediaContentType.GAME -> tested.onGameCheckChanged(newSelectionState)
            }

            // then
            assertEquals(expectedMovieSelection, tested.movieTypeSelected.value)
            assertEquals(expectedSeriesSelection, tested.seriesTypeSelected.value)
            assertEquals(expectedGameSelection, tested.gameTypeSelected.value)
        }

        private fun paramsForTypeFilterChangeTest() = arrayOf(
            arrayOf(false, false, false, MediaContentType.MOVIE, false, false, false, false),
            arrayOf(false, false, false, MediaContentType.MOVIE, true, false, false, false),
            arrayOf(false, false, false, MediaContentType.SERIES, false, false, false, false),
            arrayOf(false, false, false, MediaContentType.SERIES, true, false, false, false),
            arrayOf(false, false, false, MediaContentType.GAME, false, false, false, false),
            arrayOf(false, false, false, MediaContentType.GAME, true, false, false, false),
            arrayOf(true, false, false, MediaContentType.MOVIE, false, true, false, false),
            arrayOf(true, false, false, MediaContentType.MOVIE, true, true, false, false),
            arrayOf(true, false, false, MediaContentType.SERIES, false, true, false, false),
            arrayOf(true, false, false, MediaContentType.SERIES, true, true, false, false),
            arrayOf(true, false, false, MediaContentType.GAME, false, true, false, false),
            arrayOf(true, false, false, MediaContentType.GAME, true, true, false, false),
            arrayOf(false, true, false, MediaContentType.MOVIE, false, false, true, false),
            arrayOf(false, true, false, MediaContentType.MOVIE, true, false, true, false),
            arrayOf(false, true, false, MediaContentType.SERIES, false, false, true, false),
            arrayOf(false, true, false, MediaContentType.SERIES, true, false, true, false),
            arrayOf(false, true, false, MediaContentType.GAME, false, false, true, false),
            arrayOf(false, true, false, MediaContentType.GAME, true, false, true, false),
            arrayOf(false, false, true, MediaContentType.MOVIE, false, false, false, true),
            arrayOf(false, false, true, MediaContentType.MOVIE, true, false, false, true),
            arrayOf(false, false, true, MediaContentType.SERIES, false, false, false, true),
            arrayOf(false, false, true, MediaContentType.SERIES, true, false, false, true),
            arrayOf(false, false, true, MediaContentType.GAME, false, false, false, true),
            arrayOf(false, false, true, MediaContentType.GAME, true, false, false, true)
        )

        @Test
        fun `given cleared when filter params restored then selected year index not updated`() {
            // given
            tested.onCleared()

            // when
            tested.onFilterParamsRestored(FilterParams(1902, MediaContentType.GAME))

            // then
            assertEquals(0, tested.selectedYearPosition.value)
        }

        @Test
        @Parameters(method = "paramsForFullFilterParamsRestorationTest")
        @TestCaseName("given movie selected: {0}, series selected: {1}, game selected: {2} when {3} filter restored then movie selected: {4}, series selected: {5}, game selected: {6}")
        fun `given selected type filter and cleared when filter params restored then type filter stays selected`(
            givenMovieSelection: Boolean,
            givenSeriesSelection: Boolean,
            givenGameSelection: Boolean,
            newFilterParams: FilterParams,
            expectedMovieSelection: Boolean,
            expectedSeriesSelection: Boolean,
            expectedGameSelection: Boolean
        ) {
            // given
            tested.onMovieCheckChanged(givenMovieSelection)
            tested.onSeriesCheckChanged(givenSeriesSelection)
            tested.onGameCheckChanged(givenGameSelection)
            tested.onCleared()

            // when
            tested.onFilterParamsRestored(newFilterParams)

            // then
            assertEquals(expectedMovieSelection, tested.movieTypeSelected.value)
            assertEquals(expectedSeriesSelection, tested.seriesTypeSelected.value)
            assertEquals(expectedGameSelection, tested.gameTypeSelected.value)
        }

        private fun paramsForFullFilterParamsRestorationTest() = arrayOf(
            arrayOf(false, false, false, FilterParams(null, null), false, false, false),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.MOVIE), false, false, false),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.SERIES), false, false, false),
            arrayOf(false, false, false, FilterParams(null, MediaContentType.GAME), false, false, false),
            arrayOf(true, false, false, FilterParams(null, null), true, false, false),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.MOVIE), true, false, false),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.SERIES), true, false, false),
            arrayOf(true, false, false, FilterParams(null, MediaContentType.GAME), true, false, false),
            arrayOf(false, true, false, FilterParams(null, null), false, true, false),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.MOVIE), false, true, false),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.SERIES), false, true, false),
            arrayOf(false, true, false, FilterParams(null, MediaContentType.GAME), false, true, false),
            arrayOf(false, false, true, FilterParams(null, null), false, false, true),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.MOVIE), false, false, true),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.SERIES), false, false, true),
            arrayOf(false, false, true, FilterParams(null, MediaContentType.GAME), false, false, true)
        )
    }
}
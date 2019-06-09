package by.ve.omdbapiandroid.view.model.factory

import android.app.Application
import by.ve.omdbapiandroid.R
import by.ve.omdbapiandroid.repositories.model.MediaContentType
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.SearchQueryAdapterItem


class SearchQueryAdapterItemFactoryImpl(application: Application) : SearchQueryAdapterItemFactory {

    private val anyValue = application.resources.getString(R.string.any_value)
    private val titlePattern = application.resources.getString(R.string.title_pattern)
    private val yearPattern = application.resources.getString(R.string.year_pattern)
    private val typePattern = application.resources.getString(R.string.type_pattern)

    private val movieType = application.resources.getString(R.string.type_movie)
    private val seriesType = application.resources.getString(R.string.type_series)
    private val gameType = application.resources.getString(R.string.type_game)

    override fun create(dto: SearchQueryDto, onClickAction: (SearchQueryDto) -> Unit): SearchQueryAdapterItem {
        return SearchQueryAdapterItem(
            title = titlePattern.format(dto.query),
            year = yearPattern.format(dto.yearString),
            type = typePattern.format(dto.typeString),
            onClick = { onClickAction(dto) }
        )
    }

    private val SearchQueryDto.yearString: String get() = year?.toString() ?: anyValue

    private val SearchQueryDto.typeString: String
        get() = when (type) {
            MediaContentType.MOVIE -> movieType
            MediaContentType.SERIES -> seriesType
            MediaContentType.GAME -> gameType
            else -> anyValue
        }
}

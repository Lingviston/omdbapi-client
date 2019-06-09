package by.ve.omdbapiandroid.view.model.factory

import android.app.Application
import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.SearchQueryAdapterItem


class SearchQueryAdapterItemFactoryImpl(application: Application) : SearchQueryAdapterItemFactory {

    override fun create(dto: SearchQueryDto, onClickAction: (SearchQueryDto) -> Unit): SearchQueryAdapterItem =
        SearchQueryAdapterItem(
            query = dto.query,
            year = dto.year.toString(),
            type = dto.type.toString(),
            onClick = { onClickAction(dto) }
        )
}
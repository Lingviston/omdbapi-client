package by.ve.omdbapiandroid.view.model.factory

import by.ve.omdbapiandroid.repositories.model.SearchQueryDto
import by.ve.omdbapiandroid.view.model.SearchQueryAdapterItem


interface SearchQueryAdapterItemFactory {

    fun create(dto: SearchQueryDto, onClickAction: (SearchQueryDto) -> Unit): SearchQueryAdapterItem
}
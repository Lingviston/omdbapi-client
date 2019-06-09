package by.ve.omdbapiandroid.presentation

import by.ve.omdbapiandroid.repositories.model.SearchQueryDto

sealed class SearchQueryUpdate {

    data class QueryUpdate(val query: String): SearchQueryUpdate()

    data class FilterParamsUpdate(val filterParams: FilterParams): SearchQueryUpdate()

    data class FullQueryUpdate(val searchQueryDto: SearchQueryDto): SearchQueryUpdate()
}
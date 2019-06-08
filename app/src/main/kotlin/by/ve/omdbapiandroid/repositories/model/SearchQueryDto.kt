package by.ve.omdbapiandroid.repositories.model


data class SearchQueryDto(val query: String, val year: Int?) {

    companion object {

        val EMPTY = SearchQueryDto(query = "", year = null)
    }
}
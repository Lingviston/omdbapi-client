package by.ve.omdbapiandroid.repositories.model


data class SearchQueryDto(val query: String, val year: Int?, val type: MediaContentType?) {

    companion object {

        val EMPTY = SearchQueryDto(query = "", year = null, type = null)
    }
}
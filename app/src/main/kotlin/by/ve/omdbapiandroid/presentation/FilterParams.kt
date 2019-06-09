package by.ve.omdbapiandroid.presentation

import by.ve.omdbapiandroid.repositories.model.MediaContentType

data class FilterParams(val year: Int?, val type: MediaContentType?) {

    companion object {

        val EMPTY = FilterParams(year = null, type = null)
    }
}
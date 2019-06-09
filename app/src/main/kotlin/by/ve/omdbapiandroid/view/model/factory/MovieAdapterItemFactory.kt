package by.ve.omdbapiandroid.view.model.factory

import by.ve.omdbapiandroid.repositories.model.MovieDto
import by.ve.omdbapiandroid.view.model.MovieAdapterItem


interface MovieAdapterItemFactory {

    fun create(dto: MovieDto): MovieAdapterItem
}
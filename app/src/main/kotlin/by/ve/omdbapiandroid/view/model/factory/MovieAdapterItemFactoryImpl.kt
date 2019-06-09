package by.ve.omdbapiandroid.view.model.factory

import by.ve.omdbapiandroid.repositories.model.MovieDto
import by.ve.omdbapiandroid.view.model.MovieAdapterItem


class MovieAdapterItemFactoryImpl : MovieAdapterItemFactory {

    override fun create(dto: MovieDto): MovieAdapterItem =
        MovieAdapterItem(
            title = dto.title,
            year = dto.year,
            posterUrl = dto.posterUrl
        )
}

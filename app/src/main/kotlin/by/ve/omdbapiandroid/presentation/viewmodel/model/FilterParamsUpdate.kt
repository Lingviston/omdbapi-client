package by.ve.omdbapiandroid.presentation.viewmodel.model

import by.ve.omdbapiandroid.repositories.model.MediaContentType

sealed class FilterParamsUpdate {

    data class YearUpdate(val year: Int?) : FilterParamsUpdate()

    data class TypeUpdate(val type: MediaContentType, val isSelected: Boolean) : FilterParamsUpdate()

    data class FullParamsUpdate(val params: FilterParams) : FilterParamsUpdate()
}

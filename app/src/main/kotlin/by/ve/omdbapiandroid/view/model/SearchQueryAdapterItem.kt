package by.ve.omdbapiandroid.view.model

import androidx.recyclerview.widget.DiffUtil


class SearchQueryAdapterItem(val query: String, val year: String, val type: String, private val onClick: () -> Unit) {

    fun onClick() {
        onClick.invoke()
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<SearchQueryAdapterItem>() {

            override fun areItemsTheSame(
                oldItem: SearchQueryAdapterItem,
                newItem: SearchQueryAdapterItem
            ): Boolean =
                oldItem.query == newItem.query && oldItem.year == newItem.year && oldItem.type == newItem.type

            override fun areContentsTheSame(
                oldItemQuery: SearchQueryAdapterItem,
                newItemQuery: SearchQueryAdapterItem
            ): Boolean = areItemsTheSame(oldItemQuery, newItemQuery)
        }
    }
}
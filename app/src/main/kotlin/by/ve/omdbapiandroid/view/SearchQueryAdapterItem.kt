package by.ve.omdbapiandroid.view

import androidx.recyclerview.widget.DiffUtil


data class SearchQueryAdapterItem(val query: String, val year: String, private val onClick: () -> Unit) {

    fun onClick() {
        onClick.invoke()
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<SearchQueryAdapterItem>() {

            override fun areItemsTheSame(
                oldItem: SearchQueryAdapterItem,
                newItem: SearchQueryAdapterItem
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItemQuery: SearchQueryAdapterItem,
                newItemQuery: SearchQueryAdapterItem
            ): Boolean = areItemsTheSame(oldItemQuery, newItemQuery)
        }
    }
}
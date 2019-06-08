package by.ve.omdbapiandroid.view

import androidx.recyclerview.widget.DiffUtil


class RecentSearchAdapterItem(val query: String, private val onClick: (String) -> Unit) {

    fun onClick() {
        onClick.invoke(query)
    }

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<RecentSearchAdapterItem>() {

            override fun areItemsTheSame(oldItem: RecentSearchAdapterItem, newItem: RecentSearchAdapterItem): Boolean =
                oldItem.query == newItem.query

            override fun areContentsTheSame(
                oldItem: RecentSearchAdapterItem,
                newItem: RecentSearchAdapterItem
            ): Boolean = areItemsTheSame(oldItem, newItem)
        }
    }
}
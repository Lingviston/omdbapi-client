package by.ve.omdbapiandroid.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.ve.omdbapiandroid.databinding.ItemRecentSearchBinding


class SearchQueriesAdapter :
    PagedListAdapter<SearchQueryAdapterItem, SearchQueriesAdapter.ViewHolder>(SearchQueryAdapterItem.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemQuery = getItem(position)
    }

    class ViewHolder(private val binding: ItemRecentSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        var itemQuery: SearchQueryAdapterItem? = null
            set(value) {
                field = value
                binding.item = value
            }
    }
}
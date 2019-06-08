package by.ve.omdbapiandroid.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.ve.omdbapiandroid.databinding.ItemRecentSearchBinding


class RecentSearchesAdapter :
    PagedListAdapter<RecentSearchAdapterItem, RecentSearchesAdapter.ViewHolder>(RecentSearchAdapterItem.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = getItem(position)
    }

    class ViewHolder(private val binding: ItemRecentSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        var item: RecentSearchAdapterItem? = null
            set(value) {
                field = value
                binding.item = value
            }
    }
}
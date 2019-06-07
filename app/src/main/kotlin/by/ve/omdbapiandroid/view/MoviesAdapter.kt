package by.ve.omdbapiandroid.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.ve.omdbapiandroid.databinding.ItemMovieBinding

class MoviesAdapter : PagedListAdapter<MovieAdapterItem, MoviesAdapter.ViewHolder>(MovieAdapterItem.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = getItem(position)
    }

    class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        var item: MovieAdapterItem? = null
        set(value) {
            field = value
            binding.item = item
        }
    }
}
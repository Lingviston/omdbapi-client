package by.ve.omdbapiandroid.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.ve.omdbapiandroid.databinding.ItemLoadingBinding
import by.ve.omdbapiandroid.databinding.ItemMovieBinding

private const val VIEW_TYPE_LOADED = 0
private const val VIEW_TYPE_LOADING = 1

class MoviesAdapter : PagedListAdapter<MovieAdapterItem, MoviesAdapter.ViewHolder>(MovieAdapterItem.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LOADED -> {
                val binding = ItemMovieBinding.inflate(inflater, parent, false)
                ViewHolder.LoadedItemHolder(binding)
            }
            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingBinding.inflate(inflater, parent, false)
                ViewHolder.LoadingItemHolder(binding)
            }
            else -> throw IllegalArgumentException("View type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ViewHolder.LoadedItemHolder) {
            holder.item = getItem(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_LOADED
        }
    }

    sealed class ViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        class LoadedItemHolder(private val binding: ItemMovieBinding) : ViewHolder(binding) {

            var item: MovieAdapterItem? = null
                set(value) {
                    field = value
                    binding.item = item
                }
        }

        class LoadingItemHolder(binding: ItemLoadingBinding) : ViewHolder(binding)
    }
}
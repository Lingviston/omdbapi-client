package by.ve.omdbapiandroid.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.ve.omdbapiandroid.R

class MoviesAdapter : PagedListAdapter<MovieAdapterItem, MoviesAdapter.ViewHolder>(MovieAdapterItem.diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val titleView: TextView = view.findViewById(R.id.title)

        fun bindTo(item: MovieAdapterItem?) {
            titleView.text = item?.title ?: "Placeholder"
        }
    }
}
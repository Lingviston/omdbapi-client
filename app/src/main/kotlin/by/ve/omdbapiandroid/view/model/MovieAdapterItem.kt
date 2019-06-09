package by.ve.omdbapiandroid.view.model

import androidx.recyclerview.widget.DiffUtil

data class MovieAdapterItem(val title: String, val year: String, val posterUrl: String) {

    companion object {

        val diffCallback = object : DiffUtil.ItemCallback<MovieAdapterItem>() {

            override fun areItemsTheSame(oldItem: MovieAdapterItem, newItem: MovieAdapterItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: MovieAdapterItem, newItem: MovieAdapterItem): Boolean =
                oldItem == newItem
        }
    }
}
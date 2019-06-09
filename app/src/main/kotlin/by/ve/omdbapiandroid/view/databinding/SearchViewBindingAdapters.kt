package by.ve.omdbapiandroid.view.databinding

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["query"])
fun SearchView.query(query: String?) {
    if (query != null && this.query != query) {
        setQuery(query, false)
    }
}

@BindingAdapter(value = ["onQueryChange", "onQuerySubmit"], requireAll = false)
fun SearchView.onQueryChanged(onQueryChange: BindingAction<String>?, onQuerySubmit: BindingAction<String>?) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            onQuerySubmit?.invoke(query)
            return false
        }

        override fun onQueryTextChange(newText: String): Boolean {
            onQueryChange?.invoke(newText)
            return false
        }
    })
}

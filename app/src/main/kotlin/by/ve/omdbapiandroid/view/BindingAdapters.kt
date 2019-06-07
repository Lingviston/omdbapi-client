package by.ve.omdbapiandroid.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter(value = ["adapter"])
fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?) {
    if (adapter != null) {
        this.adapter = adapter
    }
}
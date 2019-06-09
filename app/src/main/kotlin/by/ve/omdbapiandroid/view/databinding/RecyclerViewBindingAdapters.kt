package by.ve.omdbapiandroid.view.databinding

import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.ve.omdbapiandroid.view.recyclerview.ItemOffsetDecoration

@BindingAdapter(value = ["adapter"])
fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?) {
    if (adapter != null) {
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["showHorizontalDividers"])
fun RecyclerView.showHorizontalDividers(show: Boolean?) {
    if (show != null && show) {
        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}

@BindingAdapter(value = ["itemSpacing"])
fun RecyclerView.itemSpacing(spacing: Float?) {
    if (spacing != null) {
        clipToPadding = false
        val spacingInt = spacing.toInt()
        ViewCompat.setPaddingRelative(this, spacingInt, spacingInt, spacingInt, spacingInt)
        addItemDecoration(ItemOffsetDecoration(spacingInt))
    }
}

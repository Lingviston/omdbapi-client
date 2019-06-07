package by.ve.omdbapiandroid.view

import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@BindingAdapter(value = ["adapter"])
fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?) {
    if (adapter != null) {
        this.adapter = adapter
    }
}

@BindingAdapter(value = ["imageUrl"])
fun ImageView.imageUrl(url: String?) {
    if (url != null) {
        Glide.with(this).load(url).into(this)
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
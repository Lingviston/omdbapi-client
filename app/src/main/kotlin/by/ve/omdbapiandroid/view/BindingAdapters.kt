package by.ve.omdbapiandroid.view

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.bumptech.glide.Glide


@BindingAdapter(value = ["onClick"])
fun View.onClick(onClick: BindingActionWithoutArgument?) {
    setOnClickListener { onClick?.invoke() }
}

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

@BindingAdapter(value = ["onNavigationClick"])
fun Toolbar.onNavigationClick(onClick: BindingActionWithoutArgument?) {
    setNavigationOnClickListener { onClick?.invoke() }
}

@BindingAdapter(value = ["open"])
fun SlidingPaneLayout.setOpen(isOpen: Boolean?) {
    if (isOpen != null) {
        if (isOpen) openPane() else closePane()
    }
}

@InverseBindingAdapter(attribute = "open")
fun SlidingPaneLayout.getOpen(): Boolean = isOpen

@BindingAdapter(value = ["openAttrChanged"])
fun SlidingPaneLayout.openAttrChangedListener(listener: InverseBindingListener) {
    setPanelSlideListener(object : SlidingPaneLayout.PanelSlideListener {

        override fun onPanelSlide(panel: View, slideOffset: Float) {

        }

        override fun onPanelClosed(panel: View) {
            listener.onChange()
        }

        override fun onPanelOpened(panel: View) {
            listener.onChange()
        }
    })
}

@BindingAdapter(value = ["entries"])
fun Spinner.entries(entries: Array<Any>?) {
    if (entries != null) {
        val adapter = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, entries)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        setAdapter(adapter)
    }
}

@BindingAdapter(value = ["onItemSelected"])
fun Spinner.onItemSelected(onItemSelected: BindingAction<Int>?) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {
            onItemSelected?.invoke(0)
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected?.invoke(position)
        }
    }
}

@BindingAdapter(value = ["selectedPosition"])
fun Spinner.setSelectedPosition(position: Int?) {
    if (position != null && position != selectedItemPosition) {
        setSelection(position)
    }
}

interface BindingActionWithoutArgument {

    fun invoke()
}

interface BindingAction<T> {

    fun invoke(value: T)
}
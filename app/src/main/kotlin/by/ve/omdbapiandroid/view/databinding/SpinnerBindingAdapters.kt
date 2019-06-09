package by.ve.omdbapiandroid.view.databinding

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["entries"])
fun Spinner.entries(entries: Array<Any>?) {
    if (entries != null) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, entries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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

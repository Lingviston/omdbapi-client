package by.ve.omdbapiandroid.view.databinding

import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import by.ve.omdbapiandroid.R
import com.bumptech.glide.Glide


@BindingAdapter(value = ["onClick"])
fun View.onClick(onClick: BindingActionWithoutArgument?) {
    setOnClickListener { onClick?.invoke() }
}

@BindingAdapter(value = ["imageUrl"])
fun ImageView.imageUrl(url: String?) {
    if (url != null) {
        Glide.with(this).load(url).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(this)
    }
}

@BindingAdapter(value = ["onNavigationClick"])
fun Toolbar.onNavigationClick(onClick: BindingActionWithoutArgument?) {
    setNavigationOnClickListener { onClick?.invoke() }
}

@BindingAdapter(value = ["checked"])
fun CompoundButton.checked(isChecked: Boolean?) {
    if (isChecked != null && isChecked != isChecked()) {
        setChecked(isChecked)
    }
}

@BindingAdapter(value = ["onCheckedChanged"])
fun CompoundButton.onCheckedChanged(onCheckedChanged: BindingAction<Boolean>?) {
    setOnCheckedChangeListener { _, isChecked -> onCheckedChanged?.invoke(isChecked) }
}

interface BindingActionWithoutArgument {

    fun invoke()
}

interface BindingAction<T> {

    fun invoke(value: T)
}

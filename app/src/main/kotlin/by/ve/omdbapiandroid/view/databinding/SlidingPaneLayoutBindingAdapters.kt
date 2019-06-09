package by.ve.omdbapiandroid.view.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.slidingpanelayout.widget.SlidingPaneLayout

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

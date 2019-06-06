package by.ve.omdbapiandroid.view

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import by.ve.omdbapiandroid.R
import by.ve.omdbapiandroid.databinding.ActivityMainBinding
import by.ve.omdbapiandroid.network.SearchService
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var service: SearchService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        service.search("Home Alone").subscribeBy {
            Log.d("TestSearch", "$it")
        }
    }
}
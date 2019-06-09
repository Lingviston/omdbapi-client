package by.ve.omdbapiandroid.rule

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.ExternalResource

class RxTestRule(private val scheduler: Scheduler = Schedulers.trampoline()) : ExternalResource() {

    override fun before() {
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler }

        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
    }

    override fun after() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}
package by.ve.omdbapiandroid.repositories.utils


class CurrentTimeProviderImpl : CurrentTimeProvider {

    override fun getCurrentTimeMs(): Long = System.currentTimeMillis()
}

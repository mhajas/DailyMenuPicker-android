package soft.brunhilda.org.dailymenupicker.resolvers

import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

interface DataResolver {

    val callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit
    val storage: ResolvedDataStorage
    fun resolvePlaces(places: List<ComparablePlace>, callback: (Map<ComparablePlace, RestaurantWeekData?>) -> Unit)
}

class ResolvedDataStorage {

    companion object {
        private var mInstance: ResolvedDataStorage = ResolvedDataStorage()

        @Synchronized
        fun getInstance(): ResolvedDataStorage {
            return mInstance
        }
    }

    var resolvedPlaces: MutableMap<ComparablePlace, RestaurantWeekData?> = mutableMapOf()
}
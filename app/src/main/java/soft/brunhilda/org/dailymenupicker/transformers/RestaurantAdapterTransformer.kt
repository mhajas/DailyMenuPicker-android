package soft.brunhilda.org.dailymenupicker.transformers

import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

class RestaurantAdapterTransformer : Transformer<RestaurantEntityAdapterItem> {

    companion object {
        private var mInstance: RestaurantAdapterTransformer = RestaurantAdapterTransformer()

        @Synchronized
        fun getInstance(): RestaurantAdapterTransformer {
            return mInstance
        }
    }

    override fun transform(from: Map<ComparablePlace, RestaurantWeekData?>): MutableList<RestaurantEntityAdapterItem> {
        val resultList: MutableList<RestaurantEntityAdapterItem> = mutableListOf()

        from.forEach { (place, weekData) ->
            resultList.add(RestaurantEntityAdapterItem(
                    weekData?.googlePlaceData ?: place,
                    weekData))
        }

        return resultList
    }
}
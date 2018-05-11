package soft.brunhilda.org.dailymenupicker.transformers

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver

class FoodAdapterTransformer : Transformer<FoodEntityAdapterItem> {

    companion object {
        private var mInstance: FoodAdapterTransformer = FoodAdapterTransformer()

        @Synchronized
        fun getInstance(): FoodAdapterTransformer {
            return mInstance
        }
    }

    override fun transform(from: Map<Place, RestaurantWeekData?>): MutableList<FoodEntityAdapterItem> {
        val resultList: MutableList<FoodEntityAdapterItem> = mutableListOf()
        from.forEach { (place, weekData) ->
            run {
                if (weekData == null) {
                    println("Didn't find any menu for restaurant ${place.name}")
                    // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                    return@run // end run for this restaurant
                }

                val todayData = weekData.findTodayMenu()

                if (todayData == null) {
                    println("Didn't find any menu for restaurant ${place.name}")
                    // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                    return@run // end run for this restaurant
                }

                todayData.menu.forEach {
                    resultList.add(FoodEntityAdapterItem(it, todayData, weekData.googlePlaceData)) // TODO: Do some evaluation of food
                }
            }
        }

        return resultList
    }
}
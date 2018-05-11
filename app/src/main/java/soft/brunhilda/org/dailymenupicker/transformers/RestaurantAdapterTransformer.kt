package soft.brunhilda.org.dailymenupicker.transformers

import android.widget.Toast
import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver

class RestaurantAdapterTransformer : Transformer<RestaurantEntityAdapterItem> {

    companion object {
        private var mInstance: RestaurantAdapterTransformer = RestaurantAdapterTransformer()

        @Synchronized
        fun getInstance(): RestaurantAdapterTransformer {
            return mInstance
        }
    }

    override fun transform(from: Map<Place, RestaurantWeekData?>): MutableList<RestaurantEntityAdapterItem> {
        val resultList: MutableList<RestaurantEntityAdapterItem> = mutableListOf()

        from.forEach { (place, weekData) ->
            run {
                if (weekData == null) {
                    println("Didn't find any menu for restaurant ${place.name}")
                    // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                    return@run // end run for this restaurant
                }

                val todayData = weekData.findTodayMenu()

                if (todayData == null) {
                    println("Didn't find any today menu for restaurant ${place.name}")
                    // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                    return@run // end run for this restaurant
                }

                resultList.add(RestaurantEntityAdapterItem(
                        weekData.googlePlaceData,
                        todayData.menu.map { it.price }.filterNotNull().average(),
                        todayData.soup[0].price
                ))
            }
        }

        return resultList
    }
}
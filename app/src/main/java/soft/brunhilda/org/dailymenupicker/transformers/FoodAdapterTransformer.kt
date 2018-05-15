package soft.brunhilda.org.dailymenupicker.transformers

import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

class FoodAdapterTransformer : Transformer<FoodEntityAdapterItem> {

    companion object {
        private var mInstance: FoodAdapterTransformer = FoodAdapterTransformer()

        @Synchronized
        fun getInstance(): FoodAdapterTransformer {
            return mInstance
        }
    }

    //TODO
    override fun transform(from: Map<ComparablePlace, RestaurantWeekData?>): MutableList<FoodEntityAdapterItem> {
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

    fun transform(comparablePlace: ComparablePlace, dailyData: RestaurantDailyData): MutableList<FoodEntityAdapterItem>{
        val resultList: MutableList<FoodEntityAdapterItem> = mutableListOf()
        dailyData.menu.forEach{
            resultList.add(FoodEntityAdapterItem(it, dailyData, comparablePlace))
        }
        return resultList
    }
}
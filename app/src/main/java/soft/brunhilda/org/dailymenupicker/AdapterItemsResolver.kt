package soft.brunhilda.org.dailymenupicker

import android.app.Activity
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.collectors.rest.RESTFoodCollector
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

class AdapterItemsResolver (var context: Activity,
                            var listView: ListView,
                            val restaurantsStore: MutableMap<Place, RestaurantWeekData?>, // We need to have Place as key because we need to be able to get restaurant name in case RestaurantWeekData is null
                            val foodCollector: FoodCollector = RESTFoodCollector()
    ) : CollectedRestaurantProcessor {

    override fun displayCollectedRestaurant(restaurantDailyData: RestaurantWeekData) {
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println("Sending REST request for restaurant ${restaurantDailyData.googlePlaceData}")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")
        println(" -------------------------------- ")

        val placeId = restaurantDailyData.googlePlaceData.placeId
        Hawk.put(placeId, restaurantDailyData)
        restaurantsStore[restaurantDailyData.googlePlaceData] = restaurantDailyData
    }

    override fun displayNotFoundRestaurant(placeData: Place) {
        restaurantsStore[placeData] = null
    }

    fun addPlace(place: Place) {
        if (!containsPlace(place)) {
            resolveFromLocalStorage(place)

            if (restaurantsStore[place] == null || !restaurantsStore[place]!!.isCurrent()) { // TODO: no idea why !! is necessary but I think this can't be null as if it is null the first condition will be true and the second won't be evaluated
                foodCollector.getRestaurantData(place, this)
            } else {
                println("---------------------------")
                println("---------------------------")
                println("---------------------------")
                println("---------------------------")
                println("We found data in Hawk for restaurant $place")
                println("---------------------------")
                println("---------------------------")
                println("---------------------------")
            }
        }else {
            println("---------------------------")
            println("---------------------------")
            println("---------------------------")
            println("---------------------------")
            println("We found data in memory for restaurant $place")
            println("---------------------------")
            println("---------------------------")
            println("---------------------------")
        }
    }

    fun addPlaces(place: List<Place>?) {
        place?.forEach{addPlace(it)}
    }

    fun resolveFromLocalStorage(place: Place) {
        restaurantsStore[place] = Hawk.get(place.placeId, null)
    }

    fun containsPlace(place: Place): Boolean {
        restaurantsStore.forEach { (k,_) -> if (k.placeId == place.placeId) return@containsPlace true}
        return false
    }

    fun showFood() { // TODO: refactor: break into more functions
        val resultList: MutableList<FoodEntityAdapterItem> = mutableListOf()
        restaurantsStore.forEach{
            (place, weekData) -> run{
                if (weekData == null) {
                    println("Didn't find any menu for restaurant ${place.name}")
                    Toast.makeText(context, "Didn't find any menu for restaurant ${place.name}", Toast.LENGTH_LONG).show()
                    // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                    return@run // end run for this restaurant
                }

                val todayData = weekData.findTodayMenu()

                if (todayData == null) {
                    println("Didn't find any menu for restaurant ${place.name}")
                    Toast.makeText(context, "Didn't find any menu for restaurant ${place.name}", Toast.LENGTH_LONG).show()
                    // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                    return@run // end run for this restaurant
                }

                todayData.menu.forEach{
                    resultList.add(FoodEntityAdapterItem(it, todayData)) // TODO: Do some evaluation of food
                }
            }
        }

        resultList.sortWith(compareBy { it.preferenceEvaluation })

        listView.adapter = FoodEntityAdapter(context, resultList)
    }

}
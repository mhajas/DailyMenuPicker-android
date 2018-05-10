package soft.brunhilda.org.dailymenupicker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.content_food_today.*
import kotlinx.android.synthetic.main.content_restaurant_today.*
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.FoodEntityAdapter
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.RestaurantEntityAdapter
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver

class TodayAllRestaurantFragment : Fragment(){

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataResolver = CachedRestDataResolver.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataPreparer.callback = this::placesPreparationIsFinished
        dataPreparer.findPlaces()
    }

    fun placesPreparationIsFinished(places: List<Place>) {
        dataResolver.callback = this::placesResolvingIsFinished
        dataResolver.resolvePlaces(places)
    }

    fun placesResolvingIsFinished(places: Map<Place, RestaurantWeekData?>) {
        val resultList: MutableList<RestaurantEntityAdapterItem> = mutableListOf()

        places.forEach{
            (place, weekData) -> run{
            if (weekData == null) {
                println("Didn't find any menu for restaurant ${place.name}")
                Toast.makeText(context, "Didn't find any menu for restaurant ${place.name}", Toast.LENGTH_LONG).show()
                // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                return@run // end run for this restaurant
            }

            val todayData = weekData.findTodayMenu()

            if (todayData == null) {
                println("Didn't find any today menu for restaurant ${place.name}")
                Toast.makeText(context, "Didn't find any menu for restaurant ${place.name}", Toast.LENGTH_LONG).show()
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

        resultList.sortWith(compareBy { it.preferenceEvaluation })
        today_restaurant_list_view.adapter = RestaurantEntityAdapter(context, resultList)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_restaurant_today, container, false)
    }

}

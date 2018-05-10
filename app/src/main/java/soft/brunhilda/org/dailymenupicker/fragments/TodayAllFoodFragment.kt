package soft.brunhilda.org.dailymenupicker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.content_food_today.*
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.FoodEntityAdapter
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver

class TodayAllFoodFragment : Fragment() {

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataResolver = CachedRestDataResolver(this::placesResolvingIsFinished)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataPreparer.callback = this::placesPreparationIsFinished
        dataPreparer.findPlaces()
    }

    fun placesPreparationIsFinished(places: List<Place>) {
        dataResolver.resolvePlaces(places)
    }

    fun placesResolvingIsFinished(places: Map<Place, RestaurantWeekData?>) {
        val resultList: MutableList<FoodEntityAdapterItem> = mutableListOf()
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
                println("Didn't find any menu for restaurant ${place.name}")
                Toast.makeText(context, "Didn't find any menu for restaurant ${place.name}", Toast.LENGTH_LONG).show()
                // TODO: show something which says sorry for place place we didn't find any menu, directly in adapter list
                return@run // end run for this restaurant
            }

            todayData.menu.forEach{
                resultList.add(FoodEntityAdapterItem(it, todayData, weekData.googlePlaceData)) // TODO: Do some evaluation of food
            }
        }
        }

        resultList.sortWith(compareBy { it.preferenceEvaluation })
        today_food_list_view.adapter = FoodEntityAdapter(context, resultList)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_food_today, container, false)
    }

}
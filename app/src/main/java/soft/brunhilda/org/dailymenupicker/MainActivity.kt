package soft.brunhilda.org.dailymenupicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.collectors.rest.RESTFoodCollector
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData


class MainActivity : AppCompatActivity(), PlacesListener, CollectedRestaurantProcessor {

    private val foodCollector = RESTFoodCollector()
    private var listItems = mutableListOf<FoodEntityAdapterItem>()

    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        places?.forEach {
            foodCollector.getRestaurantData(it, this)
        }
    }

    override fun displayCollectedRestaurant(restaurantDailyData: RestaurantDailyData) {
        restaurantDailyData.menu.forEach({
            listItems.add(FoodEntityAdapterItem(it, restaurantDailyData))
        })

        runOnUiThread {
            refreshList()
        }
    }

    override fun displayNotFoundRestaurant(placeData: Place) {
        // Do something smart
    }

    override fun onPlacesFinished() {

    }

    override fun onPlacesStart() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyAMJQuIQAzLRHdCGbxhfsvr-q7lFEaPxPg")
                .latlng(49.2227476, 16.584627)
                .radius(20)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()
    }

    private fun refreshList() {
        main_list_view.adapter = FoodEntityAdapter(this, listItems)
    }
}

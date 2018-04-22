package soft.brunhilda.org.dailymenupicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.collectors.rest.RESTFoodCollector
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData


class MainActivity : AppCompatActivity(), PlacesListener, CollectedRestaurantProcessor {

    val foodCollector = RESTFoodCollector()
    var listItems = mutableListOf<FoodEntityAdapterItem>()
    var restaurants = mutableMapOf<String, RestaurantDailyData>()

    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        places!!.forEach {
            println("Found: " + it.placeId)
            foodCollector.getRestaurantData(it.placeId, this)
        }
    }

    override fun displayCollectedRestaurant(placeID: String, restaurantDailyData: RestaurantDailyData?) {
        if (restaurantDailyData == null) {
            return
        }

        restaurants[placeID] = restaurantDailyData

        restaurantDailyData.menu.forEach({
            listItems.add(FoodEntityAdapterItem(it, restaurantDailyData.soup, restaurantDailyData.soupIncludedInPrice))
        })

        runOnUiThread {
            refreshList()
        }
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

    fun refreshList() {
        main_list_view.adapter = FoodEntityAdapter(this, listItems)
    }
}

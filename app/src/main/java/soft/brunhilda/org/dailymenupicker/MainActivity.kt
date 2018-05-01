package soft.brunhilda.org.dailymenupicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.collectors.rest.RESTFoodCollector
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import soft.brunhilda.org.dailymenupicker.entity.RestaurantDailyData


class MainActivity : AppCompatActivity(), PlacesListener {
    private var adapterItemsPreparer: AdapterItemsResolver? = null

    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        adapterItemsPreparer?.addPlaces(places)

        runOnUiThread {
            adapterItemsPreparer?.showFood()
        }
    }

    override fun onPlacesFinished() {

    }

    override fun onPlacesStart() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapterItemsPreparer = AdapterItemsResolver(this, main_list_view, mutableMapOf())

        Hawk.init(this).build() // no idea where to put this

        NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyAMJQuIQAzLRHdCGbxhfsvr-q7lFEaPxPg")
                .latlng(49.2227476, 16.584627)
                .radius(20)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()
    }
}

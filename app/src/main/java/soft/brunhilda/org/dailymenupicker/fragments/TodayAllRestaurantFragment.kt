package soft.brunhilda.org.dailymenupicker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_restaurant_today.*
import noman.googleplaces.*
import soft.brunhilda.org.dailymenupicker.AdapterItemsResolver
import soft.brunhilda.org.dailymenupicker.R

class TodayAllRestaurantFragment : Fragment(), PlacesListener{

    private var adapterItemsPreparer: AdapterItemsResolver? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterItemsPreparer = AdapterItemsResolver(activity, today_restaurant_list_view, mutableMapOf())

        NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyAMJQuIQAzLRHdCGbxhfsvr-q7lFEaPxPg")
                .latlng(49.2227476, 16.584627)
                .radius(20)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_restaurant_today, container, false)
    }

    override fun onPlacesFailure(e: PlacesException?) {
    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        adapterItemsPreparer?.addPlaces(places)
        activity.runOnUiThread {
            adapterItemsPreparer?.showRestaurants()
        }
    }

    override fun onPlacesFinished() {
    }

    override fun onPlacesStart() {
    }
}

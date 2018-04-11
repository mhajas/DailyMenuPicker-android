package soft.brunhilda.org.dailymenupicker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import noman.googleplaces.*
import pv239.fi.muni.cz.dailymenupicker.parser.WebPageHtmlParser
import soft.brunhilda.org.dailymenupicker.restaurant.server.MockService


class MainActivity : AppCompatActivity(), PlacesListener {

    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        places!!.forEach {
            val mockService = MockService()
            val foods = WebPageHtmlParser(mockService.getParserConfig(it.placeId)).parse(null)

            runOnUiThread {
                main_activity_restaurant_name.text = it.name
                main_activity_soup.text = foods.menuHeader
                main_list_view.adapter = FoodEntityAdapter(this, foods.menuItems)
            }
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
                .key("")
                .latlng(49.2227476, 16.584627)
                .radius(20)
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()
    }
}

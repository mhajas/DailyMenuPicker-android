package soft.brunhilda.org.dailymenupicker.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_restaurant_today.*
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.adapters.RestaurantEntityAdapter
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver
import soft.brunhilda.org.dailymenupicker.transformers.RestaurantAdapterTransformer

class TodayAllRestaurantFragment : Fragment(){

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataResolver = CachedRestDataResolver.getInstance()
    private val dataTransformer = RestaurantAdapterTransformer.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataPreparer.findPlaces(this::placesPreparationIsFinished)
    }

    private fun placesPreparationIsFinished(places: List<ComparablePlace>) {
        dataResolver.resolvePlaces(places, this::placesResolvingIsFinished)
    }

    private fun placesResolvingIsFinished(places: Map<ComparablePlace, RestaurantWeekData?>) {
        val adapterItems = dataTransformer.transform(places)
        adapterItems.sortWith(compareBy { it.preferenceEvaluation })

        today_restaurant_list_view.adapter = RestaurantEntityAdapter(context, adapterItems)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_restaurant_today, container, false)
    }

}

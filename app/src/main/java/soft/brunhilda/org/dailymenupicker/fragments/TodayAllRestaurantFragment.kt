package soft.brunhilda.org.dailymenupicker.fragments

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_all_restaurants.*
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.adapters.RestaurantEntityAdapter
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.evaluators.RestaurantEvaluator
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver
import soft.brunhilda.org.dailymenupicker.transformers.RestaurantAdapterTransformer
import android.widget.AdapterView


class TodayAllRestaurantFragment : Fragment(){

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataTransformer = RestaurantAdapterTransformer.getInstance()
    private val dataEvaluator = RestaurantEvaluator.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataPreparer.findPlaces(this::placesPreparationIsFinished)
    }

    private fun placesPreparationIsFinished(places: Set<ComparablePlace>) {
        val dataResolver = CachedRestDataResolver()
        dataResolver.resolvePlaces(places.toList(), this::placesResolvingIsFinished)
    }

    private fun placesResolvingIsFinished(places: Map<ComparablePlace, RestaurantWeekData?>) {
        if (context != null) {
            var adapterItems = dataTransformer.transform(places)
            val database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
                    .allowMainThreadQueries()
                    .build()
            adapterItems = dataEvaluator.evaluate(adapterItems, database.favoriteRestaurantDao().findAll(), database.favoriteIngredientDao().findAll())

            adapterItems.sortWith(compareByDescending { it.preferenceEvaluation })
            today_restaurant_list_view.adapter = RestaurantEntityAdapter(context, adapterItems)
            today_restaurant_list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
               /* val fragment = ParticularRestaurantFragment()
                fragment.arguments.putString("googleId",adapterItems[position].googlePlace.placeId)*/
                val fragment = ParticularRestaurantFragment()
                fragment.arguments = Bundle()
                fragment.arguments.putString("googleID",adapterItems[position].googlePlace.placeId)
                activity.supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_main, fragment)
                        .commit()
            };
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_all_restaurants, container, false)
    }

}

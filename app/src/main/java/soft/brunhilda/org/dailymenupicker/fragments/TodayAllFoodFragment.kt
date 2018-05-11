package soft.brunhilda.org.dailymenupicker.fragments

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_food_today.*
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.adapters.FoodEntityAdapter
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.evaluators.FoodEvaluator
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver
import soft.brunhilda.org.dailymenupicker.transformers.FoodAdapterTransformer

class TodayAllFoodFragment : Fragment() {

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataResolver = CachedRestDataResolver.getInstance()
    private val dataTransformer = FoodAdapterTransformer.getInstance()
    private val dataEvaluator = FoodEvaluator.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataPreparer.findPlaces(this::placesPreparationIsFinished)
    }

    private fun placesPreparationIsFinished(places: List<ComparablePlace>) {
        dataResolver.resolvePlaces(places, this::placesResolvingIsFinished)
    }

    private fun placesResolvingIsFinished(places: Map<ComparablePlace, RestaurantWeekData?>) {
        val database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
                .allowMainThreadQueries()
                .build()
        val adapterItems = dataEvaluator.evaluate(dataTransformer.transform(places), database.favoriteRestaurantDao().findAll(), database.favoriteIngredientDao().findAll())

        adapterItems.sortWith(compareBy { it.preferenceEvaluation })

        today_food_list_view.adapter = FoodEntityAdapter(context, adapterItems)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_food_today, container, false)
    }

}
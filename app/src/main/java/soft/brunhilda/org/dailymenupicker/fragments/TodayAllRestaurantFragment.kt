package soft.brunhilda.org.dailymenupicker.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import com.github.ybq.android.spinkit.SpinKitView
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


class TodayAllRestaurantFragment : Fragment(){

    companion object {
        private var mInstance: TodayAllRestaurantFragment = TodayAllRestaurantFragment()

        @Synchronized
        fun getInstance(): TodayAllRestaurantFragment {
            return mInstance
        }
    }


    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataTransformer = RestaurantAdapterTransformer.getInstance()
    private val dataEvaluator = RestaurantEvaluator.getInstance()
    private var animated = false

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
                val fragment = ParticularRestaurantFragment()
                fragment.arguments = Bundle()
                fragment.arguments.putString("googleID",adapterItems[position].googlePlace.placeId)
                fragment.arguments.putString("restaurantName",adapterItems[position].googlePlace.name)
                activity.supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.content_main, fragment)
                        .commit()
            };

            if (!animated) {
                val animatedView: SpinKitView? = view?.findViewById(R.id.restaurants_loading_animation)
                val params = animatedView?.layoutParams as LinearLayout.LayoutParams
                val animator = ValueAnimator.ofInt(params.topMargin, -230)

                animatedView.animate()
                        .alpha(0.0f)
                        .setDuration(1200)

                today_restaurant_list_view.animate()
                        .alpha(1f)
                        .setDuration(1000)


                animator.addUpdateListener { valueAnimator ->
                    params.topMargin = valueAnimator.animatedValue as Int
                    animatedView.requestLayout()
                }

                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        restaurants_loading_animation?.visibility = View.GONE
                        animated = true
                    }
                })

                animator.duration = 1000
                animator.start()
            } else {
                restaurants_loading_animation?.visibility = View.GONE
                today_restaurant_list_view.alpha = 1f
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_all_restaurants, container, false)
    }

}

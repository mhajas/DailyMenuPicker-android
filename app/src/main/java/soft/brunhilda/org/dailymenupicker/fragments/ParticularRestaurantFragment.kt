package soft.brunhilda.org.dailymenupicker.fragments

import android.arch.persistence.room.Room
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import soft.brunhilda.org.dailymenupicker.R
import android.widget.Toast
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.content_scrolling.*
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.adapters.FoodEntityAdapter_recycler
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.database.FavoriteRestaurantEntity
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.evaluators.FoodEvaluator
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver
import soft.brunhilda.org.dailymenupicker.transformers.FoodAdapterTransformer
import com.google.android.gms.maps.MapView

class ParticularRestaurantFragment : Fragment(), OnMapReadyCallback {
    private var isFavourite = false
    private lateinit var place: ComparablePlace
    private lateinit var mapView: MapView

    private val dataResolver = CachedRestDataResolver()
    private val dataTransformer = FoodAdapterTransformer.getInstance()
    private val dataEvaluator = FoodEvaluator.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
                .allowMainThreadQueries()
                .build()

        place = this.arguments.getSerializable("googlePlace") as ComparablePlace

        val myFab = view?.findViewById(R.id.fab) as FloatingActionButton
        if(isPlaceInFavourite(database)){
            myFab.setImageResource(android.R.drawable.ic_delete)
        }else{
            myFab.setImageResource(android.R.drawable.ic_menu_save)
        }
        myFab.setOnClickListener {
            if (isPlaceInFavourite(database)) {
                removeFromDB(database)
                Toast.makeText(activity, "Place was removed from the favourite places",
                        Toast.LENGTH_LONG).show()
                isFavourite = false
                myFab.setImageResource(android.R.drawable.ic_menu_save)
            } else {
                addToDB(database)
                Toast.makeText(activity, "Place was added to the favourite places",
                        Toast.LENGTH_LONG).show()
                isFavourite = true
                myFab.setImageResource(android.R.drawable.ic_delete)
            }
        }
        Toast.makeText(activity, "Place name: ${place.name} placeID: ${place.placeId}",
            Toast.LENGTH_LONG).show()

        //TODO add NAME
        placesPreparationIsFinished(mutableSetOf(place))
    }

    private fun placesPreparationIsFinished(places: Set<ComparablePlace>) {
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

            restaurant_food_recycle_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            val adapter = FoodEntityAdapter_recycler(adapterItems)
            restaurant_food_recycle_view.adapter = adapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.content_particular_restaurant, container, false)

        mapView = view?.findViewById(R.id.mapwhere) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    private fun isPlaceInFavourite(database: DailyMenuPickerDatabase): Boolean {
        val favourite = database.favoriteRestaurantDao().getByPlaceId(place.placeId)
        return favourite != null
    }

    private fun addToDB(database: DailyMenuPickerDatabase) {
        val restaurantEntity = FavoriteRestaurantEntity()
        restaurantEntity.name = place.name
        restaurantEntity.placeId = place.placeId
        database.favoriteRestaurantDao().insert(restaurantEntity)
    }

    private fun removeFromDB(database: DailyMenuPickerDatabase) {
        val favourite = database.favoriteRestaurantDao().getByPlaceId(place.placeId)
        if (favourite != null) {
            database.favoriteRestaurantDao().delete(favourite)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val mapManager = MapManager(activity, context, googleMap,place)
        if (mapManager.checkPermission())
            mapManager.createMap()
        else
            requestPermissions(
                    arrayOf(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    ),1)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.filter { it==PackageManager.PERMISSION_DENIED }.isNotEmpty()){
            System.err.println("Permission was denied, permission: $permissions")
            fragmentManager.popBackStackImmediate() //sorry, get back
        }else{
            val mapView = activity.findViewById(R.id.mapwhere) as MapView
            mapView.getMapAsync(this)
        }
    }
}
package soft.brunhilda.org.dailymenupicker.fragments

import android.content.pm.PackageManager
import android.os.Bundle
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
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.evaluators.FoodEvaluator
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver
import soft.brunhilda.org.dailymenupicker.transformers.FoodAdapterTransformer
import com.google.android.gms.maps.MapView
import soft.brunhilda.org.dailymenupicker.database.DatabaseManager

class ParticularRestaurantFragment : ParentFragment(), OnMapReadyCallback {
    private var isFavourite = false
    private lateinit var place: ComparablePlace
    private lateinit var mapView: MapView

    private val dataResolver = CachedRestDataResolver()
    private val dataTransformer = FoodAdapterTransformer.getInstance()
    private val dataEvaluator = FoodEvaluator.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val databaseManager = DatabaseManager(context)
        place = this.arguments.getSerializable("googlePlace") as ComparablePlace

        val myFab = view?.findViewById(R.id.fab) as FloatingActionButton
        if(databaseManager.isPlaceInDb(place.placeId)){
            myFab.setImageResource(android.R.drawable.ic_delete)
        }else{
            myFab.setImageResource(android.R.drawable.ic_menu_save)
        }
        myFab.setOnClickListener {
            if (databaseManager.isPlaceInDb(place.placeId)) {
                databaseManager.deleteFavouritePlace(place)
                Toast.makeText(activity, "Place was removed from the favourite places",
                        Toast.LENGTH_LONG).show()
                isFavourite = false
                myFab.setImageResource(android.R.drawable.ic_menu_save)
            } else {
                databaseManager.addFavouritePlace(place)
                Toast.makeText(activity, "Place was added to the favourite places",
                        Toast.LENGTH_LONG).show()
                isFavourite = true
                myFab.setImageResource(android.R.drawable.ic_delete)
            }
        }
        Toast.makeText(activity, "Place name: ${place.name} placeID: ${place.placeId}",
            Toast.LENGTH_LONG).show()

        placesPreparationIsFinished(mutableSetOf(place))
    }

    private fun placesPreparationIsFinished(places: Set<ComparablePlace>) {
        dataResolver.resolvePlaces(places.toList(), this::placesResolvingIsFinished)
    }

    private fun placesResolvingIsFinished(places: Map<ComparablePlace, RestaurantWeekData?>) {
        if (context != null) {
            var adapterItems = dataTransformer.transform(places)
            val database = DatabaseManager(context)
            adapterItems = dataEvaluator.evaluate(adapterItems, database.getAllFavouritePlaces(), database.getAllFavouriteIngredients())

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

    override fun onMapReady(googleMap: GoogleMap) {
        val mapManager = MapViewManager(activity, context, googleMap,place)
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
        activity.title = place.name
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
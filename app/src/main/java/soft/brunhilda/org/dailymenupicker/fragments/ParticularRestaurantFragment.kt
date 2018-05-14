package soft.brunhilda.org.dailymenupicker.fragments

import android.arch.persistence.room.Room
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.content_scrolling.*
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.adapters.FoodEntityAdapter_recycler
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.database.FavoriteRestaurantEntity
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData
import soft.brunhilda.org.dailymenupicker.evaluators.FoodEvaluator
import soft.brunhilda.org.dailymenupicker.preparers.NearestPlacesDataPreparer
import soft.brunhilda.org.dailymenupicker.resolvers.CachedRestDataResolver
import soft.brunhilda.org.dailymenupicker.transformers.FoodAdapterTransformer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapView
import noman.googleplaces.Place

class ParticularRestaurantFragment : Fragment(), OnMapReadyCallback {
    private var isFavourite = false
    private val place: Place = Place()
    private lateinit var mapView: MapView

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataTransformer = FoodAdapterTransformer.getInstance()
    private val dataEvaluator = FoodEvaluator.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
                .allowMainThreadQueries()
                .build()

        place.name = this.arguments.getString("googleID",null); //TODO .. check null value in the next lines 
        place.placeId = this.arguments.getString("restaurantName",null);

        val myFab = view?.findViewById(R.id.fab) as FloatingActionButton
        if(isPlaceInFavourite(database)){
            myFab.setImageResource(android.R.drawable.ic_delete);
        }else{
            myFab.setImageResource(android.R.drawable.ic_menu_save);
        }
        myFab.setOnClickListener {
            if (isPlaceInFavourite(database)) {
                removeFromDB(database)
                Toast.makeText(activity, "Place was removed from the favourite places",
                        Toast.LENGTH_LONG).show()
                isFavourite = false
                myFab.setImageResource(android.R.drawable.ic_menu_save);
            } else {
                addToDB(database)
                Toast.makeText(activity, "Place was added to the favourite places",
                        Toast.LENGTH_LONG).show()
                isFavourite = true
                myFab.setImageResource(android.R.drawable.ic_delete);
            }
        }
        Toast.makeText(activity, "Place name: ${place.name} placeID: ${place.placeId}",
            Toast.LENGTH_LONG).show()

        //TODO add NAME to the toolbar
        placesPreparationIsFinished(mutableSetOf(ComparablePlace(place)))
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

            restaurant_food_recycle_view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            var adapter = FoodEntityAdapter_recycler(adapterItems)
            restaurant_food_recycle_view.adapter = adapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.content_particular_restaurant, container, false)

        mapView = view?.findViewById(R.id.mapwhere) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view;
    }

    private fun isPlaceInFavourite(database: DailyMenuPickerDatabase): Boolean {
        val favourite = database.favoriteRestaurantDao().getByPlaceId(place.placeId)
        return favourite != null
    }

    private fun addToDB(database: DailyMenuPickerDatabase) {
        val restaurantEntity: FavoriteRestaurantEntity = FavoriteRestaurantEntity()
        restaurantEntity.name = place.name
        restaurantEntity.placeId = place.placeId
        database.favoriteRestaurantDao().insert(restaurantEntity)
    }

    private fun removeFromDB(database: DailyMenuPickerDatabase) {
        val favourite = database.favoriteRestaurantDao().getByPlaceId(place.placeId);
        if (favourite != null) {
            database.favoriteRestaurantDao().delete(favourite)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        val marker_latlng: LatLng = LatLng(49.2227476, 16.584627) //TODO
        val cameraPosition: CameraPosition = CameraPosition.Builder()
                .target(marker_latlng)
                .zoom(15.0f).build()
        val cameraUpdate: CameraUpdate = CameraUpdateFactory . newCameraPosition (cameraPosition);
        googleMap.moveCamera(cameraUpdate);
        googleMap.addMarker(MarkerOptions()
                .position(marker_latlng)
                .title(place.name)).showInfoWindow()
    }

    override fun onResume() {
        super.onResume();
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
}
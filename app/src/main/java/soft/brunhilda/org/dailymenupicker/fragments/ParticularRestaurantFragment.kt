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



class ParticularRestaurantFragment : Fragment() {
    private var isFavourite = false;
    private var googleID: String = ""
    private var name: String = ""

    private val dataPreparer = NearestPlacesDataPreparer.getInstance()
    private val dataTransformer = FoodAdapterTransformer.getInstance()
    private val dataEvaluator = FoodEvaluator.getInstance()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
                .allowMainThreadQueries()
                .build()
        googleID = this.arguments.getString("googleID",null);
        name = this.arguments.getString("restaurantName",null);

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
        Toast.makeText(activity, "Place name: $name placeID: $googleID",
            Toast.LENGTH_LONG).show()

        //TODO change to find places only for this restaurants and add NAME to the toolbar
        dataPreparer.findPlaces(this::placesPreparationIsFinished)
        showMap()
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


            recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            var adapter = FoodEntityAdapter_recycler(adapterItems)
            recyclerView1.adapter = adapter
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_particular_restaurant, container, false)
    }

    private fun isPlaceInFavourite(database: DailyMenuPickerDatabase): Boolean {
        val favourite = database.favoriteRestaurantDao().getByPlaceId(googleID)
        return favourite != null
    }

    private fun addToDB(database: DailyMenuPickerDatabase) {
        val restaurantEntity: FavoriteRestaurantEntity = FavoriteRestaurantEntity()
        restaurantEntity.name = name
        restaurantEntity.placeId = googleID
        database.favoriteRestaurantDao().insert(restaurantEntity)
    }

    private fun removeFromDB(database: DailyMenuPickerDatabase) {
        val favourite = database.favoriteRestaurantDao().getByPlaceId(googleID);
        if (favourite != null) {
            database.favoriteRestaurantDao().delete(favourite)
        }
    }

    private fun showMap(){
        System.out.println("Showing map")
        val mSupportMapFragment = MapFragment.newInstance();
        activity.fragmentManager.beginTransaction().replace(R.id.mapwhere, mSupportMapFragment).commit();

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(this::mapReady);
        }
    }

    private fun mapReady(googleMap: GoogleMap){
        System.out.println("Showing in map ready function")

        if (googleMap != null) {
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            val marker_latlng: LatLng = LatLng(49.2227476, 16.584627) //TODO
            val cameraPosition: CameraPosition = CameraPosition.Builder()
                    .target(marker_latlng)
                    .zoom(15.0f).build()
            val cameraUpdate: CameraUpdate = CameraUpdateFactory . newCameraPosition (cameraPosition);
            googleMap.moveCamera(cameraUpdate);
            googleMap.addMarker(MarkerOptions()
                    .position(marker_latlng)
                    .title(name)).showInfoWindow()
        }
    }
}

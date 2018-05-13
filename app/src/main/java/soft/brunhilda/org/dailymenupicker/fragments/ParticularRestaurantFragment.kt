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
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.database.FavoriteRestaurantEntity


class ParticularRestaurantFragment : Fragment() {
    private var isFavourite = false;
    private var googleID: String = ""
    private var name: String = ""


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

        //TODO
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
}

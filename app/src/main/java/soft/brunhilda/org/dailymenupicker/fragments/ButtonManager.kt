package soft.brunhilda.org.dailymenupicker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.database.DatabaseManager
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

class ButtonManager(val context: Context){


    fun agendaAddButton(food: FoodEntityAdapterItem, context: Context){
        Toast.makeText(context, "Added to agenda", Toast.LENGTH_LONG).show()
        //TODO agenda
    }

    fun goToRestaurant(food: FoodEntityAdapterItem, activity: Activity){
        val fragment = ParticularRestaurantFragment()
        fragment.arguments = Bundle()
        fragment.arguments.putSerializable("googlePlace", food.googlePlace)
        activity.fragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.content_main, fragment)
                .commit()
    }

    fun addToFavourite(databaseManager: DatabaseManager,
                         myFab: FloatingActionButton,
                         place: ComparablePlace): (View) -> Unit {
        return {
            if (databaseManager.isPlaceInDb(place.placeId)) {
                databaseManager.deleteFavouritePlace(place)
                Snackbar
                        .make(it, "Place was removed from the favourite places", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", View.OnClickListener { view ->
                            Snackbar
                                    .make(view, "Place was added to the favourite places!", Snackbar.LENGTH_SHORT)
                                    .show()
                            myFab.callOnClick()
                        })
                        .show()
                myFab.setImageResource(R.drawable.ic_like)
            } else {
                databaseManager.addFavouritePlace(place)
                Snackbar
                        .make(it, "Place was added to the favourite places", Snackbar.LENGTH_LONG)
                        .setAction("UNdo", View.OnClickListener { view ->
                            Snackbar
                                    .make(view, "Place was removed from the favourite places!", Snackbar.LENGTH_SHORT)
                                    .show()
                            myFab.callOnClick()
                        })
                        .show()
                myFab.setImageResource(R.drawable.ic_dislike)
            }
        }
    }
}
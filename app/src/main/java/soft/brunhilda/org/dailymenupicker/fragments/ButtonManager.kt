package soft.brunhilda.org.dailymenupicker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.database.AgendaManager
import soft.brunhilda.org.dailymenupicker.database.DatabaseManager
import soft.brunhilda.org.dailymenupicker.entity.DayOfWeek
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

class ButtonManager{

    var agendaManager: AgendaManager = AgendaManager()

    fun agendaAddButton(context: Context, dayOfWeek: DayOfWeek): (View,FoodEntityAdapterItem) -> Unit{
        return { view: View, food: FoodEntityAdapterItem ->
            if(agendaManager.isInAgenda(food,dayOfWeek)){
                agendaManager.deleteFromAgenda(food, dayOfWeek)
                Toast.makeText(context, "Food was removed from agenda",Toast.LENGTH_LONG).show()

            }else{
                agendaManager.addToAgenda(food, dayOfWeek)
                Toast.makeText(context, "Food was added to agenda",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun agendaAddButtonLayout(): (View,FoodEntityAdapterItem) -> Unit{
        return { view: View, food: FoodEntityAdapterItem ->
           view.findViewById<CheckBox>(R.id.agenda_button).performClick()
        }
    }

    fun inAgendaButton(activity: Activity, context: Context, dayOfWeek: DayOfWeek): (View, FoodEntityAdapterItem) -> Unit{
        return { view: View, food: FoodEntityAdapterItem ->
            agendaAddButton(context, dayOfWeek).invoke(view,food)
            val fragment = AgendaFragment()
            fragment.arguments = Bundle()
            fragment.arguments.putSerializable("googlePlace", food.googlePlace)
            activity.fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit()
        }
    }

    fun goToRestaurant(activity: Activity): (View,FoodEntityAdapterItem) -> Unit {
        return { view: View, food: FoodEntityAdapterItem ->
            val fragment = ParticularRestaurantFragment()
            fragment.arguments = Bundle()
            fragment.arguments.putSerializable("googlePlace", food.googlePlace)
            activity.fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.content_main, fragment)
                    .commit()
        }
    }

    fun addToFavourite(databaseManager: DatabaseManager,
                         myFab: FloatingActionButton,
                         place: ComparablePlace): (View) -> Unit {
        return {
            if (databaseManager.isPlaceInDb(place.placeId)) {
                databaseManager.deleteFavouritePlace(place)
                Snackbar
                        .make(it, "Place was removed from the favourite places", Snackbar.LENGTH_LONG)
                        .setAction("Undo", View.OnClickListener { view ->
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
                        .setAction("Undo", View.OnClickListener { view ->
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
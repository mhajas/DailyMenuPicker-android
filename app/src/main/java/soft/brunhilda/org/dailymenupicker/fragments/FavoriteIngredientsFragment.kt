package soft.brunhilda.org.dailymenupicker.fragments

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import kotlinx.android.synthetic.main.content_favorite_ingredients.*
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.adapters.IngredientEntityAdapter
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.database.DatabaseManager
import soft.brunhilda.org.dailymenupicker.database.Ingredient

class FavoriteIngredientsFragment : Fragment() {

    companion object {
        private var mInstance: FavoriteIngredientsFragment = FavoriteIngredientsFragment()

        @Synchronized
        fun getInstance(): FavoriteIngredientsFragment {
            return mInstance
        }
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = DatabaseManager(context)

        favorite_ingredients_list_view.adapter = IngredientEntityAdapter(context, Ingredient.values().toList(), database)

        favorite_ingredients_list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            view?.findViewById<CheckBox>(R.id.ingredient_checkbox)?.performClick()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_favorite_ingredients, container, false)
    }

}
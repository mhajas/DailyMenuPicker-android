package soft.brunhilda.org.dailymenupicker.fragments

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_favorite_ingredients.*
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.adapters.IngredientEntityAdapter
import soft.brunhilda.org.dailymenupicker.database.DailyMenuPickerDatabase
import soft.brunhilda.org.dailymenupicker.database.Ingredient

class FavoriteIngredientsFragment : Fragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = Room.databaseBuilder(context, DailyMenuPickerDatabase::class.java, "db")
                .allowMainThreadQueries()
                .build()

        favorite_ingredients_list_view.adapter = IngredientEntityAdapter(context, Ingredient.values().toList(), database)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_favorite_ingredients, container, false)
    }

}
package soft.brunhilda.org.dailymenupicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.database.DatabaseManager
import soft.brunhilda.org.dailymenupicker.database.FavoriteIngredientEntity
import soft.brunhilda.org.dailymenupicker.database.Ingredient

class IngredientEntityAdapter(
        private val context: Context,
        private val ingredients: List<Ingredient>,
        private val database: DatabaseManager,
        private val favoriteIngredients: List<FavoriteIngredientEntity> = database.getAllFavouriteIngredients()

) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val ingredient = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_favorite_ingredients, parent, false)
        }

        val identifier = context.resources.getIdentifier(ingredient.toString().toLowerCase(), "string", context.packageName)
        val iconIdentifier = context.resources.getIdentifier("ic_" + ingredient.toString().toLowerCase(), "drawable", context.packageName)

        view?.findViewById<TextView>(R.id.ingredient_name)?.text = context.resources.getString(identifier)
        view?.findViewById<ImageView>(R.id.ingredient_icon)?.setImageResource(iconIdentifier)

        if (favoriteIngredients.map { it.ingredient }.contains(ingredient)) {
            view?.findViewById<CheckBox>(R.id.ingredient_checkbox)?.isChecked = true
        }

        view?.findViewById<CheckBox>(R.id.ingredient_checkbox)?.setOnCheckedChangeListener({ button, b ->
            run {
                if (b) {
                    database.addFavouriteIngredient(FavoriteIngredientEntity(ingredient))
                } else {
                    database.deleteFavouriteIngredient(FavoriteIngredientEntity(ingredient))
                }

                button.isChecked = b
            }
        })

        return view
    }

    override fun getItem(position: Int): Ingredient = ingredients[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = ingredients.size
}

package soft.brunhilda.org.dailymenupicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import java.text.NumberFormat
import java.util.*

class FoodEntityAdapter(
        private val context: Context,
        private val foodEntities: List<FoodEntityAdapterItem>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val food = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_dayfood, parent, false)
        }

        view?.findViewById<TextView>(R.id.dayfood_item_name)?.text = food.foodEntity.name.toLowerCase().capitalize()
        val currencyFormater = NumberFormat.getCurrencyInstance()
        currencyFormater.currency = Currency.getInstance("CZK")

        view?.findViewById<TextView>(R.id.dayfood_item_price)?.text = currencyFormater.format(food.foodEntity.price)
        view?.findViewById<TextView>(R.id.dayfood_soup)?.text = food.restaurantDailyData.soup[0].name.toLowerCase().capitalize() // TODO: add support for more soups from one restaurant

        if (food.restaurantDailyData.soup[0].price == null) {
            view?.findViewById<TextView>(R.id.dayfood_soup_price)?.text = "v cene"
        } else {
            view?.findViewById<TextView>(R.id.dayfood_soup_price)?.text = currencyFormater.format(food.restaurantDailyData.soup[0].price)
        }

        view?.findViewById<TextView>(R.id.restaurant_name)?.text = food.googlePlace.name
        view?.findViewById<TextView>(R.id.dayfood_evaluation)?.text = String.format("%.2f", food.preferenceEvaluation)


        return view
    }

    override fun getItem(position: Int): FoodEntityAdapterItem = foodEntities[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = foodEntities.size
}

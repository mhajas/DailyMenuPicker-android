package soft.brunhilda.org.dailymenupicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem
import java.text.NumberFormat
import java.util.*

class RestaurantEntityAdapter(
        private val context: Context,
        private val restaurantEntities: List<RestaurantEntityAdapterItem>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val restaurant = getItem(position)

        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_restaurant, parent, false)
        }

        val currencyFormater = NumberFormat.getCurrencyInstance()
        currencyFormater.currency = Currency.getInstance("CZK")

        view?.findViewById<TextView>(R.id.restaurant_name)?.text = restaurant.googlePlace.name
        view?.findViewById<TextView>(R.id.restaurant_average_price)?.text =  currencyFormater.format(restaurant.averagePrice)
        view?.findViewById<TextView>(R.id.restaurant_evaluation)?.text =  String.format("%.2f", restaurant.preferenceEvaluation)

        if (restaurant.soupPrice == null) {
            view?.findViewById<TextView>(R.id.restaurant_soup_price)?.text = "v cene"
        } else {
            view?.findViewById<TextView>(R.id.restaurant_soup_price)?.text = currencyFormater.format(restaurant.soupPrice)
        }


        return view
    }

    override fun getItem(position: Int): RestaurantEntityAdapterItem = restaurantEntities[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = restaurantEntities.size
}

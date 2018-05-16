package soft.brunhilda.org.dailymenupicker.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
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

        view?.findViewById<TextView>(R.id.restaurant_name)?.text = restaurant.googlePlace.name

        if (restaurant.restaurantWeekData != null) {

            val todayData = restaurant.restaurantWeekData.findTodayMenu()

            if (todayData != null) {
                val currencyFormater = NumberFormat.getCurrencyInstance()
                currencyFormater.currency = Currency.getInstance("CZK")

                val restaurantTodayAverageFoodPrice = todayData.menu.mapNotNull { it.price }.average()
                view?.findViewById<TextView>(R.id.restaurant_average_price)?.text = currencyFormater.format(restaurantTodayAverageFoodPrice)

                view?.findViewById<TextView>(R.id.restaurant_evaluation)?.text = String.format("%.2f", restaurant.preferenceEvaluation)

                view?.findViewById<LinearLayout>(R.id.restaurant_list_price_of_soup)?.visibility = View.VISIBLE
                view?.findViewById<LinearLayout>(R.id.restaurant_list_rating)?.visibility = View.VISIBLE
                view?.findViewById<LinearLayout>(R.id.restaurant_list_average_price_layout)?.visibility = View.VISIBLE

                view?.findViewById<ConstraintLayout>(R.id.restaurant_no_food_message)?.visibility = View.GONE

                when {
                    todayData.soup.isEmpty() -> view?.findViewById<LinearLayout>(R.id.restaurant_list_price_of_soup)?.visibility = View.GONE
                    todayData.soup[0].price == null -> view?.findViewById<TextView>(R.id.restaurant_soup_price)?.text = "v cene"
                    else -> view?.findViewById<TextView>(R.id.restaurant_soup_price)?.text = currencyFormater.format(todayData.soup[0].price)
                }

                return view
            }
        }

        // Hide all info because we don't have any
        view?.findViewById<LinearLayout>(R.id.restaurant_list_price_of_soup)?.visibility = View.GONE
        view?.findViewById<LinearLayout>(R.id.restaurant_list_rating)?.visibility = View.GONE
        view?.findViewById<LinearLayout>(R.id.restaurant_list_average_price_layout)?.visibility = View.GONE

        // Show message about it
        view?.findViewById<ConstraintLayout>(R.id.restaurant_no_food_message)?.visibility = View.VISIBLE

        return view
    }

    override fun getItem(position: Int): RestaurantEntityAdapterItem = restaurantEntities[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = restaurantEntities.size
}

package soft.brunhilda.org.dailymenupicker.adapters

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem
import java.text.NumberFormat
import java.util.*

class RestaurantEntityAdapter(
        private val restaurantEntitiesToDisplay: List<RestaurantEntityAdapterItem>,
        private val callbackAfterClickOnSpecificItem: (ComparablePlace) -> Unit
): RecyclerView.Adapter<RestaurantEntityAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val restaurant = restaurantEntitiesToDisplay[position]

        holder?.restaurantName?.text = restaurant.googlePlace.name

        holder?.itemView?.setOnClickListener {
            callbackAfterClickOnSpecificItem(restaurant.googlePlace)
        }
        
        if (restaurant.restaurantWeekData != null) {

            val todayData = restaurant.restaurantWeekData.findTodayMenu()

            if (todayData != null) {
                val currencyFormater = NumberFormat.getCurrencyInstance()
                currencyFormater.currency = Currency.getInstance("CZK")

                val restaurantTodayAverageFoodPrice = todayData.menu.mapNotNull { it.price }.average()
                holder?.averagePriceRowPrice?.text = currencyFormater.format(restaurantTodayAverageFoodPrice)

                holder?.restaurantRatingRowValue?.text = String.format("%.2f", restaurant.preferenceEvaluation)

                holder?.soupPriceRow?.visibility = View.VISIBLE
                holder?.restaurantRatingRow?.visibility = View.VISIBLE
                holder?.averagePriceRow?.visibility = View.VISIBLE

                holder?.restaurantNoFoodLayout?.visibility = View.GONE

                when {
                    todayData.soup.isEmpty() -> holder?.soupPriceRow?.visibility = View.GONE
                    todayData.soup[0].price == null -> holder?.soupPriceRowPrice?.text = "v cene"
                    else -> holder?.soupPriceRowPrice?.text = currencyFormater.format(todayData.soup[0].price)
                }

                return
            }
        }

        // Hide all info because we don't have any
        holder?.soupPriceRow?.visibility = View.GONE
        holder?.restaurantRatingRow?.visibility = View.GONE
        holder?.averagePriceRow?.visibility = View.GONE

        // Show message about it
        holder?.restaurantNoFoodLayout?.visibility = View.VISIBLE
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_restaurant, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return restaurantEntitiesToDisplay.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val restaurantName = itemView.findViewById<TextView?>(R.id.restaurant_name)

        val averagePriceRow = itemView.findViewById<LinearLayout?>(R.id.restaurant_list_average_price_layout)
        val averagePriceRowPrice = itemView.findViewById<TextView?>(R.id.restaurant_average_price)

        val soupPriceRow = itemView.findViewById<LinearLayout?>(R.id.restaurant_list_price_of_soup)
        val soupPriceRowPrice = itemView.findViewById<TextView?>(R.id.restaurant_soup_price)

        val restaurantRatingRow = itemView.findViewById<LinearLayout?>(R.id.restaurant_list_rating)
        val restaurantRatingRowValue = itemView.findViewById<TextView?>(R.id.restaurant_evaluation)

        val restaurantNoFoodLayout = itemView.findViewById<ConstraintLayout?>(R.id.restaurant_no_food_message)
    }
}
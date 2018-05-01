package soft.brunhilda.org.dailymenupicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.entity.RestaurantEntityAdapterItem

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

		view?.findViewById<TextView>(R.id.restaurant_name)?.text = restaurant.googleData.name
		view?.findViewById<TextView>(R.id.restaurant_average_price)?.text = restaurant.averagePrice.toString() + " CZK"

		if (restaurant.soupPrice == null) {
			view?.findViewById<TextView>(R.id.restaurant_soup_price)?.text = "v cene"
		} else {
			view?.findViewById<TextView>(R.id.restaurant_soup_price)?.text = restaurant.soupPrice.toString() + " CZK"
		}


		return view
	}

	override fun getItem(position: Int): RestaurantEntityAdapterItem = restaurantEntities[position]

	override fun getItemId(position: Int): Long = position.toLong()

	override fun getCount(): Int = restaurantEntities.size
}

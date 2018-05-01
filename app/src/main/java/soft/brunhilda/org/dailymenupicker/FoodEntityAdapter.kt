package soft.brunhilda.org.dailymenupicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

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

		view?.findViewById<TextView>(R.id.dayfood_item_name)?.text = food.foodEntity.name
		view?.findViewById<TextView>(R.id.dayfood_item_price)?.text = food.foodEntity.price.toString() + " CZK"
		view?.findViewById<TextView>(R.id.dayfood_soup)?.text = food.restaurantDailyData.soup[0].name // TODO: add support for more soups from one restaurant

		if (food.restaurantDailyData.soup[0].price == null) {
			view?.findViewById<TextView>(R.id.dayfood_soup_price)?.text = "v cene"
		} else {
			view?.findViewById<TextView>(R.id.dayfood_soup_price)?.text = food.restaurantDailyData.soup[0].price.toString() + " CZK"
		}


		return view
	}

	override fun getItem(position: Int): FoodEntityAdapterItem = foodEntities[position]

	override fun getItemId(position: Int): Long = position.toLong()

	override fun getCount(): Int = foodEntities.size
}
